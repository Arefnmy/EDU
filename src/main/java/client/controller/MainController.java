package client.controller;

import client.filemanager.ResourceManager;
import client.gui.LoginMenu;
import client.gui.MainFrame;
import client.gui.panels.MainMenu;
import client.gui.panels.adminpanel.MainMenuAdmin;
import client.gui.panels.assistantpanel.MainMenuAssistant;
import client.gui.panels.bosspanel.MainMenuBoss;
import client.gui.panels.mohsenipanel.MainMenuMohseni;
import client.gui.panels.professorpanel.MainMenuProfessor;
import client.gui.panels.studentpanel.MainMenuStudent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import shared.model.chatroom.Message;
import shared.util.Loop;
import shared.model.media.Media;
import shared.model.notification.Notification;
import shared.model.*;
import shared.model.educaionalrequests.EducationalRequest;
import shared.model.users.*;
import shared.response.Response;

import javax.swing.*;
import java.io.*;
import java.time.DayOfWeek;
import java.util.*;

public class MainController implements GraphicalAgent{
    private MainFrame mainFrame;
    private ServerController serverController;
    private final OnlineController onlineController;
    private final OfflineController offlineController;
    private GraphicalAgent controller;
    private boolean isOnline;
    private final Loop offlineDataLoop;
    private final ResourceManager resourceManager = ResourceManager.getInstance();

    private static final MainController MAIN_CONTROLLER = new MainController();
    private MainController(){
        onlineController = new OnlineController();
        offlineController = new OfflineController();
        offlineDataLoop = new Loop(resourceManager.getValue(Double.class , "offlineData-fps" , .5)
                , this::setOfflineData);
    }

    public static MainController getInstance(){
        return MAIN_CONTROLLER;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline){
        this.isOnline = isOnline;
        if (isOnline){
            controller = onlineController;
            mainFrame.setTitle(resourceManager.getGraphicConfig().getProperty("title" , "edu"));
        }
        else {
            stopLoop();
            controller = offlineController;
            changeContentPane(getMainMenu());
            mainFrame.setTitle(resourceManager.getGraphicConfig().getProperty("offlineTitle" , "edu(offline)"));
        }
    }

    public void setOfflineData(){
        Response response = onlineController.getOfflineData();
        if (response != null)
            offlineController.setOfflineData(response);
    }

    public void reconnect(){
        if (serverController.reconnect()){
            setOnline(true);
            logout();
        }
    }

    public void login(){
        offlineDataLoop.restart();
        sendMessagesToAdmin();
    }

    public void logout(){
        changeContentPane(new LoginMenu());
        setMenuBar(null);
    }

