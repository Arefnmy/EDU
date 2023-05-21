package client.gui.panels.professorpanel;

import client.gui.panels.ProfilePanel;
import shared.model.users.Professor;

import javax.swing.*;
import java.awt.*;

public class ProfilePanelProfessor extends ProfilePanel {
    private Professor professor;

    protected JLabel roomNumberLabel;
    protected JLabel degreeLabel;

    public ProfilePanelProfessor(Professor professor) {
        super(professor);
        this.professor = professor;
        setInfo();
        alignComp();
        setActionListener();
    }

    public void initComp(){
        super.initComp();
        roomNumberLabel = new JLabel();
        degreeLabel = new JLabel();
    }

    public void alignComp(){
        super.alignComp();

        roomNumberLabel.setText(professor.getRoomNumber());
        degreeLabel.setText(professor.getDegree().toString());

        information.setLayout(new GridLayout(9 , 2 , 15 , 10));
        information.add(new JLabel("Room number :"));
        information.add(roomNumberLabel);
        information.add(new JLabel("Degree :"));
        information.add(degreeLabel);
    }

    @Override
    public void setInfo(){
        super.setUser(professor);
        super.setInfo();
        roomNumberLabel.setText(professor.getRoomNumber());
        degreeLabel.setText(professor.getDegree().toString());
        mainController.refresh();
    }

    @Override
    public void getResponse() {
        professor = (Professor) mainController.getUser();
    }
}
