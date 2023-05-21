package server.model.users;

import server.model.College;

public class Mohseni extends User{
    private final long userCode = 400000000L;
    public Mohseni(College college, String username, String password, String firstName, String lastName, String email,
                   String mobileNumber, String nationalCode) {
        super(college, username, password, firstName, lastName, email, mobileNumber, nationalCode);
    }

    @Override
    public long getUserCode() {
        return userCode;
    }
}
