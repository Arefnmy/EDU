package server.model.educationalrequest;


import server.database.UserDatabase;
import server.filemanager.ResourceManager;
import server.model.users.Student;
import server.model.users.EducationalAssistant;
import shared.model.educaionalrequests.RequestStatus;

public class Minor extends EducationalRequest { //todo origin college
    private final boolean confirmedAverage;
    private Boolean confirmedOrigin;
    private Boolean confirmedGoal;
    private final long originAssistantCode;
    private final long goalAssistantCode;

    public Minor(Student student, EducationalAssistant originAssistant, EducationalAssistant goalAssistant) {
        super(student , requestCounter + "Mnr");

        this.originAssistantCode = originAssistant.getUserCode();
        this.goalAssistantCode = goalAssistant.getUserCode();

        originAssistant.addRequest(requestNumber);
        goalAssistant.addRequest(requestNumber);

        confirmedAverage = student.getTotalAverage() > ResourceManager.getInstance()
                .getValue(Integer.class , "minimumNumberOfUnits-minor" , 19);
        statusRegistration();
    }

    public void statusRegistration() {
        if (confirmedOrigin == null || confirmedGoal == null) {
            status = RequestStatus.REGISTERED;
            message = status.toString();
        }
        else if (confirmedAverage && confirmedOrigin && confirmedGoal) {
            status = RequestStatus.ACCEPTED;
            message = status.toString();
        }
        else{
            status = RequestStatus.FAILED;
            message = status.toString();
        }
    }

    public void setConfirmedOrigin(Boolean confirmedOrigin) {
        this.confirmedOrigin = confirmedOrigin;
    }

    public void setConfirmedGoal(Boolean confirmedGoal) {
        this.confirmedGoal = confirmedGoal;
    }

    public EducationalAssistant getOriginAssistant() {
        return (EducationalAssistant) UserDatabase.getInstance().getProfessor(originAssistantCode);
    }

    public EducationalAssistant getGoalAssistant() {
        return (EducationalAssistant) UserDatabase.getInstance().getProfessor(goalAssistantCode);
    }

    public Boolean getConfirmedOrigin() {
        return confirmedOrigin;
    }

    public Boolean getConfirmedGoal() {
        return confirmedGoal;
    }
}
