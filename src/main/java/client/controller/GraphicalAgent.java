package client.controller;

import shared.model.Lesson;
import shared.model.Time;
import shared.model.UploadType;
import shared.model.educaionalrequests.EducationalRequest;
import shared.model.media.Media;
import shared.model.notification.Notification;
import shared.model.users.User;
import shared.response.Response;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public interface GraphicalAgent {
    void setCloseFrame();
    Response login(String username , String Password);
    User getUser();
    Response resetPassword(String lastPassword , String newPassword);
    Response changeProfile(String email , String mobileNumber);
    Response getSortedLessonInWeek();
    Response getExamList();
    Response getLessonList(boolean isSelectedGrade , boolean isSelectedProfessor , boolean isSelectedCollege,
                           String grade , String professor , String college);
    Response getProfessorList();
    Response getAddUser();
    Response getAddLesson();
    Response addStudent(String username, String password, String firstName, String lastName,
                        String email, String mobileNumber, String nationalCode,
                        String grade, int entryYear, String status , String supervisor);
    Response addProfessor(String username, String password, String firstName, String lastName,
                          String email, String mobileNumber, String nationalCode,
                          String roomNumber, String degree , List<Lesson> lessonList);
    Response addLesson(String name, int numberOfUnits, int finalDateMonth, int finalDateDay,
                       int finalTimeHour, int starHour, int endHour, int capacity, List<DayOfWeek> daysOfWeek, String grade,
                       String professor, List<Lesson> prerequisites, List<Lesson> requirements, int term);
    Response editProfessor(long professor, String username, String password , String roomNumber);
    Response removeUser(long user);
    Response editLesson(int lesson , int numberOfUnit , int finalDateMonth , int finalDateDay ,
                        int finalTimeHour, int starHour, int endHour, int capacity);
    Response removeLesson(int lesson);
    Response getRequest();
    Response addRequest(EducationalRequest educationalRequest);
    Response handelRequest(EducationalRequest educationalRequest, boolean confirmed);
    Response getAllChats();
    Response getChat(long userCode);
    Response getCreateChat();
    Response getCreateChatMohseni(boolean isSelectedEntryYear , boolean isSelectedGrade , boolean isSelectedCollege,
                                  int entryYear , String grade , String college);
    Response getSearch(String code);
    Response sendMessage(String message, List<Long> users, Media media);
    Response sendRequestToMessage(String userCode);
    Response getStudents();
    Response addLessonTime(long userCode , Time time);
    Response setEndChooseLessonTime(Time time);
    Response getChooseLesson(String college);
    Response getNotification();
    Response handelNotification(Notification notification , Boolean yes);
    Response takeLessonFromAssistant(int lesson);
    Response takeLesson(int lesson);
    Response removeLessonFromChooseLesson(int lesson);
    Response getAllCoursewares();
    Response getCourseware(int lesson);
    Response addStudentToCourseware(String code , boolean isTA , int lesson);
    Response editEducationalMaterial(int lesson, String name, String description, List<Media> mediaList);
    Response editExercise(int lesson , String exercise , String description , Media media);
    Response addEducationalMaterial(int lesson , String name , String description , List<Media> mediaList);
    Response addExercise(int lesson, String name, String description, Media media, Time startTime,
                         Time endTime, Time uploadTime, UploadType uploadType);
    Response removeEducationalMaterial(int lesson , String name);
    Response removeExercise(int lesson , String name);
    Response getTemporaryScores();
    Response getTemporaryScores(String userCode);
    Response getTemporaryScores(String firstName , String lastName);
    Response addProtest(int lessonNumber , String text);
    Response setRegister(int lessonNumber, String text, long userCode);
    Response registerScores(int lessonNumber , Map<Long, String> scoreMap , boolean isTemporary);
    Response getEducationalStatus();
    Response getEducationalStatus(String userCode);
    Response getEducationalStatus(String firstName , String lastName);
    Response getCalender();
    Response getSolution(int lesson , String exercise);
    Response addSolution(int lesson , String exercise , String text , Media media);
    Response registerScoreSolution(int lesson , String exercise , long userCode , String score);
}
