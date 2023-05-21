package server.model.educationalrequest;

import server.model.users.EducationalAssistant;
import server.model.users.Student;
import shared.model.educaionalrequests.RequestStatus;

public class Cancel extends EducationalRequest {
    private Boolean confirmed;

    public Cancel(Student student, EducationalAssistant assistant) {
        super(student , requestCounter + "Cnl");
        assistant.addRequest(requestNumber);

        statusRegistration();
    }

    public void statusRegistration() {
        if (confirmed == null) {
            status = RequestStatus.REGISTERED;
        }
        else if (confirmed){
            status = RequestStatus.ACCEPTED;
        }
        else{
            status = RequestStatus.FAILED;
        }
        message = status.toString();
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}
