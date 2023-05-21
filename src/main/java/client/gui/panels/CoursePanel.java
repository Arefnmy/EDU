package client.gui.panels;

import client.controller.MainController;
import client.filemanager.ResourceManager;
import client.gui.MainFrame;
import client.gui.RowInformation;
import client.gui.AutoRefresh;
import shared.model.courseware.Courseware;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;

public abstract class CoursePanel extends JPanel implements AutoRefresh {
    protected Courseware courseware;
    protected JLabel name;
    protected JLabel materials;
    protected JScrollPane materialScrollPane;
    protected JPanel materialPanel;
    protected JLabel exercises;
    protected JScrollPane exerciseScrollPanel;
    protected JPanel exercisePanel;
    protected JButton calender;
    protected RowInformation headerMaterial;
    protected RowInformation headerExercise;

    protected MainController mainController = MainController.getInstance();
    protected CoursePanel(Courseware courseware){
        this.courseware = courseware;
        initComp();
        alignComp();
        setActionListener();
        setLoop(ResourceManager.getInstance().getValue(Integer.class , "defaultPanel-fps" , 1));
    }

    public void initComp(){
        name = new JLabel(courseware.getLessonName().toUpperCase());
        materials = new JLabel("Educational Materials");
        exercises = new JLabel("Exercises");
        calender = new JButton("Calender");
        materialPanel = new JPanel();
        exercisePanel = new JPanel();
        materialScrollPane = new JScrollPane(materialPanel);
        exerciseScrollPanel = new JScrollPane(exercisePanel);
        headerMaterial = new RowInformation( 3 , true);
        headerMaterial.addComponent(new JLabel("Name"));
        headerMaterial.addComponent(new JLabel("Description"));
        headerMaterial.addComponent(new JLabel("Show more detail"));
        headerExercise = new RowInformation( 3 , true);
        headerExercise.addComponent(new JLabel("Name"));
        headerExercise.addComponent(new JLabel("Description"));
        headerExercise.addComponent(new JLabel("Show more detail"));
    }

    public void alignComp(){
        setLayout(null);
        name.setBounds((MainFrame.FRAME_WIDTH - 200) /2, 10 , 200 , 20);
        materials.setBounds(0 , 50 , 200 , 20);
        exercises.setBounds(MainFrame.FRAME_WIDTH/2 , 50 , 200, 20);
        calender.setBounds(10 , 550 , MainFrame.FRAME_WIDTH/2 - 200 , 50);
        materialScrollPane.setBounds(0 , 100 , MainFrame.FRAME_WIDTH/2 , 450);
        exerciseScrollPanel.setBounds(MainFrame.FRAME_WIDTH/2 , 100 , MainFrame.FRAME_WIDTH/2 , 450);
        materials.setBackground(Color.GREEN);
        exercises.setBackground(Color.GREEN);
        calender.setBackground(Color.GREEN);

        add(name);
        add(materials);
        add(exercises);
        add(calender);
        add(materialScrollPane);
        add(exerciseScrollPanel);
    }

    public void setActionListener(){
        calender.addActionListener( e-> mainController.changeContentPane(new CalenderPanel()));
    }

    @Override
    public void getResponse() {
        Response response = mainController.getCourseware(courseware.getLessonNumber());
        courseware = (Courseware) response.getData("courseware");
    }
}
