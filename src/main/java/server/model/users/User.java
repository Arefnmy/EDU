package server.model.users;

import server.database.CollegeDatabase;
import server.database.RequestDatabase;
import server.model.College;
import server.model.educationalrequest.EducationalRequest;
import shared.model.notification.Notification;
import shared.model.Time;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String collegeName;

    protected Time lastEnter;
    protected String username;
    protected String password;

    protected final String firstName;
    protected final String lastName;
    protected String email;
    protected String mobileNumber;
    protected final String nationalCode;

    protected List<String> requests;
    protected List<Notification> notifications;

    public User(College college, String username, String password, String firstName, String lastName,
                String email, String mobileNumber, String nationalCode) {
        this.collegeName = college == null ? null : college.getName();
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.nationalCode = nationalCode;

        lastEnter = new Time(LocalDateTime.MAX);

        requests = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    //getters

    public College getCollege() {
        return CollegeDatabase.getInstance().getCollege(collegeName);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName(){
        return firstName + " " + lastName;
    }

    public List<EducationalRequest> getRequests() {
        List<EducationalRequest> educationalRequestList = new ArrayList<>();
        for (String s : requests)
            educationalRequestList.add(RequestDatabase.getInstance().getRequest(s));
        return educationalRequestList;
    }

    public abstract long getUserCode();

    public Time getLastEnter() {
        return lastEnter;
    }

    public Notification getNotification(int code){
        for (Notification n : notifications)
            if (n.getCode() == code)
                return n;
        return null;
    }
//setters

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void addRequest(String educationalRequest){
        requests.add(educationalRequest);
    }

    public void addNotification(Notification notification){
        notifications.add(notification);
    }

    public void removeNotification(Notification notification){
        notifications.remove(notification);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setLastEnter(Time lastEnter) {
        this.lastEnter = lastEnter;
    }

    public boolean checkPassword(String password){
        return password.equals(this.password);
    }

}
