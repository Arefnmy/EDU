package client.gui.panels;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.gui.RowInformation;
import shared.model.Lesson;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.List;

public class WeeklySchedulePanel extends JPanel implements AutoRefresh {
    private List<List<Lesson>> lessonList;

    private JPanel mainPanel;
    private RowInformation header;
    private RowInformation[] rows;

    MainController mainController = MainController.getInstance();

    public WeeklySchedulePanel() {
        initComp();
        getResponse();
        setInfo();
        alignComp();

        setLoop(1);
    }

    public void initComp() {
        mainPanel = new JPanel();
        rows = new RowInformation[7];

        header = new RowInformation(2, true);
        header.addComponent(new JLabel("Day"));
        header.addComponent(new JLabel("Lessons"));
    }

    public void alignComp() {
        mainPanel.setLayout(new GridLayout(8, 1, 0, 20));
        mainPanel.add(header);
        for (int i = 0; i < 7; i++) {
            mainPanel.add(rows[i]);
        }
        add(mainPanel);
    }

    @Override
    public void setInfo(){
        for (int i = 0; i < 7; i++) {
            List<Lesson> lessons = lessonList.get(i);
            rows[i] = new RowInformation(lessons.size() + 1, false);
            rows[i].addComponent(new JLabel(DayOfWeek.of(i + 1).toString()));
            for (Lesson l : lessons) {
                String lessonTime = l.getName() + "  " + l.getTime().getTimeInWeek();
                rows[i].addComponent(new JLabel(lessonTime));
            }
        }
        mainController.refresh();
    }

    @Override
    public void getResponse() {
        lessonList = (List) mainController.getSortedLessonInWeek().getData("lessonList");
    }
}
