package server.model;

import server.database.LessonDatabase;
import server.database.UserDatabase;
import server.model.users.*;

import java.util.ArrayList;
import java.util.List;

public class College {
    private final String name;

    private long collegeBossCode;
    private long educationalAssistantCode;

    private final List<Long> studentsCode;
    private final List<Long> professorsCode;
    private final List<Integer> lessonsCode;


    public College(String name) {
        this.name = name;
        studentsCode = new ArrayList<>();
        professorsCode = new ArrayList<>();
        lessonsCode = new ArrayList<>();
    }

    //getters

    public String getName() {
        return name;
    }

    public CollegeBoss getCollegeBoss() {
        return (CollegeBoss) UserDatabase.getInstance().getProfessor(collegeBossCode);
    }

    public EducationalAssistant getEducationalAssistant() {
        return (EducationalAssistant) UserDatabase.getInstance().getProfessor(educationalAssistantCode);
    }

    public List<Student> getStudentList() {
        List<Student> studentList = new ArrayList<>();
        for (Student s : UserDatabase.getInstance().getStudents())
            if (studentsCode.contains(s.getUserCode()))
                studentList.add(s);

        return studentList;
    }

    public List<Professor> getProfessorList() {
        List<Professor> professorList = new ArrayList<>();
        for (Professor p : UserDatabase.getInstance().getProfessors())
            if (professorsCode.contains(p.getUserCode()))
                professorList.add(p);

        return professorList;
    }

    public List<Lesson> getLessonList(){
        List<Lesson> lessonList = new ArrayList<>();
        for (Lesson l : LessonDatabase.getInstance().getLessons())
            if (lessonsCode.contains(l.getNumber()))
                lessonList.add(l);
        return lessonList;
    }

//setters

    public void setCollegeBoss(CollegeBoss collegeBoss) {
        this.collegeBossCode = collegeBoss.getUserCode();
    }

    public void setEducationalAssistant(EducationalAssistant educationalAssistant) {
        if (! (educationalAssistant instanceof  CollegeBoss))
        this.educationalAssistantCode = educationalAssistant.getUserCode();
    }

    public void addStudent(long student){
        studentsCode.add(student);
    }

    public void addProfessor(long professor){
        professorsCode.add(professor);
    }

    public void addLesson(int lesson){
        lessonsCode.add(lesson);
    }
}
