package client.gui.panels.studentpanel;

import client.controller.MainController;
import client.filemanager.ResourceManager;
import shared.model.educaionalrequests.EducationalRequest;
import shared.model.educaionalrequests.RequestType;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MinorRequestDialog extends JDialog {
    private final List<String> collegeList;
    private JComboBox<String> originCollegeList;
    private JComboBox<String> goalCollegeList;

    public MinorRequestDialog(List<String> collegeList){
        this.collegeList = collegeList;
        setBounds(ResourceManager.getInstance().getValue(Integer.class , "defaultDialog-x" , 400) ,
                ResourceManager.getInstance().getValue(Integer.class , "defaultDialog-y" , 400) ,
                ResourceManager.getInstance().getValue(Integer.class , "minorRequest-width" , 700) ,
                ResourceManager.getInstance().getValue(Integer.class , "minorRequest-height" , 100));
        setLayout(new FlowLayout());

        originCollegeList = new JComboBox<>();
        goalCollegeList = new JComboBox<>();
        for (String c : collegeList){
            originCollegeList.addItem(c);
            goalCollegeList.addItem(c);
        }

        JLabel originCollegeLabel = new JLabel("Origin College :");
        JLabel goalCollegeLabel = new JLabel("Goal College :");

        JButton addRequest = new JButton("Add EducationalRequest");
        addRequest.addActionListener(e-> setActionListener());

        add(originCollegeLabel);
        add(originCollegeList);
        add(goalCollegeLabel);
        add(goalCollegeList);
        add(addRequest);

        setVisible(true);
    }

    public void setActionListener(){
        MainController mainController = MainController.getInstance();
        String originCollege = (String) originCollegeList.getSelectedItem();
        String goalCollege = (String) goalCollegeList.getSelectedItem();
        EducationalRequest educationalRequest = new EducationalRequest(RequestType.MINOR);
        educationalRequest.setCollege(goalCollege);
        Response response = mainController.addRequest(educationalRequest);
        JOptionPane.showMessageDialog(this , response.getMessage());
    }
}
