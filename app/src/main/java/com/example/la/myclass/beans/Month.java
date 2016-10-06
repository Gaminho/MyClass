package com.example.la.myclass.beans;

import com.example.la.myclass.C;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gaminho on 21/09/2016.
 */
public class Month  extends PeriodicItem {

    protected long beginning;

    public Month(int nbOfCourses, double money, long beginning) {
        this.beginning = beginning;
        this.money = money;
        this.nbCourse = nbOfCourses;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(beginning);
        this.label = C.formatDate(beginning, C.MONTH_YEAR);
    }

    @Override
    public String toString() {
        return "Month{" +
                "beginning=" + beginning +
                ", label='" + label + '\'' +
                ", money=" + money +
                ", nbOfCourses=" + nbCourse +
                '}';
    }
}
