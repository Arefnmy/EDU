package client.controller;

import client.filemanager.ResourceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.model.Lesson;
import shared.model.Time;
import shared.model.UploadType;
import shared.model.chatroom.Message;
import shared.model.educaionalrequests.EducationalRequest;
import shared.model.media.Media;
import shared.model.notification.Notification;
import shared.model.users.AdminEdu;
import shared.model.users.User;
import shared.response.Response;
import shared.response.ResponseState;

import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfflineController implements GraphicalAgent {
    private Response offlineData;

    public void setOfflineData(Response response) {
        offlineData = response;
    }

    @Override
    public void setCloseFrame() {
    }

    @Override
    public Response login(String username, String Password) {
        return null;
    }

    @Override
    public User getUser() {
        Response response = (Response) offlineData.getData("user");
        return (User) response.getData("user");
    }

    @Override
    public Response resetPassword(String lastPassword, String newPassword) {
        return null;
    }

    @Override
    public Response changeProfile(String email, String mobileNumber) {
        return null;
    }

    @Override
    public Response getSortedLessonInWeek() {
        return (Response) offlineData.getData("weeklySchedule");
    }

    @Override
    public Response getExamList() {
        return (Response) offlineData.getData("examList");
    }

    @Override
    public Response getLessonList(boolean isSelectedGrade, boolean isSelectedProfessor, boolean isSelectedCollege, String grade, String professor, String college) {
        return null;
    }

    @Override
    public Response getProfessorList() {
        return null;
    }

    @Override
    public Response getAddUser() {
        return null;
    }

    @Override
    public Response getAddLesson() {
        return null;
    }

    @Override
    public Response addStudent(String username, String password, String firstName, String lastName, String email, String mobileNumber, String nationalCode, String grade, int entryYear, String status, String supervisor) {
        return null;
    }

    @Override
    public Response addProfessor(String username, String password, String firstName, String lastName, String email, String mobileNumber, String nationalCode, String roomNumber, String degree, List<Lesson> lessonList) {
        return null;
    }

    @Override
    public Response addLesson(String name, int numberOfUnits, int finalDateMonth, int finalDateDay, int finalTimeHour, int starHour, int endHour, int capacity, List<DayOfWeek> daysOfWeek, String grade, String professor, List<Lesson> prerequisites, List<Lesson> requirements, int term) {
        return null;
    }

    @Override
    public Response editProfessor(long professor, String username, String password, String roomNumber) {
        return null;
    }

    @Override
    public Response removeUser(long user) {
        return null;
    }

    @Override
    public Response editLesson(int lesson, int numberOfUnit, int finalDateMonth, int finalDateDay, int finalTimeHour, int starHour, int endHour, int capacity) {
        return null;
    }

    @Override
    public Response removeLesson(int lesson) {
        return null;
    }

    @Override
    public Response getRequest() {
        return null;
    }

    @Override
    public Response addRequest(EducationalRequest educationalRequest) {
        return null;
    }

    @Override
    public Response handelRequest(EducationalRequest educationalRequest, boolean confirmed) {
        return null;
    }

    @Override
    public Response getAllChats() {
        return (Response) offlineData.getData("chatroom");
    }

    @Override
    public Response getChat(long userCode) {
        return null;
    }

    @Override
    public Response getCreateChat() {
        Response response = new Response(ResponseState.OK, null);
        response.addData("userList", new ArrayList<>(List.of(new AdminEdu("Admin", "Edu", 1))));
        return response;
    }

    @Override
    public Response getCreateChatMohseni(boolean isSelectedEntryYear, boolean isSelectedGrade, boolean isSelectedCollege, int entryYear, String grade, String college) {
        return null;
    }

    @Override
    public Response getSearch(String code) {
        return null;
    }

    @Override
    public Response sendMessage(String message, List<Long> users, Media media) {
        if (users.isEmpty())
            return new Response(ResponseState.ERROR, "Select a user!");
        String path = ResourceManager.getInstance().getMainPath() + "files/messages/" + getUser().getUserCode() + ".txt";
        Message m = new Message(null ,null ,message , null , media);
        Gson gson = new GsonBuilder().create();
        try {//todo create directory
           FileWriter fileWriter = new FileWriter(path , true);
           fileWriter.write(gson.toJson(m) + "\n");
           fileWriter.close();
        } catch (IOException ignored) {
        }
        return new Response(ResponseState.OK, "Done!");
    }

    @Override
    public Response sendRequestToMessage(String userCode) {
        return null;
    }

    @Override
    public Response getStudents() {
        return null;
    }

    @Override
    public Response addLessonTime(long userCode, Time time) {
        return null;
    }

    @Override
    public Response setEndChooseLessonTime(Time time) {
        return null;
    }

    @Override
    public Response getChooseLesson(String college) {
        return null;
    }

    @Override
    public Response getNotification() {
        return null;
    }

    @Override
    public Response handelNotification(Notification notification, Boolean yes) {
        return null;
    }

    @Override
    public Response takeLessonFromAssistant(int lesson) {
        return null;
    }

    @Override
    public Response takeLesson(int lesson) {
        return null;
    }

    @Override
    public Response removeLessonFromChooseLesson(int lesson) {
        return null;
    }

    @Override
    public Response getAllCoursewares() {
        return null;
    }

    @Override
    public Response getCourseware(int lesson) {
        return null;
    }

    @Override
    public Response addStudentToCourseware(String code, boolean isTA , int lesson) {
        return null;
    }

    @Override
    public Response removeEducationalMaterial(int lesson , String name) {
        return null;
    }

    @Override
    public Response removeExercise(int lesson, String name) {
        return null;
    }

    @Override
    public Response getTemporaryScores() {
        return null;
    }

    @Override
    public Response getTemporaryScores(String userCode) {
        return null;
    }

    @Override
    public Response getTemporaryScores(String firstName, String lastName) {
        return null;
    }

    @Override
    public Response addProtest(int lessonNumber, String text) {
        return null;
    }

    @Override
    public Response setRegister(int lessonNumber, String text, long userCode) {
        return null;
    }

    @Override
    public Response registerScores(int lessonNumber, Map<Long, String> scoreMap, boolean isTemporary) {
        return null;
    }

    @Override
    public Response getEducationalStatus() {
        return (Response) offlineData.getData("educationalStatus");
    }

    @Override
    public Response getEducationalStatus(String userCode) {
        return null;
    }

    @Override
    public Response getEducationalStatus(String firstName, String lastName) {
        return null;
    }

    @Override
    public Response getCalender() {
        return null;
    }

    @Override
    public Response getSolution(int lesson, String exercise) {
        return null;
    }

    @Override
    public Response addSolution(int lesson, String exercise, String text, Media media) {
        return null;
    }

    @Override
    public Response registerScoreSolution(int lesson, String exercise, long userCode, String score) {
        return null;
    }

    @Override
    public Response editEducationalMaterial(int lesson, String name, String description, List<Media> mediaList) {
        return null;
    }

    @Override
    public Response editExercise(int lesson, String exercise, String description, Media media) {
        return null;
    }

    @Override
    public Response addEducationalMaterial(int lesson, String name, String description, List<Media> mediaList) {
        return null;
    }

    @Override
    public Response addExercise(int lesson, String name, String description, Media media, Time startTime, Time endTime, Time uploadTime, UploadType uploadType) {
        return null;
    }
}
