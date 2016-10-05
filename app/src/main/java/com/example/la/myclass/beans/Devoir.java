package com.example.la.myclass.beans;

/**
 * Created by Léa on 02/10/2015.
 */
public class Devoir {

    // Static
    public static final int STATE_PREPARATING = 0;
    public static final int STATE_DONE = 1;
    public static final int ALL_DEVOIRS = 2;

    protected int id;
    protected int pupilID;
    protected long date;
    protected double note;
    protected String theme;
    protected String commentaire;
    protected int state;
    protected Pupil pupil;

    public Devoir() {
    }

    public Devoir(long date, double note, String theme, String commentaire, int state) {
        this.date = date;
        this.note = note;
        this.theme = theme;
        this.commentaire = commentaire;
        this.state = state;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Pupil getPupil() {
        return pupil;
    }

    public void setPupil(Pupil pupil) {
        this.pupil = pupil;
    }

    public static String getStateLabel(int state){
        switch(state){
            case STATE_PREPARATING:
                return "En préparation";
            case STATE_DONE:
                return "Terminé";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "Devoir{" +
                "id=" + id +
                ", pupilID=" + pupilID +
                ", date=" + date +
                ", note=" + note +
                ", theme='" + theme + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", state=" + state +
                '}';
    }
}
