package client.gui.panels;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.gui.RowInformation;
import client.gui.panels.assistantpanel.AddProfessorPanel;
import client.gui.panels.bosspanel.EditProfessorDialog;
import shared.model.users.Professor;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProfessorListPanel extends JPanel implements AutoRefresh {
    private List<Professor> professorList;
    private final boolean isBoss;

    private JPanel professorPanel;
    private RowInformation header;

    private JButton addProfessor;

    MainController mainController = MainController.getInstance();
    public ProfessorListPanel(){
        Response response = mainController.getProfessorList();
        isBoss = (boolean) response.getData("isBoss");
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setActionListener();
        setLoop(1);
    }

    public void initComp(){
        addProfessor = new JButton("Add Professor");

        professorPanel = new JPanel();

        header = new RowInformation(5 , true);
        header.addComponent(new JLabel("College"));
        header.addComponent(new JLabel("Name"));
        header.addComponent(new JLabel("Degree"));
        header.addComponent(new JLabel("Room number"));
        if (isBoss)
            header.addComponent(new JLabel("Edit"));
    }

    public void alignComp(){
        if (isBoss)
            add(addProfessor);

        add(professorPanel);
    }

    public void setActionListener(){
        addProfessor.addActionListener( a->
                mainController.changeContentPane(new AddProfessorPanel()));
    }

    @Override
    public void setInfo() {
        professorPanel.removeAll();
        professorPanel.add(header);

        professorPanel.setLayout(new GridLayout(professorList.size() +1 , 1 , 0 , 20));
        for (Professor p : professorList){
            RowInformation rowInformation = new RowInformation(5 , false);
            rowInformation.addComponent(new JLabel(p.getCollegeName()));
            rowInformation.addComponent(new JLabel(p.getName()));
            rowInformation.addComponent(new JLabel(p.getDegree().toString()));
            rowInformation.addComponent(new JLabel(p.getRoomNumber()));

            if (isBoss){
                JButton editButton = new JButton("Edit");
                rowInformation.addComponent(editButton);
                editButton.addActionListener( a-> {
                    /*if (user.getCollege() != p.getCollege())
                        JOptionPane.showMessageDialog(new JFrame(), "You don't have access to edit this Professor!");
                    else*/
                        new EditProfessorDialog(p);
                });
            }
            professorPanel.add(rowInformation);
        }
        mainController.refresh();
    }

    @Override
    public void getResponse() {
        Response response = mainController.getProfessorList();
        professorList = (List<Professor>) response.getData("professorList");
    }
}
