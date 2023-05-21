package client.gui.panels.studentpanel;

import client.gui.RowInformation;
import shared.model.educaionalrequests.EducationalRequest;
import shared.model.educaionalrequests.RequestType;
import shared.response.Response;

import javax.swing.*;

public class BusyStudyingPanel extends RequestPanel{

    public BusyStudyingPanel() {
        super();
    }

    public void initComp() {
        super.initComp();

       request.addActionListener(e -> {
           EducationalRequest educationalRequest = new EducationalRequest(RequestType.BUSY_STUDYING);
           Response response = mainController.addRequest(educationalRequest);
           JOptionPane.showMessageDialog(new JFrame() , response.getMessage());
       });

        header = new RowInformation(2, true);
        header.addComponent(new JLabel("Status"));
        header.addComponent(new JLabel("Show Request"));

    }

    @Override
    public void setInfo() {
        super.update();
        for (EducationalRequest req : educationalRequestList) {
            if (req.getRequestType() == RequestType.BUSY_STUDYING) {
                RowInformation rowInformation = new RowInformation(2, false);
                rowInformation.addComponent(new JLabel(req.getStatus().toString()));

                JButton show = new JButton("Show Request");
                show.addActionListener(e -> new RequestStatusDialog(req));
                rowInformation.addComponent(show);

                requestPanel.add(rowInformation);
            }
        }
        mainController.refresh();
    }
}
