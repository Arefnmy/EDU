package client.gui;

import shared.model.Time;

import javax.swing.*;
import java.awt.*;
import java.time.Month;

public class TimePanel extends JPanel {
    private final JSpinner year;
    private final JSpinner month;
    private final JSpinner day;
    private final JSpinner hour;
    private final JSpinner minuets;

    public TimePanel(Time defaultTime){
        setLayout(new FlowLayout());
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(defaultTime == null ? 2022 : defaultTime.getYear() ,
                1900 , 2100 , 1);
        year = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(defaultTime == null ? 3 : defaultTime.getMonth().getValue() ,
                1 , 12 , 1);
        month = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(defaultTime == null ? 1 : defaultTime.getDayOfMonth() ,
                1 , 31 , 1);
        day = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(defaultTime == null ? 7 : defaultTime.getHour() ,
                0 , 23 , 1);
        hour = new JSpinner(spinnerNumberModel);
        spinnerNumberModel = new SpinnerNumberModel(defaultTime == null ? 0 : defaultTime.getMinutes() ,
                0 , 59 , 1);
        minuets = new JSpinner(spinnerNumberModel);

        add(year);
        add(new JLabel("/"));
        add(month);
        add(new JLabel("/"));
        add(day);
        add(new JLabel("-"));
        add(hour);
        add(new JLabel(":"));
        add(minuets);
    }

    public Time getTime(){
        return new Time((int) year.getValue(), Month.of((int) month.getValue()) , (int)day.getValue() ,
                (int)hour.getValue() , (int) minuets.getValue(), 0);
    }
}
