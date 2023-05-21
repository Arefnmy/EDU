package client.gui;

import client.controller.MainController;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;

public class ResetPassword extends JDialog {
    private JPanel mainPanel;
    private JPasswordField oldPassword;
    private JPasswordField newPassword;
    private JButton changePassword;

    MainController mainController = MainController.getInstance();
    public ResetPassword() {
        setTitle("Reset Password");
        setBounds(400 , 400 , 300 , 400);

        initComp();
        alignComp();
        setActionListener();

        setVisible(true);
    }

    public void initComp(){
        mainPanel = new JPanel(new GridLayout(2 , 2 , 10 , 20));
        oldPassword = new JPasswordField();
        newPassword = new JPasswordField();
        changePassword = new JButton("Change Password");
    }

    public void alignComp(){
        setLayout(new GridLayout(3 , 1));
        add(new JLabel("You should change your password!"));
        mainPanel.add(new JLabel("Old Password :"));
        mainPanel.add(oldPassword);
        mainPanel.add(new JLabel("New Password :"));
        mainPanel.add(newPassword);
        add(mainPanel);
        add(changePassword);
    }

    public void setActionListener(){
        changePassword.addActionListener( e-> {
            String oldPass = new String(oldPassword.getPassword());
            String newPass = new String(newPassword.getPassword());
            Response response = mainController.resetPassword(oldPass , newPass);
            JOptionPane.showMessageDialog(this , response.getMessage());
        });
    }
}

