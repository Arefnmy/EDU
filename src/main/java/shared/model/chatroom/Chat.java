package shared.model.chatroom;

import java.util.LinkedList;

public class Chat {
    private long userCode;
    private String userName;
    private byte[] image;
    private LinkedList<Message> messages;

    public Chat(){}
    public Chat(long userCode, String userName, LinkedList<Message> messages) {
        this.userCode = userCode;
        this.userName = userName;
        this.messages = messages;
    }

    public long getUserCode() {
        return userCode;
    }

    public String getUserName() {
        return userName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public LinkedList<Message> getMessages() {
        return messages;
    }
}
