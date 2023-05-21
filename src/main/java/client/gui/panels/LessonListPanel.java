package client.gui.panels;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.filemanager.ResourceManager;
import client.gui.RowInformation;
import client.gui.panels.assistantpanel.AddLessonPanel;
import client.gui.panels.assistantpanel.EditLessonDialog;
import shared.model.Grade;
import shared.model.Lesson;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LessonListPanel extends JPanel implements AutoRefresh {
    private boolean isAssistant;
    private List<String> professorList;
    private List<String> collegeList;
    private List<Lesson> lessonList;

    private JPanel sortPanel;
    private boolean isGradeSelected = false;
    private boolean isProfessorSelected = false;
    private boolean isCollegeSelected = false;
    private String grade = null;
    private String professor = null;
    private String college = null;

    private JPanel gradePanel;
    private JCheckBox gradeCheckBox;
    private JComboBox<String> gradeComboBox;

    private JPanel professorPanel;
    private JCheckBox professorCheckBox;
    private JComboBox<String> professorComboBox;

    private JPanel collegePanel;
    private JCheckBox collegeCheckBox;
    private JComboBox<String> collegeComboBox;

    private JPanel lessonsPanel;
    private RowInformation header;

    private JButton showButton;
    private JButton addLessonButton;

    MainController mainController = MainController.getInstance();
    public LessonListPanel(){
        Response response = mainController.getLessonList(isGradeSelected , isProfessorSelected , isCollegeSelected,
                grade , professor , college);
        isAssistant = (boolean) response.getData("isAssistant");
        collegeList = (List<String>) response.getData("collegeList");
        professorList = (ArrayList<String>) response.getData("professorList");

        initComp();
        getResponse();
        setInfo();
        alignComp();
        setActionListener();
        setLoop(ResourceManager.getInstance().getValue(Integer.class , "defaultPanel-fps" , 1));
    }

    public void initComp(){
        header = new RowInformation(7 , true);
        header.addComponent(new JLabel("College"));
        header.addComponent(new JLabel("Lesson"));
        header.addComponent(new JLabel("Professor"));
        header.addComponent(new JLabel("Grade"));
        header.addComponent(new JLabel("Final time"));
        header.addComponent(new JLabel("Class time"));
        if (isAssistant)
            header.addComponent(new JLabel("Edit"));

        String[] grades = Grade.getGrades();
        String[] professors = new String[professorList.size()];
        professorList.toArray(professors);
        String[] colleges = new String[collegeList.size()];
        collegeList.toArray(colleges);

        gradePanel = new JPanel();
        professorPanel = new JPanel();
        collegePanel = new JPanel();

        gradeCheckBox = new JCheckBox("Grade");
        professorCheckBox = new JCheckBox("Professor");
        collegeCheckBox = new JCheckBox("College");

        gradeComboBox = new JComboBox<>(grades);
        professorComboBox = new JComboBox<>(professors);
        collegeComboBox = new JComboBox<>(colleges);

        sortPanel = new JPanel();
        lessonsPanel = new JPanel();
        showButton = new JButton("Show Lessons");

        addLessonButton = new JButton("Add Lesson");
    }

    public void alignComp(){
        gradePanel.add(gradeCheckBox);
        gradePanel.add(gradeComboBox);

        professorPanel.add(professorCheckBox);
        professorPanel.add(professorComboBox);

        collegePanel.add(collegeCheckBox);
        collegePanel.add(collegeComboBox);

        sortPanel.add(gradePanel);
        sortPanel.add(professorPanel);
        sortPanel.add(collegePanel);
        sortPanel.add(showButton);

        if (isAssistant)
            add(addLessonButton);

        add(sortPanel);
        add(lessonsPanel);
    }

    public void setActionListener(){
        showButton.addActionListener( e-> {
            isGradeSelected = gradeCheckBox.isSelected();
            isProfessorSelected = professorCheckBox.isSelected();
            isCollegeSelected = collegeCheckBox.isSelected();
            grade = (String) gradeComboBox.getSelectedItem();
            professor = (String) professorComboBox.getSelectedItem();
            college = (String) collegeComboBox.getSelectedItem();
            setInfo();
        });

        addLessonButton.addActionListener( e->
                mainController.changeContentPane(new AddLessonPanel()));
    }

    @Override
    public void setInfo() {
        lessonsPanel.removeAll();
        lessonsPanel.add(header);

            for (Lesson l : lessonList){
                RowInformation rowInformation = new RowInformation(7 , false);
                rowInformation.addComponent(new JLabel(l.getCollegeName()));
                rowInformation.addComponent(new JLabel(l.getName()));
                rowInformation.addComponent(new JLabel(l.getProfessorName()));
                rowInformation.addComponent(new JLabel(l.getGrade().toString()));
                rowInformation.addComponent(new JLabel(l.getTime().getFinalDateStr() +" " +
                        l.getTime().getFinalTimeStr()));
                rowInformation.addComponent(new JLabel(l.getTime().getTimeInWeek()));

                if (isAssistant) {
                    JButton editButton = new JButton("Edit");
                    rowInformation.addComponent(editButton);
                    editButton.addActionListener( a-> {
                        /*if ( user.getCollege() != l.getCollege())
                            JOptionPane.showMessageDialog(new JFrame() , "You don't have access to edit this lesson!");
                        else*/
                            new EditLessonDialog(l);
                    });
                }

                lessonsPanel.add(rowInformation);
            }

            lessonsPanel.setLayout(new GridLayout(lessonList.size() + 1 , 1 , 0 , 20));
            mainController.refresh();
    }

    @Override
    public void getResponse() {
        Response response = mainController.getLessonList(isGradeSelected , isProfessorSelected , isCollegeSelected,
                grade , professor , college);
        lessonList = (List<Lesson>) response.getData("lessonList");
    }
}
