package client.gui.panels.assistantpanel;

import client.controller.MainController;
import client.gui.RowInformation;
import shared.model.Lesson;
import shared.model.users.Student;
import shared.response.Response;
import shared.response.ResponseState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EducationalStatusPanel extends JPanel {
    private JPanel studentPanel;
    private RowInformation header1;
    private RowInformation header2;
    private RowInformation info;
    private List<RowInformation> scoreRows;

    private JPanel searchPanel;
    private JLabel searchLabel;
    private JButton searchButton;

    private JLabel firstnameLabel;
    private JLabel lastnameLabel;
    private JTextField firstnameField;
    private JTextField lastnameField;
    private JRadioButton nameButton;

    private JLabel studentCodeLabel;
    private JTextField studentCodeField;
    private JRadioButton codeButton;

    MainController mainController = MainController.getInstance();
    public EducationalStatusPanel(){
        initComp();
        alignComp();
        setActionListener();
    }

    public void initComp(){
        searchPanel = new JPanel(null);
        studentPanel = new JPanel();
        header1 = new RowInformation(4 , true);
        header2 = new RowInformation(4 , true);

        searchLabel = new JLabel("Choose search model");
        searchButton = new JButton("Search");

        firstnameLabel = new JLabel("Student First name");
        lastnameLabel = new JLabel("Student Last name");
        firstnameField = new JTextField();
        lastnameField = new JTextField();
        nameButton = new JRadioButton("Name");

        studentCodeLabel = new JLabel("Student Code");
        studentCodeField = new JTextField();
        codeButton = new JRadioButton("Code" , true);

        ButtonGroup bg = new ButtonGroup();
        bg.add(nameButton);
        bg.add(codeButton);
    }

    public void alignComp(){
        setLayout(new GridLayout(2 , 1));

        header1.addComponent(new JLabel("Name"));
        header1.addComponent(new JLabel("Student Code"));
        header1.addComponent(new JLabel("Number of units passed"));
        header1.addComponent(new JLabel("Average"));

        header2.addComponent(new JLabel("Lesson (summery)"));
        header2.addComponent(new JLabel("Professor"));
        header2.addComponent(new JLabel("Number of units"));
        header2.addComponent(new JLabel("Score"));

        //searchPanel.setBounds(0 , 0 , 800, 400);

        searchLabel.setBounds(10 , 100 , 200 , 50);
        searchPanel.add(searchLabel);
        firstnameLabel.setBounds(250 , 0 , 150 , 50);
        searchPanel.add(firstnameLabel);
        firstnameField.setBounds(400 , 0 , 150 , 50);
        searchPanel.add(firstnameField);
        lastnameLabel.setBounds(250 , 50 , 150 , 50);
        searchPanel.add(lastnameLabel);
        lastnameField.setBounds(400 , 50 , 150 , 50);
        searchPanel.add(lastnameField);
        nameButton.setBounds(550 , 25 ,20 , 20);
        searchPanel.add(nameButton);
        studentCodeLabel.setBounds(250 , 100 , 150 , 50);
        searchPanel.add(studentCodeLabel);
        studentCodeField.setBounds(400 , 100 , 150 , 50);
        searchPanel.add(studentCodeField);
        codeButton.setBounds(550 , 125 , 20 , 20);
        searchPanel.add(codeButton);
        searchButton.setBounds(10 , 150 , 150 , 50);
        searchPanel.add(searchButton);

       add(searchPanel);
       add(studentPanel);
    }

    public void setActionListener(){
        searchButton.addActionListener( e-> {
            Response response;
            if (nameButton.isSelected())
                response = mainController.getEducationalStatus(firstnameField.getText(), lastnameField.getText());
            else
                response = mainController.getEducationalStatus(studentCodeField.getText());
            if (response.getResponseState() == ResponseState.ERROR){
                JOptionPane.showMessageDialog(null , response.getMessage());
                return;
            }
            Student student = (Student) response.getData("student");
            studentPanel.removeAll();
            info = new RowInformation(4, false);
            info.addComponent(new JLabel(student.getName()));
            info.addComponent(new JLabel(String.valueOf(student.getUserCode())));
            info.addComponent(new JLabel(String.valueOf((int) response.getData("numberOfUnitsPassed"))));
            info.addComponent(new JLabel(String.valueOf(student.getTotalAverage())));

            List<Lesson> lessonList = (List<Lesson>) response.getData("lessonList");
            Map<Integer , Double> scoreMap = (Map<Integer, Double>) response.getData("scoreMap");
            scoreRows = new ArrayList<>();
            for (Lesson l : lessonList) {
                RowInformation rowInformation = new RowInformation(4, false);
                JButton summery = new JButton(l.getName());
                //summery.addActionListener( a -> setSummeryButton(l));
                rowInformation.addComponent(summery);

                rowInformation.addComponent(new JLabel(l.getProfessorName()));
                rowInformation.addComponent(new JLabel(String.valueOf(l.getNumberOfUnits())));
                rowInformation.addComponent(new JLabel(String.valueOf(scoreMap.get(l.getLessonNumber() + ""))));

                scoreRows.add(rowInformation);
            }
            studentPanel.setLayout(new GridLayout(scoreRows.size() + 3, 1, 0, 20));
            studentPanel.add(header1);
            studentPanel.add(info);
            studentPanel.add(header2);
            for (RowInformation r : scoreRows)
                studentPanel.add(r);

            mainController.refresh();
        });
    }
    /*public void setSummeryButton(Lesson lesson){
        JDialog dialog = new JDialog();
        dialog.setBounds(400 , 400 , 400 , 500);
        dialog.setLayout(new GridLayout(4 , 2 , 0 , 20));
        dialog.add(new JLabel("Total Average :"));
        dialog.add(new JLabel(String.valueOf(lesson.getAverage(true))));
        dialog.add(new JLabel("Number of rejected students :"));
        dialog.add(new JLabel(String.valueOf(lesson.getRejectedStudents(true).size())));
        dialog.add(new JLabel("Number of passed students"));
        dialog.add(new JLabel(String.valueOf(lesson.getPassedStudents(true).size())));
        dialog.add(new JLabel("Average of passed students"));
        dialog.add(new JLabel(String.valueOf(lesson.getAverageOfPassedStudents(true))));

        dialog.setVisible(true);
    }*/
}
