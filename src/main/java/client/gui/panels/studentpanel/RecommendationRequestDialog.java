package client.gui.panels.studentpanel;

import client.controller.MainController;
import client.filemanager.ResourceManager;
import shared.model.educaionalrequests.EducationalRequest;
import shared.model.educaionalrequests.RequestType;
import shared.model.users.Professor;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RecommendationRequestDialog extends JDialog {
    private JComboBox<String> professors;
    private List<Professor> professorList;

    MainController mainController = MainController.getInstance();
    public RecommendationRequestDialog(){
        setBounds(ResourceManager.getInstance().getValue(Integer.class , "defaultDialog-x" , 400) ,
                ResourceManager.getInstance().getValue(Integer.class , "defaultDialog-y" , 400) ,
                ResourceManager.getInstance().getValue(Integer.class , "recommendationRequest-width" , 500) ,
                ResourceManager.getInstance().getValue(Integer.class , "recommendationRequest-height" , 100));
        setLayout(new FlowLayout());

        Response response = mainController.getProfessorList();
        professorList = (List<Professor>) response.getData("professorList");
        String[] professorStr = new String[professorList.size()];
        for (int i = 0; i < professorList.size(); i++)
            professorStr[i] = professorList.get(i).getName();
        professors = new JComboBox<>(professorStr);

        JLabel professorLabel = new JLabel("Professor Name:");

        JButton addRequest = new JButton("Add EducationalRequest");
        addRequest.addActionListener(e-> setActionListener());
        
        add(professorLabel);
        add(professors);
        add(addRequest);

        setVisible(true);
    }

    public void setActionListener(){
        EducationalRequest educationalRequest = new EducationalRequest(RequestType.Recommendation);
        educationalRequest.setProfessor((String) professors.getSelectedItem());
        Response response = mainController.addRequest(educationalRequest);
        JOptionPane.showMessageDialog(this , response.getMessage());
    }
}
