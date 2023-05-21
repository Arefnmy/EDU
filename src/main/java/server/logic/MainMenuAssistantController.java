package server.logic;

import server.controllers.ChooseLessonController;
import server.database.LessonDatabase;
import server.database.RequestDatabase;
import server.database.UserDatabase;
import server.model.Lesson;
import shared.model.EducationalStatus;
import server.model.educationalrequest.Cancel;
import server.model.educationalrequest.Minor;
import server.model.users.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.model.Time;
import shared.model.notification.Notification;
import shared.model.notification.NotificationType;
import shared.response.Response;
import shared.response.ResponseState;

import java.util.Timer;
import java.util.TimerTask;

public class MainMenuAssistantController extends MainMenuProfessorController{
    private final EducationalAssistant educationalAssistant;

    Logger logger = LogManager.getLogger(MainMenuAssistantController.class);
    public MainMenuAssistantController(EducationalAssistant educationalAssistant) {
        super(educationalAssistant);
        this.educationalAssistant = educationalAssistant;
    }

    public EducationalAssistant getUser(){
        return educationalAssistant;
    }

    public int addStudent(shared.model.users.Student student){

        if (!userDatabase.usernameNotUsed(student.getUsername())) {
            logger.info("Add student failed : username is already taken, User code : " + educationalAssistant.getUserCode());
            return 1;
        }

        if (student.getUsername().isEmpty() || student.getPassword().isEmpty() || student.getFirstName().isEmpty() ||
                student.getLastName().isEmpty() || student.getEmail().isEmpty() || student.getMobileNumber().isEmpty() ||
                student.getNationalCode().isEmpty()){
            logger.info("Add student failed : empty fields, User code : " + educationalAssistant.getUserCode());
            return 2;
        }


        Student s = new Student( educationalAssistant.getCollege(), student.getUsername(), student.getPassword(),
                student.getFirstName(),  student.getLastName(), student.getEmail(), student.getMobileNumber(),
                student.getNationalCode(), student.getGrade(), student.getEntryYear(), student.getStatus());

        Professor supervisor = userDatabase.getProfessorByName(student.getSupervisor());
        s.setSupervisor(supervisor);

        userDatabase.addUser(s);

        logger.info("Add student, User code : " + educationalAssistant.getUserCode() +
                ",Student code : " + student.getUserCode());
        return 3;
    }

    public int addProfessor(shared.model.users.Professor professor){

        if (!userDatabase.usernameNotUsed(professor.getUsername())) {
            logger.info("Add professor failed : username is already taken, User code : " + educationalAssistant.getUserCode());
            return 1;
        }

        if (professor.getUsername().isEmpty() || professor.getPassword().isEmpty() || professor.getFirstName().isEmpty() ||
                professor.getLastName().isEmpty() || professor.getEmail().isEmpty() ||
                professor.getMobileNumber().isEmpty() || professor.getNationalCode().isEmpty()) {
            logger.info("Add professor failed : empty fields, User code : " + educationalAssistant.getUserCode());
            return 2;
        }

        Professor p = new Professor( educationalAssistant.getCollege(),  professor.getUsername(),  professor.getPassword(),
                professor.getFirstName(),  professor.getLastName(), professor.getEmail(),  professor.getMobileNumber(),
                professor.getNationalCode(), professor.getRoomNumber(),  professor.getDegree());

        for(shared.model.Lesson l : professor.getAvailableLessons())
            p.addLesson(l.getLessonNumber());

        userDatabase.addUser(p);
        logger.info("Add professor, User code : " + educationalAssistant.getUserCode() +
                ",professor code : " + professor.getUserCode());
        return 3;
    }

    public boolean addLesson(shared.model.Lesson lesson){
        Professor professor = UserDatabase.getInstance().getProfessorByName(lesson.getProfessorName());
        Lesson l = new Lesson(lesson.getName() , educationalAssistant.getCollege() , lesson.getNumberOfUnits() ,
               lesson.getGrade() , professor , lesson.getTime() , lesson.getCapacity() , lesson.getTerm());
        for (shared.model.Lesson p : lesson.getPrerequisites())
            l.addPrerequisites(p.getLessonNumber());
        for (shared.model.Lesson r : lesson.getRequirements())
            l.addRequirements(r.getLessonNumber());

        lessonDatabase.addLesson(l);
        logger.info("Add lesson, User code : " + educationalAssistant.getUserCode() +
                " ,Lesson number : " + l.getNumber());
        return true;
    }

