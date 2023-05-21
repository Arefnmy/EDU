package client.gui.panels;

import client.gui.AutoRefresh;
import client.controller.MainController;
import shared.model.courseware.Courseware;
import shared.model.courseware.Exercise;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;

public abstract class ExercisePanel extends JPanel implements AutoRefresh {
    protected Courseware courseware;
    protected Exercise exercise;
    protected JLabel name;
    protected JTextArea description;
    protected JPanel time;
    private JLabel uploadType;
    protected JPanel media;

    protected MainController mainController = MainController.getInstance();
    protected ExercisePanel(Exercise exercise ,Courseware courseware){
        this.courseware = courseware;
        this.exercise = exercise;
        initComp();
        alignComp();
        setLoop(.2);
    }

    public void initComp(){
        name = new JLabel(exercise.getName());
        description = new JTextArea(exercise.getDescription());
        description.setLineWrap(true);
        uploadType = new JLabel("Upload Type : " + exercise.getUploadType());

        JLabel startTime = new JLabel("Start Time : " + exercise.getStartTime().toString());
        JLabel endTime = new JLabel("End Time : " + exercise.getEndTime().toString());
        JLabel endUploadTime = new JLabel("End Upload Time Without Decreasing Score : " +
                exercise.getUploadWithoutDecreasingScore().toString());
        time = new JPanel(new GridLayout(3 , 1 , 0 , 10));
        time.add(startTime);
        time.add(endTime);
        time.add(endUploadTime);

        media = new JPanel();
    }

    public void alignComp(){
        setLayout(null);
        name.setBounds(50, 20, 240, 30);
        media.setBounds(600, 200, 340, 100);
        time.setBounds(500, 20, 500, 180);
        JScrollPane scrollPane = new JScrollPane(description);
        scrollPane.setBounds(50, 120, 200, 210);
        uploadType.setBounds(50 , 50 , 200 , 30);

        add(name);
        add(uploadType);
        add(scrollPane);
        add(time);
        add(media);
    }

    @Override
    public void getResponse(){
        Response response = mainController.getCourseware(courseware.getLessonNumber());
        courseware = (Courseware) response.getData("courseware");
        exercise = courseware.getExercise(exercise.getName());
    }
}
