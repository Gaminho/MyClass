package com.example.la.myclass.beans.periodic;

/**
 * Created by Gaminho on 26/09/2016.
 */
public class Year extends PeriodicItem {


    public Year(String label, double money, int nbOfCourses) {
        this.label = label;
        this.money = money;
        this.nbCourse = nbOfCourses;
    }

    // Utils

    @Override
    public String toString() {
        return "Year{" +
                "beginning=" + beginning +
                ", label='" + label + '\'' +
                ", money=" + money +
                ", nbOfCourses=" + nbCourse +
                '}';
    }
}
