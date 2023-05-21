package client.gui.panels.assistantpanel;

import shared.model.Lesson;
import shared.model.ProfessorDegree;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddProfessorPanel extends AddUserPanel {

    private JLabel roomNumberLabel;
    private JTextField roomNumberField;

    private JComboBox<String> degrees;
    private List<Lesson> lessonList;
    private JScrollPane lessonPane;
    private JList<String> lessons;

    public AddProfessorPanel() {
        super();
    }

    @Override
    public void initComp() {
        super.initComp();
        roomNumberLabel = new JLabel("Room number");
        roomNumberField = new JTextField();

        rightPanel = new JPanel(new GridLayout(1 , 2 , 5 , 10));

        degrees = new JComboBox<>(ProfessorDegree.getDegrees());

        String[] lessonStr = new String[lessonList.size()];
        for (int i = 0; i < lessonList.size() ; i++)
            lessonStr[i] = lessonList.get(i).getName();
        lessons = new JList<>(lessonStr);
        lessonPane = new JScrollPane(lessons);
    }

    @Override
    public void alignComp() {
        super.alignComp();
        panel.add(roomNumberLabel);
        panel.add(roomNumberField);
        panel.add(degrees);

        panel.add(saveUser);
        panel.setLayout(new GridLayout(10 , 20 , 20 , 50));

        rightPanel.add(new JLabel("Select lessons"));
        rightPanel.add(lessonPane);

        add(rightPanel);
    }

    public void setActionListener(){
        saveUser.addActionListener( e-> {
            int[] indices = lessons.getSelectedIndices();
            List<Lesson> lessonList = new ArrayList<>();
            for (int i : indices)
                lessonList.add(this.lessonList.get(i));

            Response response = mainController.addProfessor(usernameField.getText(), passwordField.getText(),
                    firstnameField.getText(), lastNameField.getText(), emailField.getText(),
                    mobileNumberField.getText(), nationalCodeField.getText(),
                    roomNumberField.getText() , (String) degrees.getSelectedItem() ,lessonList);
            JOptionPane.showMessageDialog(null , response.getMessage());
        });
    }

    @Override
    public void getResponse() {
        Response response = mainController.getAddUser();
        lessonList = (List<Lesson>) response.getData("lessonList");
    }
}
