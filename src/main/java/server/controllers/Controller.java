package server.controllers;

import server.database.*;
import server.filemanager.ResourceManager;
import server.logic.*;
import server.model.*;
import server.model.Courseware;
import server.model.chatroom.Chat;
import server.model.chatroom.ChatMessage;
import server.model.educationalrequest.*;
import server.model.users.*;
import shared.model.Time;
import shared.model.courseware.EducationalMaterial;
import shared.model.courseware.Exercise;
import shared.model.courseware.Solution;
import shared.model.educaionalrequests.Protest;
import shared.model.media.Media;
import shared.model.notification.Notification;
import shared.request.Request;
import shared.model.educaionalrequests.RequestType;
import shared.response.Response;
import shared.response.ResponseState;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    private final User user;
    private final MainMenuController mainMenuController;
    private final ResourceManager resourceManager = ResourceManager.getInstance();

    public Controller(User user, MainMenuController mainMenuController) {
        this.user = user;
        this.mainMenuController = mainMenuController;
    }

    public Response getUser() {
        Response response = new Response(ResponseState.OK , null);
        shared.model.users.User u = null;
        if (user instanceof Student){
            u = new shared.model.users.Student(user.getCollege().getName() ,
                    user.getLastEnter() , user.getUsername() , user.getPassword(), user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getMobileNumber(), user.getNationalCode(),
                    user.getUserCode(), ((Student) user).getEntryYear(), ((Student) user).getGrade(), ((Student) user).getStatus());
            ((shared.model.users.Student) u).setSupervisor(((Student) user).getSupervisor().getName());
            ((shared.model.users.Student) u).setChooseLessonTime(((Student) user) .getChooseLessonTime());
        }
        else if (user instanceof CollegeBoss){
            u = new shared.model.users.CollegeBoss(user.getCollege().getName() ,
                    user.getLastEnter() , user.getUsername() , user.getPassword(), user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getMobileNumber(), user.getNationalCode(),
                    user.getUserCode(), ((CollegeBoss) user).getRoomNumber() , ((CollegeBoss) user).getDegree());
        }
        else if (user instanceof EducationalAssistant){
            u = new shared.model.users.EducationalAssistant(user.getCollege().getName() ,
                    user.getLastEnter() , user.getUsername() , user.getPassword(), user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getMobileNumber(), user.getNationalCode(),
                    user.getUserCode(), ((EducationalAssistant) user).getRoomNumber() , ((EducationalAssistant) user).getDegree());
        }
        else if(user instanceof Professor){
            u = new shared.model.users.Professor(user.getCollege().getName() ,
                    user.getLastEnter() , user.getUsername() , user.getPassword(), user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getMobileNumber(), user.getNationalCode(),
                    user.getUserCode(), ((Professor) user).getRoomNumber() , ((Professor) user).getDegree());
        }
        else if (user instanceof AdminEdu){
            u = new shared.model.users.AdminEdu(user.getFirstName() , user.getLastName() , user.getUserCode());
            u.setEmail(user.getEmail());
        }
        else if (user instanceof Mohseni){
            u = new shared.model.users.Mohseni(user.getFirstName() , user.getLastName() , user.getUserCode());
            u.setEmail(user.getEmail());
        }
        assert u != null;
        u.setImage(resourceManager.getUserImage(user.getUserCode()));
        response.addData("user" , u);
        return response;
    }

    public Response changeProfile(Request request){
        mainMenuController.changeProfile((String) request.getData("email"), (String) request.getData("mobileNumber"));
        return new Response(ResponseState.OK , "Info Saved!");
    }

    public Response getLessonInWeek(){
        Response response = new Response(ResponseState.OK , null);
        List<List<Lesson>> lessonList = mainMenuController.getSortedLessonInWeek();
        List<List<shared.model.Lesson>> returnLesson = new ArrayList<>();
        for (List<Lesson> list : lessonList){
            List<shared.model.Lesson> newList = new ArrayList<>();
            for (Lesson l : list){
                newList.add(new shared.model.Lesson(l.getName() , null , -1 ,
                        null , null , l.getTime() , -1 , -1));
            }
            returnLesson.add(newList);
        }
        response.addData("lessonList" , returnLesson);
        return response;
    }

    public Response getExamList(){
        Response response = new Response(ResponseState.OK , null);
        response.addData("isStudent" , user instanceof Student);
        List<shared.model.Lesson> lessons = new ArrayList<>();
        for (Lesson l : mainMenuController.getLesson()){
            lessons.add(new shared.model.Lesson(l.getName() ,l.getProfessor().getName() , l.getTime(), -1));
        }
        response.addData("lessonList" , lessons);
        return response;
    }

    public Response getLessonList(Request request){
        Response response = new Response(ResponseState.OK , null);

        boolean isAssistant = user instanceof EducationalAssistant;
        response.addData("isAssistant" , isAssistant);

        List<String> professorList = new ArrayList<>();
        for (Professor p : UserDatabase.getInstance().getProfessors())
            professorList.add(p.getName());
        response.addData("professorList" , professorList);

        List<String> collegeList = CollegeDatabase.getInstance().getCollegesName();
        response.addData("collegeList" , collegeList);

        String grade = (String) request.getData("grade");
        String professor = (String) request.getData("professor");
        String college = (String) request.getData("college");
        List<Lesson> lessons = mainMenuController.listedLessons(grade , professor , college );
        List<shared.model.Lesson> returnLesson = new ArrayList<>();
        for (Lesson l : lessons){
            returnLesson.add(new shared.model.Lesson(l.getName() , l.getCollege().getName() ,
                    isAssistant? l.getNumberOfUnits() : -1 , l.getGrade() ,l.getProfessor().getName(),
                    l.getTime() ,l.getCapacity(), isAssistant? l.getNumber() : -1));
        }
        response.addData("lessonList" , returnLesson);
        return response;
    }

    public Response getProfessorList(){
        boolean isBoss = user instanceof CollegeBoss;
        Response response = new Response(ResponseState.OK , null);
        response.addData("isBoss" , isBoss);

        List<shared.model.users.Professor> professors = new ArrayList<>();
        for (Professor p : UserDatabase.getInstance().getProfessors()){
            professors.add(new shared.model.users.Professor(p.getCollege().getName() , null ,
                    isBoss? p.getUsername(): null , isBoss? p.getPassword() : null ,
                    p.getFirstName() , p.getLastName() , isBoss? p.getEmail() : null ,
                    isBoss? p.getMobileNumber() : null , isBoss? p.getNationalCode() : null ,
                    isBoss? p.getUserCode() : -1 ,  p.getRoomNumber() , p.getDegree()));
        }
        response.addData("professorList" , professors);
        return response;
    }

    public Response getAddUser(){
        Response response = new Response(ResponseState.OK , null);
        College college = user.getCollege();
        List<shared.model.Lesson> lessonList = new ArrayList<>();
        for (Lesson l : college.getLessonList()){
            lessonList.add(new shared.model.Lesson(l.getName() , null , -1 ,
                    null , null , null ,-1 ,  l.getNumber()));
        }
        response.addData("lessonList" , lessonList);
        List<shared.model.users.Professor> professorList = new ArrayList<>();
        for (Professor p : UserDatabase.getInstance().getProfessors()){
            if (p.getCollege() == college){
                professorList.add(new shared.model.users.Professor(p.getFirstName() ,p.getLastName() ,p.getUserCode()));
            }
        }
        response.addData("professorList" , professorList);

        return response;
    }

    public Response addUser(Request request){
        Response response = null;
        MainMenuAssistantController controller = (MainMenuAssistantController) mainMenuController;
        int state;
        if (request.getData("student") != null) {
            shared.model.users.Student student = (shared.model.users.Student) request.getData("student");
            state = controller.addStudent(student);
        }
        else{
            shared.model.users.Professor professor = (shared.model.users.Professor) request.getData("professor");
            state = controller.addProfessor(professor);
        }
        switch (state){
            case 1:
                response = new Response(ResponseState.ERROR , "Username is already taken!");
                break;
            case 2:
                response = new Response(ResponseState.ERROR , "Fill all empty fields!");
                break;
            case 3:
                response = new Response(ResponseState.OK , "Done!");
        }

        return response;
    }

    public Response editUser(Request request){
        MainMenuBossController controller = (MainMenuBossController) mainMenuController;
        long professor = (long) request.getData("professor");
        shared.model.users.Professor newProfessor = (shared.model.users.Professor) request.getData("newProfessor");
        if (controller.editProfessor(professor , newProfessor)){
            return new Response(ResponseState.OK , "Done!");
        }
        return new Response(ResponseState.ERROR , "Username is already taken");
    }

    public Response removeUser(Request request){
        MainMenuBossController controller = (MainMenuBossController) mainMenuController;
        controller.removeProfessor((long) request.getData("user"));
        return new Response(ResponseState.OK , "Done!");
    }

    public Response getAddLesson(){
        Response response = new Response(ResponseState.OK , null);
        College college = user.getCollege();

        List<shared.model.users.Professor> professorList = new ArrayList<>();
        for (Professor p : UserDatabase.getInstance().getProfessors()){
            if (p.getCollege() == college){
                professorList.add(new shared.model.users.Professor(p.getFirstName() ,p.getLastName() ,p.getUserCode()));
            }
        }
        response.addData("professorList" , professorList);

        List<shared.model.Lesson> lessonList = new ArrayList<>();
        for (Lesson l : LessonDatabase.getInstance().getLessons()){
            lessonList.add(new shared.model.Lesson(l.getName() , l.getCollege().getName() , -1 , null ,
                    null, null , -1 , l.getNumber()));
        }
        response.addData("lessonList" , lessonList);

        return response;
    }

    public Response addLesson(Request request){
        MainMenuAssistantController controller = (MainMenuAssistantController) mainMenuController;
        shared.model.Lesson lesson = (shared.model.Lesson) request.getData("lesson");
        if (controller.addLesson(lesson))
            return new Response(ResponseState.OK , "Done!");
        else
            return new Response(ResponseState.ERROR , "Fill all empty fields!");
    }

    public Response editLesson(Request request){
        int lessonNumber = (int) request.getData("lesson");
        shared.model.Lesson lesson = (shared.model.Lesson) request.getData("newLesson");
        if (((MainMenuAssistantController) mainMenuController).editLesson(lessonNumber , lesson))
            return new Response(ResponseState.OK , "Done!");

        return new Response(ResponseState.ERROR , "Error!");
    }

    public Response removeLesson(Request request){
        int lesson = (int) request.getData("lesson");
        ((MainMenuAssistantController) mainMenuController).removeLesson(lesson);
        return new Response(ResponseState.OK , "Done!");
    }

    public Response getRequest(){
        Response response = new Response(ResponseState.OK , null);
        List<shared.model.educaionalrequests.EducationalRequest> educationalRequests = new ArrayList<>();
        for (EducationalRequest r : user.getRequests()){
            shared.model.educaionalrequests.EducationalRequest educationalRequest = null;
            if (r instanceof Recommendation) {
                educationalRequest = new shared.model.educaionalrequests.EducationalRequest(RequestType.Recommendation);
                educationalRequest.setProfessor(((Recommendation) r).getProfessor().getName());
            }
            if (r instanceof BusyStudying)
                educationalRequest = new shared.model.educaionalrequests.EducationalRequest(RequestType.BUSY_STUDYING);
            if (r instanceof Cancel)
                educationalRequest = new shared.model.educaionalrequests.EducationalRequest(RequestType.CANCEL);
            if (r instanceof Dorm)
                educationalRequest = new shared.model.educaionalrequests.EducationalRequest(RequestType.DORM);
            if (r instanceof Defence)
                educationalRequest = new shared.model.educaionalrequests.EducationalRequest(RequestType.DEFENCE);
            if (r instanceof Minor) {
                educationalRequest = new shared.model.educaionalrequests.EducationalRequest(RequestType.MINOR);
                educationalRequest.setCollege(((Minor) r).getGoalAssistant().getName());
                List<String> collegeList = CollegeDatabase.getInstance().getCollegesName();
                response.addData("collegeList" , collegeList);
            }
            educationalRequest.setStudent(r.getStudent().getName());
            educationalRequest.setStatus(r.getStatus());
            educationalRequest.setMessage(r.getMessage());
            if (user instanceof Professor) {
                educationalRequest.setStudentCode(r.getStudent().getUserCode());
                educationalRequest.setRequestNumber(r.getRequestNumber());
            }
            educationalRequests.add(educationalRequest);
        }
        response.addData("educationalRequestList" , educationalRequests);
        return response;
    }

    public Response addRequest(Request request){
        MainMenuStudentController controller = (MainMenuStudentController) mainMenuController;
        shared.model.educaionalrequests.EducationalRequest educationalRequest = (shared.model.educaionalrequests.EducationalRequest) request.getData("educationalRequest");
        switch (educationalRequest.getRequestType()){
            case DORM:
                controller.addDorm();
                break;
            case DEFENCE:
                controller.addDefence();
                break;
            case Recommendation:
                controller.addRecommendation(educationalRequest.getProfessor());
                break;
            case BUSY_STUDYING:
                controller.addBusyStudying();
                break;
            case MINOR:
                if (!controller.addMinor(user.getCollege() , educationalRequest.getCollege()))
                    return new Response(ResponseState.ERROR , "Educational Assistant not found!");
                break;
            case CANCEL:
                if (!controller.addCancel())
                    return new Response(ResponseState.ERROR , "Educational Assistant not found!");
        }
        return new Response(ResponseState.OK , "Done!");
    }

    public Response handelRequest(Request request){
        MainMenuProfessorController controller = (MainMenuProfessorController) mainMenuController;
        shared.model.educaionalrequests.EducationalRequest educationalRequest = (shared.model.educaionalrequests.EducationalRequest) request.getData("educationalRequest");
        boolean confirmed = (boolean) request.getData("confirmed");
        switch (educationalRequest.getRequestType()){
            case Recommendation:
                controller.handelRecommendation(educationalRequest.getRequestNumber() , confirmed);
                break;
            case CANCEL:
                ((MainMenuAssistantController) controller).handelCancel(educationalRequest.getRequestNumber() , confirmed);
                break;
            case MINOR:
                ((MainMenuAssistantController) controller).handelMinor(educationalRequest.getRequestNumber() , confirmed);
                break;
        }
        return new Response(ResponseState.OK , "Done!");
    }

    public Response getAllChats(){
        Response response = new Response(ResponseState.OK , null);
        LinkedList<shared.model.chatroom.Chat> chatList = new LinkedList<>();
        for (Chat chat : ChatDatabase.getInstance().getAllChats(user)){
            LinkedList<shared.model.chatroom.Message> messages = new LinkedList<>();
            for (ChatMessage m : chat.getMessages())
                messages.add(new shared.model.chatroom.Message(m.getType() ,m.getSender().getName() , m.getText() , m.getTime() , m.getMedia()));
            User otherUser = chat.getOtherUser(user.getUserCode());
            shared.model.chatroom.Chat c = new shared.model.chatroom.Chat(otherUser.getUserCode() ,otherUser.getName() ,messages);
            c.setImage(resourceManager.getUserImage(otherUser.getUserCode()));
            chatList.add(c);
        }
        response.addData("chatList" , chatList);
        return response;
    }

    public Response getChat(Request request){
        Response response = new Response(ResponseState.OK , null);
        long userCode = (long) request.getData("code");
        User user = UserDatabase.getInstance().getUser(userCode);
        Chat chat = ChatDatabase.getInstance().getChat(user , this.user);

        LinkedList<shared.model.chatroom.Message> messages = new LinkedList<>();//todo one function
        for (ChatMessage m : chat.getMessages())
            messages.add(new shared.model.chatroom.Message(m.getType() , m.getSender().getName() , m.getText() , m.getTime() , m.getMedia()));
        shared.model.chatroom.Chat c = new shared.model.chatroom.Chat(userCode , user.getName(), messages);
        c.setImage(resourceManager.getUserImage(userCode));
        response.addData("chat" , c);
        return response;
    }

    public Response getCreateChat(Request request){
        Response response = new Response(ResponseState.OK , null);
        List<shared.model.users.User> userList = new ArrayList<>();
        if (user instanceof Student) {
            for (Student s : ((MainMenuStudentController)mainMenuController).getStudentsSameCollegeAndEntryYear())
                userList.add(new shared.model.users.Student(s.getFirstName() , s.getLastName() , s.getUserCode()));

            userList.add(new shared.model.users.Professor(((Student) user).getSupervisor().getFirstName() ,
                    ((Student) user).getSupervisor().getLastName() , ((Student) user).getSupervisor().getUserCode()));
        }
        else if (user instanceof EducationalAssistant){
            for (Student s : user.getCollege().getStudentList())
                userList.add(new shared.model.users.Student(s.getFirstName() , s.getLastName() , s.getUserCode()));
        }
        else if (user instanceof  Professor){
            for (Student s : ((MainMenuProfessorController)mainMenuController).getStudentsWithSupervisor())
                userList.add(new shared.model.users.Student(s.getFirstName() , s.getLastName() , s.getUserCode()));
        }

        else if (user instanceof AdminEdu){
            for (Student s : UserDatabase.getInstance().getStudents())
                userList.add(new shared.model.users.Student(s.getFirstName() , s.getLastName() , s.getUserCode()));
        }
        else if (user instanceof Mohseni){
            Integer entryYear = (Integer) request.getData("entryYear");
            String grade = (String) request.getData("grade");
            String college = (String) request.getData("college");
            List<shared.model.users.Student> studentList = new ArrayList<>();
            for (Student s : ((MainMenuMohseniController)mainMenuController).getCreateChat(
                    entryYear , grade , college) ){
                shared.model.users.Student student = new shared.model.users.Student(s.getCollege().getName(),null ,
                        null ,null, s.getFirstName() , s.getLastName() , s.getEmail() , null ,
                        null , s.getUserCode() , s.getEntryYear() , null , null);
                studentList.add(student);
            }
            response.addData("studentList" , studentList);
            List<String> collegeList = CollegeDatabase.getInstance().getCollegesName();
            response.addData("collegeList" , collegeList);
            return response;
        }
        List<AdminEdu> adminEduList = UserDatabase.getInstance().getAdmins();
        for (AdminEdu a : adminEduList)
            userList.add(new shared.model.users.AdminEdu(a.getFirstName() , a.getLastName() , a.getUserCode()));
        response.addData("userList", userList);
        return response;
    }

    public Response getSearch(Request request){
        Response response = new Response(ResponseState.OK , null);
        String code = (String) request.getData("code");
        List<shared.model.users.Student> studentList = new ArrayList<>();
        for (Student s : ((MainMenuMohseniController)mainMenuController).getSearch(code)){
            shared.model.users.Student student = new shared.model.users.Student(s.getCollege().getName(),null ,
                    null ,null, s.getFirstName() , s.getLastName() , s.getEmail() , s.getMobileNumber() ,
                    s.getNationalCode() , s.getUserCode() , s.getEntryYear() , s.getGrade() , s.getStatus());
            student.setSupervisor(s.getSupervisor() == null ? "Unknown" : s.getSupervisor().getName());
            student.setImage(resourceManager.getUserImage(user.getUserCode()));
            studentList.add(student);
        }
        response.addData("studentList" , studentList);
        return response;
    }

    public Response sendMessage(Request request){
        List<Long> userList = (List<Long>) request.getData("userList");
        String messageStr = (String) request.getData("message");
        Media media = (Media) request.getData("media");
        List<User> users = userList.stream()
                .map(u -> UserDatabase.getInstance().getUser(u)).collect(Collectors.toList());
        mainMenuController.sendMessage(users , messageStr , media);
        return new Response(ResponseState.OK , "Done!");
    }

    public Response sendRequestToMessage(Request request){
        String code = (String) request.getData("code");
        switch (mainMenuController.sendRequestToMessage(code)){
            case 1:
                return new Response(ResponseState.ERROR , "input should be number");
            case 2:
                return new Response(ResponseState.ERROR , "User not found!");
            default:
                return new Response(ResponseState.OK , "Done!");
        }
    }

    public Response getNotification(){
        Response response = new Response(ResponseState.OK , null);
        response.addData("notificationList" , user.getNotifications());
        return response;
    }

    public Response getStudents(){
        Response response = new Response(ResponseState.OK , null);
        List<shared.model.users.Student> studentList = new ArrayList<>();
        List<Student> students = user.getCollege().getStudentList();
        for (Student s : students){
            shared.model.users.Student student = new shared.model.users.Student(null , null , null ,
                    null , s.getFirstName() , s.getLastName(), null , null , null ,
                    s.getUserCode() , s.getEntryYear() , s.getGrade() , s.getStatus());
            student.setChooseLessonTime(student.getChooseLessonTime());
            studentList.add(student);
        }
        response.addData("studentList" , studentList);
        return response;
    }

    public Response addLessonTime(Request request){
        long userCode = (long) request.getData("code");
        Time time = (Time) request.getData("time");
        MainMenuAssistantController controller = (MainMenuAssistantController) mainMenuController;
        controller.addLessonTime(userCode , time);
        return new Response(ResponseState.OK , "Done!");
    }

    public Response getChooseLesson(Request request){
        String collegeName = (String) request.getData("college");
        List<Lesson> lessons;
        if (collegeName.equals("Suggested"))
           lessons = ((MainMenuStudentController) mainMenuController).getSuggestedLesson();
        else {
            College college = CollegeDatabase.getInstance().getCollege(collegeName);
            lessons = college.getLessonList()
                    .stream().filter( l-> l.getTerm() == ChooseLessonController.getTerm())
                    .collect(Collectors.toList());
        }

        List<shared.model.Lesson> lessonList = new ArrayList<>();
        for (Lesson l : lessons){
            lessonList.add(new shared.model.Lesson(l.getName() , collegeName , l.getNumberOfUnits(), l.getGrade() ,
                    l.getProfessor().getName() , l.getTime() , l.getCapacity() , l.getNumber()));
        }
        List<shared.model.Lesson> allLessons = LessonDatabase.getInstance().getLessons().stream()
                .map(l-> new shared.model.Lesson(l.getName() , collegeName , l.getNumberOfUnits(), l.getGrade() ,
                        l.getProfessor().getName() , l.getTime() , l.getCapacity() , l.getNumber()))
                .collect(Collectors.toList());
        Response response = new Response(ResponseState.OK , null);
        response.addData("lessonList" , lessonList);
        response.addData("allLessons" , allLessons);

        List<String> collegeList = CollegeDatabase.getInstance().getCollegesName();
        response.addData("collegeList" , collegeList);

        ChooseLessonController controller = LessonDatabase.getInstance().getChooseLessonController();
        if (controller != null)
            response.addData("lessonsTook" , controller.getLessons(user.getUserCode()));

        return response;
    }

   public Response handelNotification(Request request){
       Notification notification = (Notification) request.getData("notification");
       Boolean yes = (Boolean) request.getData("yes");
       user.removeNotification(user.getNotification(notification.getCode()));
       if (yes != null){
           switch (notification.getType()){
               case SEND_MESSAGE:
                   mainMenuController.handelSendMessage(notification , yes);
                   break;
               case TAKE_LESSON:
                   ((MainMenuAssistantController) mainMenuController).handelTakeLesson(notification , yes);
                   break;
           }
       }
       return new Response(ResponseState.OK , "Done!");
   }

   public Response takeLesson(Request request){
       int lessonNumber = (int) request.getData("lesson");
       boolean isTake = (boolean) request.getData("take");
       ChooseLessonController controller = LessonDatabase.getInstance().getChooseLessonController();
       if (isTake) {
           int state = controller.addLesson(user.getUserCode(), lessonNumber , false);
           switch (state) {
               case 0:
                   return new Response(ResponseState.OK, "Done!");
               case 1:
                   return new Response(ResponseState.ERROR, "The capacity of lesson is complete.");
               case 2:
                   return new Response(ResponseState.ERROR, "Prerequisite not met");
               case 3:
                   return new Response(ResponseState.ERROR , "Requirement not met");
               case 4:
                   return new Response(ResponseState.ERROR, "Class time interference!");
               case 5:
                   return new Response(ResponseState.ERROR, "Final time interference!");
               case 6:
                   return new Response(ResponseState.ERROR, "Ma`aref extra lesson!");
           }
       }
       else{
           controller.removeLesson(user.getUserCode() , lessonNumber);
           return new Response(ResponseState.OK , "Done!");
       }
       return null;
   }

   public Response takeLessonFromAssistant(Request request){
        int lessonNumber = (int) request.getData("lesson");
       ((MainMenuStudentController) mainMenuController).takeLesson(lessonNumber , (Student) user);
       return new Response(ResponseState.OK , "Done!");
   }

   public Response setEndChooseLessonTime(Request request){
       Time time = (Time) request.getData("time");
        if (((MainMenuAssistantController) mainMenuController).setEndChooseLessonTime(time))
            return new Response(ResponseState.OK , "Done!");

        return new Response(ResponseState.ERROR ,
                "End time had been set : " + LessonDatabase.getInstance().getChooseLessonController().getFinish());
   }

   public Response getOfflineData(){
        Response response = new Response(ResponseState.OK , null);
        if (!(user instanceof AdminEdu || user instanceof Mohseni)) {
            response.addData("examList", getExamList());
            response.addData("weeklySchedule", getLessonInWeek());
        }
        response.addData("user" , getUser());
        response.addData("chatroom" , getAllChats());
        if(user instanceof Student)
            response.addData("educationalStatus" , getEducationalStatus(null));

        return response;
   }

   public Response getAllCourseware(){
        List<shared.model.courseware.Courseware> coursewareList = mainMenuController.getLesson().stream()
                .map(l -> LessonDatabase.getInstance().getCourseware(l.getNumber()))
                .filter(Objects::nonNull)
                .map( c-> new shared.model.courseware.Courseware(c.getLesson().getName() , c.getLesson().getNumber() ,
                        false , c.getExercises() , c.getEducationalMaterials()))
                .collect(Collectors.toList());
        Response response = new Response(ResponseState.OK , null);
        if (user instanceof Student){
            List<shared.model.courseware.Courseware> coursewareListTA = LessonDatabase.getInstance()
                    .coursewareListTA((Student) user).stream()
                    .map( c-> new shared.model.courseware.Courseware(c.getLesson().getName() , c.getLesson().getNumber() ,
                            true , c.getExercises() , c.getEducationalMaterials()))
                    .collect(Collectors.toList());
            coursewareList.addAll(coursewareListTA);
        }
        response.addData("coursewareList" , coursewareList);
        response.addData("isProfessor" , user instanceof Professor);
        return response;
   }

   public Response getCourseware(Request request){
        int lessonCode = (int) request.getData("code");
        Response response = new Response(ResponseState.OK , null);
        Courseware courseware = LessonDatabase.getInstance().getCourseware(lessonCode);
        shared.model.courseware.Courseware c = new shared.model.courseware.Courseware(
                courseware.getLesson().getName() , lessonCode ,
                user instanceof  Student && courseware.getTeachingAssistants().contains((Student) user) ,//todo
                courseware.getExercises() , courseware.getEducationalMaterials());
        response.addData("courseware" , c);
        return response;
   }

   public Response addStudentToCourseware(Request request){
        String studentCode = (String) request.getData("studentCode");
        int lessonCode = (int) request.getData("lessonCode");
        boolean isTA = (boolean) request.getData("isTA");
       int state = ((MainMenuProfessorController)mainMenuController).addStudentToCourseware(studentCode , isTA , lessonCode);
       switch (state){
           case 1:
               return new Response(ResponseState.ERROR , "Wrong code format!");
           case 2:
               return new Response(ResponseState.ERROR , "User not found!");
           case 0:
               return new Response(ResponseState.OK , "Done!");
       }
       return null;
   }

   public Response editEducationalMaterial(Request request){
        boolean remove = (boolean) request.getData("remove");
        int lessonNumber = (int) request.getData("lesson");
        EducationalMaterial educationalMaterial = (EducationalMaterial) request.getData("educationalMaterial");
        int state = mainMenuController.editEducationalMaterial(lessonNumber , remove , educationalMaterial);
        switch (state){
            case 1:
                return new Response(ResponseState.ERROR , "The number of files exceeds the limit");
            case 0:
                return new Response(ResponseState.OK , "Done!");
        }
        return null;
   }

   public Response editExercise(Request request){
       boolean remove = (boolean) request.getData("remove");
       int lessonNumber = (int) request.getData("lesson");
       Exercise exercise = (Exercise) request.getData("exercise");
       mainMenuController.editExercise(lessonNumber , remove , exercise);
       return new Response(ResponseState.OK , "Done!");
   }

   public Response addEducationalMaterial(Request request){
        int lessonNumber = (int) request.getData("lesson");
        EducationalMaterial educationalMaterial = (EducationalMaterial) request.getData("educationalMaterial");
        mainMenuController.addEducationalMaterial(lessonNumber , educationalMaterial);
        return new Response(ResponseState.OK , "Done!");
   }

   public Response addExercise(Request request){
        int lesson = (int) request.getData("lesson");
       Exercise exercise = (Exercise) request.getData("exercise");
       mainMenuController.addExercise(lesson , exercise);
       return new Response(ResponseState.OK , "Done!");
   }

   public Response getCalender(){
       LinkedHashMap<Time , String> calender = mainMenuController.getCalender();
       Response response = new Response(ResponseState.OK , null);
       response.addData("calender" , calender);
       return response;
   }

   public Response getEducationalStatus(Request request){
        Response response = new Response(ResponseState.OK , null);
        if (user instanceof Student){
            List<shared.model.Lesson> lessonList = ((Student) user).getFinalScoreLessons().stream()
                    .map( l-> new shared.model.Lesson(l.getName() , l.getCollege().getName() , l.getNumberOfUnits() ,
                            l.getGrade() , l.getProfessor().getName() , l.getTime() , l.getCapacity() , l.getNumber()))
                    .collect(Collectors.toList());
            response.addData("lessonList" , lessonList);
            response.addData("scoreMap" , ((Student) user).getFinalScores());
            response.addData("numberOfUnitPassed" , ((Student) user).getNumberOfUnitPassed());
            response.addData("average" , ((Student) user).getTotalAverage());
        }
        else{
            String userCode = (String) request.getData("code");
            Student student;
            if (userCode != null) {
                if (!mainMenuController.isNumber(userCode))
                    return new Response(ResponseState.ERROR, "Wrong format of code!");
                student = UserDatabase.getInstance().getStudent(Integer.parseInt(userCode));
            }
            else{
                String firstName = (String) request.getData("firstName");
                String lastName = (String) request.getData("lastName");
                student = UserDatabase.getInstance().getStudentByName(firstName + " " + lastName);
            }
            if (student == null)
                return new Response(ResponseState.ERROR, "User not found!");
            shared.model.users.Student s =
                    new shared.model.users.Student(student.getFirstName() , student.getLastName() , student.getUserCode());
            s.setTotalAverage(student.getTotalAverage());
            response.addData("student" , s);
            List<shared.model.Lesson> lessonList = student.getFinalScoreLessons().stream()
                    .map(l -> new shared.model.Lesson(l.getName(), l.getProfessor().getName(), null, l.getNumber()))
                    .collect(Collectors.toList());
            response.addData("lessonList" , lessonList);
            response.addData("scoreMap" , (student).getTemporaryScores());
            response.addData("numberOfUnitsPassed" , student.getNumberOfUnitPassed());
        }
        return response;
   }

   public Response getTemporaryScores(Request request){
        Response response = new Response(ResponseState.OK , null);
        if (user instanceof Student) {
            List<shared.model.Lesson> lessonList = ((Student) user).getTemporaryScoreLessons().stream()
                    .map(l -> new shared.model.Lesson(l.getName(), l.getProfessor().getName(), l.getTime(), l.getNumber()))
                    .collect(Collectors.toList());
            response.addData("lessonList" , lessonList);
            Map<Integer , Protest> protestMap = new HashMap<>();
            for (Lesson l : ((Student) user).getTemporaryScoreLessons())
                protestMap.put(l.getNumber() , RequestDatabase.getInstance().getProtest(user.getUserCode() , l.getNumber()));
            response.addData("protestMap" , protestMap);
            response.addData("scoreMap" , ((Student) user).getTemporaryScores());
        }

        else  if (user instanceof EducationalAssistant){
            String userCode = (String) request.getData("code");
            if (userCode != null) {
                if (!mainMenuController.isNumber(userCode))
                    return new Response(ResponseState.ERROR, "Wrong format of code!");
                Student student = UserDatabase.getInstance().getStudent(Integer.parseInt(userCode));
                if (student == null)
                    return new Response(ResponseState.ERROR, "User not found!");
                response.addData("student" ,
                        new shared.model.users.Student(student.getFirstName() , student.getLastName() , student.getUserCode()));
                List<shared.model.Lesson> lessonList = (student).getTemporaryScoreLessons().stream()
                        .map(l -> new shared.model.Lesson(l.getName(), l.getProfessor().getName(), null, l.getNumber()))
                        .collect(Collectors.toList());
                response.addData("lessonList" , lessonList);
                Map<Integer , Protest> protestMap = new HashMap<>();
                for (Lesson l : (student).getTemporaryScoreLessons())
                    protestMap.put(l.getNumber() , RequestDatabase.getInstance().getProtest(student.getUserCode() , l.getNumber()));
                response.addData("protestMap" , protestMap);
                response.addData("scoreMap" , (student).getTemporaryScores());
            }
            else{
                String firstName = (String) request.getData("firstName");
                String lastName = (String) request.getData("lastName");
                Professor professor = UserDatabase.getInstance().getProfessorByName(firstName + " " + lastName);
                if (professor == null)
                    return new Response(ResponseState.ERROR , "User not found!");
                List<shared.model.Lesson> lessonList = professor.getLesson().stream()
                        .map(l-> new shared.model.Lesson(l.getName() , professor.getName() , null , l.getNumber()))
                        .collect(Collectors.toList());
                response.addData("lessonList" , lessonList);
                Map<Integer , List<shared.model.users.Student>> studentMap = new HashMap<>();
                Map<Integer , Map<Long , Protest>> protestMap = new HashMap<>();
                Map<Integer , Map<Long , Double>> scoreMap = new HashMap<>();
                for (Lesson l : professor.getLesson()){
                    List<Student> students = l.getStudents().stream()
                            .filter(s-> s.getTemporaryScoreLessons().contains(l)).collect(Collectors.toList());
                    List<shared.model.users.Student> studentList = students.stream()
                            .map(s -> new shared.model.users.Student(s.getFirstName() , s.getLastName() , s.getUserCode()))
                            .collect(Collectors.toList());
                    Map<Long , Protest> longProtestMap = new HashMap<>();
                    Map<Long , Double> longScoreMap = new HashMap<>();
                    for (Student s : students){
                        longProtestMap.put(s.getUserCode() , RequestDatabase.getInstance().getProtest(s.getUserCode() , l.getNumber()));
                        longScoreMap.put(s.getUserCode() , s.getScore(l.getNumber()));
                    }
                    studentMap.put(l.getNumber() , studentList);
                    protestMap.put(l.getNumber() , longProtestMap);
                    scoreMap.put(l.getNumber() , longScoreMap);
                }
                response.addData("studentMap" , studentMap);
                response.addData("protestMap" , protestMap);
                response.addData("scoreMap" , scoreMap);
            }
        }

        else {
            List<shared.model.Lesson> lessonList = mainMenuController.getLesson().stream()
                    .map(l-> new shared.model.Lesson(l.getName() , null , null , l.getNumber()))
                    .collect(Collectors.toList());
            response.addData("lessonList" , lessonList);
            Map<Integer , List<shared.model.users.Student>> studentMap = new HashMap<>();
            Map<Integer , Map<Long , Protest>> protestMap = new HashMap<>();
            Map<Integer , Map<Long , Double>> scoreMap = new HashMap<>();
            for (Lesson l : mainMenuController.getLesson()){
                List<Student> students = l.getStudents().stream()
                        .filter(s-> s.getTemporaryScoreLessons().contains(l)).collect(Collectors.toList());
                List<shared.model.users.Student> studentList = students.stream()
                                        .map(s -> new shared.model.users.Student(s.getFirstName() , s.getLastName() , s.getUserCode()))
                                                .collect(Collectors.toList());
                Map<Long , Protest> longProtestMap = new HashMap<>();
                Map<Long , Double> longScoreMap = new HashMap<>();
                for (Student s : students){
                    longProtestMap.put(s.getUserCode() , RequestDatabase.getInstance().getProtest(s.getUserCode() , l.getNumber()));
                    longScoreMap.put(s.getUserCode() , s.getScore(l.getNumber()));
                }
                studentMap.put(l.getNumber() , studentList);
                protestMap.put(l.getNumber() , longProtestMap);
                scoreMap.put(l.getNumber() , longScoreMap);
            }
            response.addData("studentMap" , studentMap);
            response.addData("protestMap" , protestMap);
            response.addData("scoreMap" , scoreMap);
        }
        return response;
   }

   public Response addProtest(Request request){
        int lessonNumber = (int) request.getData("code");
        String text = (String) request.getData("text");
       ((MainMenuStudentController) mainMenuController).addProtest(lessonNumber , text);
       return new Response(ResponseState.OK , "Done!");
   }

   public Response setRegisterProtest(Request request){
       int lessonNumber = (int) request.getData("lesson");
       String text = (String) request.getData("text");
       long userCode = (long) request.getData("code");
       if (((MainMenuProfessorController) mainMenuController).setReply(lessonNumber , userCode, text))
           return new Response(ResponseState.OK , "Done!");
       return new Response(ResponseState.ERROR , "There is no protest!");
   }

   public Response registerScores(Request request){
       int lessonNumber = (int) request.getData("lesson");
       Map<Long , String> scoreMap = (Map<Long, String>) request.getData("scoreMap");
       boolean isTemporary = (boolean) request.getData("isTemporary");
       if(((MainMenuProfessorController) mainMenuController).setScores(lessonNumber , scoreMap , isTemporary))
           return new Response(ResponseState.OK , "Done!");
       return new Response(ResponseState.ERROR , "Fill fields truly!");
   }

   public Response getSolution(Request request){
        Response response = new Response(ResponseState.OK , null);
        int lessonNumber = (int) request.getData("lesson");
        Courseware courseware = LessonDatabase.getInstance().getCourseware(lessonNumber);
        Lesson lesson = LessonDatabase.getInstance().getLesson(lessonNumber);
        String exerciseName = (String) request.getData("exercise");
        if (user instanceof Student){
            if (courseware.isTA(user.getUserCode())){
                List<Solution> solutionList = LessonDatabase.getInstance().getSolutionList(lessonNumber , exerciseName);
                response.addData("solutionList" , solutionList);
            }
            else{
                Solution solution = LessonDatabase.getInstance().getSolution(lessonNumber , exerciseName , user.getUserCode());
                response.addData("solution" , solution);
            }
        }
        else{
            Map<Long , Solution> solutionMap = new HashMap<>();
            Map<Long , String> nameMap = new HashMap<>();
            for (Student s : lesson.getStudents()){
                solutionMap.put(s.getUserCode() ,
                        LessonDatabase.getInstance().getSolution(lessonNumber , exerciseName , s.getUserCode()));
                nameMap.put(s.getUserCode() , s.getName());
            }
            response.addData("solutionMap" , solutionMap);
            response.addData("nameMap" , nameMap);
        }
        return response;
   }

   public Response addSolution(Request request){
        int lessonNumber = (int) request.getData("lesson");
        String exercise = (String) request.getData("exercise");
        String text = (String)  request.getData("text");
        Media media = (Media) request.getData("media");
        int state = ((MainMenuStudentController) mainMenuController).addSolution(lessonNumber , exercise , text , media);
        switch (state){
            case 1:
                return new Response(ResponseState.ERROR , "The submission time has expired");
            case 2:
                return new Response(ResponseState.ERROR , "The submission time has not started");
            case 3:
                return new Response(ResponseState.OK , "Done! your solution changed");
            case 0:
                return new Response(ResponseState.OK , "Done!");
        }
        return null;
   }

   public Response registerScoreSolution(Request request){
       int lessonNumber = (int) request.getData("lesson");
       String exercise = (String) request.getData("exercise");
       long userCode = (long)  request.getData("code");
       String score = (String) request.getData("score");
       int state = mainMenuController.registerScoreSolution(lessonNumber , exercise, userCode , score);
       switch (state){
           case 1 :
               return new Response(ResponseState.ERROR , "Wrong score format!");
           case 0:
               return new Response(ResponseState.OK , "Done!");
       }
       return null;
   }
}