package com.example.la.myclass.beans.periodic;

import com.example.la.myclass.beans.Course;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Léa on 03/10/2015.
 */
public class Week extends PeriodicItem{

    static final String FORMAT = "%02d/%02d/%02d";
    public static final int MOD_NEXT = 0;
    public static final int MOD_LAST = 1;


    public Week() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        this.beginning = calendar.getTimeInMillis();
        this.label = String.format(FORMAT, calendar.get(Calendar.DAY_OF_MONTH), (calendar.get(Calendar.MONTH)+1), (calendar.get(Calendar.YEAR) - 2000));


        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        this.ending = calendar.getTimeInMillis();
        this.label = this.label + " - " + String.format(FORMAT, calendar.get(Calendar.DAY_OF_MONTH), (calendar.get(Calendar.MONTH)+1), (calendar.get(Calendar.YEAR) - 2000));
    }

    public Week(int nbCourse, double money, long beginning){
        this.beginning = beginning;
        this.nbCourse = nbCourse;
        this.money = money;

        Calendar calendar = Calendar.getInstance();

        this.beginning = beginning;
        calendar.setTime(new Date(beginning));
        this.label = String.format(FORMAT, calendar.get(Calendar.DAY_OF_MONTH), (calendar.get(Calendar.MONTH)+1), (calendar.get(Calendar.YEAR) - 2000));

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        this.ending = calendar.getTimeInMillis();
        this.label = this.label + " - " + String.format(FORMAT, calendar.get(Calendar.DAY_OF_MONTH), (calendar.get(Calendar.MONTH)+1), (calendar.get(Calendar.YEAR) - 2000));
    }

    public Week(long beginning){

        Calendar calendar = Calendar.getInstance();

        this.beginning = beginning;
        calendar.setTime(new Date(beginning));
        this.label = String.format(FORMAT, calendar.get(Calendar.DAY_OF_MONTH), (calendar.get(Calendar.MONTH)+1), (calendar.get(Calendar.YEAR) - 2000));

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        this.ending = calendar.getTimeInMillis();
        this.label = this.label + " - " + String.format(FORMAT, calendar.get(Calendar.DAY_OF_MONTH), (calendar.get(Calendar.MONTH)+1), (calendar.get(Calendar.YEAR) - 2000));
    }

    public long getBeginning() {
        return beginning;
    }

    public void setBeginning(long beginning) {
        this.beginning = beginning;
    }

    public long getEnding() {
        return ending;
    }

    public void setEnding(long ending) {
        this.ending = ending;
    }


    public String toString(){
        return "Label : " + this.label
                +"\nDébut : " + new Date(beginning).toString()
                + "\nFin : " + new Date(ending).toString()
                + "\nArgent : " + money
                + "\nCours : " + nbCourse;
    }




    // Utils
    public Week getWeekWithOffset(Week week, int mod){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(week.getBeginning()));

        if(mod == MOD_LAST)
            calendar.add(Calendar.DAY_OF_YEAR, -7);

        else if (mod == MOD_NEXT)
            calendar.add(Calendar.DAY_OF_YEAR, 7);


        return new Week(calendar.getTimeInMillis());
    }

    public boolean isEmpty(List<Course> list){

        for (Course course : list)
            if(course.getDate() > this.beginning && course.getDate() < this.ending)
                return false;

        return true;
    }

    public boolean isTheOldest(List<Course> list){

        Course course = list.get(list.size() - 1);
        if(course.getDate() > this.ending)
            return true;

        return false;
    }

    public boolean isTheMostRecent(List<Course> list){

        Course course = list.get(0);
        if(course.getDate() < this.beginning)
            return true;

        return false;
    }


}
