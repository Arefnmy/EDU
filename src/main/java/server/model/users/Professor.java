package server.model.users;

import server.database.LessonDatabase;
import server.model.College;
import server.model.Lesson;
import shared.model.ProfessorDegree;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User{
    private static long professorCodeCounter = 300200101;
    protected long professorCode;
    protected String roomNumber;
    protected ProfessorDegree degree;

    protected final List<Integer> lessonsCode;

    public Professor(College college, String username, String password, String firstName, String lastName,
                     String email, String mobileNumber, String nationalCode, String roomNumber, ProfessorDegree degree ) {
        super(college, username, password, firstName, lastName, email, mobileNumber, nationalCode);
        professorCode = professorCodeCounter ++;

        if (!(this instanceof EducationalAssistant)) //todo
            college.addProfessor(professorCode);

        this.roomNumber = roomNumber;
        this.degree = degree;

        lessonsCode = new ArrayList<>();
    }

    //getters

    public long getUserCode() {
        return professorCode;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public ProfessorDegree getDegree() {
        return degree;
    }

    public List<Lesson> getLesson() {
        return LessonDatabase.getInstance().getLessons(lessonsCode);
    }

    //setters

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setDegree(ProfessorDegree degree) {
        this.degree = degree;
    }

    public void setProfessorCode(long professorCode) {
        this.professorCode = professorCode;
    }

    public void addLesson(int lesson){
        lessonsCode.add(lesson);
    }

    public static void codeUpdate() {
        professorCodeCounter ++;
    }
}
