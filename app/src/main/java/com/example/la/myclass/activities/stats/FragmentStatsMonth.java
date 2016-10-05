package com.example.la.myclass.activities.stats;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Month;
import com.example.la.myclass.customviews.GraphicView;
import com.example.la.myclass.customviews.Podium2;
import com.example.la.myclass.database.CoursesBDD;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Gaminho on 21/09/2016.
 */
public class FragmentStatsMonth extends Fragment implements View.OnClickListener{


    // Views
    protected TextView mTVFirstCourse, mTVNbMonths;
    protected Podium2 mPodium;
    protected GraphicView mGraphic;


    // Fragment life cycle
    public static FragmentStatsMonth newInstance() {
        return new FragmentStatsMonth();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats_month, container, false);
        getAllViews(view);
        fillViews();

        return view;
    }


    // Utils

    public void getAllViews(View view){
        mTVFirstCourse = (TextView) view.findViewById(R.id.firstCourse);
        mTVFirstCourse.setTypeface(null, Typeface.ITALIC);
        mTVNbMonths = (TextView) view.findViewById(R.id.nbMonths);
        mGraphic = (GraphicView) view.findViewById(R.id.graph);
        mPodium = (Podium2) view.findViewById(R.id.podium);
    }

    public void fillViews(){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> listCourse = coursesBDD.getAllCourses();

        // Nombre de mois
        mTVNbMonths.setText(""+coursesBDD.getNumberMonths());

        // Premier cours
        mTVFirstCourse.setText(C.formatDate(new Date().getTime(), C.DAY_DATE_D_MONTH_YEAR));
        if(listCourse != null) {
            Collections.reverse(listCourse);
            mTVFirstCourse.setText(C.formatDate(listCourse.get(0).getDate(), C.DAY_DATE_D_MONTH_YEAR));
        }

        // Classement mois
        List<Month> monthList = coursesBDD.getAllMonths();
        if(monthList.size() > 0) {
            fillBestMonths(monthList);
            mGraphic.setListMonths(monthList);
        }

        coursesBDD.close();
    }

    public void fillBestMonths(List<Month> months){
        double bestMoney = 0;
        int nbCourse = 0;
        String monthLabel = "";

        // Numéro 1
        for(Month month : months){
            if (month.getMoney() > bestMoney) {
                bestMoney = month.getMoney();
                nbCourse = month.getNbOfCourses();
                monthLabel = month.getLabel().replace(" ", "\n");
            }
        }
        mPodium.addGoldItem(String.format("%.2f€", bestMoney), String.format("%d cours", nbCourse), monthLabel);

        if(months.size() == 1)
            return;

        double maxMoney = bestMoney;
        bestMoney = 0;

        // Numéro 2
        for(Month month : months){
            if (month.getMoney() > bestMoney && month.getMoney() < maxMoney){
                bestMoney = month.getMoney();
                nbCourse = month.getNbOfCourses();
                monthLabel = month.getLabel().replace(" ", "\n");
            }
        }
        mPodium.addSilverItem(String.format("%.2f€", bestMoney), String.format("%d cours", nbCourse), monthLabel);

        if(months.size() == 2)
            return;

        maxMoney = bestMoney; bestMoney = 0;

        // Numéro 3
        for(Month month : months){
            if (month.getMoney() > bestMoney && month.getMoney() < maxMoney){
                bestMoney = month.getMoney();
                nbCourse = month.getNbOfCourses();
                monthLabel = month.getLabel().replace(" ", "\n");
            }
        }
        mPodium.addBronzeItem(String.format("%.2f€", bestMoney), String.format("%d cours", nbCourse), monthLabel);

    }


    // Interface

    @Override
    public void onClick(View view) {
    }
}
