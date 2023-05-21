package client.gui.panels.studentpanel;

import client.gui.RowInformation;
import shared.model.educaionalrequests.EducationalRequest;
import shared.model.educaionalrequests.RequestType;
import shared.response.Response;

import javax.swing.*;

public class CancelPanel extends RequestPanel{

    public CancelPanel() {
        super();
    }

    public void initComp() {
        super.initComp();

        request.addActionListener(e -> {
            EducationalRequest educationalRequest = new EducationalRequest(RequestType.CANCEL);
            Response response = mainController.addRequest(educationalRequest);
            JOptionPane.showMessageDialog(this , response.getMessage());
        });

        header = new RowInformation(1, true);
        header.addComponent(new JLabel("Status"));
    }

    @Override
    public void setInfo() {
        super.update();
        for (EducationalRequest r : educationalRequestList){
            if (r.getRequestType() == RequestType.CANCEL){
                RowInformation rowInformation = new RowInformation(1, false);
                rowInformation.addComponent(new JLabel(r.getStatus().toString()));
                requestPanel.add(rowInformation);
            }
        }
        mainController.refresh();
    }
}
