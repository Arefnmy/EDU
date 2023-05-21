package server.model.educationalrequest;


import server.model.users.Student;
import shared.model.educaionalrequests.RequestStatus;

public class BusyStudying extends EducationalRequest {
    public BusyStudying(Student student) {
        super(student ,  requestCounter + "Bst");

        statusRegistration();
    }

    public void statusRegistration() {
        status = RequestStatus.ACCEPTED;
        message = String.format("It is certified that Mr./Mrs. %s with a student number %d," +
                        "\nis studying at the university Sharif." +
                        "\nCertificate validity date: 1401/04/01",
                getStudent().getName(), getStudent().getUserCode());
    }
}
