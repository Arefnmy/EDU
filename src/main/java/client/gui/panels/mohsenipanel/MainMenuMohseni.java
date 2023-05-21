package client.gui.panels.mohsenipanel;

import client.gui.LoginMenu;
import client.gui.MainFrame;
import client.gui.panels.ChatRoom;
import client.gui.panels.MainMenu;
import shared.model.users.User;

import javax.swing.*;
import java.awt.*;

public class MainMenuMohseni extends MainMenu {
    private JMenu search;
    private JMenuItem searchStudent;

    public MainMenuMohseni(User user) {
        super(user);
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setActionListener();
    }

    public void initComp(){
        super.initComp();
        search = new JMenu("Search");
        searchStudent = new JMenuItem("Search Student");
    }

    public void alignComp() {
        messenger.add(chatroom);
        messenger.add(createChat);

        search.add(searchStudent);

        menuBar.add(home);
        menuBar.add(reconnect);
        menuBar.add(logout);
        menuBar.add(messenger);
        menuBar.add(search);
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

    public void setActionListener(){
        createChat.addActionListener(e -> mainController.changeContentPane(new CreateChatMohseni()));
        chatroom.addActionListener( e-> mainController.changeContentPane(new ChatRoom()));
        searchStudent.addActionListener( e-> mainController.changeContentPane(new SearchPanel()));

        logout.addActionListener(e -> {
            mainController.changeContentPane(new LoginMenu());
            mainController.setMenuBar(null);
        });

        home.addActionListener( e-> mainController.changeContentPane(mainController.getMainMenu()));
    }

    @Override
    public void setInfo() {

    }

    @Override
    public void getResponse() {

    }
}
