package server.model.users;

import server.database.LessonDatabase;
import server.database.UserDatabase;
import server.model.College;
import server.model.Lesson;
import shared.model.EducationalStatus;
import shared.model.Grade;
import shared.model.Time;

import java.util.*;

public class Student extends User{
    private static int studentCodeCounter = 100101;
    private final long studentCode;
    private double totalAverage;
    private long supervisorCode;
    private final int entryYear;
    private Time chooseLessonTime;
    private Grade grade;
    private EducationalStatus status;

    private final List<Integer> lessonsCode;
    private final List<Integer> passedLesson;
    private final Map<Integer , Double> temporaryScores;
    private final Map<Integer , Double> finalScores;


    public Student(College college, String username, String password, String firstName, String lastName,
                   String email, String mobileNumber, String nationalCode, Grade grade, int entryYear, EducationalStatus status) {
        super(college, username, password, firstName, lastName, email, mobileNumber, nationalCode);
        int mode = entryYear >= 1400 ? 1000 : 100;
        studentCode = (entryYear % mode) * 1000000L + studentCodeCounter++;

        this.grade = grade;
        this.entryYear = entryYear;
        this.status = status;

        college.addStudent(studentCode);

        lessonsCode = new ArrayList<>();
        passedLesson = new ArrayList<>();
        temporaryScores = new HashMap<>();
        finalScores = new HashMap<>();
    }

    //getters

    public long getUserCode() {
        return studentCode;
    }

    public double getTotalAverage() {
        int sum = 0;
        int number = 0;
        for (Lesson l : getFinalScoreLessons()) {
            sum += finalScores.get(l.getNumber()) * l.getNumberOfUnits();
            number += l.getNumberOfUnits();
        }
        if (number > 0)
            totalAverage = (double) sum/number;
        return totalAverage;
    }

    public Professor getSupervisor() {
        return UserDatabase.getInstance().getProfessor(supervisorCode);
    }

    public Grade getGrade() {
        return grade;
    }

    public EducationalStatus getStatus() {
        return status;
    }

    public int getEntryYear() {
        return entryYear;
    }

    public List<Lesson> getLesson() {
        return LessonDatabase.getInstance().getLessons(lessonsCode);
    }

    public Map<Integer, Double> getTemporaryScores() {
        return temporaryScores;
    }

    public Map<Integer, Double> getFinalScores() {
        return finalScores;
    }

    public Time getChooseLessonTime() {
        return chooseLessonTime;
    }

    public List<Lesson> getPassedLesson() {
        return LessonDatabase.getInstance().getLessons(passedLesson);
    }
//setters

    public void setSupervisor(Professor supervisor) {
        this.supervisorCode = supervisor.professorCode;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public void setStatus(EducationalStatus status) {
        this.status = status;
    }

    public void addLesson(Lesson lesson){
        lessonsCode.add(lesson.getNumber());
        lesson.addStudent(studentCode);

        temporaryScores.put(lesson.getNumber() , null);
    }

    public void addScore(int lesson , double score , boolean isFinal){
        if(isFinal){
            temporaryScores.remove(lesson);
            finalScores.put(lesson , score);
            if (score > 12)
                passedLesson.add(lesson);
        }
        else{
            temporaryScores.put(lesson , score);
        }
    }

    public List<Lesson> getTemporaryScoreLessons(){
        return  LessonDatabase.getInstance().getLessons(new ArrayList<>(temporaryScores.keySet()));
    }

    public List<Lesson> getFinalScoreLessons(){
        return  LessonDatabase.getInstance().getLessons(new ArrayList<>(finalScores.keySet()));
    }

    public void setChooseLessonTime(Time chooseLessonTime) {
        this.chooseLessonTime = chooseLessonTime;
    }

    public Double getScore(int lesson){
        if (temporaryScores.containsKey(lesson))
            return temporaryScores.get(lesson);
        else
            return finalScores.get(lesson);
    }

    public int getNumberOfUnitPassed(){
        int total = 0;
        for (Lesson l : getPassedLesson())
            total += l.getNumberOfUnits();
        return total;
    }

    public static void codeUpdate() {
        studentCodeCounter ++;
    }
}
