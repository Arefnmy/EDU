package client.gui.panels.studentpanel;

import client.controller.MainController;
import client.gui.RowInformation;
import client.gui.AutoRefresh;
import shared.model.Lesson;
import shared.model.educaionalrequests.Protest;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class TemporaryScoresStudentPanel extends JPanel implements AutoRefresh {
    private List<Lesson> lessonList;
    private Map<Integer , Protest> protestMap;
    private Map<Integer , Double> scoreMap;
    private JPanel mainPanel;
    private RowInformation header;

    MainController mainController = MainController.getInstance();
    public TemporaryScoresStudentPanel() {
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setLoop(.2);
    }

    public void initComp(){
        mainPanel = new JPanel();

        header = new RowInformation(6 , true);
        header.addComponent(new JLabel("Lesson"));
        header.addComponent(new JLabel("Professor"));
        header.addComponent(new JLabel("Score"));
        header.addComponent(new JLabel("Protest"));
        header.addComponent(new JLabel("Register"));
        header.addComponent(new JLabel("Reply"));
    }

    public void alignComp(){
        add(mainPanel);
    }

    @Override
    public void getResponse() throws NullPointerException {
        Response response = mainController.getTemporaryScores();
        lessonList = (List<Lesson>) response.getData("lessonList");
        protestMap = (Map<Integer, Protest>) response.getData("protestMap");
        scoreMap = (Map<Integer, Double>) response.getData("scoreMap");
    }

    @Override
    public void setInfo() {
        mainPanel.removeAll();
        mainPanel.add(header);

        mainPanel.setLayout(new GridLayout(lessonList.size() + 1 , 1 , 0 , 10));
        for (Lesson l : lessonList){
            RowInformation rowInformation = new RowInformation(6 , false);
            rowInformation.addComponent(new JLabel(l.getName()));
            rowInformation.addComponent(new JLabel(l.getProfessorName()));

            Double score = scoreMap.get(l.getLessonNumber()  + "");
            if (score == null)
                rowInformation.addComponent(new JLabel("N/A"));
            else
                rowInformation.addComponent(new JLabel(String.valueOf(score)));

            Protest protest = protestMap.get(l.getLessonNumber() + "");
            JTextField textField = new JTextField();
            if (protest != null) {
                textField.setText(protest.getRegister());
            }
            rowInformation.addComponent(textField);

            JButton register = new JButton("Register");
            rowInformation.addComponent(register);
            register.addActionListener( e-> {
                Response response = mainController.addProtest(l.getLessonNumber() , textField.getText());
                JOptionPane.showMessageDialog(null , response.getMessage());
                mainController.refresh();
            });

            JLabel reply = new JLabel();
            if (protest != null)
                reply.setText(protest.getReplay());
            rowInformation.addComponent(reply);

            mainPanel.add(rowInformation);
        }

        mainController.refresh();
    }
}
