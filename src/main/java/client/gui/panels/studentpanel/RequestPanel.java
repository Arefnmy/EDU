package client.gui.panels.studentpanel;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.gui.RowInformation;
import shared.model.educaionalrequests.EducationalRequest;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class RequestPanel extends JPanel implements AutoRefresh {
    protected List<EducationalRequest> educationalRequestList;
    protected JPanel requestPanel;
    protected RowInformation header;
    protected JButton request;

    MainController mainController = MainController.getInstance();

    protected RequestPanel(){
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setLoop(1);
    }

    public void initComp(){
        requestPanel = new JPanel();
        request = new JButton("EducationalRequest");
    }

    public void alignComp(){
        add(requestPanel);
        add(request);
    }

    @Override
    public void getResponse(){
        Response response = mainController.getRequest();
        educationalRequestList = (List<EducationalRequest>) response.getData("educationalRequestList");
    }

    public void update(){
        requestPanel.removeAll();
        requestPanel.setLayout(new GridLayout(educationalRequestList.size()+1 , 3));
        requestPanel.add(header);
    }
}
