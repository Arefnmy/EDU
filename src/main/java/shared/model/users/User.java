package shared.model.users;

import org.codehaus.jackson.annotate.JsonIgnore;
import shared.model.Time;

public abstract class User{
    protected String collegeName;
    protected Time lastEnter;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String mobileNumber;
    protected String nationalCode;
    protected long userCode;
    protected byte[] image;

    protected User(){}
    protected User(String collegeName, Time lastEnter, String username, String password, String firstName,
                String lastName, String email, String mobileNumber, String nationalCode, long userCode) {
        this.collegeName = collegeName;
        this.lastEnter = lastEnter;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.nationalCode = nationalCode;
        this.userCode = userCode;
    }

    protected User(String firstName , String lastName , long userCode){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userCode = userCode;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public Time getLastEnter() {
        return lastEnter;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getName(){
        return firstName + " " + lastName;
    }

    public long getUserCode() {
        return userCode;
    }
}
