package server.database;

import server.model.users.*;

import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    private final List<User> users = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private final List<Professor> professors = new ArrayList<>();
    private final List<AdminEdu> admins = new ArrayList<>();
    private Mohseni mohseni;

    private static final UserDatabase instance = new UserDatabase();
    public synchronized static UserDatabase getInstance(){
        return instance;
    }

    private UserDatabase(){}

    public void addUser(User user){
        users.add(user);
        if (user instanceof Student)
            students.add((Student) user);
        else if(user instanceof CollegeBoss)
            professors.add((CollegeBoss) user);
        else if (user instanceof EducationalAssistant)
            professors.add((EducationalAssistant) user);
        else if (user instanceof Professor)
            professors.add((Professor) user);
        else if (user instanceof AdminEdu)
            admins.add((AdminEdu) user);
        else if (user instanceof Mohseni)
            mohseni = (Mohseni) user;
    }


    public void removeUser(User user){
        users.remove(user);
        if (user instanceof Student)
            students.remove((Student) user);
        else
            professors.remove((Professor) user);
    }

    public User getUser(String username){
        for (User u : users) {
            if (u.getUsername().equals(username))
                return u;
        }
        return null;
    }

    public User getUser(long userCode){
        for (User u : users) {
            if (u.getUserCode() == userCode)
                return u;
        }
        return null;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public List<AdminEdu> getAdmins() {
        return admins;
    }

    public Mohseni getMohseni() {
        return mohseni;
    }

    public Student getStudent(long code){
        for (Student s : students)
            if (s.getUserCode() == code)
                return s;
        return null;
    }

    public Professor getProfessor(long code){
        for(Professor p : professors)
            if (p.getUserCode() == code)
                return p;
        return null;
    }

    public Professor getProfessorByName(String name){
        for (Professor p : professors){
            if (p.getName().equals(name))
                return p;
        }
        return null;
    }

    public Student getStudentByName(String name){
        for (Student s : students){
            if (s.getName().equals(name))
                return s;
        }
        return null;
    }

    public boolean usernameNotUsed(String username){
        for (User user : users)
            if (user.getUsername().equals(username))
                return false;
        return true;
    }
}
