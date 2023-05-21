package client.gui;

import client.controller.MainController;
import client.filemanager.ResourceManager;
import shared.model.LoginState;
import shared.response.Response;
import shared.response.ResponseState;

import javax.swing.*;

public class LoginMenu extends JPanel {
    private JLabel userLabel;
    private JTextField usernameField;

    private JLabel passwordLabel;
    private JPasswordField passwordField;

    private Captcha captcha;
    private JTextField captchaNumber;

    private JButton loginButton;

    MainController mainController = MainController.getInstance();

    public LoginMenu(){
        setLayout(null);
        initialize();
        align();
        setLoginButton();
    }

    public void initialize(){
        userLabel = new JLabel("username :");
        usernameField = new JTextField();
        passwordLabel = new JLabel("password :");
        passwordField = new JPasswordField();
        captcha = new Captcha();
        captchaNumber = new JTextField();
        loginButton = new JButton("login");
    }

    public void align(){
        userLabel.setBounds(MainFrame.FRAME_WIDTH/2 - 200 , 10 , 100 , 20);
        usernameField.setBounds(MainFrame.FRAME_WIDTH/2 - 100 , 10 , 100 , 20);
        passwordLabel.setBounds(MainFrame.FRAME_WIDTH/2 - 200, 40 , 100 , 20);
        passwordField.setBounds(MainFrame.FRAME_WIDTH/2 - 100 , 40 , 100 , 20);
        captcha.setBounds(MainFrame.FRAME_WIDTH/2 - 150 , 100 , 100 , 100);
        captchaNumber.setBounds(MainFrame.FRAME_WIDTH/2 - 150 , 200 , 100 , 20);
        loginButton.setBounds(MainFrame.FRAME_WIDTH/2 - 150 , 250 , 100 , 50);
        add(userLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(captcha);
        add(captchaNumber);
        add(loginButton);
    }

    public void setLoginButton(){
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String captchaN = captchaNumber.getText();

            if (!captchaN.equals(String.valueOf( captcha.getCaptchaNumber())) ){
                JOptionPane.showMessageDialog( null,
                        ResourceManager.getInstance().getGraphicConfig().getProperty("captcha-err" ,"Captcha Error!"));
                clearFields();
                return;
            }
            Response response = mainController.login(username , password);
            LoginState state = (LoginState) response.getData("loginState");
            if (response.getResponseState() == ResponseState.ERROR)
                JOptionPane.showMessageDialog(null , response.getMessage());
            switch (state){
                case USERNAME_NOT_FOUND:
                case PASSWORD_IS_WRONG:
                    clearFields();
                    break;
                case RESET_PASSWORD:
                    new ResetPassword();
                    clearFields();
                    break;
                case CAN_LOGIN:
                    mainController.changeContentPane(mainController.getMainMenu());
                    mainController.login();
            }
        });
    }

    public void clearFields(){
        usernameField.setText("");
        passwordField.setText("");
        captchaNumber.setText("");
        captcha.changeIcon();
    }
}
