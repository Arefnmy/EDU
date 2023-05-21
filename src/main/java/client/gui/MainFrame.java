package client.gui;

import client.controller.MainController;
import client.filemanager.ResourceManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private static final ResourceManager resourceManager = ResourceManager.getInstance();
    public static final int FRAME_WIDTH = resourceManager.getValue(Integer.class , "frame-width" , 1000);
    public static final int FRAME_HEIGHT = resourceManager.getValue(Integer.class , "frame-height" , 700);
    public static final int FRAME_X = resourceManager.getValue(Integer.class , "frame-x" , 300);
    public static final int FRAME_Y = resourceManager.getValue(Integer.class , "frame-y" , 100);

    public MainFrame(){
        setLayout(null);
        setBounds(FRAME_X , FRAME_Y , FRAME_WIDTH , FRAME_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(resourceManager.getGraphicConfig().getProperty("title" , "edu"));
        //setResizable(false);
        setFocusable(false);


        JPanel loginMenu = new LoginMenu();
        setContentPane(loginMenu);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainController.getInstance().setCloseFrame();
                super.windowClosing(e);
            }
        });

        setVisible(true);
    }

    public void refresh(){
        revalidate();
        repaint();
    }

    public void changeContentPane(JPanel panel){
        setContentPane(panel);
        refresh();
    }

    public void setBar(JMenuBar menuBar){
        setJMenuBar(menuBar);
        refresh();
    }
}
