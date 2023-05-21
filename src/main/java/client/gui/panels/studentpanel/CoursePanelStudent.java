package client.gui.panels.studentpanel;

import client.gui.RowInformation;
import client.gui.panels.CoursePanel;
import shared.model.courseware.Courseware;
import shared.model.courseware.EducationalMaterial;
import shared.model.courseware.Exercise;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CoursePanelStudent extends CoursePanel {
    public CoursePanelStudent(Courseware courseware) {
        super(courseware);
    }

    @Override
    public void setInfo() {
        List<EducationalMaterial> educationalMaterials = courseware.getEducationalMaterials();
        materialPanel.removeAll();
        materialPanel.setLayout(new GridLayout(educationalMaterials.size() + 1 , 1 , 0 , 10));
        materialPanel.add(headerMaterial);
        for (EducationalMaterial e : educationalMaterials){
            RowInformation rowInformation = new RowInformation(3 , false);
            rowInformation.addComponent(new JLabel(e.getName()));
            rowInformation.addComponent(new JLabel((e.getDescription()+"        ").substring(0 , 8) + "..."));
            JButton show = new JButton("Show");
            show.addActionListener( l-> mainController.changeContentPane(new EducationalMaterialStudentPanel(e , courseware)));
            rowInformation.addComponent(show);

            materialPanel.add(rowInformation);
        }

        List<Exercise> exercises = courseware.getExercises();
        exercisePanel.removeAll();
        exercisePanel.setLayout(new GridLayout(exercises.size() + 1, 1 , 0 , 10));
        exercisePanel.add(headerExercise);
        for (Exercise e : exercises){
            RowInformation rowInformation = new RowInformation(3 , false);
            rowInformation.addComponent(new JLabel(e.getName()));
            rowInformation.addComponent(new JLabel((e.getDescription()+"        ").substring(0 , 8) + "..."));
            JButton show = new JButton("Show");
            show.addActionListener( l-> mainController.changeContentPane(courseware.isAssistant() ?
                    new ExerciseTAPanel(e , courseware) : new ExerciseStudentPanel(e , courseware)));
            rowInformation.addComponent(show);

            exercisePanel.add(rowInformation);
        }

        mainController.refresh();
    }
}
