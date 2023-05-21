package client.gui.panels.professorpanel;

import client.gui.panels.MainMenu;
import shared.model.users.EducationalAssistant;
import shared.model.users.Professor;

import javax.swing.*;

public class MainMenuProfessor extends MainMenu {
    private Professor professor;
    private JMenuItem recommendation;

    public MainMenuProfessor(Professor professor) {
        super(professor);
        this.professor = professor;
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setActionListener();
    }

    @Override
    public void initComp() {
        super.initComp();

        name = new JLabel(professor.getName());
        email = new JLabel(professor.getEmail());

        recommendation = new JMenuItem("Recommendation");
    }

    @Override
    public void alignComp(){
        super.alignComp();

        educationalServicesMenu.add(recommendation);

        if (!(professor instanceof EducationalAssistant))
            educationalStatus.setVisible(false);
    }

    @Override
    public void setActionListener() {
        super.setActionListener();
        userProfile.addActionListener(e -> mainController.changeContentPane(new ProfilePanelProfessor(professor)));
        recommendation.addActionListener(e -> mainController.changeContentPane(new RequestPanel()));
        if (!(professor instanceof EducationalAssistant))
            temporaryScores.addActionListener(e -> mainController.changeContentPane(new TemporaryScoresProfessorPanel()));
    }

    @Override
    public void setInfo() {
        mainController.refresh();
    }

    @Override
    public void getResponse() {
        professor = (Professor) mainController.getUser();
    }
}
