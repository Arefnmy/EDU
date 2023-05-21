package client.gui.panels.studentpanel;

import client.gui.RowInformation;
import client.gui.panels.ExercisePanel;
import shared.model.UploadType;
import shared.model.courseware.Courseware;
import shared.model.courseware.Exercise;
import shared.model.courseware.Solution;
import shared.model.media.Media;
import shared.response.Response;
import shared.util.FileUploader;

import javax.swing.*;
import java.awt.*;

public class ExerciseStudentPanel extends ExercisePanel {
    private JPanel solutionPanel;
    private JLabel deliveryStatus;
    private JLabel score;
    private Solution solution;


    protected ExerciseStudentPanel(Exercise exercise, Courseware courseware) {
        super(exercise, courseware);
    }

    public void initComp() {
        super.initComp();
        solutionPanel = new JPanel();
        deliveryStatus = new JLabel();
        score = new JLabel();
    }

    public void alignComp() {
        super.alignComp();
        if (exercise.getUploadType() == UploadType.TEXT){
            JTextArea textArea = new JTextArea();
            textArea.setPreferredSize(new Dimension(200 , 100));
            JButton sendMessage = new JButton("Send Message");
            sendMessage.addActionListener( e-> {
                Response response = mainController.addSolution(courseware.getLessonNumber() , exercise.getName() ,
                        textArea.getText() , null);
                JOptionPane.showMessageDialog(null , response.getMessage());
            });
            solutionPanel.add(new JScrollPane(textArea));
            solutionPanel.add(sendMessage);
        }
        else{
            JButton addMedia = new JButton("Add Media");
            addMedia.addActionListener( e->{
                Media media = FileUploader.uploadFile();
                Response response = mainController.addSolution(courseware.getLessonNumber() , exercise.getName() ,
                        null , media);
                JOptionPane.showMessageDialog(null , response.getMessage());
            });
            solutionPanel.add(addMedia);
        }
        solutionPanel.add(deliveryStatus);
        solutionPanel.add(score);
        solutionPanel.setBounds(10 , 400 , 300 , 300);
        add(solutionPanel);
        description.setEditable(false);
    }

    @Override
    public void getResponse(){
        super.getResponse();
        Response response = mainController.getSolution(courseware.getLessonNumber() , exercise.getName());
        solution = (Solution) response.getData("solution");
    }

    @Override
    public void setInfo(){
        if (solution == null)
            deliveryStatus.setText("Not Delivered!");
        else {
            deliveryStatus.setText("Delivered.");
            score.setText("Score :" + solution.getScore());
        }

        media.removeAll();
        Media m = exercise.getMedia();
        if (m != null) {
            RowInformation rowInformation = new RowInformation(2, false);
            JLabel name = new JLabel(m.getName() + "(" + m.getMediaType() + ")");
            rowInformation.addComponent(name);
            JButton download = new JButton("Download");
            download.addActionListener(e -> JOptionPane.showMessageDialog(null, FileUploader.downloadFile(m)));
            rowInformation.addComponent(download);

            media.add(rowInformation);
        }

        mainController.refresh();
    }

}
