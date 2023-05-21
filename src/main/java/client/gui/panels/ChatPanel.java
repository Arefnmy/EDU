package client.gui.panels;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.filemanager.ResourceManager;
import shared.model.UploadType;
import shared.model.chatroom.Chat;
import shared.model.chatroom.Message;
import shared.model.media.Media;
import shared.response.Response;
import shared.util.FileUploader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class ChatPanel extends JPanel implements AutoRefresh {
    private Chat chat;
    private JLabel image;
    private JLabel name;
    private JTextField messageField;
    private JButton sendMessage;
    private JButton sendFile;

    private JScrollPane scrollPane;
    private JPanel chatPage;

    MainController mainController = MainController.getInstance();
    public ChatPanel(Chat chat){
        this.chat = chat;
        initComp();
        //getResponse();
        setInfo();
        alignComp();
        setActionListener();
        setLoop(1);
    }

    public void initComp(){
        chatPage = new JPanel();
        scrollPane = new JScrollPane();
        image = new JLabel(ResourceManager.getInstance().getImage(chat.getImage()));
        name = new JLabel(chat.getUserName());
        messageField = new JTextField();
        sendMessage = new JButton("Send");
        sendFile = new JButton("Send File");
        scrollPane = new JScrollPane();
    }

    public void alignComp(){
        setLayout(null);
        chatPage.setLayout(new BoxLayout(chatPage , BoxLayout.Y_AXIS));
        scrollPane.setViewportView(chatPage);
        image.setBounds(10 , 10 , 120 ,  100);
        name.setBounds(300 , 40 , 280 , 40);
        messageField.setBounds(0 , 560 , 860 , 70);
        sendMessage.setBounds(860 , 560 , 140 , 35);
        sendFile.setBounds(860 , 595 , 140 , 35);
        scrollPane.setBounds(0 , 110 , 1000 , 550);

        add(image);
        add(name);
        add(messageField);
        add(sendMessage);
        add(sendFile);
        add(scrollPane);
    }

    public void setActionListener(){
        sendMessage.addActionListener( e-> {
            mainController.sendMessage(messageField.getText() , new ArrayList<>(List.of(chat.getUserCode())), null);
            messageField.setText("");
        });

        sendFile.addActionListener( e->{
            Media media = FileUploader.uploadFile();
            mainController.sendMessage(null , new ArrayList<>(List.of(chat.getUserCode())) , media);
        });
    }

    @Override
    public void setInfo() {
        chatPage.removeAll();
        for (Message m : chat.getMessages()){
            JLabel messageLabel;
            if (m.getUploadType() == UploadType.TEXT) {
                String message = String.format("%s : %s (%s)", m.getSender(), m.getText(), m.getTime());
                messageLabel = new JLabel(message);
            }
            else{
                String message = String.format("%s : %s (%s)",
                        m.getSender(), m.getMedia().getName() + "-" + m.getMedia().getMediaType(), m.getTime());
                messageLabel = new JLabel(message);
                messageLabel.setForeground(Color.DARK_GRAY);
                messageLabel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        JOptionPane.showMessageDialog(null , FileUploader.downloadFile(m.getMedia()));
                    }
                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {}
                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {}
                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {}
                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {}
                });
            }
            chatPage.add(messageLabel);
        }

        mainController.refresh();
    }

    @Override
    public void getResponse() {
        Response response = mainController.getChat(chat.getUserCode());
        chat = (Chat) response.getData("chat");
    }
}
