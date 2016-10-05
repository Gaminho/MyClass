package com.example.la.myclass.beans;

import com.example.la.myclass.C;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gaminho on 21/09/2016.
 */
public class Month {

    protected long beginning;
    protected String label;
    protected double money;
    protected int nbOfCourses;

    public Month(int nbOfCourses, double money, long beginning) {
        this.beginning = beginning;
        this.money = money;
        this.nbOfCourses = nbOfCourses;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(beginning);
        this.label = C.formatDate(beginning, C.MONTH_YEAR);
    }



    public long getBeginning() {
        return beginning;
    }

    public void setBeginning(long beginning) {
        this.beginning = beginning;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getNbOfCourses() {
        return nbOfCourses;
    }

    public void setNbOfCourses(int nbOfCourses) {
        this.nbOfCourses = nbOfCourses;
    }


    @Override
    public String toString() {
        return "Month{" +
                "beginning=" + beginning +
                ", label='" + label + '\'' +
                ", money=" + money +
                ", nbOfCourses=" + nbOfCourses +
                '}';
    }
}
