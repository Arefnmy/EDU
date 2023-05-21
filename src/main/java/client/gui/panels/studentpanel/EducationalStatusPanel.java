package client.gui.panels.studentpanel;

import client.gui.AutoRefresh;
import client.controller.MainController;
import client.gui.RowInformation;
import shared.model.Lesson;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class EducationalStatusPanel extends JPanel implements AutoRefresh {
    private List<Lesson> lessonList;
    private Map<Integer , Double> scoreMap;
    private int numberOfUnitPassed;
    private Double average;

    private JLabel numberOfUnitPassedLabel;
    private JLabel averageLabel;

    private JPanel scorePanel;
    private RowInformation header;

    MainController mainController = MainController.getInstance();
    public EducationalStatusPanel() {
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setLoop(1);
    }

    public void initComp(){
        scorePanel = new JPanel();
        numberOfUnitPassedLabel = new JLabel();
        averageLabel = new JLabel();

        header = new RowInformation(4 , true);
        header.addComponent(new JLabel("Lesson"));
        header.addComponent(new JLabel("Professor"));
        header.addComponent(new JLabel("Number of units"));
        header.addComponent(new JLabel("Score"));
    }

    public void alignComp(){
        numberOfUnitPassedLabel.setBackground(Color.GRAY);
        averageLabel.setBackground(Color.GRAY);

        add(scorePanel);
        add(numberOfUnitPassedLabel);
        add(averageLabel);
    }

    @Override
    public void getResponse() throws NullPointerException {
        Response response = mainController.getEducationalStatus();
        lessonList = (List<Lesson>) response.getData("lessonList");
        scoreMap = (Map<Integer, Double>) response.getData("scoreMap");
        numberOfUnitPassed = (int) response.getData("numberOfUnitPassed");
        average = (Double) response.getData("average");
    }

    @Override
    public void setInfo() {
        numberOfUnitPassedLabel.setText("Number of units passed : " + numberOfUnitPassed);
        averageLabel.setText("Total Average : " + average);

        scorePanel.removeAll();
        scorePanel.setLayout(new GridLayout(lessonList.size() + 1 , 1 , 0 , 10));
        scorePanel.add(header);
        for (Lesson l : lessonList){
            RowInformation rowInformation = new RowInformation(4 , false);
            rowInformation.addComponent(new JLabel(l.getName()));
            rowInformation.addComponent(new JLabel(l.getProfessorName()));
            rowInformation.addComponent(new JLabel(String.valueOf(l.getNumberOfUnits())));
            rowInformation.addComponent(new JLabel(String.valueOf(scoreMap.get(l.getLessonNumber() +""))));

            scorePanel.add(rowInformation);
        }
        mainController.refresh();
    }
}
