package client.gui.panels.professorpanel;

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
import java.util.Collections;
import java.util.Map;

public class ExerciseProfessorPanel extends ExercisePanel {
    private JPanel scorePanel;
    private RowInformation header;
    private Map<Long , Solution> solutionMap;
    private Map<Long , String> nameMap;//Jackson convert

    protected ExerciseProfessorPanel(Exercise exercise, Courseware courseware) {
        super(exercise, courseware);
    }

    public void initComp(){
        super.initComp();
        scorePanel = new JPanel();
        header = new RowInformation(6 , true);
        header.addComponent(new JLabel("Name"));
        header.addComponent(new JLabel("User Code"));
        header.addComponent(new JLabel("Solution"));
        header.addComponent(new JLabel("Upload Time"));
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
        solutionMap = (Map<Long, Solution>) response.getData("solutionMap");
        nameMap = (Map<Long, String>) response.getData("nameMap");
    }

    @Override
    public void setInfo() {
        scorePanel.removeAll();
        scorePanel.setLayout(new GridLayout(solutionMap.size() + 1, 1, 0, 10));
        scorePanel.add(header);
        for (Object o : solutionMap.keySet()) {
            RowInformation rowInformation = new RowInformation(6, false);
            rowInformation.addComponent(new JLabel(nameMap.get(o)));
            rowInformation.addComponent(new JLabel(o.toString()));
            Solution solution = solutionMap.get(o);
            if (solution != null) {
                if (exercise.getUploadType() == UploadType.TEXT)
                    rowInformation.addComponent(new JScrollPane(new JTextArea(solution.getText())));
                else {
                    JButton download = new JButton("Download");
                    download.addActionListener(e -> FileUploader.downloadFile(solution.getMedia()));
                }
                rowInformation.addComponent(new JLabel(solution.getUploadTime().toString()));
                JTextField score = new JTextField(String.valueOf(solution.getScore()));
                rowInformation.addComponent(score);
                JButton register = new JButton("Register");
                register.addActionListener(e -> {
                    Response response = mainController.registerScoreSolution(courseware.getLessonNumber(), exercise.getName(),
                            solution.getStudentCode(), score.getText());
                    JOptionPane.showMessageDialog(null, response.getMessage());
                });
                rowInformation.addComponent(register);
            } else {
                rowInformation.addComponent(new JLabel("Not Delivered"));
            }
            scorePanel.add(rowInformation);
        }

        media.removeAll();
        Media m = exercise.getMedia();
        if (m != null) {
            RowInformation rowInformation = new RowInformation(4 , false);
            JLabel name = new JLabel(m.getName() + "(" + m.getMediaType() + ")");
            rowInformation.addComponent(name);
            JButton download = new JButton("Download");
            download.addActionListener(e -> JOptionPane.showMessageDialog(null, FileUploader.downloadFile(m)));
            rowInformation.addComponent(download);
            JButton remove = new JButton("Remove");
            remove.addActionListener( e-> {
                Response response = mainController.editExercise(courseware.getLessonNumber() , exercise.getName(),
                        description.getText() , null);
                JOptionPane.showMessageDialog(null, response.getMessage());
            });
            rowInformation.addComponent(remove);
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
        else{
            JButton addMedia = new JButton("Add Media");
            addMedia.addActionListener( e-> {
                Media newMedia = FileUploader.uploadFile();
                Response response = mainController.editExercise(courseware.getLessonNumber() , exercise.getName() ,
                        description.getText() , newMedia);
                JOptionPane.showMessageDialog(null , response.getMessage());
            });
            media.add(addMedia);
        }

        mainController.refresh();
    }
}
