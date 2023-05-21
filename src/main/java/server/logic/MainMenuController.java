package server.logic;


import client.filemanager.ResourceManager;
import server.database.*;
import server.model.*;
import server.model.chatroom.Chat;
import server.model.chatroom.ChatMessage;
import server.model.users.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.model.EducationalStatus;
import shared.model.Grade;
import shared.model.ProfessorDegree;
import shared.model.Time;
import shared.model.courseware.EducationalMaterial;
import shared.model.courseware.Exercise;
import shared.model.courseware.Solution;
import shared.model.media.Media;
import shared.model.notification.Notification;
import shared.model.notification.NotificationType;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

public class MainMenuController {
    private final User user;

    protected UserDatabase userDatabase = UserDatabase.getInstance();
    protected LessonDatabase lessonDatabase = LessonDatabase.getInstance();
    protected RequestDatabase requestDatabase = RequestDatabase.getInstance();

    Logger logger = LogManager.getLogger(MainMenuController.class);
    public MainMenuController(User user){
        this.user = user;
    }

    public void changeProfile(String email , String mobileNumber){
        logger.info("Edit profile, User code : " + user.getUserCode());
        user.setEmail(email);
        user.setMobileNumber(mobileNumber);
    }

    public List<Lesson> listedLessons(String grade , String professorName , String collegeName){
        boolean isSelectedGrade = grade != null;
        boolean isSelectedProfessor = professorName != null;
        boolean isSelectedCollege = collegeName != null;
        Professor professor = userDatabase.getProfessorByName(professorName);
        College college = CollegeDatabase.getInstance().getCollege(collegeName);

        List<Lesson> lessonList = new ArrayList<>();
        for(Lesson l : lessonDatabase.getLessons()){
            if ( ( !isSelectedGrade || l.getGrade().toString().equals(grade) ) &&
                    ( !isSelectedProfessor || l.getProfessor() == professor ) &&
                    ( !isSelectedCollege || l.getCollege() == college ) )
                lessonList.add(l);
        }
        return lessonList;
    }

    public List<Professor> listedProfessor(boolean isSelectedDegree , boolean isSelectedProfessor , boolean isSelectedCollege,
                                           String degree , Professor professor , College college){
        List<Professor> professorList = new ArrayList<>();
        for (Professor p : userDatabase.getProfessors()){
            if ( ( !isSelectedDegree || p.getDegree().toString().equals(degree) ) &&
                    ( !isSelectedProfessor || p == professor ) &&
                    ( !isSelectedCollege || p.getCollege() == college ) )
                professorList.add(p);
        }

        return professorList;
    }

    public List<Lesson> sortedLesson(List<Lesson> lessonList){
        for (int i = 0; i < lessonList.size(); i++) {
            for (int j = i+1; j <lessonList.size() ; j++) {
                if (lessonList.get(j).getTime().isSooner(lessonList.get(i).getTime())) {
                    Lesson temp = lessonList.get(j);
                    lessonList.set(j , lessonList.get(i));
                    lessonList.set(i , temp);
                }
            }
        }
        return lessonList;
    }

    public List<List<Lesson>> getSortedLessonInWeek(){
        List<List<Lesson>> result = new ArrayList<>();

        List<Lesson> mondayLessons = new ArrayList<>();
        List<Lesson> tuesdayLessons = new ArrayList<>();
        List<Lesson> wednesdayLessons = new ArrayList<>();
        List<Lesson> thursdayLessons = new ArrayList<>();
        List<Lesson> fridayLessons = new ArrayList<>();
        List<Lesson> sundayLessons = new ArrayList<>();
        List<Lesson> saturdayLessons = new ArrayList<>();

        result.add(mondayLessons);
        result.add(tuesdayLessons);
        result.add(wednesdayLessons);
        result.add(thursdayLessons);
        result.add(fridayLessons);
        result.add(sundayLessons);
        result.add(saturdayLessons);

        for (Lesson l : getLesson()){
            for (DayOfWeek d : l.getTime().getDayOfWeeks()){
                int day = d.getValue();
                result.get(day-1).add(l);
            }
        }
        for (List<Lesson> l : result)
            l = sortedLesson(l);

        return result;
    }

    public List<Lesson> getLesson(){
        if (user instanceof Professor)
            return ((Professor) user).getLesson();
        if (user instanceof Student)
            return ((Student) user).getLesson();
        return null;
    }


    public void sendMessage(List<User> users , String messageStr , Media media){
        if (messageStr == null && media == null)
            return;

        ChatMessage message;
        if (messageStr != null)
         message = new ChatMessage(user , messageStr , new Time());
        else
            message = new ChatMessage(user , media , new Time());
        for (User u : users){
            Chat chat = ChatDatabase.getInstance().getChat(user , u);
            if (chat == null){
                chat = new Chat(user , u);
                ChatDatabase.getInstance().addChat(chat);
            }
            chat.addMessage(message);
        }
    }

