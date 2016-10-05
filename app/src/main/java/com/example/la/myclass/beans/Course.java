package com.example.la.myclass.beans;

import com.example.la.myclass.C;

import java.util.Date;

/**
 * Created by Léa on 27/09/2015.
 */
public class Course {

    //Statics
    public static final int FORESEEN = 0;
    public static final int VALIDATED = 1;
    public static final int WAITING_FOT_VALIDATION = 2;
    public static final int CANCELED = 3;
    public static final int ALL_COURSES = 3;
    public static final int DURATION_1H30 = 90;
    public static final int DURATION_1H = 60;
    public static final int DURATION_2H = 120;


    // Variables
    protected int id;
    protected int pupilID;
    protected long date;
    protected int duration;
    protected int state;
    protected double money;
    protected String theme;
    protected String memo;
    protected Pupil pupil;

    public Course(){}

    public Course(long date, int duration, int state, double money) {
        this.date = date;
        this.duration = duration;
        this.state = state;
        this.money = money;
    }

    public Course(long date, int duration, int state, double money, String theme, String memo) {
        this.date = date;
        this.duration = duration;
        this.state = state;
        this.money = money;
        this.theme = theme;
        this.memo = memo;
    }

    public Course(int id, int pupilID, long date, int duration, int state, double money, String theme, String memo) {
        this.id = id;
        this.pupilID = pupilID;
        this.date = date;
        this.duration = duration;
        this.state = state;
        this.money = money;
        this.theme = theme;
        this.memo = memo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPupilID() {
        return pupilID;
    }

    public void setPupilID(int pupilID) {
        this.pupilID = pupilID;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Pupil getPupil() {
        return pupil;
    }

    public void setPupil(Pupil pupil) {
        this.pupil = pupil;
    }

    public String getHoursSlot(){
        Date d = new Date(this.getDate());
        Date d2 = new Date((this.getDate() + this.getDuration() * C.MINUTE));
        return String.format( "%02dh%02d - %02dh%02d", d.getHours(), d.getMinutes(), d2.getHours(), d2.getMinutes());
    }

    public static String getStateLabel(int state){
        switch(state){
            case FORESEEN:
                return "A venir";
            case WAITING_FOT_VALIDATION:
                return "En attente";
            case CANCELED:
                return "Annulé";
            default:
                return "Validé";
        }
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", pupilID=" + pupilID +
                ", date=" + date +
                ", duration=" + duration +
                ", state=" + state +
                ", money=" + money +
                ", theme='" + theme + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}
