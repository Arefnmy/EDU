package client.gui.panels;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.filemanager.ResourceManager;
import client.gui.RowInformation;
import shared.model.users.User;
import shared.response.Response;

import javax.swing.*;

public abstract class ProfilePanel extends JPanel implements AutoRefresh {
    private User user;

    protected JPanel information;

    protected JButton themeButton;
    protected JTextField mobileNumberField;
    protected JTextField emailFiled;

    protected JLabel firstNameLabel;
    protected JLabel lastNameLabel;
    protected JLabel nationalCodeLabel;
    protected JLabel collegeLabel;
    protected JLabel userCodeLabel;
    protected JLabel image;

    protected JButton saveInfo;

    protected MainController mainController = MainController.getInstance();
    protected ProfilePanel(User user){
        this.user = user;
        initComp();
        setLoop(1);

        if (!mainController.isOnline()){
            mobileNumberField.setEditable(false);
            emailFiled.setEditable(false);
            saveInfo.setEnabled(false);
        }
    }

    public void initComp(){
        information = new JPanel();

        themeButton = new JButton(
                ResourceManager.getInstance().getGraphicConfig().getProperty("themeButton-msg" , "Change Theme"));
        themeButton.setRolloverEnabled(false);
        themeButton.setBackground(RowInformation.getHeaderColor());

        mobileNumberField = new JTextField(user.getMobileNumber());
        emailFiled = new JTextField(user.getEmail());

        firstNameLabel = new JLabel();
        lastNameLabel = new JLabel();
        nationalCodeLabel = new JLabel();
        collegeLabel = new JLabel();
        userCodeLabel = new JLabel();

        image = new JLabel(ResourceManager.getInstance().getImage(user.getImage()));
        saveInfo = new JButton("Save Information");
    }

    public void alignComp(){
        information.add(new JLabel("First name:"));
        information.add(firstNameLabel);
        information.add(new JLabel("Last name:" ));
        information.add(lastNameLabel);
        information.add(new JLabel("Email:"));
        information.add(emailFiled);
        information.add(new JLabel("Mobile number:"));
        information.add(mobileNumberField);
        information.add(new JLabel("National code:"));
        information.add(nationalCodeLabel);
        information.add(new JLabel("College:"));
        information.add(collegeLabel);
        information.add(new JLabel("User code :"));
        information.add(userCodeLabel);

        add(themeButton);
        add(information);
        add(saveInfo);
        add(image);
    }

    public void setActionListener(){
        saveInfo.addActionListener( e-> {
            Response response = mainController.changeProfile(emailFiled.getText() , mobileNumberField.getText());

            JOptionPane.showMessageDialog(new JFrame() , response.getMessage());
            mainController.refresh();
        });

        themeButton.addActionListener( e ->{
            RowInformation.changeStyle();
            themeButton.setBackground(RowInformation.getHeaderColor());

            mainController.refresh();
        });
    }

    @Override
    public void setInfo(){
        firstNameLabel.setText(user.getFirstName());
        lastNameLabel.setText(user.getLastName());
        nationalCodeLabel.setText(user.getNationalCode());
        collegeLabel.setText(user.getCollegeName());
        userCodeLabel.setText(String.valueOf(user.getUserCode()));
    }

    protected void setUser(User user) {
        this.user = user;
    }
}
