package client.gui.panels;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.filemanager.ResourceManager;
import shared.model.courseware.Courseware;
import shared.model.courseware.EducationalMaterial;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;

public abstract class EducationalMaterialPanel extends JPanel implements AutoRefresh {
    protected Courseware courseware;
    protected EducationalMaterial educationalMaterial;
    protected JLabel name;
    protected JTextArea description;
    protected JPanel medias;

    protected MainController mainController = MainController.getInstance();
    protected EducationalMaterialPanel(EducationalMaterial educationalMaterial , Courseware courseware){
        this.educationalMaterial = educationalMaterial;
        this.courseware = courseware;

        initComp();
        alignComp();
        setLoop(ResourceManager.getInstance().getValue(Integer.class , "defaultPanel-fps" , 1));
    }

    public void initComp(){
        name = new JLabel(educationalMaterial.getName());
        description = new JTextArea(educationalMaterial.getDescription());
        medias = new JPanel(new GridLayout(5 , 1 , 0 , 10));
    }

    public void alignComp(){
        setLayout(null);
        name.setBounds(50, 20, 240, 30);
        medias.setBounds(500, 140, 500, 470);
        JScrollPane scrollPane = new JScrollPane(description);
        description.setLineWrap(true);
        scrollPane.setBounds(50, 120, 280, 210);

        add(name);
        add(scrollPane);
        add(medias);
    }

    @Override
    public void getResponse(){
        Response response = mainController.getCourseware(courseware.getLessonNumber());
        courseware = (Courseware) response.getData("courseware");
        educationalMaterial = courseware.getEducationalMaterial(educationalMaterial.getName());
    }
}
