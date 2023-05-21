package client.gui.panels.studentpanel;

import client.gui.RowInformation;
import shared.model.educaionalrequests.EducationalRequest;
import shared.model.educaionalrequests.RequestType;
import shared.response.Response;

import javax.swing.*;

public class DefencePanel extends RequestPanel {

    public DefencePanel() {
        super();
    }

    public void initComp() {
        super.initComp();

        request.addActionListener(e -> {
            EducationalRequest educationalRequest = new EducationalRequest(RequestType.DEFENCE);
            Response response = mainController.addRequest(educationalRequest);
            JOptionPane.showMessageDialog(this , response.getMessage());
        });

        header = new RowInformation(2, true);
        header.addComponent(new JLabel("Status"));
        header.addComponent(new JLabel("Show Request"));
    }

    @Override
    public void setInfo() {
        super.update();
        for (EducationalRequest r : educationalRequestList){
            if (r.getRequestType() == RequestType.DEFENCE){
                RowInformation rowInformation = new RowInformation(2, false);
                rowInformation.addComponent(new JLabel(r.getStatus().toString()));

                JButton show = new JButton("Show Request");
                show.addActionListener( e-> new RequestStatusDialog(r));
                rowInformation.addComponent(show);
            }
        }
        mainController.refresh();
    }
}
