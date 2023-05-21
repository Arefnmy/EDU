package client.gui.panels.mohsenipanel;

import client.controller.MainController;
import shared.model.Grade;
import shared.model.users.Student;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CreateChatMohseni extends JPanel{
    private List<String> collegeList;
    private List<Student> studentList;
    private JList<String> students;

    private JScrollPane scrollPane;
    private JTextField messageField;
    private JButton sendMessage;
    private JPanel sortPanel;
    private JButton showStudent;

    private JPanel entryYearPanel;
    private JCheckBox entryYearCheckBox;
    private JSpinner entryYearField;

    private JPanel gradePanel;
    private JCheckBox gradeCheckBox;
    private JComboBox<String> grades;

    private JPanel collegePanel;
    private JCheckBox collegeCheckBox;
    private JComboBox<String> colleges;

    MainController mainController = MainController.getInstance();
    public CreateChatMohseni(){
        Response response = mainController.getCreateChatMohseni(false , false , false ,
                -1 , null , null);
        collegeList = (List<String>) response.getData("collegeList");
        studentList = (List<Student>) response.getData("studentList");

        initComp();
        alignComp();
        setActionListener();
    }

    public void initComp(){
        scrollPane = new JScrollPane();
        messageField = new JTextField();
        sendMessage = new JButton("Send");
        sortPanel = new JPanel(new FlowLayout());
        showStudent = new JButton("Show Student");
        entryYearPanel = new JPanel(new FlowLayout());
        gradePanel = new JPanel(new FlowLayout());
        collegePanel = new JPanel(new FlowLayout());
        entryYearCheckBox = new JCheckBox();
        gradeCheckBox = new JCheckBox();
        collegeCheckBox = new JCheckBox();

        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(1400 , 1300 , 1500 , 1);
        entryYearField = new JSpinner(spinnerNumberModel);

        grades = new JComboBox<>(Grade.getGrades());
        String[] collegeStr = new String[collegeList.size()];
        collegeList.toArray(collegeStr);
        colleges = new JComboBox<>(collegeStr);

        String[] studentStr = new String[studentList.size()];
        for (int i = 0; i < studentList.size(); i++) {
            studentStr[i] = studentList.get(i).getName();
        }
        students = new JList<>(studentStr);
        scrollPane = new JScrollPane(students);

    }

    public void alignComp(){
        setLayout(null);

        entryYearPanel.add(entryYearField);
        entryYearPanel.add(entryYearCheckBox);
        gradePanel.add(grades);
        gradePanel.add(gradeCheckBox);
        collegePanel.add(colleges);
        collegePanel.add(collegeCheckBox);

        sortPanel.add(entryYearPanel);
        sortPanel.add(gradePanel);
        sortPanel.add(collegePanel);
        sortPanel.add(showStudent);

        scrollPane.setBounds(760 , 110 , 220 , 400);
        messageField.setBounds(10 , 200 , 730 , 220);
        sendMessage.setBounds(10 , 450 , 730 , 30);
        sortPanel.setBounds(160 , 20 , 570 , 40);
        add(sortPanel);
        add(scrollPane);
        add(messageField);
        add(sendMessage);
    }

    public void setActionListener(){
        showStudent.addActionListener( e-> {
            Response response = mainController.getCreateChatMohseni(entryYearCheckBox.isSelected() , gradeCheckBox.isSelected(),
                    collegeCheckBox.isSelected() , (Integer) entryYearField.getValue(), (String)grades.getSelectedItem() ,
                    (String) colleges.getSelectedItem());
            studentList = (List<Student>) response.getData("studentList");
            String[] studentStr = new String[studentList.size()];
            for (int i = 0; i < studentList.size(); i++) {
                studentStr[i] = studentList.get(i).getName();
            }
            students.setListData(studentStr);

            mainController.refresh();
        });

        sendMessage.addActionListener( e->{
            List<Long> userList = new ArrayList<>();
            for (int i : students.getSelectedIndices())
                userList.add(studentList.get(i).getUserCode());
            Response response = mainController.sendMessage(messageField.getText() , userList, null);
            JOptionPane.showMessageDialog(null , response.getMessage());
            students.clearSelection();
            messageField.setText("");
        });
    }
}
