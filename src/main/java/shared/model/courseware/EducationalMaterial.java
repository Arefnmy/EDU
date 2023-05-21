package shared.model.courseware;

import shared.model.media.Media;

import java.util.ArrayList;
import java.util.List;

public class EducationalMaterial {
    private String name;
    private String description;
    private List<Media> files;

    private EducationalMaterial(){}

    public EducationalMaterial(String name, String description) {
        this.name = name;
        this.description = description;
        files = new ArrayList<>();
    }

    public void addFile(Media media){
        files.add(media);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Media> getFiles() {
        return files;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFiles(List<Media> files) {
        this.files = files;
    }
}
