package shared.model.courseware;

import shared.model.Time;
import shared.model.UploadType;
import shared.model.media.Media;

public class Solution {
    private String name;
    private long studentCode;
    private int lessonNumber;
    private String exerciseName;
    private Time uploadTime;

    private UploadType uploadType;
    private Media media;
    private String text;

    private Double score;

    private Solution(){}

    private Solution(long studentCode, int lessonNumber, String exerciseName , Time uploadTime){
        this.studentCode = studentCode;
        this.lessonNumber = lessonNumber;
        this.exerciseName = exerciseName;
        this.uploadTime = uploadTime;
        name = studentCode + "-" + lessonNumber + "-" + exerciseName;
    }

    public Solution(long studentCode , int lessonNumber ,String exerciseName, Time time,  Media media){
        this(studentCode , lessonNumber , exerciseName , time);
        uploadType = UploadType.MEDIA;
        this.media = media;
    }

    public Solution(long studentCode , int lessonNumber ,String exerciseName , Time time, String text){
        this(studentCode , lessonNumber , exerciseName , time);
        uploadType = UploadType.TEXT;
        this.text = text;
    }

    public long getStudentCode() {
        return studentCode;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public UploadType getUploadType() {
        return uploadType;
    }

    public Media getMedia() {
        return media;
    }

    public String getText() {
        return text;
    }

    public Double getScore() {
        return score;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public String getName() {
        return name;
    }

    public Time getUploadTime() {
        return uploadTime;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public void setText(String text) {
        this.text = text;
    }
}
