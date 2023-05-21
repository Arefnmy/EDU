package server.model;

import server.database.LessonDatabase;
import server.database.UserDatabase;
import server.model.users.Student;
import shared.model.courseware.EducationalMaterial;
import shared.model.courseware.Exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Courseware {
    private final int lesson;
    private final List<Long> teachingAssistants;
    private final List<Exercise> exercises;
    private final List<EducationalMaterial> educationalMaterials;

    public Courseware(int lesson){
        this.lesson = lesson;

        teachingAssistants = new ArrayList<>();
        exercises = new ArrayList<>();
        educationalMaterials = new ArrayList<>();
    }

    public Lesson getLesson() {
        return LessonDatabase.getInstance().getLesson(lesson);
    }

    public void addTA(Student student){
        teachingAssistants.add(student.getUserCode());
    }

    public void addEducationalMaterial(EducationalMaterial educationalMaterial){
        educationalMaterials.add(educationalMaterial);
    }

    public void addExercise(Exercise exercise){
        exercises.add(exercise);
    }

    public List<Student> getTeachingAssistants() {
        return UserDatabase.getInstance().getStudents().stream()
                .filter(s-> isTA(s.getUserCode()))
                .collect(Collectors.toList());
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public List<EducationalMaterial> getEducationalMaterials() {
        return educationalMaterials;
    }

    public boolean isTA(long student){
        return teachingAssistants.contains(student);
    }
}
