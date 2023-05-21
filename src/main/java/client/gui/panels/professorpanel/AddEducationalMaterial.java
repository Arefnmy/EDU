package client.gui.panels.professorpanel;

import client.controller.MainController;
import shared.model.media.Media;
import shared.model.media.MediaType;
import shared.response.Response;
import shared.util.FileUploader;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AddEducationalMaterial extends JDialog {
    private final int lesson;
    private List<Media> mediaList;
    private JLabel name;
    private JTextField nameField;
    private JLabel description;
    private JTextArea descriptionField;
    private JButton addMedia;
    private JButton addText;
    private JButton save;

    MainController mainController = MainController.getInstance();
    public AddEducationalMaterial(int lesson){
        this.lesson = lesson;
        setBounds(400 , 200 , 500 , 500);

        initComp();
        alignComp();
        setActionListener();

        setVisible(true);
    }

    public void initComp(){
        nameField = new JTextField();
        name = new JLabel("Name");
        descriptionField = new JTextArea();
        description = new JLabel("Description");
        save = new JButton("Save");
        addMedia = new JButton("Add Media");
        addText = new JButton("Add Text");
        mediaList = new ArrayList<>();
    }

    public void alignComp(){
        setLayout(null);
        name.setBounds(50, 70, 100, 30);
        nameField.setBounds(240, 50, 190, 40);
        description.setBounds(60, 210, 110, 40);
        descriptionField.setBounds(190, 160, 270, 140);
        addMedia.setBounds(220, 370, 280, 40);
        addText.setBounds(220 , 410 , 280 , 30);
        save.setBounds(10, 400, 100, 30);

        add(name);
        add(nameField);
        add(description);
        add(descriptionField);
        add(addMedia);
        add(addText);
        add(save);
    }

    public void setActionListener(){
        addMedia.addActionListener( e-> {
            Media media = FileUploader.uploadFile();
            if (media != null)
                mediaList.add(media);
        });

        addText.addActionListener(e->{
            String name = JOptionPane.showInputDialog("Enter name for media :");
            if (name == null)
                return;
            String text = JOptionPane.showInputDialog("Plain text here.");
            if (text == null)
                return;
            Media media = new Media(name , text.getBytes(StandardCharsets.UTF_8) , MediaType.TEXT);
            mediaList.add(media);
        });

        save.addActionListener( e-> {
            Response response = mainController.addEducationalMaterial(lesson , nameField.getText() ,
                    descriptionField.getText() , mediaList);
            JOptionPane.showMessageDialog(null , response.getMessage());
            nameField.setText("");
            descriptionField.setText("");
            mediaList.clear();
        });
    }
}
