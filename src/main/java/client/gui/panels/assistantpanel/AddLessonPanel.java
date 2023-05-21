package client.gui.panels.assistantpanel;

import client.controller.MainController;
import shared.model.Grade;
import shared.model.Lesson;
import shared.model.users.Professor;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class AddLessonPanel extends JPanel {
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;

    private JTextField nameField;
    private JSpinner unitsField;
    private JSpinner capacity;
    private JSpinner finalDateMonth;
    private JSpinner finalDateDay;
    private JSpinner finalTimeMin;
    private JSpinner finalTimeHour;
    private JSpinner startHour;
    private JSpinner startMin;
    private JSpinner endHour;
    private JSpinner endMin;
    private JSpinner term;

    private JList<String> daysOfWeek;
    private JList<String> prerequisites;
    private JList<String> requirements;

    private JComboBox<String> professors;
    private JComboBox<String> grades;

    private JButton saveLesson;

    private List<Lesson> lessonList;
    private List<Professor> professorList;

    MainController mainController = MainController.getInstance();
    public AddLessonPanel(){
        Response response = mainController.getAddLesson();
        lessonList = (List<Lesson>) response.getData("lessonList");
        professorList = (List<Professor>) response.getData("professorList");

        initComp();
        alignComp();
        setActionListener();
    }

    public void initComp(){
        mainPanel = new JPanel();
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        saveLesson = new JButton("Save Lesson");

        nameField= new JTextField();
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(0, 0, 5, 1);
        unitsField = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(40 , 0 , 500 , 1);
        capacity = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(1 , 1 , 12 , 1);
        finalDateMonth = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(1 , 1 , 31 , 1);
        finalDateDay = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(0 , 0 , 23 , 1);
        finalTimeHour = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(0 , 0 , 23 , 1);
        startHour = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(0 , 0 , 23 , 1);
        endHour = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(0 , 0 , 59 , 1);
        finalTimeMin = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(0 , 0 , 59 , 1);
        startMin = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(0 , 0 , 59 , 1);
        endMin = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(14002 , 14001 , 14004 , 1);
        term = new JSpinner(spinnerNumberModel);


        saveLesson = new JButton("Save Lesson");

        String[] days = {"Monday","Tuesday" , "Wednesday", "Thursday" ,"Friday", "Saturday" , "Sunday"};

        daysOfWeek = new JList<>(days);

        String[] professorsStr = new String[professorList.size()];
        for (int i = 0; i < professorList.size(); i++)
            professorsStr[i] = professorList.get(i).getName();
        professors = new JComboBox<>(professorsStr);

        String[] lessensStr = new String[lessonList.size()];
        for (int i = 0; i < lessonList.size(); i++)
            lessensStr[i] = lessonList.get(i).getName();
        prerequisites = new JList<>(lessensStr);
        requirements = new JList<>(lessensStr);

        grades = new JComboBox<>(Grade.getGrades());
    }

    public void alignComp() {
        mainPanel.setLayout(new FlowLayout());
        leftPanel.setLayout(new GridLayout(9, 2, 50, 20));
        rightPanel.setLayout(new GridLayout(4 , 1 , 0 , 20));
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        mainPanel.add(saveLesson);

        leftPanel.add(new JLabel("Name"));
        leftPanel.add(nameField);
        leftPanel.add(new JLabel("Number of units"));
        leftPanel.add(unitsField);
        leftPanel.add(new JLabel("Final date(mm/dd)"));
        JPanel date = new JPanel(new FlowLayout());
        date.add(finalDateMonth);
        date.add(new JLabel("/"));
        date.add(finalDateDay);
        leftPanel.add(date);
        leftPanel.add(new JLabel("Final time(hh:mm)"));
        JPanel time = new JPanel(new FlowLayout());
        time.add(finalTimeHour);
        time.add(new JLabel(":"));
        time.add(finalTimeMin);
        leftPanel.add(time);
        leftPanel.add(new JLabel("Start hour"));
        leftPanel.add(startHour);
        leftPanel.add(new JLabel("End hour"));
        leftPanel.add(endHour);
        leftPanel.add(new JLabel("Grade"));
        leftPanel.add(grades);
        leftPanel.add(new JLabel("Capacity"));
        leftPanel.add(capacity);
        leftPanel.add(new JLabel("Term"));
        leftPanel.add(term);

        rightPanel.add(new JLabel("Professor"));
        rightPanel.add(professors);
        rightPanel.add(new JLabel("Days of week"));
        rightPanel.add(new JScrollPane(daysOfWeek));
        rightPanel.add(new JLabel("Prerequisites"));
        rightPanel.add(new JScrollPane(prerequisites));
        rightPanel.add(new JLabel("Requirements"));
        rightPanel.add(new JScrollPane(requirements));

        add(mainPanel);
    }

    public void setActionListener(){
        saveLesson.addActionListener( e-> {

            List<DayOfWeek> dayOfWeeks = new ArrayList<>();
            for (int i : daysOfWeek.getSelectedIndices())
                dayOfWeeks.add(DayOfWeek.of(i+1));

            List<Lesson> pre = new ArrayList<>();
            for (int i : prerequisites.getSelectedIndices())
                pre.add(lessonList.get(i));

            List<Lesson> req = new ArrayList<>();
            for (int i : requirements.getSelectedIndices())
                req.add(lessonList.get(i));

            Response response = mainController.addLesson(nameField.getText() ,(int)unitsField.getValue() ,
                    (int)finalDateMonth.getValue() , (int)finalDateDay.getValue(), (int)finalTimeHour.getValue() ,
                    (int)startHour.getValue() , (int)endHour.getValue() ,(int)capacity.getValue() ,dayOfWeeks ,
                    (String) grades.getSelectedItem(), (String) professors.getSelectedItem(), pre , req, (int) term.getValue());
            JOptionPane.showMessageDialog(this , response.getMessage());
        });
    }
}
