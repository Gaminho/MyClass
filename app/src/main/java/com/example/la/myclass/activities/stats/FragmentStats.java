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
import com.example.la.myclass.activities.FragmentMoney;
import com.example.la.myclass.activities.FragmentSettings;
import com.example.la.myclass.activities.FragmentWeek;
import com.example.la.myclass.activities.MainActivity;
import com.example.la.myclass.activities.NavigationDrawerFragment;
import com.example.la.myclass.activities.course.FragmentListCourse;
import com.example.la.myclass.activities.devoir.FragmentListDevoir;
import com.example.la.myclass.activities.pupil.FragmentListPupil;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.database.DevoirBDD;
import com.example.la.myclass.database.PupilsBDD;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.utils.DateParser;

/**
 * Created by Gaminho on 22/05/2016.
 */
public class FragmentStats extends Fragment implements View.OnClickListener{

    // Static
    public static final int MOD_VIEW_GLOBAL = 0;
    public static final int MOD_VIEW_WEEK   = 1;
    public static final int MOD_VIEW_MONTH  = 2;
    public static final int MOD_VIEW_YEAR   = 3;


    // Views
    protected CardView mCardViewPupils, mCardViewCours, mCardViewDevoirs;
    protected CardView mCardViewBestWeek, mCardViewStatsWeek;
    protected CardView mCardViewBestMonth, mCardViewStatsMonth;

    protected TextView mTextViewEleves, mTextViewCours, mTextViewDevoirs;
    protected TextView mTextViewWeeks, mTextViewMeanWeek, mTextViewBestWeek, mTextViewBestWeekDetails;
    protected TextView mTextViewMonths, mTextViewMeanMonth, mTextViewBestMonth, mTextViewBestMonthDetails;
    protected TextView mTVStatMonths, mTVStatGlobal, mTVStatWeeks, mTVStatYears;

    protected FrameLayout mFrameStats;


    // Fragment life cycle
    public static FragmentStats newInstance() {
        return new FragmentStats();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats_main, container, false);
        getAllViews(view);
        fillViews();
        return view;
    }


    // Utils

    public void getAllViews(View view){

        mTextViewEleves = (TextView) view.findViewById(R.id.tvEleves);
        mTextViewCours = (TextView) view.findViewById(R.id.tvCours);
        mTextViewDevoirs = (TextView) view.findViewById(R.id.tvDevoir);

        mTVStatGlobal = (TextView) view.findViewById(R.id.statGlobal);
        mTVStatGlobal.setOnClickListener(this);
        mTVStatMonths = (TextView) view.findViewById(R.id.statMonth);
        mTVStatMonths.setOnClickListener(this);
        mTVStatWeeks = (TextView) view.findViewById(R.id.statWeek);
        mTVStatWeeks.setOnClickListener(this);
        mTVStatYears = (TextView) view.findViewById(R.id.statYear);
        mTVStatYears.setOnClickListener(this);

        mFrameStats = (FrameLayout) view.findViewById(R.id.statsFrame);

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

        changeModView(-1);

    }

    public void changeModView(int modView){
        Fragment fragment;
        TextView textView;
        razOnglet();
        switch(modView) {
            case MOD_VIEW_WEEK :
                fragment = FragmentStatsWeek.newInstance();
                textView = mTVStatWeeks;
                break;
            case MOD_VIEW_MONTH :
                fragment = FragmentStatsMonth.newInstance();
                textView = mTVStatMonths;
                break;
            case MOD_VIEW_YEAR :
                fragment = FragmentStatsYear.newInstance();
                textView = mTVStatYears;
                break;
            default:
                fragment = FragmentStatsGlobal.newInstance();
                textView = mTVStatGlobal;
                break;
        }

        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(getActivity().getResources().getColor(R.color.unthem300));

        getFragmentManager().beginTransaction().replace(R.id.statsFrame, fragment).commit();
    }

    public void razOnglet(){
        mTVStatGlobal.setTextColor(getActivity().getResources().getColor(R.color.white));
        mTVStatGlobal.setTypeface(null, Typeface.NORMAL);
        mTVStatMonths.setTextColor(getActivity().getResources().getColor(R.color.white));
        mTVStatMonths.setTypeface(null, Typeface.NORMAL);
        mTVStatWeeks.setTextColor(getActivity().getResources().getColor(R.color.white));
        mTVStatWeeks.setTypeface(null, Typeface.NORMAL);
        mTVStatYears.setTextColor(getActivity().getResources().getColor(R.color.white));
        mTVStatYears.setTypeface(null, Typeface.NORMAL);
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
            case R.id.statGlobal :
                changeModView(-1);
                break;
            case R.id.statMonth :
                changeModView(MOD_VIEW_MONTH);
                break;
            case R.id.statWeek :
                changeModView(MOD_VIEW_WEEK);
                break;
            case R.id.statYear :
                changeModView(MOD_VIEW_YEAR);
                break;
        }
    }
}

