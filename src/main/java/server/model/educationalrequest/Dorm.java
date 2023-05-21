package server.model.educationalrequest;

import server.model.users.Student;
import shared.model.educaionalrequests.RequestStatus;

import java.util.Random;

public class Dorm extends EducationalRequest {

    public Dorm(Student student) {
        super(student , requestCounter + "Drm");

        statusRegistration();
    }

    public void statusRegistration() {
        Random random = new Random();
        int rand = random.nextInt(2);
        if (rand == 0) {
            status = RequestStatus.ACCEPTED;
        } else {
            status = RequestStatus.FAILED;
        }
        message = status.toString();
    }
}
