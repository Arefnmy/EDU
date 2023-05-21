package server.model.users;

import server.model.College;
import shared.model.ProfessorDegree;

public class EducationalAssistant extends Professor{
    private static long educationalAssistantCodeCounter = 200300101;

    public EducationalAssistant(College college, String username, String password, String firstName, String lastName,
                                String email, String mobileNumber, String nationalCode, String roomNumber, ProfessorDegree degree) {
        super(college, username, password, firstName, lastName, email, mobileNumber, nationalCode,roomNumber, degree);

        professorCode = educationalAssistantCodeCounter ++;

        college.setEducationalAssistant(this);
        if (!(this instanceof CollegeBoss))
            college.addProfessor(professorCode);
    }

}
