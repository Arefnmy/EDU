package client.gui.panels.studentpanel;

import client.gui.RowInformation;
import shared.model.educaionalrequests.EducationalRequest;
import shared.model.educaionalrequests.RequestType;
import shared.response.Response;

import javax.swing.*;
import java.util.List;

public class MinorPanel extends RequestPanel {
    private List<String> collegeList;
    public MinorPanel() {
        super();
        Response response = mainController.getRequest();
        collegeList = (List<String>) response.getData("collegeList");
        request.addActionListener(e -> new MinorRequestDialog(collegeList));
    }

    public void initComp() {
        super.initComp();

        header = new RowInformation(3, true);
        header.addComponent(new JLabel("Goal College"));
        header.addComponent(new JLabel("Status"));
        header.addComponent(new JLabel("Show Request"));
    }

    @Override
    public void setInfo() {
        super.update();
        for (EducationalRequest r : educationalRequestList){
            if (r.getRequestType() == RequestType.MINOR){
                RowInformation rowInformation = new RowInformation(3, false);
                rowInformation.addComponent(new JLabel(r.getCollege()));
                rowInformation.addComponent(new JLabel(r.getStatus().toString()));

                JButton show = new JButton("Show Request");
                show.addActionListener( e-> new RequestStatusDialog(r));
                rowInformation.addComponent(show);
            }
        }
        mainController.refresh();
    }
}
