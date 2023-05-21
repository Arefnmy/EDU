package client.gui.panels.professorpanel;

import client.gui.MainFrame;
import client.gui.RowInformation;
import client.gui.panels.CoursePanel;
import shared.model.courseware.Courseware;
import shared.model.courseware.EducationalMaterial;
import shared.model.courseware.Exercise;
import shared.model.media.Media;
import shared.response.Response;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CoursePanelProfessor extends CoursePanel {
    private JPanel addStudentPanel;
    private JTextField codeField;
    private JCheckBox isTA;
    private JButton addStudentButton;
    private JButton addMaterial;
    private JButton addExercise;

    public CoursePanelProfessor(Courseware courseware) {
        super(courseware);
    }

    public void initComp(){
        super.initComp();
        addStudentPanel = new JPanel(new FlowLayout());
        codeField = new JTextField();
        isTA = new JCheckBox("is Teaching Assistant");
        addStudentButton = new JButton("Add Student");
        addStudentPanel.add(new JLabel("Student Code :"));
        codeField.setPreferredSize(new Dimension(100 , 20));
        addStudentPanel.add(codeField);
        addStudentPanel.add(isTA);
        addStudentPanel.add(addStudentButton);

        addMaterial = new JButton("Add Educational Material");
        addExercise = new JButton("Add Exercise");
    }

    public void alignComp(){
        super.alignComp();
        addStudentPanel.setBounds((MainFrame.FRAME_WIDTH - 500) /2 , MainFrame.FRAME_HEIGHT-150 , 650 , 40);
        addExercise.setBounds(MainFrame.FRAME_WIDTH/2 + 200 , 50 , 200, 20);
        addMaterial.setBounds(200 , 50 , 200 , 20);

        add(addStudentPanel);
        add(addExercise);
        add(addMaterial);
    }

    public void setActionListener(){
        super.setActionListener();
        addStudentButton.addActionListener(e-> {
            Response response = mainController.addStudentToCourseware(codeField.getText() ,
                    isTA.isSelected() ,courseware.getLessonNumber());
            JOptionPane.showMessageDialog(null , response.getMessage());
            isTA.setSelected(false);
            codeField.setText("");
        });

        addMaterial.addActionListener( e-> new AddEducationalMaterial(courseware.getLessonNumber()));
        addExercise.addActionListener( e-> new AddExercise(courseware.getLessonNumber()));
    }

    @Override
    public void setInfo() {
        List<EducationalMaterial> educationalMaterials = courseware.getEducationalMaterials();
        materialPanel.removeAll();
        materialPanel.setLayout(new GridLayout(educationalMaterials.size() + 1 , 1 , 0 , 10));
        materialPanel.add(headerMaterial);
        for (EducationalMaterial e : educationalMaterials){
            RowInformation rowInformation = new RowInformation(4 , false);
            rowInformation.addComponent(new JLabel(e.getName()));
            rowInformation.addComponent(new JLabel((e.getDescription()+"            ").substring(0 , 12) + "..."));
            JButton show = new JButton("Show");
            show.addActionListener( l-> mainController.changeContentPane(new EducationalMaterialProfessorPanel(e , courseware)));
            rowInformation.addComponent(show);
            JButton remove = new JButton("Remove");
            remove.addActionListener( l-> {
                Response response = mainController.removeEducationalMaterial(courseware.getLessonNumber() ,
                        e.getName());
                JOptionPane.showMessageDialog(null , response.getMessage());
            });
            rowInformation.addComponent(remove);

            materialPanel.add(rowInformation);
        }

        List<Exercise> exercises = courseware.getExercises();
        exercisePanel.removeAll();
        exercisePanel.setLayout(new GridLayout(exercises.size() + 1, 1 , 0 , 10));
        exercisePanel.add(headerExercise);
        for (Exercise e : exercises){
            RowInformation rowInformation = new RowInformation(4 , false);
            rowInformation.addComponent(new JLabel(e.getName()));
            rowInformation.addComponent(new JLabel((e.getDescription()+"            ").substring(0 , 12) + "..."));
            JButton show = new JButton("Show");
            show.addActionListener( l-> mainController.changeContentPane(new ExerciseProfessorPanel(e , courseware)));
            rowInformation.addComponent(show);
            JButton remove = new JButton("Remove");
            remove.addActionListener( l-> {
                Response response = mainController.removeExercise(courseware.getLessonNumber() ,
                        e.getName());
                JOptionPane.showMessageDialog(null , response.getMessage());
            });
            rowInformation.addComponent(remove);

            exercisePanel.add(rowInformation);
        }

        mainController.refresh();
    }
}
