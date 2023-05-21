package server.model.educationalrequest;

import server.database.UserDatabase;
import server.model.users.Student;
import shared.model.educaionalrequests.RequestStatus;

public abstract class EducationalRequest {
    protected static int requestCounter = 1000;
    protected String requestNumber;
    protected long studentCode;
    protected RequestStatus status;
    protected String message;

    protected EducationalRequest(Student student , String requestNumber) {
        this.studentCode = student.getUserCode();
        this.requestNumber = requestNumber;
        student.addRequest(requestNumber);
        requestCounter ++;
    }

    public abstract void statusRegistration();

    public Student getStudent(){
        return UserDatabase.getInstance().getStudent(studentCode);
    }

    public RequestStatus getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public static void codeUpdate(){
        requestCounter ++;
    }
}

