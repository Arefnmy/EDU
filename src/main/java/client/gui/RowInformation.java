package client.gui;

import client.filemanager.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RowInformation extends JPanel {
    private static int style = 0;
    private static final Color rowColor1 =
            new Color(ResourceManager.getInstance().getValue(Integer.class ,"rowColor1" ,0x9292F8));
    private static final Color rowColor2 =
            new Color(ResourceManager.getInstance().getValue(Integer.class ,"rowColor2" ,0xFD6161));
    private static final Color headerColor1 =
            new Color(ResourceManager.getInstance().getValue(Integer.class ,"headerColor1" ,0x17D2BD));
    private static final Color headerColor2 =
            new Color(ResourceManager.getInstance().getValue(Integer.class ,"headerColor2" ,0xBAE73AC4));

    private final List<Component> componentList;

    public RowInformation(int column , boolean isHeader){
        setLayout(new GridLayout(1 , column , 10 , 0));
        setFont(getFont());

        if (isHeader) setBackground(getColor()[0]);
        else setBackground(getColor()[1]);

        componentList = new ArrayList<>();
    }

    public void addComponent(Component component){
        add(component);
        componentList.add(component);
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    public Font getFont(){
        if (style == 0)
            return new Font("font1" , Font.PLAIN , 25);
        return new Font("font2" , Font.BOLD , 20);
    }

    public Color[] getColor(){
        Color[] colors = new Color[2];
        if (style == 0){
            colors[0] = rowColor1;
            colors[1] = headerColor1;
        }
        else{
            colors[0] = rowColor2;
            colors[1] = headerColor2;
        }
        return colors;
    }

    public static void changeStyle(){
        style ++;
        style %= 2;
    }

    public static Color getHeaderColor(){
        if (style == 0)
            return headerColor1;
        return headerColor2;
    }
}