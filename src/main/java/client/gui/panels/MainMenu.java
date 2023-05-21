package client.gui.panels;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.filemanager.ImageType;
import client.filemanager.ResourceManager;
import client.gui.MainFrame;
import client.gui.RowInformation;
import shared.model.users.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public abstract class MainMenu extends JPanel implements AutoRefresh {
    private final User user;
    protected JLabel offlineMode;
    protected JLabel time;
    protected JLabel image;
    protected JLabel name;
    protected JLabel email;
    protected RowInformation header1;
    protected RowInformation info1;

    protected JMenuBar menuBar;

    protected JMenu registrationMenu;
    protected JMenu educationalServicesMenu;
    protected JMenu reportAffairsMenu;
    protected JMenu profileMenu;

    protected JButton logout;
    protected JButton reconnect;
    protected JButton home;

    protected JMenuItem lessonsList;
    protected JMenuItem professorsList;
    protected JMenuItem userProfile;

    protected JMenuItem temporaryScores;
    protected JMenuItem educationalStatus;

    protected JMenuItem weeklySchedule;
    protected JMenuItem examList;

    protected JMenu messenger;
    protected JMenuItem createChat;
    protected JMenuItem chatroom;

    protected JMenu chooseLesson;
    protected JMenu notificationMenu;
    protected JMenuItem notification;

    protected JMenu cw;
    protected JMenuItem courseware;

    protected MainController mainController = MainController.getInstance();
    protected MainMenu(User user){
        this.user = user;
        setBackground(Color.WHITE);

        setLoop(ResourceManager.getInstance().getValue(Integer.class , "defaultPanel-fps" , 1));
    }

    public void initComp() {
        offlineMode = new JLabel(
                ResourceManager.getInstance().getGraphicConfig().getProperty("offlineMode-msg" , "Offline Mode"));
        reconnect = new JButton(ResourceManager.getInstance().getImage(ImageType.REFRESH));
        home = new JButton(ResourceManager.getInstance().getImage(ImageType.HOME));
        logout = new JButton("Logout");
        logout.setBackground(Color.WHITE);

        name = new JLabel(user.getName());
        email = new JLabel(user.getEmail());
        header1 = new RowInformation(2 , true);
        info1 = new RowInformation(2 , false);

        LocalDateTime entryTime = LocalDateTime.now();

        time = new JLabel("last enter : " + entryTime.getHour() +
                ":" + entryTime.getMinute() + ":" +
                entryTime.getSecond() + "  -  " +
                entryTime.getYear() + " " + entryTime.getMonth() + " " + entryTime.getDayOfMonth() + "th");
        time.setForeground(Color.RED);

        image = new JLabel(ResourceManager.getInstance().getImage(user.getImage()));

        menuBar = new JMenuBar();
        registrationMenu = new JMenu("Registration");
        educationalServicesMenu = new JMenu("Educational Services");
        reportAffairsMenu = new JMenu("Report Affairs");
        profileMenu = new JMenu("Profile");

        lessonsList = new JMenuItem("Lesson's List");
        professorsList = new JMenuItem("Professor's List");
        userProfile = new JMenuItem("User's Profile");

        temporaryScores = new JMenuItem("Temporary Scores");
        educationalStatus = new JMenuItem("Educational Status");

        weeklySchedule = new JMenuItem("Weekly Schedule");
        examList = new JMenuItem("Exam List");

        messenger = new JMenu("Messenger");
        createChat = new JMenuItem("Create Chat");
        chatroom = new JMenuItem("Chat Room");

        chooseLesson = new JMenu("Choose Lesson");
        notificationMenu = new JMenu("Notification");
        notification = new JMenuItem("Notifications");

        cw = new JMenu("CW");
        courseware = new JMenuItem("Courseware");
    }

    public void alignComp(){
        setLayout(null);
        registrationMenu.add(lessonsList);
        registrationMenu.add(professorsList);

        profileMenu.add(userProfile);
        cw.add(courseware);

        reportAffairsMenu.add(temporaryScores);
        reportAffairsMenu.add(educationalStatus);

        educationalServicesMenu.add(weeklySchedule);
        educationalServicesMenu.add(examList);

        messenger.add(chatroom);
        messenger.add(createChat);

        menuBar.add(home);
        menuBar.add(reconnect);
        menuBar.add(logout);
        menuBar.add(registrationMenu);
        menuBar.add(educationalServicesMenu);
        menuBar.add(reportAffairsMenu);
        menuBar.add(profileMenu);
        menuBar.add(messenger);
        menuBar.add(notificationMenu);
        menuBar.add(cw);

        mainController.setMenuBar(menuBar);
        notificationMenu.add(notification);

        header1.addComponent(new JLabel("Name :"));
        header1.addComponent(new JLabel("Email :"));
        info1.addComponent(name);
        info1.addComponent(email);
        JPanel panel = new JPanel(new GridLayout(2 , 1 , 0 , 5));
        panel.add(header1);
        panel.add(info1);
        panel.setBounds(100 , 400 , 400 , 100);
        add(panel);

        time.setBounds(MainFrame.FRAME_WIDTH - 400 , 20 , 300 , 50);
        add(time);
        image.setBounds(100 , 100 , 150 , 250);
        add(image);
        offlineMode.setBounds(10 , 10 , 200 , 20);

        if (mainController.isOnline())
            reconnect.setEnabled(false);
        else {
            add(offlineMode);
            registrationMenu.setEnabled(false);
            chooseLesson.setEnabled(false);
            notificationMenu.setEnabled(false);
            cw.setEnabled(false);
            reconnect.setEnabled(true);
        }
    }

    public void setActionListener(){
        logout.addActionListener(e -> {
            mainController.logout();
            mainController.stopLoop();
        });
        home.addActionListener( e-> mainController.changeContentPane(mainController.getMainMenu()));
        reconnect.addActionListener( e-> mainController.reconnect());

        weeklySchedule.addActionListener( e-> mainController.changeContentPane(new WeeklySchedulePanel()));
        examList.addActionListener( e-> mainController.changeContentPane(new ExamListPanel()));
        lessonsList.addActionListener( e-> mainController.changeContentPane(new LessonListPanel()));
        professorsList.addActionListener(e -> mainController.changeContentPane(new ProfessorListPanel()));
        chatroom.addActionListener( e-> mainController.changeContentPane(new ChatRoom()));
        createChat.addActionListener( e-> mainController.changeContentPane(new CreateChatPanel()));
        notification.addActionListener( e-> mainController.changeContentPane(new NotificationPanel()));
        courseware.addActionListener( e-> mainController.changeContentPane(new CoursewarePanel()));
    }
}
