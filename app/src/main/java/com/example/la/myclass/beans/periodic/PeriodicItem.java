package com.example.la.myclass.beans.periodic;

/**
 * Created by ariche on 06/10/2016.
 */

public class PeriodicItem {

    /**
     * Variables de classe
     */
    protected String label;
    protected int nbCourse;
    protected double money;

    /**
     * Constructeurs
     */
    public PeriodicItem() {}
    public PeriodicItem(String label, int nbCourse, double money) {
        this.label = label;
        this.nbCourse = nbCourse;
        this.money = money;
    }

    /**
     * Getters & Setters
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getNbCourse() {
        return nbCourse;
    }

    public void setNbCourse(int nbCourse) {
        this.nbCourse = nbCourse;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    /**
     * Utils
     */
    @Override
    public String toString() {
        return "Item{" +
                "label='" + label + '\'' +
                ", nbCourse=" + nbCourse +
                ", money=" + money +
                '}';
    }

}
