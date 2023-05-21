package client.gui.panels.professorpanel;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.filemanager.ResourceManager;
import client.gui.RowInformation;
import shared.model.educaionalrequests.EducationalRequest;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RequestPanel extends JPanel implements AutoRefresh {
    private List<EducationalRequest> educationalRequestList;

    private RowInformation header;

    private JPanel mainPanel;

    MainController mainController = MainController.getInstance();
    public RequestPanel(){
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setLoop(ResourceManager.getInstance().getValue(Integer.class , "defaultPanel-fps" , 1));
    }

    public void initComp(){
        mainPanel = new JPanel();

        header = new RowInformation(6 , true);
        header.addComponent(new JLabel("Student Name"));
        header.addComponent(new JLabel("Student Code"));
        header.addComponent(new JLabel("EducationalRequest"));
        header.addComponent(new JLabel("Status"));
        header.addComponent(new JLabel("Accept"));
        header.addComponent(new JLabel("Decline"));
    }

    public void alignComp(){
        add(mainPanel);
    }

    @Override
    public void setInfo() {
        mainPanel.removeAll();
        mainPanel.add(header);

        mainPanel.setLayout(new GridLayout(educationalRequestList.size()+1 , 1 , 0 , 20));
        for (EducationalRequest r : educationalRequestList){
            RowInformation rowInformation = new RowInformation(6 , false);
            rowInformation.addComponent(new JLabel(r.getStudent()));
            rowInformation.addComponent(new JLabel(String.valueOf(r.getStudentCode())));

            String status = r.getStatus().toString();

            JButton accept = new JButton("Accept");
            accept.addActionListener( e-> {
                Response response1 = mainController.handelRequest(r , true);
                JOptionPane.showMessageDialog(null , response1.getMessage());
            });
            JButton decline = new JButton("Decline");
            decline.addActionListener( e-> {
                Response response1 = mainController.handelRequest(r , false);
                JOptionPane.showMessageDialog(null , response1.getMessage());
            });

            rowInformation.addComponent(new JLabel(r.getRequestType().toString()));
            rowInformation.addComponent(new JLabel(status));

            rowInformation.addComponent(accept);
            rowInformation.addComponent(decline);

            mainPanel.add(rowInformation);
        }
        mainController.refresh();
    }

    @Override
    public void getResponse() {
        Response response = mainController.getRequest();
        educationalRequestList = (List<EducationalRequest>) response.getData("educationalRequestList");
    }
}
