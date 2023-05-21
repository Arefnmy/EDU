package client.gui.panels;

import client.controller.MainController;
import client.filemanager.ResourceManager;
import client.gui.RowInformation;
import client.gui.panels.professorpanel.CoursePanelProfessor;
import client.gui.AutoRefresh;
import client.gui.panels.studentpanel.CoursePanelStudent;
import shared.model.courseware.Courseware;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class CoursewarePanel extends JPanel implements AutoRefresh {
    private List<Courseware> coursewares;
    private boolean isProfessor;
    private JPanel mainPanel;
    private RowInformation header;
    private JButton calender;

    MainController mainController = MainController.getInstance();
    public CoursewarePanel(){
        initComp();
        setLoop(ResourceManager.getInstance().getValue(Integer.class , "defaultPanel-fps" , 1));
    }

    public void initComp(){
        mainPanel = new JPanel();
        header = new RowInformation(1 , true);
        header.addComponent(new JLabel("Lessons "));
        calender = new JButton("Calender");
        calender.setBackground(Color.GREEN);
        calender.addActionListener( e-> mainController.changeContentPane(new CalenderPanel()));
        add(calender);
        add(mainPanel);
    }

    @Override
    public void getResponse(){
        Response response = mainController.getAllCoursewares();
        coursewares = (List<Courseware>) response.getData("coursewareList");
        isProfessor = (boolean) response.getData("isProfessor");
    }

    @Override
    public void setInfo() {
        mainPanel.removeAll();
        mainPanel.add(header);
        mainPanel.setLayout(new GridLayout(coursewares.size() + 1 , 1 , 0  , 10));

        for (Courseware c : coursewares){
            RowInformation rowInformation = new RowInformation(1 , false);
            rowInformation.addComponent(new JLabel(c.getLessonName()));
            rowInformation.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    mainController.changeContentPane(isProfessor ?
                            new CoursePanelProfessor(c) : new CoursePanelStudent(c));
                }
                @Override
                public void mousePressed(MouseEvent mouseEvent) {}
                @Override
                public void mouseReleased(MouseEvent mouseEvent) {}
                @Override
                public void mouseEntered(MouseEvent mouseEvent) {}
                @Override
                public void mouseExited(MouseEvent mouseEvent) {}
            });
            mainPanel.add(rowInformation);
        }

        mainController.refresh();
    }
}
