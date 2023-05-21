package client.gui.panels.assistantpanel;

import client.controller.MainController;
import client.filemanager.ResourceManager;
import client.gui.RowInformation;
import shared.model.Lesson;
import shared.model.educaionalrequests.Protest;
import shared.model.users.Student;
import shared.response.Response;
import shared.response.ResponseState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemporaryScoresAssistantPanel extends JPanel {
    private JPanel lessonPanel;

    private JPanel searchPanel;
    private JLabel searchLabel;
    private JButton searchButton;

    private JLabel firstnameLabel;
    private JLabel lastnameLabel;
    private JTextField firstnameField;
    private JTextField lastnameField;
    private JRadioButton professorNameButton;

    private JLabel studentCodeLabel;
    private JTextField studentCodeField;
    private JRadioButton studentCodeButton;

    private RowInformation header;

    MainController mainController = MainController.getInstance();
    public TemporaryScoresAssistantPanel() {
        initComp();
        alignComp();
        setActionListener();
    }

    public void initComp(){
        searchPanel = new JPanel(null);
        lessonPanel = new JPanel();

        header = new RowInformation(7 , true);

        searchLabel = new JLabel("Choose search model");
        searchButton = new JButton("Search");

        firstnameLabel = new JLabel("Professor First name");
        lastnameLabel = new JLabel("Professor Last name");
        firstnameField = new JTextField();
        lastnameField = new JTextField();
        professorNameButton = new JRadioButton("Professor name");

        studentCodeLabel = new JLabel("Student Code");
        studentCodeField = new JTextField();
        studentCodeButton = new JRadioButton("Student Code" , true);

        ButtonGroup bg = new ButtonGroup();
        bg.add(professorNameButton);
        bg.add(studentCodeButton);
    }

    public void alignComp(){
        setLayout(new GridLayout(2 , 1));

        header.addComponent(new JLabel("Lesson"));
        header.addComponent(new JLabel("Professor"));
        header.addComponent(new JLabel("Student"));
        header.addComponent(new JLabel("Score"));
        header.addComponent(new JLabel("Protest"));
        header.addComponent(new JLabel("Reply"));
        header.addComponent(new JLabel("Summery"));

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
        professorNameButton.setBounds(550 , 25 ,20 , 20);
        searchPanel.add(professorNameButton);
        studentCodeLabel.setBounds(250 , 100 , 150 , 50);
        searchPanel.add(studentCodeLabel);
        studentCodeField.setBounds(400 , 100 , 150 , 50);
        searchPanel.add(studentCodeField);
        studentCodeButton.setBounds(550 , 125 , 20 , 20);
        searchPanel.add(studentCodeButton);
        searchButton.setBounds(10 , 150 , 150 , 50);
        searchPanel.add(searchButton);

        add(searchPanel);
        add(lessonPanel);
    }

    public void setActionListener(){
        searchButton.addActionListener( e-> {
            List<RowInformation> rows = new ArrayList<>();
            if (professorNameButton.isSelected()) {
                Response response = mainController.getTemporaryScores(firstnameField.getText(), lastnameField.getText());
                if (response.getResponseState() == ResponseState.ERROR){
                    JOptionPane.showMessageDialog(null , response.getMessage());
                    return;
                }
                List<Lesson> lessonList = (List<Lesson>) response.getData("lessonList");
                Map<Integer , List<Student>> studentMap = (Map<Integer, List<Student>>) response.getData("studentMap");
                Map<Integer, Map<Long, Protest>> protestMap = (Map<Integer, Map<Long, Protest>>) response.getData("protestMap");
                Map<Integer, Map<Long, Double>> scoreMap = (Map<Integer, Map<Long, Double>>) response.getData("scoreMap");
                    for (Lesson l : lessonList)
                        for (Student s : studentMap.get(l.getLessonNumber() + ""))
                            rows.add(setRow(s , l ,
                                    scoreMap.get(l.getLessonNumber() + "").get(s.getUserCode() + "")
                                            , protestMap.get(l.getLessonNumber() + "").get(s.getUserCode() + "")));
            }
            else{
                Response response = mainController.getTemporaryScores(studentCodeField.getText());
                if (response.getResponseState() == ResponseState.ERROR){
                    JOptionPane.showMessageDialog(null , response.getMessage());
                    return;
                }
                Student student = (Student) response.getData("student");
                List<Lesson> lessonList = (List<Lesson>) response.getData("lessonList");
                Map<Integer , Double> scoreMap = (Map<Integer, Double>) response.getData("scoreMap");
                Map<Integer , Protest> protestMap = (Map<Integer, Protest>) response.getData("protestMap");
                for (Lesson l : lessonList)
                    rows.add(setRow(student , l ,
                            scoreMap.get(l.getLessonNumber() + "") , protestMap.get(l.getLessonNumber() + "")));
            }

            lessonPanel.removeAll();
            lessonPanel.setLayout(new GridLayout(rows.size() + 1, 1, 0, 20));
            lessonPanel.add(header);
            for (RowInformation r : rows)
                    lessonPanel.add(r);

            mainController.refresh();
        });
}

    public RowInformation setRow(Student student , Lesson lesson , Double score , Protest protest) {
        RowInformation rowInformation = new RowInformation(7, false);
        rowInformation.addComponent(new JLabel(lesson.getName()));
        rowInformation.addComponent(new JLabel(lesson.getProfessorName()));
        rowInformation.addComponent(new JLabel(student.getName()));
        JLabel scoreLabel = new JLabel(
                ResourceManager.getInstance().getGraphicConfig().getProperty("nullScore" , "N/A"));
        if (score != null)
            scoreLabel.setText(String.valueOf(score));

        rowInformation.addComponent(scoreLabel);

        JLabel protestLabel = new JLabel();
        JLabel replyLabel = new JLabel();
        if (protest != null) {
            protestLabel.setText(protest.getRegister());
            replyLabel.setText(protest.getReplay());
        }

        rowInformation.addComponent(protestLabel);
        rowInformation.addComponent(replyLabel);

        JButton summery = new JButton("Summery");
        rowInformation.addComponent(summery);
        //summery.addActionListener( e-> setSummeryButton(lesson));

        return rowInformation;
    }

    /*public void setSummeryButton(Lesson lesson){
        JDialog dialog = new JDialog();
        dialog.setBounds(400 , 400 , 400 , 500);
        dialog.setLayout(new GridLayout(4 , 2 , 0 , 20));
        dialog.add(new JLabel("Total Average :"));
        dialog.add(new JLabel(String.valueOf(lesson.getAverage(false))));
        dialog.add(new JLabel("Number of rejected students :"));
        dialog.add(new JLabel(String.valueOf(lesson.getRejectedStudents(false).size())));
        dialog.add(new JLabel("Number of passed students"));
        dialog.add(new JLabel(String.valueOf(lesson.getPassedStudents(false).size())));
        dialog.add(new JLabel("Average of passed students"));
        dialog.add(new JLabel(String.valueOf(lesson.getAverageOfPassedStudents(false))));

        dialog.setVisible(true);
    }*/
}
