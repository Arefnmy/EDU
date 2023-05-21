package server.model.users;

import server.model.College;
import shared.model.ProfessorDegree;

public class CollegeBoss extends EducationalAssistant{

    private static long collegeBossCodeCounter = 100400101;

    public CollegeBoss(College college, String username, String password, String firstName, String lastName,
                       String email, String mobileNumber, String nationalCode, String roomNumber, ProfessorDegree degree) {
        super(college, username, password, firstName, lastName, email, mobileNumber, nationalCode, roomNumber, degree);

        professorCode = collegeBossCodeCounter ++;

        college.setCollegeBoss(this);
        college.addProfessor(professorCode);
    }
}
