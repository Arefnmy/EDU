package server.model.educationalrequest;

import server.model.users.Student;
import shared.model.educaionalrequests.RequestStatus;

import java.time.LocalDateTime;

public class Defence extends EducationalRequest {

    public Defence(Student student) {
        super(student , requestCounter + "Def");

        statusRegistration();
    }

    public void statusRegistration() {
        status = RequestStatus.ACCEPTED;

        LocalDateTime time = LocalDateTime.now();
        message = String.format("You can defend your dissertation on the %s." ,
                (time.getYear()+1) + " " + time.getMonth() + " " + time.getDayOfMonth() + "th");
    }
}
