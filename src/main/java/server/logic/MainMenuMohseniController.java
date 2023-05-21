package server.logic;

import server.database.CollegeDatabase;
import server.database.UserDatabase;
import server.model.College;
import server.model.users.Student;
import server.model.users.User;
import shared.model.Grade;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainMenuMohseniController extends MainMenuController{
    public MainMenuMohseniController(User user) {
        super(user);
    }

    public List<Student> getCreateChat(Integer entryYear , String grade , String collegeName){
        boolean isSelectedGrade = grade != null;
        boolean isSelectedEntryYear = entryYear != null;
        boolean isSelectedCollege = collegeName != null;

        College college = CollegeDatabase.getInstance().getCollege(collegeName);
        List<Student> studentList = new ArrayList<>();
        for (Student s : UserDatabase.getInstance().getStudents()){
            if ( ( !isSelectedGrade || s.getGrade() == Grade.getEnum(grade) ) &&
                    ( !isSelectedEntryYear || s.getEntryYear() == entryYear ) &&
                    ( !isSelectedCollege || s.getCollege() == college ) )
                studentList.add(s);
        }

        studentList.sort(Comparator.comparingInt(Student::getEntryYear));
        return studentList;
    }

    public List<Student> getSearch(String code){
        return userDatabase.getStudents().stream()
                .filter(s-> (s.getUserCode() + "" ).startsWith(code)).collect(Collectors.toList());
    }
}
