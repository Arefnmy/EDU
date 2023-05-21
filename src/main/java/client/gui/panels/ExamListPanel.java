package client.gui.panels;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.filemanager.ResourceManager;
import client.gui.RowInformation;
import shared.model.Lesson;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExamListPanel extends JPanel implements AutoRefresh {
    private final boolean isStudent;
    private List<Lesson> lessonList;

    private JPanel mainPanel;
    private RowInformation header;
    private List<RowInformation> rows;

    MainController mainController = MainController.getInstance();
    public ExamListPanel() {
        Response response = mainController.getExamList();
        isStudent = (boolean) response.getData("isStudent");

        initComp();
        getResponse();
        setInfo();
        alignComp();
        setLoop(ResourceManager.getInstance().getValue(Integer.class , "defaultPanel-fps" , 1));
    }

    public void initComp(){
        mainPanel = new JPanel();
        rows = new ArrayList<>();
        header = new RowInformation(4 , true);
    }

    public void alignComp(){
        add(mainPanel);
        header.addComponent(new JLabel("Lesson"));
        if (isStudent)
            header.addComponent(new JLabel("Professor"));
        header.addComponent(new JLabel("Date"));
        header.addComponent(new JLabel("Time"));
    }

    public void alignRows(){
        mainPanel.removeAll();
        mainPanel.add(header);
        for (RowInformation r : rows)
            mainPanel.add(r);
        mainPanel.setLayout(new GridLayout(rows.size() + 1 , 1 , 0 , 20));
    }

    @Override
    public void setInfo() {
        rows.clear();
        for (Lesson l : lessonList){
            RowInformation rowInformation = new RowInformation(4 , false);
            rowInformation.addComponent(new JLabel(l.getName()));
            if (isStudent)
                rowInformation.addComponent(new JLabel(l.getProfessorName()));
            rowInformation.addComponent(new JLabel(l.getTime().getFinalDateStr()));
            rowInformation.addComponent(new JLabel(l.getTime().getFinalTimeStr()));

            rows.add(rowInformation);
        }

        alignRows();
        mainController.refresh();
    }

    @Override
    public void getResponse() {
        Response response = mainController.getExamList();
        lessonList = (List<Lesson>) response.getData("lessonList");
    }
}
