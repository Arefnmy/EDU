package shared.model.users;

import shared.model.ProfessorDegree;
import shared.model.Time;

public class EducationalAssistant extends Professor{

    protected EducationalAssistant(){};
    public EducationalAssistant(String collegeName, Time lastEnter, String username, String password, String firstName, String lastName, String email, String mobileNumber, String nationalCode, long userCode, String roomNumber, ProfessorDegree degree) {
        super(collegeName, lastEnter, username, password, firstName, lastName, email, mobileNumber, nationalCode, userCode, roomNumber, degree);
    }
}
