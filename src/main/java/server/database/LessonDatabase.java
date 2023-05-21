package server.database;

import server.controllers.ChooseLessonController;
import server.model.Courseware;
import server.model.Lesson;
import server.model.users.*;
import shared.model.courseware.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LessonDatabase {
    private final List<Lesson> lessons = new ArrayList<>();
    private final List<Courseware> coursewares = new ArrayList<>();
    private final List<Solution> solutions = new ArrayList<>();
    private ChooseLessonController chooseLessonController;

    private static final LessonDatabase instance = new LessonDatabase();

    public synchronized static LessonDatabase getInstance(){
        return instance;
    }

    public void addLesson(Lesson lesson){
        synchronized (lessons) {
            lessons.add(lesson);
        }
    }

    public void addCourseware(Courseware courseware){
        synchronized (coursewares) {
            coursewares.add(courseware);
        }
    }

    public void addSolution(Solution solution){
        solutions.add(solution);
    }

    public void removeLesson(Lesson lesson){
        lessons.remove(lesson);
        lesson.getProfessor().getLesson().remove(lesson);
        for (Student s : lesson.getStudents()){
            s.getLesson().remove(lesson);
        }
    }

    public Lesson getLesson(int lessonNumber){
        synchronized (lessons) {
            for (Lesson l : lessons)
                if (l.getNumber() == lessonNumber)
                    return l;
            return null;
        }
    }

    public Courseware getCourseware(int lessonNumber){
        synchronized (coursewares) {
            for (Courseware c : coursewares)
                if (c.getLesson().getNumber() == lessonNumber)
                    return c;
            return null;
        }
    }

    public Solution getSolution(int lessonNumber , String exercise , long userCode){
        for (Solution s : solutions)
            if (s.getLessonNumber() == lessonNumber &&
            s.getStudentCode() == userCode && s.getExerciseName().equals(exercise))
                return s;
        return null;
    }

    public List<Solution> getSolutionList(int lessonNumber , String exercise){
        return solutions.stream()
                .filter(s-> s.getExerciseName().equals(exercise) && s.getLessonNumber() == lessonNumber)
                .collect(Collectors.toList());
    }

    public List<Lesson> getLessons(List<Integer> lessonList){
        List<Lesson> temp = new ArrayList<>();
        for (Lesson l : lessons){
            if (lessonList.contains(l.getNumber()))
                temp.add(l);
        }
        return temp;
    }

    public List<Lesson> getLessonsInTerm(){
        return lessons.stream()
                .filter( l-> l.getTerm() == ChooseLessonController.getTerm())
                .collect(Collectors.toList());
    }

    public List<Courseware> coursewareListTA(Student student){
        return coursewares.stream()
                .filter(c -> c.isTA(student.getUserCode()))
                .collect(Collectors.toList());
    }

    public List<Lesson> getLessons() {
        synchronized (lessons) {
            return lessons;
        }
    }

    public List<Courseware> getCoursewares() {
        synchronized (coursewares) {
            return coursewares;
        }
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public ChooseLessonController getChooseLessonController() {
        return chooseLessonController;
    }

    public void setChooseLessonController(ChooseLessonController chooseLessonController) {
        this.chooseLessonController = chooseLessonController;
    }
}
