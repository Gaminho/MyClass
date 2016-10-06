package com.example.la.myclass.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.beans.Month;
import com.example.la.myclass.beans.PeriodicItem;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.beans.Week;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.database.PupilsBDD;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by L�a on 27/09/2015.
 */
public class FragmentMoney extends Fragment {


    //Views
    protected View mRootView;
    protected TextView mTVTotal, mTVForeseen, mTVDone,mTVWaiting, mTVCanceled;
    protected TextView mTVNbTotal, mTVNbForeseen, mTVNbDone, mTVNbWaiting, mTVNbCanceled;


    // Fragment life cycle
    public static FragmentMoney newInstance() {
        return new FragmentMoney();
    }

    public FragmentMoney() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_money, container, false);
        getAllViews(mRootView);

        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> list = coursesBDD.getAllCourses();
        coursesBDD.close();

        double total = 0, foressen = 0, done = 0, waitingForValidation = 0, canceled = 0;
        int nbForeseen = 0, nbDone = 0, nbWaiting = 0, nbCanceled = 0;
        for (Course course : list) {
            total = total + course.getMoney();

            if (course.getState() == Course.VALIDATED) {
                done = done + course.getMoney();
                nbDone += 1;
            }

            else if(course.getState() == Course.FORESEEN) {
                foressen = foressen + course.getMoney();
                nbForeseen += 1;
            }

            else if(course.getState() == Course.WAITING_FOT_VALIDATION) {
                waitingForValidation = waitingForValidation + course.getMoney();
                nbWaiting += 1;
            }

            else if(course.getState() == Course.CANCELED){
                canceled = canceled + course.getMoney();
                total -= course.getMoney();
                nbCanceled += 1;
            }
        }

        Course firstCourse = list.get(list.size() - 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(firstCourse.getDate());

        mTVTotal.setText(String.format("%2.2f €", total));
        mTVForeseen.setText(String.format("%2.2f €", foressen));
        mTVDone.setText(String.format("%2.2f €", done));
        mTVWaiting.setText(String.format("%2.2f €", waitingForValidation));
        mTVCanceled.setText(String.format("%2.2f €", canceled));

        mTVNbForeseen.setText(String.format("(%d)", nbForeseen));
        mTVNbWaiting.setText(String.format("(%d)", nbWaiting));
        mTVNbDone.setText(String.format("(%d)", nbDone));
        mTVNbCanceled.setText(String.format("(%d)", nbCanceled));
        mTVNbTotal.setText(String.format("(%d)", list.size()));

        setBestPupilsView(getBestPupils());
        setBestMonthsView(getBestMonths());
        setBestWeeksView(getBestWeeks());

        return mRootView;
    }


    // Utils

    public void getAllViews(View view){
        mTVTotal = (TextView) view.findViewById(R.id.total);
        mTVDone = (TextView) view.findViewById(R.id.validated);
        mTVForeseen = (TextView) view.findViewById(R.id.foreseen);
        mTVWaiting = (TextView) view.findViewById(R.id.waitingForValidation);
        mTVCanceled = (TextView) view.findViewById(R.id.canceled);

        mTVNbWaiting = (TextView) view.findViewById(R.id.nbCoursesWaiting);
        mTVNbDone = (TextView) view.findViewById(R.id.nbCoursesValidated);
        mTVNbForeseen = (TextView) view.findViewById(R.id.nbCoursesForeseen);
        mTVNbTotal = (TextView) view.findViewById(R.id.nbCoursesTotal);
        mTVNbCanceled = (TextView) view.findViewById(R.id.nbCoursesCanceled);
    }

    public List<Item> getBestPupils(){
        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Pupil> list = pupilsBDD.getAllPupils();
        List<Item> itemList = new ArrayList<>();

        for(Pupil pupil : list) {
            int nbCourse = coursesBDD.getNbCoursesWithPupilID(pupil.getId());
            if(nbCourse > 0)
                itemList.add(new Item(pupil.getFullName(), pupilsBDD.getMoneyWithPupilID(pupil.getId()), nbCourse));
        }
        pupilsBDD.close();
        coursesBDD.close();

        Collections.sort(itemList, new Comparator<Item>() {
            public int compare(Item it1, Item it2) {
                if (it1.money == it2.money)
                    return 0;
                return it1.money > it2.money ? -1 : 1;
            }
        });

        return itemList;
    }

    public List<Item> getBestWeeks(){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<PeriodicItem> list = coursesBDD.getAllWeeks();
        coursesBDD.close();

        List<Item> itemList = new ArrayList<>();
        for(PeriodicItem week : list)
            itemList.add(new Item(week.getLabel(), week.getMoney(), week.getNbCourse()));

        Collections.sort(itemList, new Comparator<Item>() {
            public int compare(Item it1, Item it2) {
                if (it1.money == it2.money)
                    return 0;
                return it1.money > it2.money ? -1 : 1;
            }
        });

        return itemList;
    }

    public List<Item> getBestMonths(){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<PeriodicItem> list = coursesBDD.getAllMonths();
        coursesBDD.close();

        List<Item> itemList = new ArrayList<>();
        for(PeriodicItem month : list)
            itemList.add(new Item(month.getLabel(), month.getMoney(), month.getNbCourse()));

        Collections.sort(itemList, new Comparator<Item>() {
            public int compare(Item it1, Item it2) {
                if (it1.money == it2.money)
                    return 0;
                return it1.money > it2.money ? -1 : 1;
            }
        });

        return itemList;
    }

    public void setBestPupilsView(List<Item> list){
        if(list.size() > 0) {
            ((TextView) mRootView.findViewById(R.id.pupil1Name)).setText(list.get(0).label);
            ((TextView) mRootView.findViewById(R.id.pupil1Course)).setText(String.format("(%d)", list.get(0).nbCourse));
            ((TextView) mRootView.findViewById(R.id.pupil1Money)).setText(String.format("%.2f €", list.get(0).money));
            // FLOP
            ((TextView) mRootView.findViewById(R.id.flopPupilName)).setText(list.get(list.size()-1).label);
            ((TextView) mRootView.findViewById(R.id.flopPupilCourse)).setText(String.format("(%d)", list.get(list.size()-1).nbCourse));
            ((TextView) mRootView.findViewById(R.id.flopPupilMoney)).setText(String.format("%.2f €", list.get(list.size()-1).money));
        }
        if(list.size() > 1) {
            ((TextView) mRootView.findViewById(R.id.pupil2Name)).setText(list.get(1).label);
            ((TextView) mRootView.findViewById(R.id.pupil2Course)).setText(String.format("(%d)", list.get(1).nbCourse));
            ((TextView) mRootView.findViewById(R.id.pupil2Money)).setText(String.format("%.2f €", list.get(1).money));
        }
        if(list.size() > 2) {
            ((TextView) mRootView.findViewById(R.id.pupil3Name)).setText(list.get(2).label);
            ((TextView) mRootView.findViewById(R.id.pupil3Course)).setText(String.format("(%d)", list.get(2).nbCourse));
            ((TextView) mRootView.findViewById(R.id.pupil3Money)).setText(String.format("%.2f €", list.get(2).money));
        }
    }

    public void setBestWeeksView(List<Item> list){
        if(list.size() > 0) {
            ((TextView) mRootView.findViewById(R.id.week1Label)).setText(list.get(0).label);
            ((TextView) mRootView.findViewById(R.id.week1Course)).setText(String.format("(%d)", list.get(0).nbCourse));
            ((TextView) mRootView.findViewById(R.id.week1Money)).setText(String.format("%.2f €", list.get(0).money));
            // FLOP
            ((TextView) mRootView.findViewById(R.id.flopWeekLabel)).setText(list.get(list.size()-1).label);
            ((TextView) mRootView.findViewById(R.id.flopWeekCourse)).setText(String.format("(%d)", list.get(list.size()-1).nbCourse));
            ((TextView) mRootView.findViewById(R.id.flopWeekMoney)).setText(String.format("%.2f €", list.get(list.size()-1).money));
        }
        if(list.size() > 1) {
            ((TextView) mRootView.findViewById(R.id.week2Label)).setText(list.get(1).label);
            ((TextView) mRootView.findViewById(R.id.week2Course)).setText(String.format("(%d)", list.get(1).nbCourse));
            ((TextView) mRootView.findViewById(R.id.week2Money)).setText(String.format("%.2f €", list.get(1).money));
        }
        if(list.size() > 2) {
            ((TextView) mRootView.findViewById(R.id.week3Label)).setText(list.get(2).label);
            ((TextView) mRootView.findViewById(R.id.week3Course)).setText(String.format("(%d)", list.get(2).nbCourse));
            ((TextView) mRootView.findViewById(R.id.week3Money)).setText(String.format("%.2f €", list.get(2).money));
        }
    }

    public void setBestMonthsView(List<Item> list){
        if(list.size() > 0) {
            ((TextView) mRootView.findViewById(R.id.month1Label)).setText(list.get(0).label);
            ((TextView) mRootView.findViewById(R.id.month1Course)).setText(String.format("(%d)", list.get(0).nbCourse));
            ((TextView) mRootView.findViewById(R.id.month1Money)).setText(String.format("%.2f €", list.get(0).money));
            // FLOP
            ((TextView) mRootView.findViewById(R.id.flopMonthLabel)).setText(list.get(list.size()-1).label);
            ((TextView) mRootView.findViewById(R.id.flopMonthCourse)).setText(String.format("(%d)", list.get(list.size()-1).nbCourse));
            ((TextView) mRootView.findViewById(R.id.flopMonthMoney)).setText(String.format("%.2f €", list.get(list.size()-1).money));
        }
        if(list.size() > 1) {
            ((TextView) mRootView.findViewById(R.id.month2Label)).setText(list.get(1).label);
            ((TextView) mRootView.findViewById(R.id.month2Course)).setText(String.format("(%d)", list.get(1).nbCourse));
            ((TextView) mRootView.findViewById(R.id.month2Money)).setText(String.format("%.2f €", list.get(1).money));
        }
        if(list.size() > 2) {
            ((TextView) mRootView.findViewById(R.id.month3Label)).setText(list.get(2).label);
            ((TextView) mRootView.findViewById(R.id.month3Course)).setText(String.format("(%d)", list.get(2).nbCourse));
            ((TextView) mRootView.findViewById(R.id.month3Money)).setText(String.format("%.2f €", list.get(2).money));
        }
    }

    private class Item{
        String label;
        int nbCourse;
        double money;

        public Item(String label, double money, int nbCourse) {
            this.label = label;
            this.money = money;
            this.nbCourse = nbCourse;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "label='" + label + '\'' +
                    ", nbCourse=" + nbCourse +
                    ", money=" + money +
                    '}';
        }
    }
}
