package server.controllers;

import server.database.UserDatabase;
import server.filemanager.ResourceManager;
import server.logic.*;
import server.model.users.*;
import shared.model.EducationalStatus;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import shared.model.LoginState;
import shared.model.Time;
import shared.request.Request;
import shared.response.Response;
import shared.response.ResponseState;

import java.time.Duration;
import java.time.LocalDateTime;

public class LoginController {
    private User user;
    private LoginState loginState;

    UserDatabase database = UserDatabase.getInstance();
    Logger logger = LogManager.getLogger(LoginController.class);

    private void login(String username , String password){
        for(User u : database.getUsers()){
            if (u.getUsername().equals(username)) {
                user = u;
                if (u.checkPassword(password)) {
                    if (Duration.between(u.getLastEnter().getLocalDateTime(), LocalDateTime.now()).toHours() >= 3) {
                        logger.info("User login failed : Reset password, User code : " + user.getUserCode());
                        loginState = LoginState.RESET_PASSWORD;
                        return;
                    }

                    if (u instanceof Student) {
                        Student student = (Student) u;
                        if (student.getStatus() == EducationalStatus.DROPPING_OUT) {
                            logger.info("User login failed : Dropping out state, User code : " + user.getUserCode());
                            loginState = LoginState.DROPPING_OUT;
                            return;
                        }
                    }

                    logger.info("User login, User code : " + user.getUserCode());
                    user.setLastEnter(new Time());
                    loginState = LoginState.CAN_LOGIN;
                } else {
                    logger.info("User login failed : Password is wrong, User code : " + user.getUserCode());
                    loginState = LoginState.PASSWORD_IS_WRONG;
                }
                return;
            }
        }
        logger.info("User login failed : Username not found, Username : " + username);
        loginState = LoginState.USERNAME_NOT_FOUND;
    }

    public Response login(Request request) {
        String username = (String) request.getData("username");
        String password = (String) request.getData("password");
        login(username , password);
        Response response = null;
        switch (loginState) {
            case USERNAME_NOT_FOUND:
                response = new Response(ResponseState.ERROR,
                        ResourceManager.getInstance().getLogicConfig().getProperty("usernameNotFound-err"));
                break;
            case PASSWORD_IS_WRONG:
                response = new Response(ResponseState.ERROR,
                        ResourceManager.getInstance().getLogicConfig().getProperty("wrongPassword-err"));
                break;
            case DROPPING_OUT:
                response = new Response(ResponseState.ERROR,
                        ResourceManager.getInstance().getLogicConfig().getProperty("droppingOut-err"));
                break;
            case RESET_PASSWORD:
                response = new Response(ResponseState.ERROR,
                        ResourceManager.getInstance().getLogicConfig().getProperty("resetPassword-err"));
                break;
            case CAN_LOGIN:
                response = new Response(ResponseState.OK, "Done!");
        }
        response.addData("loginState" , loginState);
        return response;
    }

    public User getUser(){
        return user;
    }

    public MainMenuController getMainMenuController(){
        if (user instanceof Student)
            return new MainMenuStudentController((Student) user);
        else if (user instanceof CollegeBoss)
            return new MainMenuBossController((CollegeBoss) user);
        else if (user instanceof EducationalAssistant)
            return new MainMenuAssistantController((EducationalAssistant) user);
        else if (user instanceof Professor)
            return new MainMenuProfessorController((Professor) user);
        else if (user instanceof AdminEdu)
            return new MainMenuAdminController(user);
        else if (user instanceof Mohseni)
            return new MainMenuMohseniController(user);
        return null;
    }

    public Response changePassword(Request request){
        String oldPassword = (String) request.getData("oldPassword");
        String newPassword = (String) request.getData("newPassword");
        if (user.checkPassword(oldPassword)) {
            user.setPassword(newPassword);
            user.setLastEnter(new Time(LocalDateTime.MAX));
            logger.info("User changed password, User code : " + user.getUserCode());
            return new Response(ResponseState.OK , "Done!");
        }
        logger.info("User change password failed, User code : " + user.getUserCode());
        return new Response(ResponseState.ERROR , "Old password is wrong!");
    }
}
