package client.gui.panels;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.gui.RowInformation;
import shared.model.notification.Notification;
import shared.model.notification.NotificationType;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationPanel extends JPanel implements AutoRefresh {
    private List<Notification> notifications;
    private JPanel mainPanel;
    private RowInformation header;

    MainController mainController = MainController.getInstance();
    public NotificationPanel(){
        initComp();
        getResponse();
        setInfo();
        setLoop(1);
    }

    public void initComp(){
        mainPanel = new JPanel();
        header = new RowInformation(3 , true);
        header.addComponent(new JLabel("Sender"));
        header.addComponent(new JLabel("Message"));
        header.addComponent(new JLabel("Action"));

        add(mainPanel);
    }

    @Override
    public void setInfo() {
        mainPanel.removeAll();
        mainPanel.add(header);
        mainPanel.setLayout(new GridLayout(notifications.size() + 1 , 1));

        for (Notification n : notifications){
            RowInformation rowInformation = new RowInformation(3 , false);
            rowInformation.addComponent(new JLabel(n.getSender()));
            rowInformation.addComponent(new JLabel(n.getMessage()));
            if (n.getType() == NotificationType.PREDICATIVE){
                JButton ok = new JButton("Ok");
                rowInformation.addComponent(ok);
                ok.addActionListener( e-> mainController.handelNotification(n , null));
            }
            else{
                JPanel yesNo = new JPanel(new FlowLayout());
                JButton yes = new JButton("Yes");
                JButton no = new JButton("No");
                yesNo.add(yes);
                yesNo.add(no);
                rowInformation.addComponent(yesNo);
                yes.addActionListener( e-> mainController.handelNotification(n , true));
                no.addActionListener( e-> mainController.handelNotification(n , false));
            }

            mainPanel.add(rowInformation);
        }

        mainController.refresh();
    }

    @Override
    public void getResponse() {
        Response response = mainController.getNotification();
        notifications = (List<Notification>) response.getData("notificationList");
    }
}
