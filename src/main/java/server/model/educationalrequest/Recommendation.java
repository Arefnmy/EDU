package server.model.educationalrequest;

import server.database.UserDatabase;
import server.model.users.Professor;
import server.model.users.Student;
import shared.model.educaionalrequests.RequestStatus;

public class Recommendation extends EducationalRequest {
    private final long professorCode;
    private Boolean confirmed;

    public Recommendation(Student student, Professor professor) {
        super(student , requestCounter + "Rec");
        this.professorCode = professor.getUserCode();
        professor.addRequest(requestNumber);

        statusRegistration();
    }


    public void statusRegistration() {
        if (confirmed == null){
            status = RequestStatus.REGISTERED;
            message = status.toString();
        }
        else if (confirmed){
            status = RequestStatus.ACCEPTED;
            message = String.format("I %s testify that Mr./Ms %s with student number %d," +
                            "\npassed the courses ... with a grade of %s",
                    getProfessor().getName(), getProfessor().getName(), getStudent().getUserCode(), getStudent().getGrade());
        }
        else{
            status = RequestStatus.FAILED;
            message = status.toString();
        }
    }

    public void setConfirmed(boolean confirmed){
        this.confirmed = confirmed;
    }

    public Professor getProfessor() {
        return UserDatabase.getInstance().getProfessor(professorCode);
    }
}
