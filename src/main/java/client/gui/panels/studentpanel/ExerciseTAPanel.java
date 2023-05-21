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
import java.util.List;

public class ExerciseTAPanel extends ExercisePanel {
    private JPanel scorePanel;
    private RowInformation header;
    private List<Solution> solutionList;
    protected ExerciseTAPanel(Exercise exercise, Courseware courseware) {
        super(exercise, courseware);
    }

    public void initComp(){
        super.initComp();
        scorePanel = new JPanel();
        header = new RowInformation(5 , true);
        header.addComponent(new JLabel("Name"));
        header.addComponent(new JLabel("User Code"));
        header.addComponent(new JLabel("Solution"));
        header.addComponent(new JLabel("Score"));
        header.addComponent(new JLabel("Register"));
    }

    public void alignComp(){
        super.alignComp();
        JScrollPane scrollPane = new JScrollPane(scorePanel);
        scrollPane.setBounds(10 , 350 , 600 , 300);
        add(scrollPane);
    }

    @Override
    public void getResponse(){
        super.getResponse();
        Response response = mainController.getSolution(courseware.getLessonNumber() , exercise.getName());
        solutionList = (List<Solution>) response.getData("solutionList");
    }

    @Override
    public void setInfo(){
        scorePanel.removeAll();
        scorePanel.setLayout(new GridLayout(solutionList.size() + 1 , 1 , 0 , 10));
        scorePanel.add(header);
        for (Solution s : solutionList){
            RowInformation rowInformation = new RowInformation(5 , false);
            rowInformation.addComponent(new JLabel("**********"));
            rowInformation.addComponent(new JLabel("**********"));
            if (exercise.getUploadType() == UploadType.TEXT)
                rowInformation.addComponent(new JScrollPane(new JTextArea(s.getText())));
            else{
                JButton download = new JButton("Download");
                download.addActionListener( e-> FileUploader.downloadFile(s.getMedia()));
            }
            JTextField score = new JTextField(String.valueOf(s.getScore()));
            rowInformation.addComponent(score);
            JButton register = new JButton("Register");
            register.addActionListener(e->{
                Response response = mainController.registerScoreSolution(courseware.getLessonNumber() , exercise.getName(),
                        s.getStudentCode() , score.getText());
                JOptionPane.showMessageDialog(null , response.getMessage());
            });
            rowInformation.addComponent(register);

            scorePanel.add(rowInformation);
        }

        media.removeAll();
        Media m = exercise.getMedia();
        if (m != null) {
            RowInformation rowInformation = new RowInformation(3 , false);
            JLabel name = new JLabel(m.getName() + "(" + m.getMediaType() + ")");
            rowInformation.addComponent(name);
            JButton download = new JButton("Download");
            download.addActionListener(e -> JOptionPane.showMessageDialog(null, FileUploader.downloadFile(m)));
            rowInformation.addComponent(download);
            JButton change = new JButton("Change");
            change.addActionListener(e-> {
                Media media = FileUploader.uploadFile();
                if (media != null) {
                    Response response = mainController.editExercise(courseware.getLessonNumber(),
                            exercise.getName(), description.getText(), media);
                    JOptionPane.showMessageDialog(null, response.getMessage());
                }
            });
            rowInformation.addComponent(change);
            media.add(rowInformation);
        }

        mainController.refresh();
    }
}
