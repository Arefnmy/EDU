package client.gui.panels.studentpanel;

import client.controller.MainController;
import client.gui.AutoRefresh;
import client.gui.RowInformation;
import shared.model.Lesson;
import shared.model.Time;
import shared.response.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ChooseLessonPanel extends JPanel implements AutoRefresh {
    private final List<Integer> markedLessons;
    private List<Integer> lessonsTook;
    private List<Lesson> lessonList;
    private List<Lesson> allLessons;

    private JPanel sortPanel;
    private JToggleButton nameSortButton;
    private JToggleButton timeSortButton;
    private JToggleButton gradeSortButton;
    private JToggleButton ascending;
    private JComboBox<String> collegeComboVox;

    private JPanel lessonPanel;
    private JPanel markedLessonPanel;
    private RowInformation header;

    MainController mainController = MainController.getInstance();
    public ChooseLessonPanel(){
        initComp();

        Response response = mainController.getChooseLesson("Suggested");
        List<String> colleges = (List<String>) response.getData("collegeList");
        String[] collegesName = new String[colleges.size() + 1];
        collegesName[0] = "Suggested";
        for (int i = 1; i <= colleges.size() ; i++)
            collegesName[i] = colleges.get(i-1);
        collegeComboVox = new JComboBox<>(collegesName);
        markedLessons = mainController.getMarkedLessons();

        getResponse();
        setInfo();
        alignComp();
        setActionListener();
        setLoop(1);
    }

    public void initComp(){
        sortPanel = new JPanel();
        nameSortButton = new JToggleButton("Sort By Name");
        timeSortButton = new JToggleButton("Sort By Time");
        gradeSortButton = new JToggleButton("Sort By Grade");
        ascending = new JToggleButton("Ascending");

        lessonPanel = new JPanel();
        markedLessonPanel = new JPanel();
        header = new RowInformation(7 , true);
        header.addComponent(new JLabel("Name"));
        header.addComponent(new JLabel("Grade"));
        header.addComponent(new JLabel("Capacity"));
        header.addComponent(new JLabel("Final Time"));
        header.addComponent(new JLabel("Take/Remove"));
        header.addComponent(new JLabel("Take From Assistant/Change Group"));
        header.addComponent(new JLabel("Mark"));

    }

    public void alignComp(){
        sortPanel.add(ascending);
        sortPanel.add(nameSortButton);
        sortPanel.add(timeSortButton);
        sortPanel.add(gradeSortButton);
        sortPanel.add(collegeComboVox);

        add(sortPanel);
        add(lessonPanel);
        add(new JSeparator());
        add(markedLessonPanel);
    }

    public void setActionListener(){
        nameSortButton.addItemListener( e->{
            timeSortButton.setSelected(false);
            gradeSortButton.setSelected(false);
        });

        timeSortButton.addItemListener( e->{
            nameSortButton.setSelected(false);
            gradeSortButton.setSelected(false);
        });

        gradeSortButton.addItemListener( e->{
            timeSortButton.setSelected(false);
            nameSortButton.setSelected(false);
        });

        ascending.addItemListener(e->{
            if (e.getStateChange() == ItemEvent.SELECTED)
                ascending.setText("Descending");
            else if (e.getStateChange() == ItemEvent.DESELECTED)
                ascending.setText("Ascending");
        });
    }

    private RowInformation getRow(Lesson lesson){
        RowInformation rowInformation = new RowInformation(7 , false);
        rowInformation.addComponent(new JLabel(lesson.getName()));
        rowInformation.addComponent(new JLabel(lesson.getGrade().toString()));
        rowInformation.addComponent(new JLabel(String.valueOf(lesson.getCapacity())));
        rowInformation.addComponent(new JLabel(lesson.getTime().getFinalDateStr()+" at "+lesson.getTime().getFinalTimeStr()));
        if (lessonsTook.contains(lesson.getLessonNumber())){
            JButton remove = new JButton("Remove");
            remove.addActionListener( e-> {
                Response response = mainController.removeLessonFromChooseLesson(lesson.getLessonNumber());
                JOptionPane.showMessageDialog(null , response.getMessage());
            });
            rowInformation.addComponent(remove);
            JButton change = new JButton("Change Group");
            change.addActionListener( e-> { //todo
                JOptionPane.showMessageDialog(null , "There is no other group!");
            });
            rowInformation.addComponent(change);
        }else{
            JButton take = new JButton("Take");
            take.addActionListener( e-> {
                Response response = mainController.takeLesson(lesson.getLessonNumber());
                JOptionPane.showMessageDialog(null , response.getMessage());
            });
            rowInformation.addComponent(take);
            JButton takeFromAssistant = new JButton("Take From Assistant");
            takeFromAssistant.addActionListener( e-> {
                Response response = mainController.takeLessonFromAssistant(lesson.getLessonNumber());
                JOptionPane.showMessageDialog(null , response.getMessage());
            });
            rowInformation.addComponent(takeFromAssistant);
        }
        JCheckBox mark = new JCheckBox();
        mark.setSelected(markedLessons.contains(lesson.getLessonNumber()));
        mark.addActionListener( e->{
            if (mark.isSelected())
                markedLessons.add(lesson.getLessonNumber());
            else
                markedLessons.remove(Integer.valueOf(lesson.getLessonNumber()));
        });
        rowInformation.addComponent(mark);

        return rowInformation;
    }

    @Override
    public void setInfo() {
        lessonList.sort(ascending.isSelected() ? getComparator().reversed() : getComparator());

        lessonPanel.removeAll();
        markedLessonPanel.removeAll();
        lessonPanel.setLayout(new GridLayout(lessonList.size() + 1 , 1));
        lessonPanel.add(header);
        for (Lesson l : lessonList)
            lessonPanel.add(getRow(l));

        markedLessonPanel.setLayout(new GridLayout(markedLessons.size() , 1));
        for (Lesson l : allLessons){
            if (markedLessons.contains(l.getLessonNumber()))
                markedLessonPanel.add(getRow(l));
        }
        markedLessonPanel.setVisible(Objects.equals(collegeComboVox.getSelectedItem(), "Suggested"));

        mainController.refresh();
    }

    @Override
    public void getResponse() {
        mainController.setMarkedLessons(markedLessons);
        Response response = mainController.getChooseLesson((String) collegeComboVox.getSelectedItem());
        lessonsTook = (List<Integer>) response.getData("lessonsTook");
        lessonList = (List<Lesson>) response.getData("lessonList");
        allLessons = (List<Lesson>) response.getData("allLessons");
    }

    public Comparator<Lesson> getComparator(){
        return (l1 , l2) -> {
            if (nameSortButton.isSelected())
                return l1.getName().compareTo(l2.getName());
            if (timeSortButton.isSelected()) {
                Time t1 = new Time(2022, Month.of(l1.getTime().getFinalMonth()), l1.getTime().getFinalDayOfMonth(),
                        l1.getTime().getFinalHour(), 0, 0);
                Time t2 = new Time(2022, Month.of(l2.getTime().getFinalMonth()), l2.getTime().getFinalDayOfMonth(),
                        l2.getTime().getFinalHour(), 0, 0);
                return t1.compareTo(t2);
            }
            if (gradeSortButton.isSelected())
                return l1.getGrade().compareTo(l2.getGrade());

            return 0;
        };
    }
}
