package shared.model.courseware;

import org.codehaus.jackson.annotate.JsonIgnore;
import shared.model.users.Student;

import java.util.List;

public class Courseware {
    private String lessonName;
    private int lessonNumber;
    private boolean assistant;
    private List<Exercise> exercises;
    private List<EducationalMaterial> educationalMaterials;
    //private List<Student> teachingAssistant;

    private Courseware(){}

    public Courseware(String lessonName,int lessonNumber ,  boolean isAssistant, List<Exercise> exercises,
                      List<EducationalMaterial> educationalMaterials) {
        this.lessonName = lessonName;
        this.lessonNumber = lessonNumber;
        assistant = isAssistant;
        this.exercises = exercises;
        this.educationalMaterials = educationalMaterials;
    }

    public String getLessonName() {
        return lessonName;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    /*public List<Student> getTeachingAssistant() {
        return teachingAssistant;
    }*/

    public boolean isAssistant() {
        return assistant;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public List<EducationalMaterial> getEducationalMaterials() {
        return educationalMaterials;
    }

    @JsonIgnore
    public EducationalMaterial getEducationalMaterial(String name){
        for (EducationalMaterial e : educationalMaterials){
            if (e.getName().equals(name))
                return e;
        }
        return null;
    }

    @JsonIgnore
    public Exercise getExercise(String name){
        for (Exercise e : exercises)
            if (e.getName().equals(name))
                return e;
        return null;
    }
}
