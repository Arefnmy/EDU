package shared.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

public class Time implements Comparable<Time> {
    private int year;
    private Month month;
    private int dayOfMonth;
    private int hour;
    private int minutes;
    private int second;

    public Time(){
        setTime(LocalDateTime.now());
    };

    public Time(int year, Month month, int dayOfMonth, int hour, int minutes, int second) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hour = hour;
        this.minutes = minutes;
        this.second = second;
    }

    public Time(LocalDateTime localDateTime){
        setTime(localDateTime);
    }

    public Time(Month month , int dayOfMonth , int hour){
        year = LocalDateTime.now().getYear();
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hour = hour;
        minutes = 0;
        second = 0;
    }

    public int getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSecond() {
        return second;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setTime(LocalDateTime localDateTime){
        year = localDateTime.getYear();
        month = localDateTime.getMonth();
        dayOfMonth = localDateTime.getDayOfMonth();
        hour = localDateTime.getHour();
        minutes = localDateTime.getMinute();
        second = localDateTime.getSecond();
    }

    @JsonIgnore
    public LocalDateTime getLocalDateTime(){
        return LocalDateTime.of(year , month , dayOfMonth , hour , minutes , second);
    }

    @JsonIgnore
    public Date getDate(){
        return Date.from(getLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public int compareTo(Time time) {
        if (year != time.getYear())
            return year - time.getYear();
        if (month != time.getMonth())
            return month.getValue() - time.getMonth().getValue();
        if (dayOfMonth != time.getDayOfMonth())
            return dayOfMonth - time.getDayOfMonth();
        if (hour != time.getHour())
            return hour - time.getHour();
        if (minutes != time.getMinutes())
            return minutes - time.getMinutes();

        return second - time.getSecond();
    }

    @Override
    public String toString() {
        return getLocalDateTime().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return year == time.year && dayOfMonth == time.dayOfMonth && hour == time.hour
                && minutes == time.minutes && second == time.second && month == time.month;
    }
}
