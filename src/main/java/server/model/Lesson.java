package server.model;
import server.database.CollegeDatabase;
import server.database.LessonDatabase;
import server.database.UserDatabase;
import server.model.users.*;
import shared.model.Grade;
import shared.model.LessonTime;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private static int lessonNumberCounter = 100;

    private final String name;
    private final String collegeName;
    private int numberOfUnits;
    private final int lessonNumber;
    private final List<Integer> prerequisites;
    private final List<Integer> requirements;
    private final Grade grade;
    private final LessonTime time;
    private final int term;
    private int numberOfGroup;
    private int capacity;
    private int takers;

    private final List<Long> studentsCode;
    private final long professorCode;

    public Lesson(String name, College college, int numberOfUnits, Grade grade,
                  Professor professor, LessonTime time, int capacity, int term) {
        lessonNumber = lessonNumberCounter;
        lessonNumberCounter++;

        this.name = name;
        collegeName = college.getName();
        this.numberOfUnits = numberOfUnits;
        this.grade = grade;
        this.professorCode = professor.getUserCode();
        this.time = time;
        this.term = term;
        this.capacity = capacity;
        takers = 0;

        professor.addLesson(lessonNumber);
        college.addLesson(lessonNumber);
        studentsCode = new ArrayList<>();
        prerequisites = new ArrayList<>();
        requirements = new ArrayList<>();
    }

    //getters

    public String getName() {
        return name;
    }

    public College getCollege() {
        return CollegeDatabase.getInstance().getCollege(collegeName);
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public int getNumber() {
        return lessonNumber;
    }

    public List<Lesson> getPrerequisites() {
        return LessonDatabase.getInstance().getLessons(prerequisites);
    }

    public List<Lesson> getRequirements() {
        return LessonDatabase.getInstance().getLessons(requirements);
    }

    public Grade getGrade() {
        return grade;
    }

    public Professor getProfessor() {
        return UserDatabase.getInstance().getProfessor(professorCode);
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        for (Student s : UserDatabase.getInstance().getStudents())
            if (studentsCode.contains(s.getUserCode()))
                students.add(s);

        return students;
    }

    public LessonTime getTime() {
        return time;
    }

    public int getTerm() {
        return term;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTakers() {
        return takers;
    }

    public int getNumberOfGroup() {
        return numberOfGroup;
    }

    public double getAverage(boolean isFinal){
        double total = 0;
        int number = 0;
        if (isFinal) {
            for (Student s : getStudents()) {
                if (s.getFinalScoreLessons().contains(this)) {
                    total += s.getScore(lessonNumber);
                    number++;
                }
            }
        }
        else {
            for (Student s : getStudents()) {
                if ( s.getScore(lessonNumber) != null) {
                    total += s.getScore(lessonNumber);
                    number++;
                }
            }
        }
        return total/number;
    }

    public List<Student> getRejectedStudents(boolean isFinal){
        List<Student> studentList = new ArrayList<>();
        if (isFinal) {
            for (Student s : getStudents())
                if (s.getFinalScoreLessons().contains(this) && !s.getPassedLesson().contains(this))
                    studentList.add(s);
        }
        else {
            for (Student s : getStudents())
                if (s.getScore(lessonNumber) != null && s.getScore(lessonNumber)<= 12 )
                    studentList.add(s);
        }
        return studentList;
    }

    public double getAverageOfPassedStudents(boolean isFinal){
        double total = 0;
        if (isFinal) {
            for (Student s : getPassedStudents(true))
                total += s.getScore(lessonNumber);

            return total / getPassedStudents(true).size();
        }
        else{
            for (Student s : getPassedStudents(false))
                total += s.getScore(lessonNumber);

            return total / getPassedStudents(false).size();
        }
    }

    public List<Student> getPassedStudents(boolean isFinal){
        List<Student> studentList = new ArrayList<>();
        if (isFinal) {
            for (Student s : getStudents())
                if (s.getFinalScoreLessons().contains(this) && s.getPassedLesson().contains(this))
                    studentList.add(s);
        }
        else {
            for (Student s : getStudents())
                if (s.getScore(lessonNumber) != null && s.getScore(lessonNumber)> 12)
                    studentList.add(s);
        }
        return studentList;
    }
//setters

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setNumberOfGroup(int numberOfGroup) {
        this.numberOfGroup = numberOfGroup;
    }


    public void addStudent(long student){
        studentsCode.add(student);
    }

    public void addRequirements(int lesson){
        requirements.add(lesson);
    }

    public void addPrerequisites(int lesson){
        prerequisites.add(lesson);
    }

    public void addTakers(int number){
        takers += number;
    }

    public static void codeUpdate(){
        lessonNumberCounter ++;
    }
}
