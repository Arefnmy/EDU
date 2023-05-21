package shared.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

public class LessonTime {
    private List<DayOfWeek> dayOfWeeks;
    private int startHour;
    private int endHour;

    //todo Time finalTime
    private int finalDayOfMonth;
    private int finalMonth;
    private int finalHour;

    public LessonTime(){};
    public LessonTime(List<DayOfWeek> dayOfWeeks, int startHour, int endHour,
                      int finalDayOfMonth, int finalMonth, int finalHour) {
        this.dayOfWeeks = dayOfWeeks;
        this.startHour = startHour;
        this.endHour = endHour;
        this.finalDayOfMonth = finalDayOfMonth;
        this.finalMonth = finalMonth;
        this.finalHour = finalHour;
    }

    public List<DayOfWeek> getDayOfWeeks() {
        return dayOfWeeks;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getFinalDayOfMonth() {
        return finalDayOfMonth;
    }

    public int getFinalMonth() {
        return finalMonth;
    }

    public int getFinalHour() {
        return finalHour;
    }

    @JsonIgnore
    public Time getFinalTime(){
        return new Time(Month.of(finalMonth) , finalDayOfMonth , finalHour);
    }

    @JsonIgnore
    public String getFinalDateStr(){
        String result = "";
        if (finalMonth < 10)
            result += 0;
        result += finalMonth + "/";
        if (finalDayOfMonth < 10)
            result += 0;
        result += finalDayOfMonth;

        return result;
    }

    @JsonIgnore
    public String getFinalTimeStr(){
        String result = "";
        if (finalHour < 10)
            result += 0;
        result += finalHour + ":";
        result += "00";
        return result;
    }

    @JsonIgnore
    public String getTimeInWeek(){
        String result ="";
        if (startHour < 10)
            result += 0;
        result += startHour + "-";
        if (endHour < 10)
            result += 0;
        result += endHour;
        return result;
    }

    @JsonIgnore
    public boolean isSooner(LessonTime time){
        if (finalMonth != time.getFinalMonth())
            return finalMonth < time.finalMonth;
        if (finalDayOfMonth != time.getFinalDayOfMonth())
            return finalDayOfMonth < time.finalDayOfMonth;
        return finalHour < time.finalHour;
    }

    @JsonIgnore
    public boolean isFinalInterference(LessonTime time){
        return finalDayOfMonth == time.getFinalDayOfMonth() && finalMonth == time.finalMonth &&
                Math.abs(finalHour - time.getFinalHour()) < 2;
    }

    @JsonIgnore
    public boolean isClassTimeInterference(LessonTime time){
        for (DayOfWeek d : dayOfWeeks){
            if (time.getDayOfWeeks().contains(d)){
                if (startHour > time.getStartHour()) {
                    if (endHour < time.getStartHour())
                        return true;
                }
                else{
                    if (endHour > time.getEndHour())
                        return true;
                }
            }
        }
        return false;
    }

    public void setFinalDayOfMonth(int finalDayOfMonth) {
        this.finalDayOfMonth = finalDayOfMonth;
    }

    public void setFinalMonth(int finalMonth) {
        this.finalMonth = finalMonth;
    }

    public void setFinalHour(int finalHour) {
        this.finalHour = finalHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
}
