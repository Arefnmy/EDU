package server.model.chatroom;

import server.database.UserDatabase;
import server.model.users.User;
import shared.model.Time;
import shared.model.UploadType;
import shared.model.media.Media;

public class ChatMessage implements Comparable<ChatMessage> {
    private final long sender;
    private final UploadType type;
    private final String text;
    private final Media media;
    private final Time time;

    public ChatMessage(User sender , Media media , Time time){
        type = UploadType.MEDIA;
        this.sender = sender.getUserCode();
        this.media = media;
        this.time = time;
        text = null;
    }
    public ChatMessage(User sender, String text, Time time) {
        type = UploadType.TEXT;
        this.sender = sender.getUserCode();
        this.text = text;
        this.time = time;
        media = null;
    }

    public User getSender() {
        return UserDatabase.getInstance().getUser(sender);
    }

    public String getText() {
        return text;
    }

    public Time getTime() {
        return time;
    }

    public UploadType getType() {
        return type;
    }

    public Media getMedia() {
        return media;
    }

    @Override
    public int compareTo(ChatMessage chatMessage) {
        return time.compareTo(chatMessage.getTime());
    }
}
