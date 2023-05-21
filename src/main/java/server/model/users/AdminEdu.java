package server.model.users;

import server.model.College;

public class AdminEdu extends User{
    private static int codeCounter = 1;
    private final long userCode;

    public AdminEdu(College college, String username, String password, String firstName, String lastName, String email,
                    String mobileNumber, String nationalCode) {
        super(college, username, password, firstName, lastName, email, mobileNumber, nationalCode);
        userCode = codeCounter ++;
    }

    public static void codeUpdate(){
        codeCounter ++;
    }
    @Override
    public long getUserCode() {
        return userCode;
    }
}
