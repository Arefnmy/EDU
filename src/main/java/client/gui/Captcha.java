package client.gui;

import client.filemanager.ImageType;
import client.filemanager.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Captcha extends JLabel{
    private int captchaNumber;
    private final JLabel image;

    private static int iconNumber;

    public Captcha(){
        setLayout(new FlowLayout());

        image = new JLabel();
        JButton refresh = new JButton();
        changeIcon();
        refresh.setIcon(ResourceManager.getInstance().getImage(ImageType.REFRESH));
        refresh.addActionListener(e -> changeIcon());
        add(image);
        add(refresh);

    }
    public void changeIcon(){
        Random random = new Random();
        iconNumber = random.nextInt(5);

        switch (iconNumber){
            case 0:
                captchaNumber = 2471;
                break;
            case 1:
                captchaNumber = 1746;
                break;
            case 2:
                captchaNumber = 1954;
                break;
            case 3:
                captchaNumber = 8097;
                break;
            case 4:
                captchaNumber = 8196;
        }

        image.setIcon(ResourceManager.getInstance().getImage("captcha" + iconNumber + ".jpg"));
    }

    public int getCaptchaNumber() {
        return captchaNumber;
    }
}
