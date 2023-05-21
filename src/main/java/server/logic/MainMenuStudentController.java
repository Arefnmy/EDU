package server.logic;

import server.controllers.ChooseLessonController;
import server.database.CollegeDatabase;
import server.database.LessonDatabase;
import server.database.RequestDatabase;
import server.database.UserDatabase;
import server.model.College;
import server.model.Courseware;
import server.model.Lesson;
import server.model.educationalrequest.*;
import server.model.users.EducationalAssistant;
import server.model.users.Professor;
import server.model.users.Student;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.model.Time;
import shared.model.courseware.Exercise;
import shared.model.courseware.Solution;
import shared.model.educaionalrequests.Protest;
import shared.model.media.Media;
import shared.model.notification.Notification;
import shared.model.notification.NotificationType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainMenuStudentController extends MainMenuController {
    private final Student student;

    Logger logger = LogManager.getLogger(MainMenuStudentController.class);
    public MainMenuStudentController(Student student) {
        super(student);
        this.student = student;
    }

    public Student getUser() {
        return student;
    }

    public void addProtest(int lesson , String register){
        logger.info("Add protest, User code : " + student.getUserCode());
        Protest protest = RequestDatabase.getInstance().getProtest(student.getUserCode() , lesson);
        if(protest == null)
            RequestDatabase.getInstance().addProtest(new Protest(lesson , student.getUserCode() , register));
        else
            protest.setRegister(register);
    }

    public void addRecommendation(String professor){
        Professor p = UserDatabase.getInstance().getProfessorByName(professor);
        logger.info("Add EducationalRequest : Recommendation, User code : " + student.getUserCode() +
                ",Professor code : " + p.getUserCode());
        Recommendation recommendation = new Recommendation(student , p);
        RequestDatabase.getInstance().addRequest(recommendation);
    }

    public boolean addMinor(College origin , String goalName){
        College goal = CollegeDatabase.getInstance().getCollege(goalName);
        logger.info("Add EducationalRequest : Minor, User code : " + student.getUserCode() +
                ",College name : " + goal.getName());
        if (origin.getEducationalAssistant() == null || goal.getEducationalAssistant() == null ||
                origin == goal)
            return false;

        Minor minor = new Minor(student , origin.getEducationalAssistant() ,
                goal.getEducationalAssistant());
        RequestDatabase.getInstance().addRequest(minor);

        return true;
    }

    public boolean addCancel(){
        logger.info("Add EducationalRequest : Cancel, User code : " + student.getUserCode() );
        EducationalAssistant educationalAssistant = student.getCollege().getEducationalAssistant();

        if (educationalAssistant == null)
            return false;

        Cancel cancel = new Cancel(student ,educationalAssistant );
        RequestDatabase.getInstance().addRequest(cancel);
        return true;
    }

    public void addBusyStudying(){
        logger.info("Add EducationalRequest : Busy studying, User code : " + student.getUserCode() );
        BusyStudying busyStudying = new BusyStudying(student);
        RequestDatabase.getInstance().addRequest(busyStudying);
    }

    public void addDorm(){
        logger.info("Add EducationalRequest : Dorm, User code : " + student.getUserCode() );
        Dorm dorm = new Dorm(student);
        RequestDatabase.getInstance().addRequest(dorm);
    }

    public void addDefence(){
        logger.info("Add EducationalRequest : Defence, User code : " + student.getUserCode() );
        Defence defence = new Defence(student);
        RequestDatabase.getInstance().addRequest(defence);
    }

    public List<Student> getStudentsSameCollegeAndEntryYear(){
        List<Student> studentList = new ArrayList<>();
        for (Student s : UserDatabase.getInstance().getStudents()){
            if (s.getEntryYear() == student.getEntryYear() && s.getCollege() == student.getCollege())
                studentList.add(s);
        }
        studentList.remove(student);
        return studentList;
    }

    public List<Lesson> getSuggestedLesson(){
        return LessonDatabase.getInstance().getLessons().stream()
                .filter(l -> l.getGrade() == student.getGrade())
                .filter(l -> l.getCapacity() > l.getTakers())
                .filter(l -> student.getPassedLesson().containsAll(l.getPrerequisites()))
                .filter(l -> l.getTerm() == ChooseLessonController.getTerm())
                .collect(Collectors.toList());
    }

    public void takeLesson(int lesson , Student student){
        Notification notification = new Notification(NotificationType.TAKE_LESSON ,
                "Can i take lesson with number " + lesson , student.getName());
        notification.setSenderCode(student.getUserCode());
        notification.setData(String.valueOf(lesson));
        student.getCollege().getEducationalAssistant().addNotification(notification);
    }

    public int addSolution(int lessonNumber , String exerciseName , String text , Media media){
        Time time = new Time();
        Courseware courseware = lessonDatabase.getCourseware(lessonNumber);
        Solution solution = lessonDatabase.getSolution(lessonNumber , exerciseName , student.getUserCode());
        Exercise exercise = getExercise(exerciseName , courseware);
        if (exercise.getEndTime().compareTo(time) < 0)
            return 1;
        if (exercise.getStartTime().compareTo(time) > 0)
            return 2;
        if (solution == null){
            if (text == null)
                solution = new Solution(student.getUserCode() , lessonNumber , exerciseName ,new Time() , media);
            else
                solution = new Solution(student.getUserCode() , lessonNumber , exerciseName ,new Time() , text);
            lessonDatabase.addSolution(solution);
            return 0;
        }
        else{
            solution.setMedia(media);
            solution.setText(text);
            return 3;
        }
    }
}
