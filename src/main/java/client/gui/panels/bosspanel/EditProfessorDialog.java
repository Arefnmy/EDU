package client.gui.panels.bosspanel;

import client.controller.MainController;
import client.filemanager.ResourceManager;
import client.gui.RowInformation;
import shared.model.users.EducationalAssistant;
import shared.model.users.Professor;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;

public class EditProfessorDialog extends JDialog {
    private Professor professor;

    private JPanel mainPanel;
    private JPanel rowsPanel;

    private RowInformation header1;
    private RowInformation header2;
    private RowInformation info1;
    private RowInformation info2;

    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField roomNumberField;

    private JCheckBox isAssistant;

    private JButton edit;
    private JButton remove;

    MainController mainController = MainController.getInstance();

    public EditProfessorDialog(Professor professor){
        this.professor = professor;

        setBounds(ResourceManager.getInstance().getValue(Integer.class , "defaultDialog-x" , 400) ,
                ResourceManager.getInstance().getValue(Integer.class , "defaultDialog-y" , 400) ,
                ResourceManager.getInstance().getValue(Integer.class , "editProfessor-width" , 500) ,
                ResourceManager.getInstance().getValue(Integer.class , "editProfessor-height" , 500));

        initComp();
        alignComp();
        setActionListener();

        setVisible(true);
    }

    public void initComp() {
        mainPanel = new JPanel();
        rowsPanel = new JPanel(new GridLayout(4 , 1 , 0 , 10));

        usernameField = new JTextField(professor.getUsername());
        passwordField = new JTextField(professor.getPassword());
        roomNumberField = new JTextField(professor.getRoomNumber());

        header1 = new RowInformation(3 , true);
        header1.addComponent(new JLabel("College"));
        header1.addComponent(new JLabel("Name"));
        header1.addComponent(new JLabel("Degree"));

        info1 = new RowInformation(3 , false);
        info1.addComponent(new JLabel(professor.getCollegeName()));
        info1.addComponent(new JLabel(professor.getName()));
        info1.addComponent(new JLabel(professor.getDegree().toString()));

        header2 = new RowInformation(3 , true);
        header2.addComponent(new JLabel("Username"));
        header2.addComponent(new JLabel("Password"));
        header2.addComponent(new JLabel("Room number"));

        info2 = new RowInformation(3 , false);
        info2.addComponent(usernameField);
        info2.addComponent(passwordField);
        info2.addComponent(roomNumberField);

        edit = new JButton("Edit Professor");
        remove = new JButton("Remove Professor");
        isAssistant = new JCheckBox(
                ResourceManager.getInstance().getGraphicConfig().getProperty("approveProfessor-msg" , "Approve"));
        if (professor instanceof EducationalAssistant){
            isAssistant.setSelected(true);
            isAssistant.setEnabled(false);
        }
    }

    public void alignComp(){
        setContentPane(mainPanel);

        rowsPanel.add(header1);
        rowsPanel.add(info1);
        rowsPanel.add(header2);
        rowsPanel.add(info2);

        mainPanel.add(rowsPanel);
        mainPanel.add(isAssistant);
        mainPanel.add(edit);
        mainPanel.add(remove);
    }

    public void setActionListener(){
        edit.addActionListener( e-> {
            Response response = mainController.editProfessor(professor.getUserCode(), usernameField.getText() ,
                    passwordField.getText() ,roomNumberField.getText());
            JOptionPane.showMessageDialog(this , response.getMessage());
        });

        remove.addActionListener( e-> {
            Response response = mainController.removeUser(professor.getUserCode());
            JOptionPane.showMessageDialog(this , response.getMessage());
            dispose();
        });
    }
}
