package client.gui.panels.assistantpanel;

import client.gui.panels.professorpanel.MainMenuProfessor;
import client.gui.panels.professorpanel.RequestPanel;
import shared.model.users.EducationalAssistant;

import javax.swing.*;

public class MainMenuAssistant extends MainMenuProfessor {
    private final EducationalAssistant educationalAssistant;

    private JMenu userRegistrationMenu;
    private JMenuItem studentRegistration;
    private JMenuItem professorRegistration;

    private JMenuItem cancel;
    private JMenuItem minor;

    protected JMenuItem chooseLessonTime;

    public MainMenuAssistant(EducationalAssistant educationalAssistant) {
        super(educationalAssistant);
        this.educationalAssistant = educationalAssistant;
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setActionListener();
    }

    @Override
    public void initComp() {
        super.initComp();

        userRegistrationMenu = new JMenu("User Registration");
        studentRegistration = new JMenuItem("Student Registration");
        professorRegistration = new JMenuItem("Professor Registration");

        minor = new JMenuItem("Minor");
        cancel = new JMenuItem("Cancel");

        chooseLessonTime = new JMenuItem("Time Determination");
    }

    @Override
    public void alignComp() {
        super.alignComp();

        userRegistrationMenu.add(studentRegistration);
        userRegistrationMenu.add(professorRegistration);
        menuBar.add(userRegistrationMenu);

        educationalServicesMenu.add(minor);
        educationalServicesMenu.add(cancel);

        menuBar.add(chooseLesson);
        chooseLesson.add(chooseLessonTime);
    }

    @Override
    public void setActionListener() {
        super.setActionListener();
        studentRegistration.addActionListener( e-> mainController.changeContentPane(new AddStudentPanel()));
        professorRegistration.addActionListener( e-> mainController.changeContentPane(new AddProfessorPanel()));

        cancel.addActionListener( e-> mainController.changeContentPane(new RequestPanel()));
        minor.addActionListener( e-> mainController.changeContentPane(new RequestPanel()));

        chooseLessonTime.addActionListener( e-> mainController.changeContentPane(new ChooseLessonTime()));
        temporaryScores.addActionListener( e-> mainController.changeContentPane(new TemporaryScoresAssistantPanel()));
        educationalStatus.addActionListener( e-> mainController.changeContentPane(new EducationalStatusPanel()));
    }
}
