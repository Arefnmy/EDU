package shared.model.educaionalrequests;

public enum RequestStatus {
    REGISTERED,
    ACCEPTED,
    FAILED;

    public String toString(){
            String status = super.toString().toLowerCase();
            status = (status.charAt(0) +"").toUpperCase() + status.substring(1);
            return status;
    }
}
