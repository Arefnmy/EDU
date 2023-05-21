package shared.model.courseware;

import shared.model.Time;
import shared.model.UploadType;
import shared.model.media.Media;

public class Exercise {
    private String name;
    private String description;
    private Time startTime;
    private Time endTime;
    private Time uploadWithoutDecreasingScore;
    private UploadType uploadType;
    private Media media;

    private Exercise(){}
    public Exercise(String name, String description, Time startTime,
                    Time endTime, Time uploadWithoutDecreasingScore , UploadType uploadType) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.uploadWithoutDecreasingScore = uploadWithoutDecreasingScore;
        this.uploadType = uploadType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public Time getUploadWithoutDecreasingScore() {
        return uploadWithoutDecreasingScore;
    }

    public Media getMedia() {
        return media;
    }

    public UploadType getUploadType() {
        return uploadType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setUploadWithoutDecreasingScore(Time uploadWithoutDecreasingScore) {
        this.uploadWithoutDecreasingScore = uploadWithoutDecreasingScore;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
