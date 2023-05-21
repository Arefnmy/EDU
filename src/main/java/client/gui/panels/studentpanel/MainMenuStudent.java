package client.gui.panels.studentpanel;

import client.gui.RowInformation;
import client.gui.panels.*;
import shared.model.Time;
import shared.model.users.Student;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class MainMenuStudent extends MainMenu {
    private Student student;

    private JLabel statusLabel;
    private JLabel supervisorLabel;
    private JLabel registrationLicenseLabel;
    private JLabel registrationTimeLabel;
    private RowInformation header2;
    private RowInformation info2;

    private JMenuItem recommendation;
    private JMenuItem busyStudying;
    private JMenuItem cancel;
    private JMenuItem minor;
    private JMenuItem dorm;
    private JMenuItem defence;

    private JMenuItem chooseLessonItem;

    public MainMenuStudent(Student student){
        super(student);
        this.student =  student;
        initComp();
        getResponse();
        setInfo();
        alignComp();
        setActionListener();
    }

    @Override
    public void initComp() {
        super.initComp();

        statusLabel = new JLabel(student.getStatus().toString());
        supervisorLabel = new JLabel("Unknown");
        if(student.getSupervisor() != null)
            supervisorLabel.setText(student.getSupervisor());
        registrationLicenseLabel = new JLabel("Allowed to register");
        registrationTimeLabel = new JLabel("1401/04/01 at 8 AM");

        header2 = new RowInformation(4 , true);
        info2 = new RowInformation(4 , false);

        recommendation = new JMenuItem("Recommendation");
        busyStudying = new JMenuItem("Busy Studying");
        minor = new JMenuItem("Minor");
        cancel = new JMenuItem("Cancel");
        dorm = new JMenuItem("Dorm");
        defence = new JMenuItem("Defence");

        chooseLessonItem = new JMenuItem("Lesson List");
    }

    @Override
    public void alignComp(){
        super.alignComp();

        header2.addComponent(new JLabel("Educational State :"));
        header2.addComponent(new JLabel("Supervisor :"));
        header2.addComponent(new JLabel("Registration License :"));
        header2.addComponent(new JLabel("Registration time :"));
        info2.addComponent(statusLabel);
        info2.addComponent(supervisorLabel);
        info2.addComponent(registrationLicenseLabel);
        info2.addComponent(registrationTimeLabel);
        JPanel panel = new JPanel(new GridLayout(2 , 1 , 0 , 5));
        panel.add(header2);
        panel.add(info2);

        educationalServicesMenu.add(recommendation);
        educationalServicesMenu.add(busyStudying);
        educationalServicesMenu.add(minor);
        educationalServicesMenu.add(cancel);
        educationalServicesMenu.add(dorm);
        educationalServicesMenu.add(defence);

        menuBar.add(chooseLesson);
        chooseLesson.add(chooseLessonItem);
        chooseLesson.setVisible(false);

        switch (student.getGrade()){
            case BACHELOR:
                dorm.setVisible(false);
                defence.setVisible(false);
                break;
            case MASTER:
                minor.setVisible(false);
                defence.setVisible(false);
                break;
            case DOCTORATE:
                recommendation.setVisible(false);
                minor.setVisible(false);
                dorm.setVisible(false);
        }

        panel.setBounds(100 , 500 , 800 , 100);
        add(panel);
    }

    @Override
    public void setActionListener() {
        super.setActionListener();

        userProfile.addActionListener( e-> mainController.changeContentPane(new ProfilePanelStudent(student)));
        recommendation.addActionListener( e-> mainController.changeContentPane(new RecommendationPanel()));
        dorm.addActionListener( e-> mainController.changeContentPane(new DormPanel()));
        busyStudying.addActionListener( e-> mainController.changeContentPane(new BusyStudyingPanel()));
        cancel.addActionListener( e-> mainController.changeContentPane(new CancelPanel()));
        minor.addActionListener( e-> mainController.changeContentPane(new MinorPanel()));
        defence.addActionListener( e-> mainController.changeContentPane(new DefencePanel()));
        chooseLessonItem.addActionListener( e-> mainController.changeContentPane(new ChooseLessonPanel()));
        educationalStatus.addActionListener( e-> mainController.changeContentPane(new EducationalStatusPanel()));
        temporaryScores.addActionListener( e-> mainController.changeContentPane(new TemporaryScoresStudentPanel()));
    }

    @Override
    public void setInfo() {
         if (student.getChooseLessonTime() != null &&
                 student.getChooseLessonTime().compareTo(new Time(LocalDateTime.now())) <= 0){
             if (!chooseLesson.isVisible())
                 chooseLesson.setVisible(true);
         }
         else{
             if (chooseLesson.isVisible())
                 chooseLesson.setVisible(false);
         }

         mainController.refresh();
    }

    @Override
    public void getResponse() {
        student = (Student) mainController.getUser();
    }
}
