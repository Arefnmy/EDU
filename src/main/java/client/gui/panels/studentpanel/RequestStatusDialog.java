package client.gui.panels.studentpanel;
import client.filemanager.ResourceManager;
import shared.model.educaionalrequests.EducationalRequest;

import javax.swing.*;

public class RequestStatusDialog extends JDialog {

    public RequestStatusDialog(EducationalRequest request){

        setBounds(ResourceManager.getInstance().getValue(Integer.class , "defaultDialog-x" , 400) ,
                ResourceManager.getInstance().getValue(Integer.class , "defaultDialog-y" , 400) ,
                ResourceManager.getInstance().getValue(Integer.class , "requestStatus-width" , 800) ,
                ResourceManager.getInstance().getValue(Integer.class , "requestStatus-height" , 100));

        JLabel message = new JLabel(request.getMessage());
        add(message);

        setVisible(true);
    }
}
