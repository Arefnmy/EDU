package client.gui.panels.assistantpanel;

import client.controller.MainController;
import client.gui.RowInformation;
import client.gui.TimePanel;
import shared.model.Time;
import shared.model.users.Student;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ChooseLessonTime extends JPanel {
    private List<Student> studentList;
    private JPanel studentsPanel;
    private RowInformation header;
    private List<RowInformation> rows;

    private TimePanel endTimePanel;
    private JButton setEndTime;


    MainController mainController = MainController.getInstance();
    public ChooseLessonTime(){
        Response response = mainController.getStudents();
        studentList = (List<Student>) response.getData("studentList");
        initComp();
        alignComp();
    }

    public void initComp(){
        studentsPanel = new JPanel();

        header = new RowInformation(5 , true);
        header.addComponent(new JLabel("Name"));
        header.addComponent(new JLabel("Grade"));
        header.addComponent(new JLabel("Entry Year"));
        header.addComponent(new JLabel("Time"));
        header.addComponent(new JLabel("Save"));
        rows = new ArrayList<>();

        for (Student s : studentList){
            RowInformation rowInformation = setRow(s);
            rows.add(rowInformation);
        }

        setEndTime = new JButton("Set End Time");
        Time time = new Time(LocalDateTime.now());
        time.setMinutes(59);
        endTimePanel = new TimePanel(time);
        setEndTime.addActionListener( e-> {
            Response response = mainController.setEndChooseLessonTime(endTimePanel.getTime());
            JOptionPane.showMessageDialog(null , response.getMessage());
        });
    }

    public void alignComp(){
        studentsPanel.setLayout(new GridLayout(rows.size()+1 , 1 , 0 , 15));
        studentsPanel.add(header);
        for (RowInformation r : rows)
            studentsPanel.add(r);

        add(studentsPanel);
        add(setEndTime);
        add(endTimePanel);
    }

    private RowInformation setRow(Student student){
        RowInformation rowInformation = new RowInformation(5 , false);
        rowInformation.addComponent(new JLabel(student.getName()));
        rowInformation.addComponent(new JLabel(student.getGrade().toString()));
        rowInformation.addComponent(new JLabel(String.valueOf(student.getEntryYear())));

        Time time = student.getChooseLessonTime();
        TimePanel timePanel = new TimePanel(time);
        rowInformation.addComponent(timePanel);

        JButton save = new JButton("Save Time");
        save.addActionListener( e-> {
            Time t = timePanel.getTime();
            Response response = mainController.addLessonTime(student.getUserCode() , t);
            JOptionPane.showMessageDialog(null , response.getMessage());
        });
        rowInformation.addComponent(save);
        return rowInformation;
    }
}
