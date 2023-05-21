package client.gui.panels.assistantpanel;

import client.controller.MainController;

import javax.swing.*;

public abstract class AddUserPanel extends JPanel {
    protected MainController mainController = MainController.getInstance();

    protected JScrollPane scrollPane;
    protected JPanel panel;

    protected JLabel usernameLabel;
    protected JLabel passwordLabel;
    protected JLabel firstNameLabel;
    protected JLabel lastNameLabel;
    protected JLabel emailLabel;
    protected JLabel mobileNumberLabel;
    protected JLabel nationalCodeLabel;

    protected JPanel rightPanel;

    protected JTextField usernameField;
    protected JTextField passwordField;
    protected JTextField firstnameField;
    protected JTextField lastNameField;
    protected JTextField emailField;
    protected JTextField mobileNumberField;
    protected JTextField nationalCodeField;

    protected JButton saveUser;

    protected AddUserPanel(){
        getResponse();
        initComp();
        alignComp();
        setActionListener();
    }

    public void initComp(){
        usernameLabel = new JLabel("Username :");
        passwordLabel = new JLabel("Password :");
        firstNameLabel = new JLabel("First name :");
        lastNameLabel = new JLabel("Last name :");
        emailLabel = new JLabel("Email :");
        mobileNumberLabel = new JLabel("Mobile number :");
        nationalCodeLabel = new JLabel("National code :");

        usernameField = new JTextField();
        passwordField = new JTextField();
        firstnameField = new JTextField();
        lastNameField = new JTextField();
        emailField = new JTextField();
        mobileNumberField = new JTextField();
        nationalCodeField = new JTextField();

        saveUser = new JButton("Save user");

        panel = new JPanel();
        scrollPane = new JScrollPane(panel);
    }

    public void alignComp(){
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(firstNameLabel);
        panel.add(firstnameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(mobileNumberLabel);
        panel.add(mobileNumberField);
        panel.add(nationalCodeLabel);
        panel.add(nationalCodeField);

        add(panel);
    }

    public abstract void setActionListener();

    public abstract void getResponse();
}
