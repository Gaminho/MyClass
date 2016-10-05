package com.example.la.myclass.beans;

/**
 * Created by Gaminho on 26/09/2016.
 */
public class Year {

    protected long beginning;
    protected String label;
    protected double money;
    protected int nbOfCourses;


    // Constructeurs

    public Year() {
    }

    public Year(String label, double money, int nbOfCourses) {
        this.label = label;
        this.money = money;
        this.nbOfCourses = nbOfCourses;
    }

    public Year(long beginning, String label, double money, int nbOfCourses) {
        this.beginning = beginning;
        this.label = label;
        this.money = money;
        this.nbOfCourses = nbOfCourses;
    }


    // Getters & Setters

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


    // Utils

    @Override
    public String toString() {
        return "Year{" +
                "beginning=" + beginning +
                ", label='" + label + '\'' +
                ", money=" + money +
                ", nbOfCourses=" + nbOfCourses +
                '}';
    }
}
