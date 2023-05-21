package server.logic;

import server.database.RequestDatabase;
import server.model.Lesson;
import server.model.educationalrequest.Recommendation;
import server.model.users.Professor;
import server.model.users.Student;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.model.educaionalrequests.Protest;
import shared.model.notification.Notification;
import shared.model.notification.NotificationType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainMenuProfessorController extends MainMenuController {
    private final Professor professor;

    Logger logger = LogManager.getLogger(MainMenuProfessorController.class);
    public MainMenuProfessorController(Professor professor) {
        super(professor);
        this.professor = professor;
    }

    public Professor getUser() {
        return professor;
    }

    public void handelRecommendation(String recommendationStr , boolean confirmed){
        Recommendation recommendation = (Recommendation) RequestDatabase.getInstance().getRequest(recommendationStr);
        logger.info("EducationalRequest set confirm : recommendation, User code : " + professor.getUserCode());
        recommendation.setConfirmed(confirmed);
        recommendation.statusRegistration();

        Notification response;
        if (confirmed)
            response = new Notification(NotificationType.PREDICATIVE ,
                    professor.getName() + " accepted the recommendation request." , "System");
        else
            response = new Notification(NotificationType.PREDICATIVE ,
                    professor.getName() + " did not accept the recommendation request." , "System");
        recommendation.getStudent().addNotification(response);
    }

    public boolean setReply(int lessonNumber ,long userCode , String reply){
        logger.info("Protest set reply, User code : " + professor.getUserCode());
        Protest protest = RequestDatabase.getInstance().getProtest(userCode , lessonNumber);
        if (protest == null)
            return false;
        protest.setReplay(reply);
        return true;
    }

    public boolean setScores(int lessonNumber , Map<Long , String> scoreMap , boolean isTemporary){
        for (String str : scoreMap.values())
            if (!isScore(str)) {
                logger.info("Set temporary scores failed : integer fields, User code : " + professor.getUserCode() +
                        ",Lesson number : " + lessonNumber);
                return false;
            }
        for (Object o : scoreMap.keySet()){
            long userCode = Integer.parseInt(o.toString());
            Student student = userDatabase.getStudent(userCode);
            double score = Double.parseDouble(scoreMap.get(o));
            student.addScore(lessonNumber , score , !isTemporary);
        }
        logger.info("Set temporary scores, User code : " + professor.getUserCode() +
                ",Lesson number : " + lessonNumber);
        return true;
    }

    public List<Student> getStudentsWithSupervisor(){
        return userDatabase.getStudents().stream()
                .filter(s-> s.getSupervisor() == professor)
                .collect(Collectors.toList());
    }

    public int addStudentToCourseware(String code , boolean isTA , int lessonNumber){
        if (!isNumber(code))
            return 1;
        Student student = userDatabase.getStudent(Integer.parseInt(code));
        Lesson lesson = lessonDatabase.getLesson(lessonNumber);
        if (student == null)
            return 2;
        if (isTA) {
            lessonDatabase.getCourseware(lessonNumber).addTA(student);
            Notification notification = new Notification(NotificationType.PREDICATIVE ,
                    "You added to course " + lesson.getName() + " as teaching assistant." , "System");
            student.addNotification(notification);
        }
        else {
            student.addLesson(lesson);
            Notification notification = new Notification(NotificationType.PREDICATIVE ,
                    "You added to course " + lesson.getName() + " as student." , "System");
            student.addNotification(notification);
        }
        return 0;
    }

    private double roundScore(double score){
        int integer = (int) score;
        int decimal = (int) (( score - integer )*100);
        int r = decimal % 25;
        int dec = r > 12 ? (decimal/25 + 1)*25 : ( decimal/25 )*25 ;
        if (dec == 100)
            return integer+1;
        return integer + dec/100d;
    }
}