    private void sendMessagesToAdmin(){
        File file = resourceManager.getMessages(getUser().getUserCode() + ".txt");
        Gson gson = new GsonBuilder().create();
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                Message message = gson.fromJson(scanner.nextLine() , Message.class);
                onlineController.sendMessage(message.getText(), new ArrayList<>(List.of(1L)), message.getMedia());
            }
            scanner.close();
            file.delete();
        } catch (FileNotFoundException ignored) {}
    }

    public List<Integer> getMarkedLessons(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = resourceManager.getMarkedLessons(getUser().getUserCode() + ".json");
        try {
            Reader reader = new FileReader(file);
            return gson.fromJson(reader , new TypeToken<List<Integer>>(){}.getType());
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public void setMarkedLessons(List<Integer> lessons){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = resourceManager.getMarkedLessons(getUser().getUserCode() + ".json");
        try {
            FileWriter fileWriter = new FileWriter(file , false);
            fileWriter.write(gson.toJson(lessons));
            fileWriter.close();
        } catch (IOException ignored) {}
    }

    public void setMainFrame(MainFrame mainFrame){
        this.mainFrame = mainFrame;
    }

    public void setServerController(ServerController serverController){
        this.serverController = serverController;
        onlineController.setServerController(serverController);
    }

    public void stopLoop(){
        offlineDataLoop.stop();
        onlineController.stopLoop();
    }

    public void setLoop(Loop loop){
        if (isOnline)
            onlineController.setLoop(loop);
    }

    public void changeContentPane(JPanel panel) {
        mainFrame.changeContentPane(panel);
    }

    public void setMenuBar(JMenuBar menuBar) {
        mainFrame.setBar(menuBar);
    }

    public void refresh() {
        mainFrame.refresh();
    }

    public MainMenu getMainMenu() {
        User user = getUser();

        if (user instanceof Student)
            return new MainMenuStudent((Student) user);
        if (user instanceof CollegeBoss)
            return new MainMenuBoss((CollegeBoss) user);
        if (user instanceof EducationalAssistant)
            return new MainMenuAssistant((EducationalAssistant) user);
        if (user instanceof Professor)
            return new MainMenuProfessor((Professor) user);
        if (user instanceof AdminEdu)
            return new MainMenuAdmin(user);
        if (user instanceof Mohseni)
            return new MainMenuMohseni(user);
        return null;
    }

    @Override
    public void setCloseFrame() {
        controller.setCloseFrame();
    }

    @Override
    public Response login(String username, String password) {
        return controller.login(username , password);
    }

    @Override
    public User getUser() {
        return controller.getUser();
    }

    @Override
    public Response resetPassword(String lastPassword, String newPassword) {
        return controller.resetPassword(lastPassword , newPassword);
    }

    @Override
    public Response changeProfile(String email, String mobileNumber) {
        return controller.changeProfile(email , mobileNumber);
    }

    @Override
    public Response getSortedLessonInWeek() {
        return controller.getSortedLessonInWeek();
    }

    @Override
    public Response getExamList() {
        return controller.getExamList();
    }

    @Override
    public Response getLessonList(boolean isSelectedGrade, boolean isSelectedProfessor, boolean isSelectedCollege,
                                  String grade, String professor, String college) {
        return controller.getLessonList(isSelectedGrade , isSelectedProfessor , isSelectedCollege ,
                grade , professor , college);
    }

    @Override
    public Response getProfessorList() {
        return controller.getProfessorList();
    }

    @Override
    public Response getAddUser() {
        return controller.getAddUser();
    }

    @Override
    public Response getAddLesson() {
        return controller.getAddLesson();
    }

    @Override
    public Response addStudent(String username, String password, String firstName, String lastName,
                               String email, String mobileNumber, String nationalCode, String grade,
                               int entryYear, String status, String supervisor) {
        return controller.addStudent(username , password , firstName , lastName , email , mobileNumber , nationalCode,
                grade , entryYear , status , supervisor);
    }

    @Override
    public Response addProfessor(String username, String password, String firstName, String lastName,
                                 String email, String mobileNumber, String nationalCode, String roomNumber,
                                 String degree, List<Lesson> lessonList) {
        return controller.addProfessor(username , password , firstName , lastName , email , mobileNumber,
                nationalCode , roomNumber , degree , lessonList);
    }

    @Override
    public Response addLesson(String name, int numberOfUnits, int finalDateMonth, int finalDateDay, int finalTimeHour,
                              int starHour, int endHour, int capacity, List<DayOfWeek> daysOfWeek ,
                              String grade, String professor, List<Lesson> prerequisites, List<Lesson> requirements, int term) {
        return controller.addLesson(name , numberOfUnits , finalDateMonth , finalDateDay , finalTimeHour,
                starHour , endHour , capacity , daysOfWeek , grade , professor , prerequisites , requirements, term);
    }

    @Override
    public Response editProfessor(long professor, String username, String password, String roomNumber) {
        return controller.editProfessor(professor , username , password , roomNumber);
    }

    @Override
    public Response removeUser(long user) {
        return controller.removeUser(user);
    }

    @Override
    public Response editLesson(int lesson, int numberOfUnit, int finalDateMonth, int finalDateDay,
                               int finalTimeHour, int starHour, int endHour, int capacity) {
        return controller.editLesson(lesson , numberOfUnit , finalDateMonth , finalDateDay ,
                finalTimeHour , starHour , endHour , capacity);
    }

    @Override
    public Response removeLesson(int lesson) {
        return controller.removeLesson(lesson);
    }

    @Override
    public Response getRequest() {
        return controller.getRequest();
    }

    @Override
    public Response addRequest(EducationalRequest educationalRequest) {
        return controller.addRequest(educationalRequest);
    }

    @Override
    public Response handelRequest(EducationalRequest educationalRequest, boolean confirmed) {
        return controller.handelRequest(educationalRequest , confirmed);
    }

    @Override
    public Response getAllChats() {
        return controller.getAllChats();
    }

    @Override
    public Response getChat(long userCode) {
        return controller.getChat(userCode);
    }

    @Override
    public Response getCreateChat() {
        return controller.getCreateChat();
    }

    @Override
    public Response getCreateChatMohseni(boolean isSelectedEntryYear, boolean isSelectedGrade, boolean isSelectedCollege,
                                         int entryYear, String grade, String college) {
        return controller.getCreateChatMohseni(isSelectedEntryYear , isSelectedGrade , isSelectedCollege ,
                entryYear , grade , college);
    }

    @Override
    public Response getSearch(String code) {
        return controller.getSearch(code);
    }

    @Override
    public Response sendMessage(String message, List<Long> users, Media media) {
        return controller.sendMessage(message , users, media);
    }

    @Override
    public Response sendRequestToMessage(String userCode) {
        return controller.sendRequestToMessage(userCode);
    }

    @Override
    public Response getStudents() {
        return controller.getStudents();
    }

    @Override
    public Response addLessonTime(long userCode, Time time) {
        return controller.addLessonTime(userCode , time);
    }

    @Override
    public Response setEndChooseLessonTime(Time time) {
        return controller.setEndChooseLessonTime(time);
    }

    @Override
    public Response getChooseLesson(String college) {
        return controller.getChooseLesson(college);
    }

    @Override
    public Response getNotification() {
        return controller.getNotification();
    }

    @Override
    public Response handelNotification(Notification notification, Boolean yes) {
        return controller.handelNotification(notification , yes);
    }

    @Override
    public Response takeLessonFromAssistant(int lesson) {
        return controller.takeLessonFromAssistant(lesson);
    }

    @Override
    public Response takeLesson(int lesson) {
        return controller.takeLesson(lesson);
    }

    @Override
    public Response removeLessonFromChooseLesson(int lesson) {
        return controller.removeLessonFromChooseLesson(lesson);
    }

    @Override
    public Response getAllCoursewares() {
        return controller.getAllCoursewares();
    }

    @Override
    public Response getCourseware(int lesson) {
        return controller.getCourseware(lesson);
    }

    @Override
    public Response addStudentToCourseware(String code, boolean isTA , int lesson) {
        return controller.addStudentToCourseware(code , isTA , lesson);
    }

    @Override
    public Response editEducationalMaterial(int lesson, String name, String description, List<Media> mediaList) {
        return controller.editEducationalMaterial(lesson , name , description, mediaList);
    }

    @Override
    public Response editExercise(int lesson, String exercise, String description, Media media) {
        return controller.editExercise(lesson , exercise , description , media);
    }

    @Override
    public Response addEducationalMaterial(int lesson, String name, String description, List<Media> mediaList) {
        return controller.addEducationalMaterial(lesson , name , description , mediaList);
    }

    @Override
    public Response addExercise(int lesson, String name, String description, Media media, Time startTime,
                                Time endTime, Time uploadTime, UploadType uploadType) {
        return controller.addExercise(lesson , name , description , media, startTime,endTime ,uploadTime, uploadType);
    }

    @Override
    public Response removeEducationalMaterial(int lesson , String name) {
        return controller.removeEducationalMaterial(lesson , name);
    }

    @Override
    public Response removeExercise(int lesson, String name) {
        return controller.removeExercise(lesson , name);
    }

    @Override
    public Response getTemporaryScores() {
        return controller.getTemporaryScores();
    }

    @Override
    public Response getTemporaryScores(String userCode) {
        return controller.getTemporaryScores(userCode);
    }

    @Override
    public Response getTemporaryScores(String firstName, String lastName) {
        return controller.getTemporaryScores(firstName , lastName);
    }

    @Override
    public Response addProtest(int lessonNumber, String text) {
        return controller.addProtest(lessonNumber , text);
    }

    @Override
    public Response setRegister(int lessonNumber, String text, long userCode) {
        return controller.setRegister(lessonNumber , text, userCode );
    }

    @Override
    public Response registerScores(int lessonNumber, Map<Long, String> scoreMap, boolean isTemporary) {
        return controller.registerScores(lessonNumber , scoreMap , isTemporary);
    }

    @Override
    public Response getEducationalStatus() {
        return controller.getEducationalStatus();
    }

    @Override
    public Response getEducationalStatus(String userCode) {
        return controller.getEducationalStatus(userCode);
    }

    @Override
    public Response getEducationalStatus(String firstName, String lastName) {
        return controller.getEducationalStatus(firstName , lastName);
    }

    @Override
    public Response getCalender() {
        return controller.getCalender();
    }

    @Override
    public Response getSolution(int lesson, String exercise) {
        return controller.getSolution(lesson , exercise);
    }

    @Override
    public Response addSolution(int lesson, String exercise, String text, Media media) {
        return controller.addSolution(lesson , exercise , text , media);
    }

    @Override
    public Response registerScoreSolution(int lesson, String exercise, long userCode, String score) {
        return controller.registerScoreSolution(lesson , exercise , userCode , score);
    }
}
