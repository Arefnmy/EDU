package server.logic;

import server.model.users.*;

public class MainMenuBossController extends MainMenuAssistantController{
private final CollegeBoss collegeBoss;

    public MainMenuBossController(CollegeBoss collegeBoss){
        super(collegeBoss);
        this.collegeBoss = collegeBoss;
    }

    public CollegeBoss getUser(){
        return collegeBoss;
    }

    public boolean editProfessor(long professorCode , shared.model.users.Professor newProfessor){
        Professor professor = userDatabase.getProfessor(professorCode);
        if (!newProfessor.getUsername().equals(professor.getUsername()) &&
                !userDatabase.usernameNotUsed(newProfessor.getUsername())) {
            logger.info("Edit professor failed : username is already taken, User code :" + collegeBoss.getUserCode()+
                    ",Professor code : " + professor.getUserCode());
            return false;
        }
        /*if (isSelectedAssistant && !(professor instanceof EducationalAssistant)) {
            logger.info("َApprove professor and Dismissal assistant, User code : " + collegeBoss.getUserCode() +
                    ",Professor code : " + professor.getUserCode());
            EducationalAssistant assistant = new EducationalAssistant(collegeBoss.getCollege() , professor.getUsername() ,
                    professor.getPassword() , professor.getFirstName() , professor.getLastName() , professor.getEmail(),
                    professor.getMobileNumber(),  professor.getNationalCode() , professor.getRoomNumber() , professor.getDegree());
                assistant.setProfessorCode(professor.getUserCode());
                assistant.setLastEnter(professor.getLastEnter());
            userDatabase.removeUser(professor);
            userDatabase.addUser(assistant);
        }*/

        professor.setUsername(newProfessor.getUsername());
        professor.setPassword(newProfessor.getPassword());
        professor.setRoomNumber(newProfessor.getRoomNumber());

        logger.info("Edit professor, User code : " + collegeBoss.getUserCode() +
                ",Professor code : " + professorCode);
        return true;
    }

    public void removeProfessor(long professor){
        logger.info("Remove professor, User code : " + collegeBoss.getUserCode() +
                ",Professor code : " + professor);
        Professor p = userDatabase.getProfessor(professor);
        userDatabase.removeUser(p);
    }
}
