package server.database;

import server.model.chatroom.Chat;
import server.model.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatDatabase {
    private final List<Chat> chatList = new ArrayList<>();

    private static final ChatDatabase instance = new ChatDatabase();

    public synchronized static ChatDatabase getInstance(){
        return instance;
    }

    public void addChat(Chat chat){
        chatList.add(chat);
    }

    public List<Chat> getChatList() {
        return chatList;
    }

    public List<Chat> getAllChats(User user){
        return chatList.stream()
                .filter(chat -> user.getUserCode() == chat.getUser1() || (user.getUserCode() == chat.getUser2()))
                .collect(Collectors.toList());
    }

    public Chat getChat(User user1 , User user2){
        for (Chat chat : chatList){
            if ( (user1.getUserCode() == chat.getUser1() && user2.getUserCode() == chat.getUser2()) ||
                    (user2.getUserCode() == chat.getUser1() && user1.getUserCode() == chat.getUser2()) )
                return chat;
        }
        return null;
    }
}
