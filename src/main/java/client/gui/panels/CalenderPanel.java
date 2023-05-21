package client.gui.panels;

import client.gui.AutoRefresh;
import client.controller.MainController;
import client.filemanager.ResourceManager;
import shared.model.Time;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

public class CalenderPanel extends JPanel implements AutoRefresh {
    LinkedHashMap<Time, String> calender;
    private JPanel mainPanel;

    MainController mainController = MainController.getInstance();
    public CalenderPanel(){
        mainPanel = new JPanel();
        add(mainPanel);
        setLoop(1);
    }

    public JPanel getPanel(String time , String name){
        JPanel panel = new JPanel(new GridLayout(2 , 1 , 0 , 10));
        panel.add(new JLabel(name , SwingConstants.CENTER));
        panel.add(new JLabel(time , SwingConstants.CENTER));
        panel.setPreferredSize(new Dimension(
                ResourceManager.getInstance().getValue(Integer.class , "calenderLesson-width" , 200) ,
                ResourceManager.getInstance().getValue(Integer.class , "calenderLesson-height" , 200)));
        panel.setBorder(BorderFactory.createLineBorder(
                new Color(ResourceManager.getInstance().getValue(Integer.class , "borderColor" , 0))));
        return panel;
    }

    @Override
    public void getResponse() throws NullPointerException {
        Response response = mainController.getCalender();
        calender = (LinkedHashMap<Time, String>) response.getData("calender");
    }

    @Override
    public void setInfo() {
        mainPanel.removeAll();
        mainPanel.setLayout(new GridLayout(calender.size() , 1 , 0 , 10));
        for (Object o : calender.keySet())
            mainPanel.add(getPanel(o.toString() , calender.get(o)));
        mainController.refresh();
    }
}
