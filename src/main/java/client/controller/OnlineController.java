package client.controller;

import shared.util.Loop;
import shared.model.*;
import shared.model.courseware.EducationalMaterial;
import shared.model.courseware.Exercise;
import shared.model.educaionalrequests.EducationalRequest;
import shared.model.media.Media;
import shared.model.notification.Notification;
import shared.model.users.*;
import shared.request.Request;
import shared.request.RequestType;
import shared.response.Response;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OnlineController implements GraphicalAgent{
    private ServerController serverController;
    private Loop panelLoop;

    public void setServerController(ServerController serverController){
        this.serverController = serverController;
    }

    void setLoop(Loop loop){
        stopLoop();
        panelLoop = loop;
        panelLoop.start();
    }

    void stopLoop(){
        if (panelLoop != null)
            panelLoop.stop();
    }

    Response getOfflineData(){
        Request request = new Request(RequestType.GET_OFFLINE_DATA);
        return serverController.sendMessage(request);
    }

    @Override
    public void setCloseFrame() {
        Request request = new Request(RequestType.CLOSE);
        serverController.sendMessage(request);
        serverController.closeSocket();
    }

    @Override
    public Response login(String username , String password) {
        Request request = new Request(RequestType.LOGIN);
        request.addData("username" , username);
        request.addData("password" , password);
        return serverController.sendMessage(request);
    }

    @Override
    public User getUser() {
        Request request = new Request(RequestType.GET_USER);
        Response response = serverController.sendMessage(request);
        return (User) response.getData("user");
    }

    @Override
    public Response resetPassword(String lastPassword, String newPassword) {
        Request request = new Request(RequestType.RESET_PASSWORD);
        request.addData("lastPassword" , lastPassword);
        request.addData("newPassword" , newPassword);
        return serverController.sendMessage(request);
    }

    @Override
    public Response changeProfile(String email , String mobileNumber) {
        Request request = new Request(RequestType.CHANGE_PROFILE);
        request.addData("email" , email);
        request.addData("mobileNumber" , mobileNumber);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getSortedLessonInWeek() {
        Request request = new Request(RequestType.WEEKLY_SCHEDULE);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getExamList() {
        Request request = new Request(RequestType.EXAM_LIST);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getLessonList(boolean isSelectedGrade, boolean isSelectedProfessor, boolean isSelectedCollege,
                                  String grade, String professor, String college) {
        Request request = new Request(RequestType.LESSON_LIST);
        if (isSelectedGrade)
            request.addData("grade" , grade);
        if (isSelectedProfessor)
            request.addData("professor" , professor);
        if (isSelectedCollege)
            request.addData("college" , college);

        return serverController.sendMessage(request);
    }

    @Override
    public Response getProfessorList() {
        Request request = new Request(RequestType.PROFESSOR_LIST);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getAddUser() {
        Request request = new Request(RequestType.GET_ADD_USER);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getAddLesson() {
        Request request = new Request(RequestType.GET_ADD_LESSON);
        return serverController.sendMessage(request);
    }

    @Override
    public Response addStudent(String username, String password, String firstName, String lastName,
                               String email, String mobileNumber, String nationalCode,
                               String grade, int entryYear, String status , String supervisor) {
        Request request = new Request(RequestType.ADD_USER);
        Student student = new Student(null , null , username , password , firstName , lastName,
                email , mobileNumber , nationalCode , -1 , entryYear
                , Grade.getEnum(grade) , EducationalStatus.getEnum(status));
        student.setSupervisor(supervisor);
        request.addData("student" , student);
        return serverController.sendMessage(request);
    }

    @Override
    public Response addProfessor(String username, String password, String firstName, String lastName, String email,
                                 String mobileNumber, String nationalCode, String roomNumber,
                                 String degree, List<Lesson> lessonList) {
        Request request = new Request(RequestType.ADD_USER);
        Professor professor = new Professor(null , null , username , password , firstName , lastName,
                email , mobileNumber , nationalCode , -1 , roomNumber , ProfessorDegree.getEnum(degree));
        professor.setAvailableLessons(lessonList);
        request.addData("professor" , professor);
        return serverController.sendMessage(request);
    }

    @Override
    public Response addLesson(String name, int numberOfUnits, int finalDateMonth, int finalDateDay, int finalTimeHour,
                              int starHour, int endHour, int capacity, List<DayOfWeek> daysOfWeek, String grade,
                              String professor, List<Lesson> prerequisites, List<Lesson> requirements, int term) {
        LessonTime lessonTime = new LessonTime(daysOfWeek , starHour ,endHour ,finalDateMonth ,finalDateDay ,finalTimeHour);
        Lesson lesson = new Lesson(name , null , numberOfUnits , Grade.getEnum(grade) ,
                professor, lessonTime,capacity , -1 );
        lesson.setPrerequisites(prerequisites);
        lesson.setRequirements(requirements);
        lesson.setTerm(term);

        Request request = new Request(RequestType.ADD_LESSON);
        request.addData("lesson" , lesson);
        return serverController.sendMessage(request);
    }


    @Override
    public Response editProfessor(long professor, String username, String password, String roomNumber) {
        Request request = new Request(RequestType.EDIT_USER);
        request.addData("professor" , professor);
        Professor p = new Professor(null , null , username , password , null ,null ,
                null , null , null , -1 , null , null);
        request.addData("newProfessor" , p);
        return serverController.sendMessage(request);
    }

    @Override
    public Response removeUser(long user) {
        Request request = new Request(RequestType.REMOVE_USER);
        request.addData("user" , user);
        return serverController.sendMessage(request);
    }

    @Override
    public Response editLesson(int lessonNumber, int numberOfUnit, int finalDateMonth, int finalDateDay, int finalTimeHour,
                               int starHour, int endHour, int capacity) {
        Request request = new Request(RequestType.EDIT_LESSON);
        request.addData("lesson" , lessonNumber);
        Lesson lesson = new Lesson(null , null ,
                new LessonTime(null , starHour , endHour , finalDateMonth , finalDateDay , finalTimeHour) ,lessonNumber);
        lesson.setNumberOfUnits(numberOfUnit);
        lesson.setCapacity(capacity);
        request.addData("newLesson" , lesson);
        return serverController.sendMessage(request);
    }

    @Override
    public Response removeLesson(int lesson) {
        Request request = new Request(RequestType.REMOVE_LESSON);
        request.addData("lesson" , lesson);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getRequest() {
        Request request = new Request(RequestType.GET_REQUEST);
        return serverController.sendMessage(request);
    }

    @Override
    public Response addRequest(EducationalRequest educationalRequest) {
        Request request = new Request(RequestType.ADD_REQUEST);
        request.addData("educationalRequest" , educationalRequest);
        return serverController.sendMessage(request);
    }

    @Override
    public Response handelRequest(EducationalRequest educationalRequest, boolean confirmed) {
        Request request = new Request(RequestType.HANDEL_REQUEST);
        request.addData("educationalRequest" , educationalRequest);
        request.addData("confirmed" , confirmed);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getAllChats() {
        Request request = new Request(RequestType.GET_All_CHATS);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getChat(long userCode) {
        Request request = new Request(RequestType.GET_CHAT);
        request.addData("code" , userCode);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getCreateChat() {
        Request request = new Request(RequestType.GET_CREATE_CHAT);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getCreateChatMohseni(boolean isSelectedEntryYear, boolean isSelectedGrade, boolean isSelectedCollege,
                                         int entryYear, String grade, String college) {
        Request request = new Request(RequestType.GET_CREATE_CHAT);
        if (isSelectedGrade)
            request.addData("grade" , grade);
        if (isSelectedEntryYear)
            request.addData("entryYear" , entryYear);
        if (isSelectedCollege)
            request.addData("college" , college);

        return serverController.sendMessage(request);
    }

    @Override
    public Response getSearch(String code) {
        Request request = new Request(RequestType.GET_SEARCH);
        request.addData("code" , code);
        return serverController.sendMessage(request);
    }

    @Override
    public Response sendMessage(String message, List<Long> users, Media media) {
        Request request = new Request(RequestType.SEND_MESSAGE);
        request.addData("userList" , users);
        request.addData("message" , message);
        request.addData("media" , media);
        request.addData("message" , message);
        return serverController.sendMessage(request);
    }

    @Override
    public Response sendRequestToMessage(String userCode) {
        Request request = new Request(RequestType.SEND_REQUEST);
        request.addData("code" , userCode);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getStudents() {
        Request request = new Request(RequestType.GET_STUDENTS);
        return serverController.sendMessage(request);
    }

    @Override
    public Response addLessonTime(long userCode , Time time) {
        Request request = new Request(RequestType.ADD_LESSON_TIME);
        request.addData("code" , userCode);
        request.addData("time" , time);
        return serverController.sendMessage(request);
    }

    @Override
    public Response setEndChooseLessonTime(Time time) {
        Request request = new Request(RequestType.SET_END_CHOOSE_LESSON_TIME);
        request.addData("time" , time);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getChooseLesson(String college) {
        Request request = new Request(RequestType.GET_CHOOSE_LESSON);
        request.addData("college" , college);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getNotification() {
        Request request = new Request(RequestType.GET_NOTIFICATION);
        return serverController.sendMessage(request);
    }

    @Override
    public Response handelNotification(Notification notification , Boolean yes) {
        Request request = new Request(RequestType.HANDEL_NOTIFICATION);
        request.addData("notification" , notification);
        request.addData("yes" , yes);
        return serverController.sendMessage(request);
    }

    @Override
    public Response takeLessonFromAssistant(int lesson) {
        Request request = new Request(RequestType.TAKE_LESSON_FROM_ASSISTANT);
        request.addData("lesson"  , lesson);
        return serverController.sendMessage(request);
    }

    @Override
    public Response takeLesson(int lesson) {
        Request request = new Request(RequestType.TAKE_LESSON);
        request.addData("lesson" , lesson);
        request.addData("take" , true);
        return serverController.sendMessage(request);
    }

    @Override
    public Response removeLessonFromChooseLesson(int lesson) {
        Request request = new Request(RequestType.TAKE_LESSON);
        request.addData("lesson" , lesson);
        request.addData("take" , false);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getAllCoursewares() {
        Request request = new Request(RequestType.GET_ALL_COURSEWARES);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getCourseware(int lesson) {
        Request request = new Request(RequestType.GET_COURSEWARE);
        request.addData("code" , lesson);
        return serverController.sendMessage(request);
    }

    @Override
    public Response addStudentToCourseware(String code, boolean isTA , int lesson) {
        Request request = new Request(RequestType.ADD_STUDENT_COURSEWARE);
        request.addData("studentCode" , code);
        request.addData("isTA" , isTA);
        request.addData("lessonCode" , lesson);
        return serverController.sendMessage(request);
    }

    @Override
    public Response editEducationalMaterial(int lesson, String name, String description, List<Media> mediaList) {
        Request request = new Request(RequestType.EDIT_EDUCATIONAL_MATERIAL);
        request.addData("remove" , false);
        EducationalMaterial educationalMaterial = new EducationalMaterial(name , description);
        mediaList.removeIf(Objects::isNull);
        educationalMaterial.setFiles(mediaList);
        request.addData("educationalMaterial" , educationalMaterial);
        request.addData("lesson" , lesson);
        return serverController.sendMessage(request);
    }

    @Override
    public Response editExercise(int lesson, String exerciseName, String description, Media media) {
        Request request = new Request(RequestType.EDIT_EXERCISE);
        request.addData("remove" , false);
        Exercise exercise = new Exercise(exerciseName , description ,
                null , null , null , null);
        exercise.setMedia(media);
        request.addData("lesson" , lesson);
        request.addData("exercise" , exercise);
        return serverController.sendMessage(request);
    }

    @Override
    public Response addEducationalMaterial(int lesson, String name, String description, List<Media> mediaList) {
        Request request = new Request(RequestType.ADD_EDUCATIONAL_MATERIAL);
        EducationalMaterial educationalMaterial = new EducationalMaterial(name , description);
        mediaList.removeIf(Objects::isNull);
        educationalMaterial.setFiles(mediaList);
        request.addData("lesson" , lesson);
        request.addData("educationalMaterial" , educationalMaterial);
        return serverController.sendMessage(request);
    }

    @Override
    public Response addExercise(int lesson, String name, String description, Media media,
                                Time startTime, Time endTime, Time uploadTime, UploadType uploadType) {
        Request request = new Request(RequestType.ADD_EXERCISE);
        Exercise exercise = new Exercise(name , description , startTime , endTime , uploadTime , uploadType);
        exercise.setMedia(media);
        request.addData("lesson" , lesson);
        request.addData("exercise" , exercise);
        return serverController.sendMessage(request);
    }

    @Override
    public Response removeEducationalMaterial(int lesson ,String name) {
        Request request = new Request(RequestType.EDIT_EDUCATIONAL_MATERIAL);
        request.addData("remove" , true);
        EducationalMaterial educationalMaterial = new EducationalMaterial(name , null);
        request.addData("educationalMaterial" , educationalMaterial);
        request.addData("lesson" , lesson);
        return serverController.sendMessage(request);
    }

    @Override
    public Response removeExercise(int lesson, String name) {
        Request request = new Request(RequestType.EDIT_EXERCISE);
        request.addData("remove" , true);
        Exercise exercise = new Exercise(name , null ,
                null , null , null , null);
        request.addData("exercise" , exercise);
        request.addData("lesson" , lesson);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getTemporaryScores() {
        Request request = new Request(RequestType.GET_TEMPORARY_SCORES);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getTemporaryScores(String userCode) {
        Request request = new Request(RequestType.GET_TEMPORARY_SCORES);
        request.addData("code" , userCode);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getTemporaryScores(String firstName, String lastName) {
        Request request = new Request(RequestType.GET_TEMPORARY_SCORES);
        request.addData("firstName" , firstName);
        request.addData("lastName" , lastName);
        return serverController.sendMessage(request);
    }

    @Override
    public Response addProtest(int lessonNumber, String text) {
        Request request = new Request(RequestType.ADD_PROTEST);
        request.addData("code" , lessonNumber);
        request.addData("text" , text);
        return serverController.sendMessage(request);
    }

    @Override
    public Response setRegister(int lessonNumber, String text, long userCode) {
        Request request = new Request(RequestType.SET_REGISTER_PROTEST);
        request.addData("lesson" , lessonNumber);
        request.addData("text" , text);
        request.addData("code" , userCode);
        return serverController.sendMessage(request);
    }

    @Override
    public Response registerScores(int lessonNumber, Map<Long, String> scoreMap, boolean isTemporary) {
        Request request = new Request(RequestType.REGISTER_SCORES);
        request.addData("lesson" , lessonNumber);
        request.addData("scoreMap" , scoreMap);
        request.addData("isTemporary" , isTemporary);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getEducationalStatus() {
        Request request = new Request(RequestType.GET_EDUCATIONAL_STATUS);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getEducationalStatus(String userCode) {
        Request request = new Request(RequestType.GET_EDUCATIONAL_STATUS);
        request.addData("code" , userCode);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getEducationalStatus(String firstName, String lastName) {
        Request request = new Request(RequestType.GET_EDUCATIONAL_STATUS);
        request.addData("firstName" ,firstName);
        request.addData("lastName" , lastName);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getCalender() {
        Request request = new Request(RequestType.GET_CALENDER);
        return serverController.sendMessage(request);
    }

    @Override
    public Response getSolution(int lesson, String exercise) {
        Request request = new Request(RequestType.GET_SOLUTION);
        request.addData("lesson" , lesson);
        request.addData("exercise" , exercise);
        return serverController.sendMessage(request);
    }

    @Override
    public Response addSolution(int lesson, String exercise, String text, Media media) {
        Request request = new Request(RequestType.ADD_SOLUTION);
        request.addData("lesson" , lesson);
        request.addData("exercise" , exercise);
        request.addData("text" , text);
        request.addData("media" , media);
        return serverController.sendMessage(request);
    }

    @Override
    public Response registerScoreSolution(int lesson, String exercise, long userCode, String score) {
        Request request = new Request(RequestType.REGISTER_SCORE_SOLUTION);
        request.addData("lesson" , lesson);
        request.addData("exercise" , exercise);
        request.addData("code" , userCode);
        request.addData("score" , score);
        return serverController.sendMessage(request);
    }
}
