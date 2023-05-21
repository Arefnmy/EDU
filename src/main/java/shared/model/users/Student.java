package shared.model.users;

import shared.model.EducationalStatus;
import shared.model.Grade;
import shared.model.Lesson;
import shared.model.Time;

import java.util.List;

public class Student extends User{
    private double totalAverage;
    private String supervisor;
    private int entryYear;
    private Time chooseLessonTime;
    private Grade grade;
    private EducationalStatus status;

    private List<Lesson> lessons;

    private Student(){};
    public Student(String collegeName, Time lastEnter, String username, String password, String firstName,
                   String lastName, String email, String mobileNumber, String nationalCode, long userCode, int entryYear,
                   Grade grade , EducationalStatus status) {
        super(collegeName, lastEnter, username, password, firstName, lastName, email, mobileNumber, nationalCode, userCode);
        this.entryYear = entryYear;
        this.grade = grade;
        this.status = status;
    }

    public Student(String firstName, String lastName, long userCode) {
        super(firstName, lastName, userCode);
    }

    public double getTotalAverage() {
        return totalAverage;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public int getEntryYear() {
        return entryYear;
    }

    public Grade getGrade() {
        return grade;
    }

    public EducationalStatus getStatus() {
        return status;
    }

    public Time getChooseLessonTime() {
        return chooseLessonTime;
    }

    public void setSupervisor(String supervisor){
        this.supervisor = supervisor;
    }

    public void setTotalAverage(double totalAverage){
        this.totalAverage = totalAverage;
    }

    public void setChooseLessonTime(Time chooseLessonTime) {
        this.chooseLessonTime = chooseLessonTime;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
