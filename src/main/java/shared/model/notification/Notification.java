package shared.model.notification;

public class Notification {
    private static int codeCounter = 1000;
    private int code;
    private NotificationType type;
    private String message;
    private String sender;
    private long senderCode;
    private String data;//todo object

    private Notification(){}
    public Notification(NotificationType type, String message, String sender) {
        this.type = type;
        this.message = message;
        this.sender = sender;
        code = ++ codeCounter;
    }

    public NotificationType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public long getSenderCode() {
        return senderCode;
    }

    public void setSenderCode(long senderCode) {
        this.senderCode = senderCode;
    }

    public String getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setData(String data) {
        this.data = data;
    }
}
