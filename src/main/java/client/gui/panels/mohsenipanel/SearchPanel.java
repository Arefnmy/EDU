package client.gui.panels.mohsenipanel;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.filemanager.ResourceManager;
import client.gui.RowInformation;
import shared.model.users.Student;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class SearchPanel extends JPanel implements AutoRefresh {
    private List<Student> studentList;
    private JTextField codeField;
    private JScrollPane scrollPane;
    private JPanel usersPanel;

    private RowInformation header;

    MainController mainController = MainController.getInstance();
    public SearchPanel(){
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setLoop(ResourceManager.getInstance().getValue(Integer.class , "defaultPanel-fps" , 1));
    }

    public void initComp(){
        codeField = new JTextField();
        scrollPane = new JScrollPane();
        usersPanel = new JPanel();

        header = new RowInformation(4 , true);
        header.addComponent(new JLabel("Name"));
        header.addComponent(new JLabel("Code"));
        header.addComponent(new JLabel("Grade"));
        header.addComponent(new JLabel("Entry Year"));
    }

    public void alignComp(){
        usersPanel.setLayout(new BoxLayout(usersPanel , BoxLayout.Y_AXIS));
        scrollPane.setViewportView(usersPanel);
        usersPanel.add(header);

        codeField.setPreferredSize(new Dimension(100 , 30));
        add(scrollPane);
        add(codeField);
    }

    @Override
    public void getResponse() {
        Response response = mainController.getSearch(codeField.getText());
        studentList = (List<Student>) response.getData("studentList");
    }

    @Override
    public void setInfo() {
        usersPanel.removeAll();
        usersPanel.add(header);

        for (Student s : studentList){
            RowInformation rowInformation = new RowInformation(4 , false);
            rowInformation.addComponent(new JLabel(s.getName()));
            rowInformation.addComponent(new JLabel(String.valueOf(s.getUserCode())));
            rowInformation.addComponent(new JLabel(s.getGrade().toString()));
            rowInformation.addComponent(new JLabel(String.valueOf(s.getEntryYear())));

            rowInformation.setFocusable(false);
            rowInformation.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    mainController.changeContentPane(new MohseniStudentPanel(s));
                }
                @Override
                public void mousePressed(MouseEvent mouseEvent) {}
                @Override
                public void mouseReleased(MouseEvent mouseEvent) {}
                @Override
                public void mouseEntered(MouseEvent mouseEvent) {
                    rowInformation.setForeground(Color.WHITE);
                }
                @Override
                public void mouseExited(MouseEvent mouseEvent) {
                    rowInformation.setForeground(Color.BLACK);
                }
            });

            usersPanel.add(rowInformation);
        }

        mainController.refresh();
    }
}
