package com.example.la.myclass.activities.stats;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.activities.FragmentDay;
import com.example.la.myclass.activities.FragmentMoney;
import com.example.la.myclass.activities.FragmentSettings;
import com.example.la.myclass.activities.FragmentWeek;
import com.example.la.myclass.activities.MainActivity;
import com.example.la.myclass.activities.NavigationDrawerFragment;
import com.example.la.myclass.activities.course.FragmentListCourse;
import com.example.la.myclass.activities.devoir.FragmentListDevoir;
import com.example.la.myclass.activities.pupil.FragmentListPupil;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.database.DevoirBDD;
import com.example.la.myclass.database.PupilsBDD;
import com.example.la.myclass.utils.DateParser;

import java.util.Date;

/**
 * Created by Gaminho on 21/09/2016.
 */
public class FragmentStatsGlobal extends Fragment implements View.OnClickListener{

    // Views
    protected CardView mCardViewPupils, mCardViewCours, mCardViewDevoirs;
    protected CardView mCardViewBestWeek, mCardViewStatsWeek;
    protected CardView mCardViewBestMonth, mCardViewStatsMonth;

    protected TextView mTextViewEleves, mTextViewCours, mTextViewDevoirs;
    protected TextView mTextViewWeeks, mTextViewMeanWeek, mTextViewBestWeek, mTextViewBestWeekDetails;
    protected TextView mTextViewMonths, mTextViewMeanMonth, mTextViewBestMonth, mTextViewBestMonthDetails;


    // Fragment life cycle
    public static FragmentStatsGlobal newInstance() {
        return new FragmentStatsGlobal();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats_global, container, false);
        getAllViews(view);
        fillViews();

        return view;
    }


    // Utils

    public void getAllViews(View view){

        mTextViewEleves = (TextView) view.findViewById(R.id.tvEleves);
        mTextViewCours = (TextView) view.findViewById(R.id.tvCours);
        mTextViewDevoirs = (TextView) view.findViewById(R.id.tvDevoir);


        mTextViewWeeks = (TextView) view.findViewById(R.id.tvWeeks);
        mTextViewMeanWeek = (TextView) view.findViewById(R.id.tvMeanWeek);
        mTextViewBestWeek = (TextView) view.findViewById(R.id.tvBestWeek);
        mTextViewBestWeekDetails = (TextView) view.findViewById(R.id.tvBestWeekDetails);

        mTextViewMonths = (TextView) view.findViewById(R.id.tvMonths);
        mTextViewMeanMonth = (TextView) view.findViewById(R.id.tvMeanMonth);
        mTextViewBestMonth = (TextView) view.findViewById(R.id.tvBestMonth);
        mTextViewBestMonthDetails = (TextView) view.findViewById(R.id.tvBestMonthDetails);

        mCardViewPupils = (CardView) view.findViewById(R.id.cvPupils);
        mCardViewPupils.setOnClickListener(this);
        mCardViewCours = (CardView) view.findViewById(R.id.cvCours);
        mCardViewCours.setOnClickListener(this);
        mCardViewDevoirs = (CardView) view.findViewById(R.id.cvDevoir);
        mCardViewDevoirs.setOnClickListener(this);

        mCardViewBestWeek = (CardView) view.findViewById(R.id.cvBestWeek);
        mCardViewStatsWeek = (CardView) view.findViewById(R.id.cvStatsWeek);

        mCardViewBestMonth = (CardView) view.findViewById(R.id.cvBestMonth);
        mCardViewStatsMonth = (CardView) view.findViewById(R.id.cvStatsMonth);
    }

    public void fillViews(){

        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        int nbCours = coursesBDD.getNumberOfCourses();

        // On soustrait les cours à venir
        if(coursesBDD.getCoursesWithState(Course.FORESEEN) != null)
            nbCours -= coursesBDD.getCoursesWithState(Course.FORESEEN).size();

        int nbWeeks = coursesBDD.getNumberWeeks();
        int nbMonths = coursesBDD.getNumberMonths();
        double money = coursesBDD.getMoneyEarnedWithState(Course.WAITING_FOT_VALIDATION) + coursesBDD.getMoneyEarnedWithState(Course.VALIDATED);
        Double[] bestWeek = coursesBDD.getMoneyOfBestWeek();
        Double[] bestMonth = coursesBDD.getMoneyOfBestMonth();
        coursesBDD.close();
        mTextViewCours.setText("" + nbCours);
        mTextViewWeeks.setText("" + nbWeeks);
        mTextViewMonths.setText("" + nbMonths);
        mTextViewMeanWeek.setText(String.format("%.2f€", (money / nbWeeks)));
        mTextViewMeanMonth.setText(String.format("%.2f€", (money / nbMonths)));
        mTextViewBestWeek.setText(String.format("%.2f€",bestWeek[0]));
        mTextViewBestWeekDetails.setText(new DateParser().getWeekLabelWithIndex(bestWeek[1]));
        mTextViewBestMonth.setText(String.format("%.2f€",bestMonth[0]));
        mTextViewBestMonthDetails.setText(new DateParser().getMonthLabelWithIndex(bestMonth[1]));

        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        int nbPupils = pupilsBDD.getNumberOfPupils();
        pupilsBDD.close();
        mTextViewEleves.setText("" + nbPupils);

        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        int nbDevoirs = devoirBDD.getNumberOfDevoirs();
        devoirBDD.close();
        mTextViewDevoirs.setText("" + nbDevoirs);

    }



    // Interface

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cvPupils :
                ((MainActivity) getActivity()).loadFragment(FragmentListPupil.newInstance(), NavigationDrawerFragment.INDEX_PUPILS,
                        NavigationDrawerFragment.drawer_sections[NavigationDrawerFragment.INDEX_PUPILS], false);
                break;
            case R.id.cvCours :
                ((MainActivity) getActivity()).loadFragment(FragmentListCourse.newInstance(Course.ALL_COURSES), NavigationDrawerFragment.INDEX_CLASSES,
                        NavigationDrawerFragment.drawer_sections[NavigationDrawerFragment.INDEX_CLASSES], false);
                break;
            case R.id.cvDevoir :
                ((MainActivity) getActivity()).loadFragment(FragmentListDevoir.newInstance(), NavigationDrawerFragment.INDEX_DEVOIRS,
                        NavigationDrawerFragment.drawer_sections[NavigationDrawerFragment.INDEX_DEVOIRS], false);
                break;
            case R.id.cvBestWeek :
                break;
            case R.id.cvStatsWeek :
                break;
            case R.id.cvBestMonth :
                break;
            case R.id.cvStatsMonth :
                break;
        }
    }
}
