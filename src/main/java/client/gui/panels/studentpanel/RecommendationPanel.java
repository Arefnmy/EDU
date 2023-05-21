package client.gui.panels.studentpanel;

import client.gui.RowInformation;
import shared.model.educaionalrequests.EducationalRequest;
import shared.model.educaionalrequests.RequestType;

import javax.swing.*;

public class RecommendationPanel extends RequestPanel {

    public RecommendationPanel(){
        super();
    }

    public void initComp(){
        super.initComp();
        request.addActionListener(e -> new RecommendationRequestDialog());

        header = new RowInformation(3 , true);
        header.addComponent( new JLabel("Professor Name"));
        header.addComponent( new JLabel("Status"));
        header.addComponent(new JLabel("Show Request"));

    }

    @Override
    public void setInfo() {
        super.update();
        for (EducationalRequest r : educationalRequestList){
            if (r.getRequestType() == RequestType.Recommendation){
                RowInformation rowInformation = new RowInformation(3, false);
                rowInformation.addComponent(new JLabel(r.getProfessor()));
                rowInformation.addComponent(new JLabel(r.getStatus().toString()));
                JButton show = new JButton("Show Request");
                show.addActionListener( e-> new RequestStatusDialog(r));
                rowInformation.addComponent(show);
                requestPanel.add(rowInformation);
            }
        }
        mainController.refresh();
    }
}
