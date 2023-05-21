package shared.model.educaionalrequests;


public class Protest{
    private String name;
    private long studentCode;
    private int lessonCode;
    private String register;
    private String replay;

    private Protest(){};
    public Protest(int lesson, long student , String register) {
        this.lessonCode = lesson;
        this.studentCode = student;
        this.register = register;

        replay = "Registered";
        name = studentCode + "-" + lessonCode;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public long getStudentCode() {
        return studentCode;
    }

    public String getReplay() {
        return replay;
    }

    public int getLessonCode() {
        return lessonCode;
    }

    public String getRegister() {
        return register;
    }

    public String getName() {
        return name;
    }
}
