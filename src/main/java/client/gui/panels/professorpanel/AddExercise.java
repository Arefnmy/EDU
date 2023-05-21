package client.gui.panels.professorpanel;

import client.controller.MainController;
import client.gui.TimePanel;
import shared.model.Time;
import shared.model.UploadType;
import shared.model.media.Media;
import shared.response.Response;
import shared.util.FileUploader;

import javax.swing.*;
import java.time.LocalDateTime;

public class AddExercise extends JDialog {
    private final int lesson;
    private JLabel  name;
    private JLabel description;
    private JLabel type;
    private JLabel startTime;
    private JLabel endTime;
    private JLabel uploadTime;
    private JTextField nameField;
    private JTextArea descriptionField;
    private JComboBox<UploadType> types;
    private TimePanel startTimePanel;
    private TimePanel endTimePanel;
    private TimePanel uploadTimePanel;
    private JButton setMedia;
    private JButton save;
    private Media media = null;

    MainController mainController = MainController.getInstance();
    public AddExercise(int lesson){
        this.lesson = lesson;
        setBounds(400 , 200 , 500 , 600);

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
        setMedia = new JButton("Set Media");
        type = new JLabel("Upload Type");
        types = new JComboBox<>(UploadType.values());
        startTime = new JLabel("Start Time");
        endTime = new JLabel("End Time");
        uploadTime = new JLabel("Upload Time");
        LocalDateTime now = LocalDateTime.now();
        startTimePanel = new TimePanel(new Time(now));
        endTimePanel = new TimePanel(new Time(now.plusDays(7)));
        uploadTimePanel = new TimePanel(new Time(now.plusDays(7)));
    }

    public void alignComp(){
        setLayout(null);
        name.setBounds(40, 30, 100, 30);
        nameField.setBounds(220, 20, 180, 40);
        description.setBounds(40, 110, 110, 40);
        descriptionField.setBounds(180, 80, 280, 90);
        setMedia.setBounds(220, 490, 280, 70);
        save.setBounds(0, 520, 100, 30);
        type.setBounds(40, 200, 90, 30);
        types.setBounds(240, 200, 90, 30);
        endTime.setBounds(40, 390, 90, 40);
        endTimePanel.setBounds(210, 390, 220, 50);
        startTime.setBounds(40, 260, 90, 40);
        startTimePanel.setBounds(210, 250, 220, 50);
        uploadTime.setBounds(40, 330, 90, 40);
        uploadTimePanel.setBounds(210, 320, 220, 50);


        add(name);
        add(nameField);
        add(description);
        add(descriptionField);
        add(setMedia);
        add(save);
        add(type);
        add(types);
        add(startTime);
        add(startTimePanel);
        add(endTime);
        add(endTimePanel);
        add(uploadTime);
        add(uploadTimePanel);
    }

    public void setActionListener(){
        setMedia.addActionListener( e-> media = FileUploader.uploadFile());
        save.addActionListener( e-> {
            Response response = mainController.addExercise(lesson , nameField.getText() , descriptionField.getText() ,
                    media , startTimePanel.getTime() , endTimePanel.getTime() , uploadTimePanel.getTime(),
                    (UploadType) types.getSelectedItem());
            JOptionPane.showMessageDialog(null , response.getMessage());
            nameField.setText("");
            descriptionField.setText("");
            media = null;
        });
    }
}
