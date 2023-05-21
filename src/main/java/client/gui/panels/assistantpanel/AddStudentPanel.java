package client.gui.panels.assistantpanel;

import shared.model.EducationalStatus;
import shared.model.Grade;
import shared.model.users.Professor;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddStudentPanel extends AddUserPanel {

    private JLabel entryYearLabel;
    private JSpinner entryYear;

    private List<Professor> professorList;
    private JComboBox<String> grade;
    private JComboBox<String> status;

    private JComboBox<String> supervisors;

    public AddStudentPanel() {
        super();
    }

    public void initComp() {
        super.initComp();

        rightPanel = new JPanel(new GridLayout(2 , 2 , 5 , 10));

        String[] supervisorStr = new String[professorList.size()];
        for (int i = 0; i < professorList.size(); i++)
            supervisorStr[i] = professorList.get(i).getName();
        supervisors = new JComboBox<>(supervisorStr);

        entryYearLabel = new JLabel("Entry year :");
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(1399 , 1395 , 1402 , 1);
        entryYear = new JSpinner(spinnerNumberModel);
        grade = new JComboBox<>(Grade.getGrades());
        status = new JComboBox<>(EducationalStatus.getStatuses());
    }

    public void alignComp() {
        super.alignComp();

        panel.add(entryYearLabel);
        panel.add(entryYear);
        panel.add(grade);
        panel.add(status);

        panel.add(saveUser);
        panel.setLayout(new GridLayout(11, 2, 20, 50));

        rightPanel.add(new JLabel("Select supervisor"));
        rightPanel.add(supervisors);

        add(rightPanel);
    }

    public void setActionListener() {
        saveUser.addActionListener(e -> {
            String supervisor = (String) supervisors.getSelectedItem();

            Response response = mainController.addStudent(usernameField.getText(), passwordField.getText(),
                    firstnameField.getText(), lastNameField.getText(), emailField.getText(),
                    mobileNumberField.getText(), nationalCodeField.getText(),
                    (String) grade.getSelectedItem(), (Integer) entryYear.getValue(), (String) status.getSelectedItem(),supervisor);
            JOptionPane.showMessageDialog(null , response.getMessage());
        });
    }

    @Override
    public void getResponse() {
        Response response = mainController.getAddUser();
        professorList = (List<Professor>) response.getData("professorList");
    }
}
