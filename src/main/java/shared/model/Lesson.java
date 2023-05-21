package shared.model;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private String name;
    private String collegeName;
    private int numberOfUnits;
    private int lessonNumber;
    private List<Lesson> prerequisites;
    private List<Lesson> requirements;
    private Grade grade;
    private LessonTime time;
    private int capacity;
    private int numberOfGroup;
    private int term;

    private String professorName;

    private Lesson(){};
    public Lesson(String name, String  collegeName, int numberOfUnits, Grade grade,
                  String professorName, LessonTime time,int capacity ,  int lessonNumber) {
        prerequisites = new ArrayList<>();
        requirements = new ArrayList<>();
        this.lessonNumber = lessonNumber;
        this.name = name;
        this.collegeName = collegeName;
        this.numberOfUnits = numberOfUnits;
        this.grade = grade;
        this.professorName = professorName;
        this.time = time;
        this.capacity = capacity;
    }

    public Lesson(String name , String professorName , LessonTime time , int lessonNumber){
        this.name = name;
        this.professorName = professorName;
        this.time = time;
        this.lessonNumber = lessonNumber;
    }

    public String getName() {
        return name;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public Grade getGrade() {
        return grade;
    }

    public LessonTime getTime() {
        return time;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getProfessorName() {
        return professorName;
    }

    public List<Lesson> getPrerequisites() {
        return prerequisites;
    }

    public List<Lesson> getRequirements() {
        return requirements;
    }

    public int getTerm() {
        return term;
    }

    public int getNumberOfGroup() {
        return numberOfGroup;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public void setPrerequisites(List<Lesson> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public void setRequirements(List<Lesson> requirements) {
        this.requirements = requirements;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
