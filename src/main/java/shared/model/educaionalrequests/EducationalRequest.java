package shared.model.educaionalrequests;

public class EducationalRequest {
    private RequestType requestType;
    private String student;
    private long studentCode;
    private String requestNumber;
    private RequestStatus status;
    private String message;

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String professor;
    private String college;

    public EducationalRequest(){};
    public EducationalRequest(RequestType requestType){
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getStudent() {
        return student;
    }

    public long getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(long studentCode) {
        this.studentCode = studentCode;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }
}
