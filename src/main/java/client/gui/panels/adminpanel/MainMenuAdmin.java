package client.gui.panels.adminpanel;

import client.gui.MainFrame;
import client.gui.panels.MainMenu;
import shared.model.users.User;

import javax.swing.*;
import java.awt.*;

public class MainMenuAdmin extends MainMenu {
    public MainMenuAdmin(User user) {
        super(user);
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setActionListener();
    }

    public void alignComp(){
        messenger.add(chatroom);
        messenger.add(createChat);

        menuBar.add(home);
        menuBar.add(reconnect);
        menuBar.add(logout);
        menuBar.add(messenger);
        mainController.setMenuBar(menuBar);

        header1.addComponent(new JLabel("Name :"));
        header1.addComponent(new JLabel("Email :"));
        info1.addComponent(name);
        info1.addComponent(email);
        JPanel panel = new JPanel(new GridLayout(2 , 1 , 0 , 5));
        panel.add(header1);
        panel.add(info1);
        panel.setBounds(100 , 400 , 400 , 100);
        add(panel);

        time.setBounds(MainFrame.FRAME_WIDTH - 400 , 20 , 300 , 50);
        add(time);
    }

    @Override
    public void setInfo() {

    }

    @Override
    public void getResponse() {

    }
}
