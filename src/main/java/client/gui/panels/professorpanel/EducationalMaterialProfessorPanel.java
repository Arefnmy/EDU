package client.gui.panels.professorpanel;

import client.gui.RowInformation;
import client.gui.panels.EducationalMaterialPanel;
import shared.model.courseware.Courseware;
import shared.model.courseware.EducationalMaterial;
import shared.model.media.Media;
import shared.response.Response;
import shared.response.ResponseState;
import shared.util.FileUploader;

import javax.swing.*;
import java.util.List;

public class EducationalMaterialProfessorPanel extends EducationalMaterialPanel {
    private JButton addMedia;
    private JButton saveInfo;

    public EducationalMaterialProfessorPanel(EducationalMaterial educationalMaterial, Courseware courseware) {
        super(educationalMaterial, courseware);
        setActionListener();
    }

    public void initComp(){
        super.initComp();
        addMedia = new JButton("Add Media");
        saveInfo = new JButton("Save Changes");
    }

    public void alignComp(){
        super.alignComp();
        addMedia.setBounds(500 , 100 , 100 , 30);
        saveInfo.setBounds(100 , 600 , 200 , 30);

        add(addMedia);
        add(saveInfo);
    }

    public void setActionListener(){
        addMedia.addActionListener( e-> {
            Media media = FileUploader.uploadFile();
            List<Media> mediaList = educationalMaterial.getFiles();
            mediaList.add(media);
            Response response = mainController.editEducationalMaterial(courseware.getLessonNumber() ,
                    educationalMaterial.getName() ,description.getText() , mediaList);
            JOptionPane.showMessageDialog(null , response.getMessage());
        });

        saveInfo.addActionListener( e->{
            Response response = mainController.editEducationalMaterial(courseware.getLessonNumber() ,
                    educationalMaterial.getName() , description.getText(), educationalMaterial.getFiles());
            JOptionPane.showMessageDialog(null , response.getMessage());

        });
    }

    @Override
    public void setInfo() {
        medias.removeAll();
        for (Media m : educationalMaterial.getFiles()){
            RowInformation rowInformation = new RowInformation(4 , false);
            rowInformation.addComponent(new JLabel(m.getName() + " ( " + m.getMediaType() + " )" ));
            JButton download = new JButton("Download");
            download.addActionListener( e-> JOptionPane.showMessageDialog(null , FileUploader.downloadFile(m)));
            rowInformation.addComponent(download);
            JButton remove = new JButton("Remove");
            remove.addActionListener( e-> {
                List<Media> mediaList = educationalMaterial.getFiles();
                mediaList.remove(m);
                Response response = mainController.editEducationalMaterial(courseware.getLessonNumber() ,
                        educationalMaterial.getName(), description.getText() , mediaList);
                JOptionPane.showMessageDialog(null , response.getMessage());
            });
            rowInformation.addComponent(remove);
            JButton change = new JButton("Change");
            change.addActionListener(e->{
                Media media = FileUploader.uploadFile();
                if (media != null) {
                    educationalMaterial.getFiles().set(educationalMaterial.getFiles().indexOf(m), media);
                    Response response = mainController.editEducationalMaterial(courseware.getLessonNumber(),
                            educationalMaterial.getName(), description.getText(), educationalMaterial.getFiles());
                    JOptionPane.showMessageDialog(null, response.getMessage());
                }
            });
            rowInformation.addComponent(change);

            medias.add(rowInformation);
        }
        mainController.refresh();
    }
}
