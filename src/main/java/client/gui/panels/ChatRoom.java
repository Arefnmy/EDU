package client.gui.panels;

import client.gui.AutoRefresh;
import client.controller.MainController;
import client.filemanager.ResourceManager;
import shared.model.UploadType;
import shared.model.chatroom.Chat;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

public class ChatRoom extends JPanel implements AutoRefresh {
    private List<Chat> chatList;
    private JPanel chatsPanel;

    MainController mainController = MainController.getInstance();
    public ChatRoom(){
        initComp();
        getResponse();
        setInfo();
        setLoop(ResourceManager.getInstance().getValue(Integer.class , "defaultPanel-fps" , 1));
    }

    public void initComp(){
        chatsPanel = new JPanel();
        add(chatsPanel);
    }

    public JPanel getChatPanel(Chat chat){
        JPanel panel = new JPanel(new GridLayout(2 , 1 , 0 , 15));
        panel.add(new JLabel(chat.getUserName()) , SwingConstants.CENTER);
        String lastMessage = "";
        if (!chat.getMessages().isEmpty()){
            if (chat.getMessages().getLast().getUploadType() == UploadType.TEXT)
                lastMessage = chat.getMessages().getLast().getText();
            else
                lastMessage = chat.getMessages().getLast().getMedia().getName();
        }
        panel.add(new JLabel(lastMessage));
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainController.changeContentPane(new ChatPanel(chat));
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
        });
        panel.setBorder(BorderFactory.createLineBorder(
                new Color(ResourceManager.getInstance().getValue(Integer.class , "borderColor" , 0))));
        return panel;
    }

    @Override
    public void setInfo() {
        chatsPanel.removeAll();
        chatsPanel.setLayout(new GridLayout(chatList.size() , 1));
        for (Chat chat : chatList)
            chatsPanel.add(getChatPanel(chat));
        mainController.refresh();
    }

    @Override
    public void getResponse() {
        Response response = mainController.getAllChats();
        chatList = (LinkedList<Chat>) response.getData("chatList");
    }
}
