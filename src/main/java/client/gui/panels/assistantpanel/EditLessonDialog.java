package client.gui.panels.assistantpanel;


import client.controller.MainController;
import client.filemanager.ResourceManager;
import client.gui.RowInformation;
import shared.model.Lesson;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;

public class EditLessonDialog extends JDialog {
    private final Lesson lesson;

    private JPanel mainPanel;
    private JPanel rowsPanel;

    private RowInformation header1;
    private RowInformation header2;
    private RowInformation header3;
    private RowInformation info1;
    private RowInformation info2;
    private RowInformation info3;

    private JSpinner unitsField;
    private JSpinner capacity;
    private JSpinner finalDateMonth;
    private JSpinner finalDateDay;
    private JSpinner finalTimeHour;
    private JSpinner startHour;
    private JSpinner endHour;

    private JButton edit;
    private JButton remove;

    MainController mainController = MainController.getInstance();
    public EditLessonDialog(Lesson lesson) {
        this.lesson = lesson;

        setBounds(ResourceManager.getInstance().getValue(Integer.class , "defaultDialog-x" , 400) ,
                ResourceManager.getInstance().getValue(Integer.class , "defaultDialog-y" , 400) ,
                ResourceManager.getInstance().getValue(Integer.class , "editLesson-width" , 500) ,
                ResourceManager.getInstance().getValue(Integer.class , "editLesson-height" , 500));

        initComp();
        alignComp();
        setActionListener();

        setVisible(true);
    }

    public void initComp(){
        mainPanel = new JPanel();
        rowsPanel = new JPanel();

        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(lesson.getNumberOfUnits(), 0, 5, 1);
        unitsField = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(lesson.getCapacity(), 0 , 500 , 1);
        capacity = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(lesson.getTime().getFinalMonth() , 1 , 12 , 1);
        finalDateMonth = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(lesson.getTime().getFinalDayOfMonth() , 1 , 31 , 1);
        finalDateDay = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(lesson.getTime().getFinalHour() , 0 , 23 , 1);
        finalTimeHour = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(lesson.getTime().getStartHour() , 0 , 23 , 1);
        startHour = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(lesson.getTime().getEndHour() , 0 , 23 , 1);
        endHour = new JSpinner(spinnerNumberModel);

        header1 = new RowInformation(3 , true);
        header1.addComponent(new JLabel("Lesson"));
        header1.addComponent(new JLabel("Professor"));
        header1.addComponent(new JLabel("Grade"));

        info1 = new RowInformation(3 , false);
        info1.addComponent(new JLabel(lesson.getName()));
        info1.addComponent(new JLabel(lesson.getProfessorName()));
        info1.addComponent(new JLabel(lesson.getGrade().toString()));

        header2 = new RowInformation(3 , true);
        header2.addComponent(new JLabel("Number of units"));
        header2.addComponent(new JLabel("Final date ( mm:dd )"));
        header2.addComponent(new JLabel("Final time ( hh:mm )"));

        info2 = new RowInformation(3 , false);
        info2.addComponent(unitsField);
        JPanel finalDate = new JPanel(new FlowLayout());
        finalDate.add(finalDateMonth);
        finalDate.add(new JLabel("/"));
        finalDate.add(finalDateDay);
        info2.addComponent(finalDate);
        JPanel finalTime = new JPanel(new FlowLayout());
        finalTime.add(finalTimeHour);
        finalTime.add(new JLabel(":00"));
        info2.addComponent(finalTime);

        header3 = new RowInformation(3 , true);
        header3.addComponent(new JLabel("Capacity"));
        header3.addComponent(new JLabel("Start Hour"));
        header3.addComponent(new JLabel("End Hour"));

        info3 = new RowInformation(3 , false);
        info3.addComponent(capacity);
        info3.addComponent(startHour);
        info3.addComponent(endHour);

        edit = new JButton("Edit Lesson");
        remove = new JButton("Remove Lesson");
    }

    public void alignComp(){
        setContentPane(mainPanel);

        rowsPanel.setLayout(new GridLayout( 6 , 1 , 0 , 20));
        rowsPanel.add(header1);
        rowsPanel.add(info1);
        rowsPanel.add(header2);
        rowsPanel.add(info2);
        rowsPanel.add(header3);
        rowsPanel.add(info3);

        mainPanel.add(rowsPanel);
        mainPanel.add(edit);
        mainPanel.add(remove);
    }

    public void setActionListener(){
        edit.addActionListener( e-> {
            Response response = mainController.editLesson(lesson.getLessonNumber() , (int)unitsField.getValue() ,
                    (int)finalDateMonth.getValue() , (int)finalDateDay.getValue() , (int)finalTimeHour.getValue() ,
                    (int)startHour.getValue() , (int)endHour.getValue() , (int)capacity.getValue());
            JOptionPane.showMessageDialog(null , response.getMessage());
        });

        remove.addActionListener( e-> {
            Response response = mainController.removeLessonFromChooseLesson(lesson.getLessonNumber());
            JOptionPane.showMessageDialog(this , response.getMessage());
            dispose();
        });
    }
}
