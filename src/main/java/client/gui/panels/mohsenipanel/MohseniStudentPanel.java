package client.gui.panels.mohsenipanel;

import client.gui.panels.studentpanel.ProfilePanelStudent;
import shared.model.users.Student;

public class MohseniStudentPanel extends ProfilePanelStudent {

    public MohseniStudentPanel(Student student) {
        super(student);
        emailFiled.setEditable(false);
        mobileNumberField.setEditable(false);
        saveInfo.setVisible(false);
        themeButton.setVisible(false);
    }

    @Override
    public void getResponse(){}
    @Override
    public void setActionListener(){}
}
