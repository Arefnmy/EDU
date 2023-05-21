package client.gui.panels.studentpanel;

import client.gui.RowInformation;
import client.gui.panels.EducationalMaterialPanel;
import shared.model.courseware.Courseware;
import shared.model.courseware.EducationalMaterial;
import shared.model.media.Media;
import shared.response.Response;
import shared.util.FileUploader;

import javax.swing.*;

public class EducationalMaterialStudentPanel extends EducationalMaterialPanel {
    private final boolean isTA;
    protected EducationalMaterialStudentPanel(EducationalMaterial educationalMaterial, Courseware courseware) {
        super(educationalMaterial, courseware);
        isTA = courseware.isAssistant();
        if (!isTA)
            description.setEditable(false);
    }

    @Override
    public void setInfo() {
        if (!isTA)
            description.setText(educationalMaterial.getDescription());
        medias.removeAll();
        for (Media m : educationalMaterial.getFiles()){
            RowInformation rowInformation = new RowInformation(3 , false);
            rowInformation.addComponent(new JLabel(m.getName() + " ( " + m.getMediaType() + " )" ));
            JButton download = new JButton("Download");
            download.addActionListener( e-> JOptionPane.showMessageDialog(null , FileUploader.downloadFile(m)));
            rowInformation.addComponent(download);
            if (isTA) {
                JButton change = new JButton("Change");
                change.addActionListener(e -> {
                    Media media = FileUploader.uploadFile();
                    if (media != null) {
                        educationalMaterial.getFiles().set(educationalMaterial.getFiles().indexOf(m), media);
                        Response response = mainController.editEducationalMaterial(courseware.getLessonNumber(),
                                educationalMaterial.getName(), description.getText(), educationalMaterial.getFiles());
                        JOptionPane.showMessageDialog(null, response.getMessage());
                    }
                });
                rowInformation.addComponent(change);
            }

            medias.add(rowInformation);
        }

        mainController.refresh();
    }
}