    public int sendRequestToMessage(String user){
        if (!isNumber(user))
            return 1;
        User receiver = userDatabase.getUser(Long.parseLong(user));
        if (receiver == null)
            return 2;

        Notification notification = new Notification(NotificationType.SEND_MESSAGE ,
                ResourceManager.getInstance().getGraphicConfig().getProperty(
                        "requestToMessage-ntf" , "Can I send you a message?"),
                this.user.getName());
        notification.setSenderCode(this.user.getUserCode());
        receiver.addNotification(notification);
        return 0;
    }

    public void handelSendMessage(Notification notification , boolean yes){
        User u = userDatabase.getUser(notification.getSenderCode());
        Notification response;
        if (yes) {
            Chat chat = new Chat(u, user);
            ChatDatabase.getInstance().addChat(chat);
            response = new Notification(NotificationType.PREDICATIVE,
                    user.getName() + " accepted the chat request.", "System");
        }
        else{
            response = new Notification(NotificationType.PREDICATIVE,
                    user.getName() + " did not accept the chat request.", "System");
        }
        u.addNotification(response);
    }

    public EducationalMaterial getEducationalMaterial(String name , Courseware courseware){
        for (EducationalMaterial e : courseware.getEducationalMaterials())
            if (e.getName().equals(name))
                return e;
        return null;
    }

    public Exercise getExercise(String name , Courseware courseware){
        for (Exercise e : courseware.getExercises())
            if (e.getName().equals(name))
                return e;
        return null;
    }

    public int editEducationalMaterial(int lesson , boolean remove , EducationalMaterial educationalMaterial){
        Courseware courseware = lessonDatabase.getCourseware(lesson);
        EducationalMaterial e = getEducationalMaterial(educationalMaterial.getName() , courseware);
        if (remove)
            courseware.getEducationalMaterials().remove(e);
        else{
            e.setDescription(educationalMaterial.getDescription());
            if (educationalMaterial.getFiles().size() ==
            ResourceManager.getInstance().getValue(Integer.class , "maximumNumberOfMedia" , 5))//todo
                return 1;
            e.setFiles(educationalMaterial.getFiles());
        }
        return 0;
    }

    public void editExercise(int lesson , boolean remove , Exercise exercise){
        Courseware courseware = lessonDatabase.getCourseware(lesson);
        Exercise e = getExercise(exercise.getName() , courseware);
        if (remove){
            courseware.getExercises().remove(e);
        }
        else{
            e.setDescription(exercise.getDescription());
            e.setMedia(exercise.getMedia());
        }
    }

    public void addEducationalMaterial(int lesson , EducationalMaterial educationalMaterial){
        Courseware courseware = lessonDatabase.getCourseware(lesson);
        courseware.addEducationalMaterial(educationalMaterial);
    }

    public void addExercise(int lesson , Exercise exercise){
        Courseware courseware = lessonDatabase.getCourseware(lesson);
        courseware.addExercise(exercise);
    }

    public LinkedHashMap<Time , String> getCalender(){
        LinkedHashMap<Time , String> calender = new LinkedHashMap<>();
        Map<Time , String> c = new HashMap<>();
        for (Lesson l : getLesson()) {
            c.put(l.getTime().getFinalTime(), l.getName());
            if(lessonDatabase.getCourseware(l.getNumber()) != null)
                for (Exercise e : lessonDatabase.getCourseware(l.getNumber()).getExercises())
                    c.put(e.getUploadWithoutDecreasingScore(), e.getName());
        }
        SortedSet<Time> set = new TreeSet<>(c.keySet());
        for (Time t : set)
            calender.put(t , c.get(t));
        return calender;
    }

    public int registerScoreSolution(int lessonNumber , String exerciseName , long userCode , String score){
        if (!isScore(score))
            return 1;
        Solution solution = lessonDatabase.getSolution(lessonNumber , exerciseName , userCode);
        solution.setScore(Double.valueOf(score));
        return 0;
    }

    public boolean isNumber(String number){
        if (number.isEmpty())
            return false;
        for (char c : number.toCharArray())
            if (!Character.isDigit(c))
                return false;
        return true;
    }

    protected boolean isScore(String score){
        int index = score.indexOf('.');
        if (index == -1)
            return isNumber(score) && Integer.parseInt(score)<=20 ;
        else if(index == score.length()-1)
            return false;
        else{
            String scoreInt = score.substring(0 , index);
            String scoreFloat = score.substring(index+1);
            return isNumber(scoreInt) &&
                    isNumber(scoreFloat) &&
                    ( Integer.parseInt(scoreInt) < 20 ||
                            Integer.parseInt(scoreInt) == 20 && Integer.parseInt(scoreFloat) == 0);
        }
    }
}
