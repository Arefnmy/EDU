package client.gui.panels;

import client.controller.MainController;
import shared.model.media.Media;
import shared.model.users.User;
import shared.response.Response;
import shared.util.FileUploader;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CreateChatPanel extends JPanel{
    private JScrollPane scrollPane;
    private List<User> users;
    private JList<String> usersList;

    private JTextField codeField;
    private JTextField messageField;

    private JButton sendMessage;
    private JButton sendFile;
    private JButton sendRequest;

    MainController mainController = MainController.getInstance();
    public CreateChatPanel(){
        initComp();
        Response response = mainController.getCreateChat();
        users = (List<User>) response.getData("userList");
        String[] usersName = new String[users.size()];
        for (int i = 0; i < users.size(); i++)
            usersName[i] = users.get(i).getName();
        usersList = new JList<>(usersName);
        scrollPane = new JScrollPane(usersList);
        alignComp();
        setActionListener();
    }

    public void initComp(){
        users = new ArrayList<>();
        codeField = new JTextField();
        messageField = new JTextField();
        sendMessage = new JButton("Send Message");
        sendFile = new JButton("Send Media");
        sendRequest = new JButton("Send Request");
    }

    public void alignComp(){
        setLayout(null);
        scrollPane.setBounds(760 , 110 , 220 , 400);
        messageField.setBounds(10 , 200 , 730 , 220);
        sendMessage.setBounds(10 , 450 , 600 , 30);
        sendFile.setBounds(610 , 450 , 130 , 30);
        codeField.setBounds(160 , 20 , 570 , 30);
        sendRequest.setBounds(10 , 20 , 140 , 30);

        add(scrollPane);
        add(messageField);
        add(sendMessage);
        add(sendFile);
        add(codeField);
        add(sendRequest);
    }

    public void setActionListener(){
        sendMessage.addActionListener( e-> {
            List<Long> userList = new ArrayList<>();
            for (int i : this.usersList.getSelectedIndices())
                userList.add(users.get(i).getUserCode());
            Response response = mainController.sendMessage(messageField.getText() , userList, null);
            JOptionPane.showMessageDialog(null , response.getMessage());
            usersList.clearSelection();
            messageField.setText("");
        });

        sendFile.addActionListener( e->{
            List<Long> userList = new ArrayList<>();
            for (int i : this.usersList.getSelectedIndices())
                userList.add(users.get(i).getUserCode());
            Media media = FileUploader.uploadFile();
            if (media != null) {
                Response response = mainController.sendMessage(null, userList, media);
                JOptionPane.showMessageDialog(null , response.getMessage());
                usersList.clearSelection();
            }
        });

        sendRequest.addActionListener( e-> {
            Response response = mainController.sendRequestToMessage(codeField.getText());
            JOptionPane.showMessageDialog(null , response.getMessage());
            codeField.setText("");
        });
    }
}
