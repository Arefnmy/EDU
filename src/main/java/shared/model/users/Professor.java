package shared.model.users;

import shared.model.Lesson;
import shared.model.ProfessorDegree;
import shared.model.Time;

import java.util.List;

public class Professor extends User{
    protected String roomNumber;
    protected ProfessorDegree degree;
    protected List<Lesson> availableLessons;

    protected Professor(){};
    public Professor(String collegeName, Time lastEnter, String username, String password, String firstName,
                     String lastName, String email, String mobileNumber, String nationalCode,
                     long userCode, String roomNumber, ProfessorDegree degree) {
        super(collegeName, lastEnter, username, password, firstName, lastName, email, mobileNumber, nationalCode, userCode);
        this.roomNumber = roomNumber;
        this.degree = degree;
    }

    public Professor(String firstName, String lastName, long userCode) {
        super(firstName, lastName, userCode);
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public List<Lesson> getAvailableLessons() {
        return availableLessons;
    }

    public void setAvailableLessons(List<Lesson> availableLessons) {
        this.availableLessons = availableLessons;
    }

    public ProfessorDegree getDegree() {
        return degree;
    }
}
