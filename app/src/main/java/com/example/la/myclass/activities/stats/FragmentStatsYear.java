package com.example.la.myclass.activities.stats;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Month;
import com.example.la.myclass.beans.Year;
import com.example.la.myclass.customviews.GraphicView;
import com.example.la.myclass.customviews.Podium;
import com.example.la.myclass.customviews.Podium2;
import com.example.la.myclass.database.CoursesBDD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Gaminho on 21/09/2016.
 */
public class FragmentStatsYear extends Fragment implements View.OnClickListener{

    // Views
    protected TextView mTVFirstCourse, mTVNbYears;
    protected Podium2 mPodium;
    protected GraphicView mGraphic;


    // Fragment life cycle
    public static FragmentStatsYear newInstance() {
        return new FragmentStatsYear();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats_year, container, false);
        getAllViews(view);
        fillViews();

        return view;
    }


    // Utils

    public void getAllViews(View view){
        mTVFirstCourse = (TextView) view.findViewById(R.id.firstCourse);
        mTVFirstCourse.setTypeface(null, Typeface.ITALIC);
        mTVNbYears = (TextView) view.findViewById(R.id.nbYears);
        mGraphic = (GraphicView) view.findViewById(R.id.graph);
        mPodium = (Podium2) view.findViewById(R.id.Pie2);
    }

    public void fillViews(){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> listCourse = coursesBDD.getAllCourses();
        List<Year> yearList = coursesBDD.getAllYears();
        mTVNbYears.setText(""+yearList.size());
        mTVFirstCourse.setText(C.formatDate(new Date().getTime(), C.DAY_DATE_D_MONTH_YEAR));


        if(listCourse != null) {
            Collections.reverse(listCourse);
            mTVFirstCourse.setText(C.formatDate(listCourse.get(0).getDate(), C.DAY_DATE_D_MONTH_YEAR));
        }

        // Classement années

        if(yearList.size() > 0) {
            fillBestYearss(yearList);
            mGraphic.setListYears(yearList);
        }

        coursesBDD.close();

    }

    public void fillBestYearss(List<Year> years){
        double bestMoney = 0;
        int nbCourse = 0;
        String monthLabel = "";

        // Numéro 1
        for(Year year : years){
            if (year.getMoney() > bestMoney) {
                bestMoney = year.getMoney();
                nbCourse = year.getNbOfCourses();
                monthLabel = year.getLabel();
            }
        }
        mPodium.addGoldItem(String.format("%.2f€", bestMoney), String.format("%d cours", nbCourse), monthLabel);

        if(years.size() == 1)
            return;

        double maxMoney = bestMoney;
        bestMoney = 0;

        // Numéro 2
        for(Year year : years){
            if (year.getMoney() > bestMoney && year.getMoney() < maxMoney){
                bestMoney = year.getMoney();
                nbCourse = year.getNbOfCourses();
                monthLabel = year.getLabel().replace(" ", "\n");
            }
        }
        mPodium.addSilverItem(String.format("%.2f€", bestMoney), String.format("%d cours", nbCourse), monthLabel);

        if(years.size() == 2)
            return;

        maxMoney = bestMoney; bestMoney = 0;

        // Numéro 3
        for(Year year : years){
            if (year.getMoney() > bestMoney && year.getMoney() < maxMoney){
                bestMoney = year.getMoney();
                nbCourse = year.getNbOfCourses();
                monthLabel = year.getLabel().replace(" ", "\n");
            }
        }
        mPodium.addBronzeItem(String.format("%.2f€", bestMoney), String.format("%d cours", nbCourse), monthLabel);

    }



    // Interface

    @Override
    public void onClick(View view) {
    }
}
