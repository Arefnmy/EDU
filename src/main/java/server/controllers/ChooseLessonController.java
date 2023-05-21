package server.controllers;

import server.database.CollegeDatabase;
import server.database.LessonDatabase;
import server.database.UserDatabase;
import server.filemanager.ResourceManager;
import server.model.Courseware;
import server.model.Lesson;
import server.model.users.Student;
import shared.model.Time;
import shared.model.notification.Notification;
import shared.model.notification.NotificationType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChooseLessonController {
    private static int term = ResourceManager.getInstance().getValue(Integer.class , "term" , 14002);
    private Time finish;
    private final Map<Long , List<Integer>> studentListMap;

    public ChooseLessonController(){
        studentListMap = new HashMap<>();
        for (Student s : UserDatabase.getInstance().getStudents())
            studentListMap.put(s.getUserCode() , new ArrayList<>());
    }
    public ChooseLessonController(Time time){
        this();
        finish = time;
        setTimer();
    }

    public int addLesson(long studentNumber , int lessonNumber , boolean isAssistant){
        Lesson lesson = LessonDatabase.getInstance().getLesson(lessonNumber);
        List<Lesson> lessonList = LessonDatabase.getInstance().getLessons(studentListMap.get(studentNumber));
        Student student = UserDatabase.getInstance().getStudent(studentNumber);

        if (!isAssistant) {
            if (lesson.getTakers()>=lesson.getCapacity())
                return 1;

            if (!student.getPassedLesson().containsAll(lesson.getPrerequisites()))
                return 2;

            if (!Stream.concat(student.getPassedLesson().stream() , lessonList.stream())
                    .collect(Collectors.toList()).containsAll(lesson.getRequirements()))
                return 3;

            for (Lesson l : lessonList) {
                if (l.getTime().isClassTimeInterference(lesson.getTime()))
                    return 4;
                if (l.getTime().isFinalInterference(lesson.getTime()))
                    return 5;
            }

            if (lesson.getCollege() == CollegeDatabase.getInstance().getCollege("Ma`aref")) {
                int num = 0;
                for (Lesson l : lessonList)
                    if (l.getCollege() == CollegeDatabase.getInstance().getCollege("Ma`aref"))
                        num ++;
                if (num + 1 > ResourceManager.getInstance().getValue(Integer.class ,
                        "maximumNumberOfMaarefLessons" , 1))
                    return 6;
            }
        }

        studentListMap.get(studentNumber).add(lessonNumber);
        lesson.addTakers(1);
        return 0;
    }

    public void removeLesson(long student , int lessonNumber){
        studentListMap.get(student).remove(Integer.valueOf(lessonNumber));
        Lesson lesson = LessonDatabase.getInstance().getLesson(lessonNumber);
        lesson.addTakers(-1);
    }

    public int changeGroup(long student , int lesson , int group){
        return 0;
    }

    public List<Integer> getLessons(long student){
        return studentListMap.get(student);
    }

    public Time getFinish() {
        return finish;
    }

    public void setFinish(Time finish) {
        this.finish = finish;
        setTimer();
    }

    public void finishChooseLesson(){
        LessonDatabase.getInstance().getLessonsInTerm().forEach( l->{
            Courseware courseware = new Courseware(l.getNumber());
            LessonDatabase.getInstance().addCourseware(courseware);

            studentListMap.keySet().stream()
                    .filter( s-> studentListMap.get(s).contains(l.getNumber()))
                    .forEach(s -> {
                        Student student = UserDatabase.getInstance().getStudent(s);
                        student.addLesson(l);
                        Notification notification = new Notification(NotificationType.PREDICATIVE ,
                                "Course " + l.getName() + " created on CW." , "System");
                        student.addNotification(notification);
                    });
        });
        UserDatabase.getInstance().getStudents().forEach( s-> s.setChooseLessonTime(null));
        term ++;
    }

    public void setTimer(){
        if (finish != null) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    finishChooseLesson();
                    LessonDatabase.getInstance().setChooseLessonController(null);
                    timer.cancel();
                }
            }, finish.getDate());
        }
    }

    public static int getTerm() {
        return term;
    }
}