    public boolean editLesson(int lessonNumber , shared.model.Lesson newLesson){
        Lesson lesson = lessonDatabase.getLesson(lessonNumber);
        lesson.setNumberOfUnits(newLesson.getNumberOfUnits());
        lesson.getTime().setFinalMonth(newLesson.getTime().getFinalMonth());
        lesson.getTime().setFinalDayOfMonth(newLesson.getTime().getFinalDayOfMonth());
        lesson.getTime().setFinalHour(newLesson.getTime().getFinalHour());
        lesson.getTime().setStartHour(newLesson.getTime().getStartHour());
        lesson.getTime().setEndHour(newLesson.getTime().getEndHour());
        lesson.setCapacity(newLesson.getCapacity());

        logger.info("Edit Lesson ,User code : " + educationalAssistant.getUserCode() +
                " ,Lesson number : " + lesson.getNumber());
        return true;
    }

    public void removeLesson(int lesson){
        logger.info("Remove Lesson, User code : " + educationalAssistant.getUserCode() +
                " ,Lesson number : " + lesson);
        lessonDatabase.removeLesson(lessonDatabase.getLesson(lesson));
    }

    public void handelMinor(String minorStr , boolean confirmed){
        Minor minor = (Minor) requestDatabase.getRequest(minorStr);
        logger.info("EducationalRequest set confirm : Minor, User code : " + educationalAssistant.getUserCode());
        if (educationalAssistant == minor.getGoalAssistant())
            minor.setConfirmedGoal(confirmed);
        else
            minor.setConfirmedOrigin(confirmed);
        minor.statusRegistration();

        Notification response;
        if (confirmed)
            response = new Notification(NotificationType.PREDICATIVE ,
                    "Educational Assistant accepted the minor request." , "System");
        else
            response = new Notification(NotificationType.PREDICATIVE ,
                   "Educational Assistant did not accept the minor request." , "System");
        minor.getStudent().addNotification(response);
    }

    public void handelCancel(String cancelStr , boolean confirmed){
        Cancel cancel = (Cancel) requestDatabase.getRequest(cancelStr);
        logger.info("EducationalRequest set confirm : Cancel, User code : " + educationalAssistant.getUserCode());
        cancel.setConfirmed(confirmed);
        if (confirmed)
            cancel.getStudent().setStatus(EducationalStatus.DROPPING_OUT);

        cancel.statusRegistration();
        Notification response;
        if (confirmed)
            response = new Notification(NotificationType.PREDICATIVE ,
                    "Educational Assistant accepted the cancel request." , "System");
        else
            response = new Notification(NotificationType.PREDICATIVE ,
                    "Educational Assistant did not accept the cancel request." , "System");
        cancel.getStudent().addNotification(response);
    }

    public void addLessonTime(long userCode , Time time){
        Student student = userDatabase.getStudent(userCode);
        student.setChooseLessonTime(time);
    }

    public void handelTakeLesson(Notification notification, boolean yes){
        Notification response;
        if (yes) {
            int lesson = Integer.parseInt(notification.getData());
            lessonDatabase.getChooseLessonController().addLesson(notification.getSenderCode(), lesson, true);
            response = new Notification(NotificationType.PREDICATIVE ,
                    "Educational Assistant accepted take lesson request." , "System");
        }
        else{
            response = new Notification(NotificationType.PREDICATIVE ,
                    "Educational Assistant did not accept the take lesson request." , "System");
        }
        Student student = userDatabase.getStudent(notification.getSenderCode());
        student.addNotification(response);
    }

    public boolean setEndChooseLessonTime(Time time){
        ChooseLessonController controller = lessonDatabase.getChooseLessonController();
        if (controller != null && controller.getFinish() != null)
            return false;
        if (controller == null)
            lessonDatabase.setChooseLessonController(new ChooseLessonController(time));
        else
            controller.setFinish(time);
/*
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                lessonDatabase.getChooseLessonController().finishChooseLesson();
                lessonDatabase.setChooseLessonController(null);
                timer.cancel();
            }
        } , lessonDatabase.getChooseLessonController().getFinish().getDate());*/
        return true;
    }
}
