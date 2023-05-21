package client.gui.panels.professorpanel;

import client.controller.MainController;
import client.gui.RowInformation;
import client.gui.AutoRefresh;
import shared.model.Lesson;
import shared.model.educaionalrequests.Protest;
import shared.model.users.Student;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemporaryScoresProfessorPanel extends JPanel implements AutoRefresh {
    private List<Lesson> lessonList;
    private Map<Integer , List<Student>> studentMap;
    private Map<Integer , Map<Long , Protest>> protestMap;
    private Map<Integer , Map<Long , Double>> scoreMap;
    private JPanel mainPanel;
    private RowInformation header;


    MainController mainController = MainController.getInstance();
    public TemporaryScoresProfessorPanel() {
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setLoop(.2);
    }

    public void initComp(){
        mainPanel = new JPanel();

        header = new RowInformation(6 , true);
        header.addComponent(new JLabel("Student name"));
        header.addComponent(new JLabel("Student code"));
        header.addComponent(new JLabel("Score"));
        header.addComponent(new JLabel("Protest"));
        header.addComponent(new JLabel("Reply"));
        header.addComponent(new JLabel("Register Reply"));
    }

    public void alignComp(){
        mainPanel.add(header);
        add(mainPanel);
    }

    public void setActionListenerTemporary(int lesson , Map<Long , JTextField> textFieldMap){
        Map<Long , String> scoreMap = new HashMap<>();
        textFieldMap.forEach((l , j) -> scoreMap.put(l , j.getText()));
        Response response = mainController.registerScores(lesson , scoreMap , true);
        JOptionPane.showMessageDialog(null , response.getMessage());
    }

    public void setActionListenerFinal(int lesson , Map<Long , JTextField> textFieldMap){
        Map<Long , String> scoreMap = new HashMap<>();
        textFieldMap.forEach((l , j) -> scoreMap.put(l , j.getText()));
        Response response = mainController.registerScores(lesson , scoreMap , false);
        JOptionPane.showMessageDialog(null , response.getMessage());
    }

    @Override
    public void getResponse() throws NullPointerException {
        Response response = mainController.getTemporaryScores();
        lessonList = (List<Lesson>) response.getData("lessonList");
        studentMap = (Map<Integer, List<Student>>) response.getData("studentMap");
        protestMap = (Map<Integer, Map<Long, Protest>>) response.getData("protestMap");
        scoreMap = (Map<Integer, Map<Long, Double>>) response.getData("scoreMap");
    }

    @Override
    public void setInfo() {
        mainPanel.removeAll();
        mainPanel.setLayout(new GridLayout(2*lessonList.size() + 1 , 1 , 0 , 10));
        mainPanel.add(header);

        for (Lesson l : lessonList){
            Map<Long , JTextField> registerScoreMap = new HashMap<>();
            JButton temporaryRegister = new JButton("Temporary Register");
            JButton finalRegister = new JButton("Final Registration");

            RowInformation buttons = new RowInformation(4 , true);
            buttons.addComponent(new JLabel("Lesson :"));
            buttons.addComponent(new JLabel(l.getName()));
            buttons.addComponent(temporaryRegister);
            buttons.addComponent(finalRegister);
            mainPanel.add(buttons);

            List<Student> studentList = studentMap.get(l.getLessonNumber() + "");
            for (Student s : studentList){
                RowInformation info = new RowInformation(6 , false);
                info.addComponent(new JLabel(s.getName()));
                info.addComponent(new JLabel(String.valueOf(s.getUserCode())));

                Double score = scoreMap.get(l.getLessonNumber() + "").get(s.getUserCode() + "");
                JTextField scoreField = new JTextField("N/A");
                if (score != null)
                    scoreField.setText(String.valueOf(score));

                registerScoreMap.put(s.getUserCode() , scoreField);
                info.addComponent(scoreField);

                Protest protest = protestMap.get(l.getLessonNumber() + "").get(s.getUserCode() + "");
                JLabel registerLabel = new JLabel("-");
                if (protest != null)
                    registerLabel.setText(protest.getRegister());

                info.addComponent(registerLabel);

                JTextField replyField = new JTextField("-");
                if (protest != null)
                    replyField.setText(protest.getReplay());

                info.addComponent(replyField);

                JButton reply = new JButton("Send Reply");
                info.addComponent(reply);

                reply.addActionListener( e-> {
                    Response response = mainController.setRegister(l.getLessonNumber() , replyField.getText(), s.getUserCode());
                    JOptionPane.showMessageDialog(null , response.getMessage());
                });

                mainPanel.add(info);
            }

            temporaryRegister.addActionListener( e-> setActionListenerTemporary(l.getLessonNumber() , registerScoreMap));
            finalRegister.addActionListener( e-> setActionListenerFinal(l.getLessonNumber() , registerScoreMap));
        }

        mainController.refresh();
    }
}
