package com.example.la.myclass.beans;

/**
 * Created by Gaminho on 26/09/2016.
 */
public class Year extends PeriodicItem {

    protected long beginning;


    // Constructeurs

    public Year() {
    }

    public Year(String label, double money, int nbOfCourses) {
        this.label = label;
        this.money = money;
        this.nbCourse = nbOfCourses;
    }

    public Year(long beginning, String label, double money, int nbOfCourses) {
        this.beginning = beginning;
        this.label = label;
        this.money = money;
        this.nbCourse = nbOfCourses;
    }


    // Getters & Setters

    public long getBeginning() {
        return beginning;
    }

    public void setBeginning(long beginning) {
        this.beginning = beginning;
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
