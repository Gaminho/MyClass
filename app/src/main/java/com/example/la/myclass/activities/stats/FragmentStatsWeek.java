package com.example.la.myclass.activities.stats;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.activities.course.ActivityCourse;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Month;
import com.example.la.myclass.beans.Week;
import com.example.la.myclass.customviews.GraphicView;
import com.example.la.myclass.customviews.Podium2;
import com.example.la.myclass.database.CoursesBDD;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Gaminho on 21/09/2016.
 */
public class FragmentStatsWeek extends Fragment implements View.OnClickListener{

    // Static

    // Views
    protected TextView mTVFirstCourse, mTVNbWeeks;
    protected TextView mTVMeanItem;
    protected Podium2 mPodium;
    protected GraphicView mGraphic;

    // Class
    protected List<Week> mListWeek;


    // Fragment life cycle

    public static FragmentStatsWeek newInstance() {
        return new FragmentStatsWeek();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        mListWeek = coursesBDD.getAllWeeks();
        coursesBDD.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats_week, container, false);
        getAllViews(view);
        fillViews();

        return view;
    }


    // Utils

    public void getAllViews(View view){
        mTVFirstCourse = (TextView) view.findViewById(R.id.firstCourse);
        mTVFirstCourse.setTypeface(null, Typeface.ITALIC);
        mTVNbWeeks = (TextView) view.findViewById(R.id.nbWeeks);
        mPodium = (Podium2) view.findViewById(R.id.podium);
        mGraphic = (GraphicView) view.findViewById(R.id.graph);

    }

    public void fillViews(){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> listCourse = coursesBDD.getAllCourses();

        // Nombre de semaines
        mTVNbWeeks.setText("" + coursesBDD.getNumberWeeks());

        // Premier cours
        mTVFirstCourse.setText(C.formatDate(new Date().getTime(), C.DAY_DATE_D_MONTH_YEAR));
        if(listCourse != null) {
            Collections.reverse(listCourse);
            mTVFirstCourse.setText(C.formatDate(listCourse.get(0).getDate(), C.DAY_DATE_D_MONTH_YEAR));
        }

        // Classement semaines
        if(mListWeek.size() > 0) {
            fillBestWeeks(mListWeek);
            mGraphic.setListWeeks(mListWeek);
        }

        coursesBDD.close();
    }

    public void fillBestWeeks(List<Week> weeks){
        double bestMoney = 0;
        int nbCourse = 0;
        String weekLabel = "";

        // Numéro 1
        for(Week week : weeks){
            if (week.getMoney() > bestMoney) {
                bestMoney = week.getMoney();
                nbCourse = week.getNbOfCourses();
                weekLabel = week.getLabel().replace(" - ", "\n");
                Log.e("TAG", week.getBeginning()+"");
            }
        }
        mPodium.addGoldItem(String.format("%.2f€",bestMoney), String.format("%d cours", nbCourse), weekLabel);

        if(weeks.size() == 1)
            return;

        double maxMoney = bestMoney;
        bestMoney = 0;

        // Numéro 2
        for(Week week : weeks){
            if (week.getMoney() > bestMoney && week.getMoney() < maxMoney) {
                bestMoney = week.getMoney();
                nbCourse = week.getNbOfCourses();
                weekLabel = week.getLabel().replace(" - ", "\n");
            }
        }
        mPodium.addSilverItem(String.format("%.2f€", bestMoney), String.format("%d cours", nbCourse), weekLabel);

        if(weeks.size() == 2)
            return;

        maxMoney = bestMoney; bestMoney = 0;

        // Numéro 3
        for(Week week : weeks){
            if (week.getMoney() > bestMoney && week.getMoney() < maxMoney) {
                bestMoney = week.getMoney();
                nbCourse = week.getNbOfCourses();
                weekLabel = week.getLabel().replace(" - ", "\n");
            }
        }

        mPodium.addBronzeItem(String.format("%.2f€",bestMoney), String.format("%d cours", nbCourse), weekLabel);
        return;
    }

    // Interface

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }
}

