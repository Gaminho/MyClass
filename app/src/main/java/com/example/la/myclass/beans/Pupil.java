package com.example.la.myclass.beans;

import com.example.la.myclass.C;

/**
 * Created by Léa on 27/09/2015.
 */
public class Pupil {

    public static final int SEXE_MALE = 0;
    public static final int SEXE_FEMALE = 1;

    public static final int REGULIAR = 0;
    public static final int OCCASIONALY = 1;
    public static final int TEMPORALY = 2;


    public static final int AGENCY = 0;
    public static final int BLACK = 1;

    protected int id;
    protected String name;
    // classe
    protected int level;
    // black / agence
    protected int type;
    protected int sex;
    protected int frequency;
    protected String adress;
    protected double price;
    protected long sinceDate;
    protected long tel1, tel2;
    //Image
    protected String imgPath;

    // Constructeurs
    public Pupil() {
        this.type = -1;
        this.frequency = -1;
        this.imgPath = "";
    }

    public Pupil(String name, int level, int type, int sex, int frequency, String adress, double price) {
        this.name = name;
        this.level = level;
        this.type = type;
        this.sex = sex;
        this.frequency = frequency;
        this.adress = adress;
        this.price = price;
    }

    public Pupil(String name, int level, int type, int sex, int frequency, String adress, double price, long sinceDate) {
        this.name = name;
        this.level = level;
        this.type = type;
        this.sex = sex;
        this.frequency = frequency;
        this.adress = adress;
        this.price = price;
        this.sinceDate = sinceDate;
    }

    public Pupil(String name, int level, int type, int sex, int frequency, String adress, double price, long sinceDate, long tel1, long tel2) {
        this.name = name;
        this.level = level;
        this.type = type;
        this.sex = sex;
        this.frequency = frequency;
        this.adress = adress;
        this.price = price;
        this.sinceDate = sinceDate;
        this.tel1 = tel1;
        this.tel2 = tel2;
    }

    public Pupil(String name, int level, int type, int sex, int frequency, String adress, double price, long sinceDate, long tel1, long tel2, String imgPath) {
        this.name = name;
        this.level = level;
        this.type = type;
        this.sex = sex;
        this.frequency = frequency;
        this.adress = adress;
        this.price = price;
        this.sinceDate = sinceDate;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.imgPath = imgPath;
    }

    // Getters & Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public long getTel1() {
        return tel1;
    }

    public void setTel1(long tel1) {
        this.tel1 = tel1;
    }

    public long getTel2() {
        return tel2;
    }

    public void setTel2(long tel2) {
        this.tel2 = tel2;
    }

    public long getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(long sinceDate) {
        this.sinceDate = sinceDate;
    }

    public String getName() {
        return name;
    }

    public String getFullName(){return name.replace(C.STRING_SEPARATOR," ");}

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // METHODES

    @Override
    public String toString() {
        return "Pupil{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", type=" + type +
                ", sex=" + sex +
                ", frequency=" + frequency +
                ", adress='" + adress + '\'' +
                ", price=" + price +
                ", sinceDate=" + sinceDate +
                ", tel1=" + tel1 +
                ", tel2=" + tel2 +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }


}
