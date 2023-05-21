package client.gui.panels.studentpanel;

import client.gui.panels.ProfilePanel;
import shared.model.users.Student;

import javax.swing.*;
import java.awt.*;

public class ProfilePanelStudent extends ProfilePanel {
    private Student student;

    private JLabel totalAverageLabel;
    private JLabel supervisorLabel;
    private JLabel entryYearLabel;
    private JLabel gradeLabel;
    private JLabel stateLabel;

    public ProfilePanelStudent(Student  student) {
        super(student);
        this.student = student;
        setInfo();
        alignComp();
        setActionListener();
    }


    public void initComp(){
        super.initComp();
        totalAverageLabel = new JLabel();
        supervisorLabel = new JLabel();
        entryYearLabel = new JLabel();
        gradeLabel = new JLabel();
        stateLabel = new JLabel();
    }

    public void alignComp(){
        super.alignComp();

        information.setLayout(new GridLayout(12 , 2 , 15 , 10));
        information.add(new JLabel("Total Average :"));
        information.add(totalAverageLabel);
        information.add(new JLabel("Supervisor :"));
        information.add(supervisorLabel);
        information.add(new JLabel("Entry year :"));
        information.add(entryYearLabel);
        information.add(new JLabel("Grade :"));
        information.add(gradeLabel);
        information.add(new JLabel("Educational status :"));
        information.add(stateLabel);
    }

    @Override
    public void setInfo() {
        super.setUser(student);
        super.setInfo();
        totalAverageLabel.setText(String.valueOf(student.getTotalAverage()));
        supervisorLabel.setText(student.getSupervisor());
        entryYearLabel.setText(String.valueOf(student.getEntryYear()));
        gradeLabel.setText(student.getGrade().toString());
        stateLabel.setText(student.getStatus().toString());
        mainController.refresh();
    }

    @Override
    public void getResponse() {
        student = (Student) mainController.getUser();
    }
}
