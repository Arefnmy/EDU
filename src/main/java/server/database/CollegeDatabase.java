package server.database;

import server.model.College;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CollegeDatabase {
    private final List<College> colleges = new ArrayList<>();

    private static final CollegeDatabase instance = new CollegeDatabase();

    public synchronized static CollegeDatabase getInstance(){
        return instance;
    }

    private CollegeDatabase(){}

    public void addCollege(College college){
        colleges.add(college);
    }

    public College getCollege(String name){
        for (College c : colleges)
            if (c.getName().equalsIgnoreCase(name))
                return c;
        return null;
    }

    public List<College> getColleges() {
        return colleges;
    }

    public List<String> getCollegesName(){
        return colleges.stream()
                .map(College::getName)
                .collect(Collectors.toList());
    }
}
