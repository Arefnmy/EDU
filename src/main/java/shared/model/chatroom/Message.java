package shared.model.chatroom;

import shared.model.Time;
import shared.model.UploadType;
import shared.model.media.Media;

public class Message {
    private UploadType uploadType;
    private String sender;
    private String text;
    private Media media;
    private Time time;

    public Message(){}
    public Message(UploadType uploadType , String sender, String text, Time time , Media media) {
        this.uploadType = uploadType;
        this.sender = sender;
        this.text = text;
        this.media = media;
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public UploadType getUploadType() {
        return uploadType;
    }

    public Media getMedia() {
        return media;
    }

    public Time getTime() {
        return time;
    }
}
