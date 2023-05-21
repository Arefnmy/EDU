package server.model.chatroom;

import server.database.UserDatabase;
import server.model.users.User;

import java.util.LinkedList;

public class Chat {
    private final long user1;
    private final long user2;
    private final String name;
    private final LinkedList<ChatMessage> messages;

    public Chat(User user1 , User user2){
        this.user1 = user1.getUserCode();
        this.user2 = user2.getUserCode();

        name = this.user1 + "-" + this.user2;
        messages = new LinkedList<>();
    }

    public long getUser1() {
        return user1;
    }

    public long getUser2() {
        return user2;
    }

    public String getName() {
        return name;
    }

    public User getOtherUser(long userCode){
        if (userCode == user1)
            return UserDatabase.getInstance().getUser(user2);
        if (userCode == user2)
            return UserDatabase.getInstance().getUser(user1);
        return null;
    }

    public LinkedList<ChatMessage> getMessages() {
        return messages;
    }

    public void addMessage(ChatMessage message){
        messages.add(message);
    }
}
