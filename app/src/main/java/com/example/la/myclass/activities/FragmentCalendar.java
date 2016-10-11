package com.example.la.myclass.activities;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.la.myclass.R;
import com.example.la.myclass.adapters.AdapterGridViewMonth;
import com.example.la.myclass.adapters.AdapterGridViewWeek;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.database.DevoirBDD;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Léa on 27/09/2015.
 */
public class FragmentCalendar extends Fragment implements View.OnClickListener {

    /**
     * Variables Statics
     */
    protected static final int NEXT = 1;
    protected static final int PAST = -1;
    protected static final int MONTH_MODE = 0;
    protected static final int WEEK_MODE = 1;

    /**
     * Variables de classe
     */
    protected int mCurrentOffset;
    protected float mMeanCourse, mMeanDevoir;
    protected double mMeanMoney;
    protected int mCurrentMod;

    /**
     * Views
     */
    protected GridView mGVPeriod;
    protected Calendar mCalendar;
    protected View mRootView;
    protected TextView mTVMonthLabel, mTVModWeek, mTVModMonth;


    /**
     * Fragment Life Cycle
     */
    public static FragmentCalendar newInstance() {
        return new FragmentCalendar();
    }

    public FragmentCalendar() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        getAllViews(mRootView);
        fillViews();
        fillMeanStats(mCurrentMod);
        fillPeriodStats(mCurrentMod);

        return mRootView;
    }


    // Utils

    public void getAllViews(View view) {

        mGVPeriod = (GridView) view.findViewById(R.id.gridView);
        mTVMonthLabel = (TextView) view.findViewById(R.id.monthLabel);
        mTVModMonth = (TextView) view.findViewById(R.id.modMonth);
        mTVModWeek = (TextView) view.findViewById(R.id.modWeek);

        mTVModWeek.setOnClickListener(this);
        mTVModMonth.setOnClickListener(this);
        view.findViewById(R.id.nextPeriod).setOnClickListener(this);
        view.findViewById(R.id.pastPeriod).setOnClickListener(this);
    }

    public void fillViews() {

        if(mCurrentMod == MONTH_MODE) {
            mGVPeriod.setAdapter(new AdapterGridViewMonth(getActivity(), mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewMonth) mGVPeriod.getAdapter()).getCurrentLabel());
        }

        else if(mCurrentMod == WEEK_MODE){
            mGVPeriod.setAdapter(new AdapterGridViewWeek(getActivity(), mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewWeek) mGVPeriod.getAdapter()).getCurrentLabel());
        }

        mGVPeriod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),
                        mGVPeriod.getAdapter().getItem(i).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fillPeriodStats(int mod){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();

        List<Course> courses = new ArrayList<>();
        List<Devoir> devoirs = new ArrayList<>();

        if(mod == MONTH_MODE) {
            courses = coursesBDD.getListCourseForAMonth(mCalendar.getTimeInMillis());
            devoirs = devoirBDD.getListDevoirForAMonth(mCalendar.getTimeInMillis());
        }

        else if(mod == WEEK_MODE) {
            courses = coursesBDD.getListCourseForAWeek(mCalendar.getTimeInMillis());
            devoirs = devoirBDD.getListDevoirForAWeek(mCalendar.getTimeInMillis());
        }

        coursesBDD.close();
        devoirBDD.close();

        ((TextView)mRootView.findViewById(R.id.nbCours)).setText(String.format("%d", courses.size()));
        ((TextView)mRootView.findViewById(R.id.nbDevoir)).setText(String.format("%d", devoirs.size()));
        double money = 0;
        for(Course course : courses)
            money += course.getMoney();
        ((TextView)mRootView.findViewById(R.id.money)).setText(String.format("%.2f€", money));

        if(courses.size() < mMeanCourse) {
            ((ImageView)mRootView.findViewById(R.id.trendCourse)).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_48dp));
            ((ImageView)mRootView.findViewById(R.id.trendCourse)).setColorFilter(getResources().getColor(R.color.red500));
        }
        else{
            ((ImageView)mRootView.findViewById(R.id.trendCourse)).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_white_48dp));
            ((ImageView)mRootView.findViewById(R.id.trendCourse)).setColorFilter(getResources().getColor(R.color.green500));
        }

        if(devoirs.size() < mMeanDevoir) {
            ((ImageView)mRootView.findViewById(R.id.trendDevoir)).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_48dp));
            ((ImageView)mRootView.findViewById(R.id.trendDevoir)).setColorFilter(getResources().getColor(R.color.red500));
        }
        else{
            ((ImageView)mRootView.findViewById(R.id.trendDevoir)).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_white_48dp));
            ((ImageView)mRootView.findViewById(R.id.trendDevoir)).setColorFilter(getResources().getColor(R.color.green500));
        }

        if(money < mMeanMoney) {
            ((ImageView)mRootView.findViewById(R.id.trendMoney)).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_48dp));
            ((ImageView)mRootView.findViewById(R.id.trendMoney)).setColorFilter(getResources().getColor(R.color.red500));
        }
        else{
            ((ImageView)mRootView.findViewById(R.id.trendMoney)).setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_white_48dp));
            ((ImageView)mRootView.findViewById(R.id.trendMoney)).setColorFilter(getResources().getColor(R.color.green500));
        }
    }

    public void fillMeanStats(int mod){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> courses = coursesBDD.getActiveCourses();

        float nbCours = courses.size(), nbItem = 0;

        if(mod == MONTH_MODE)
            nbItem = coursesBDD.getAllMonths().size();
        else if (mod == WEEK_MODE)
            nbItem = coursesBDD.getAllWeeks().size();

        coursesBDD.close();

        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        float nbDevoirs = devoirBDD.getActiveDevoirs().size();
        devoirBDD.close();

        double money = 0;
        for(Course course : courses)
            money += course.getMoney();

        mMeanCourse = nbCours / nbItem;
        mMeanDevoir = nbDevoirs / nbItem;
        mMeanMoney = money / nbItem;

        ((TextView)mRootView.findViewById(R.id.meanCours)).setText(String.format("%.2f", mMeanCourse));
        ((TextView)mRootView.findViewById(R.id.meanDevoir)).setText(String.format("%.2f", mMeanDevoir));
        ((TextView)mRootView.findViewById(R.id.meanMoney)).setText(String.format("%.2f€", mMeanMoney));
    }

    public void changePeriod(int mode){
        mCurrentOffset += mode;
        if(mCurrentMod == MONTH_MODE){
            mGVPeriod.setAdapter(new AdapterGridViewMonth(getActivity(),mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewMonth) mGVPeriod.getAdapter()).getCurrentLabel());
            mCalendar.add(Calendar.MONTH, mode);
        }
        else if(mCurrentMod == WEEK_MODE){
            mGVPeriod.setAdapter(new AdapterGridViewWeek(getActivity(), mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewWeek) mGVPeriod.getAdapter()).getCurrentLabel());
            mCalendar.add(Calendar.WEEK_OF_YEAR, mode);
        }

        fillPeriodStats(mCurrentMod);
    }

    public void changeModView(int mod){
        mCurrentMod = mod;
        fillMeanStats(mod);
        mCurrentOffset = 0;
        mCalendar = Calendar.getInstance();
        fillPeriodStats(mod);

        if(mod == WEEK_MODE){
            mTVModMonth.setTextColor(getActivity().getResources().getColor(R.color.white));
            mTVModMonth.setTypeface(null, Typeface.NORMAL);
            mTVModWeek.setTypeface(null, Typeface.BOLD);
            mTVModWeek.setTextColor(getActivity().getResources().getColor(R.color.unthem300));
            mGVPeriod.setAdapter(new AdapterGridViewWeek(getActivity(), mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewWeek) mGVPeriod.getAdapter()).getCurrentLabel());
        }
        else if(mod == MONTH_MODE){
            mTVModWeek.setTextColor(getActivity().getResources().getColor(R.color.white));
            mTVModWeek.setTypeface(null, Typeface.NORMAL);
            mTVModMonth.setTypeface(null, Typeface.BOLD);
            mTVModMonth.setTextColor(getActivity().getResources().getColor(R.color.unthem300));
            mGVPeriod.setAdapter(new AdapterGridViewMonth(getActivity(), mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewMonth) mGVPeriod.getAdapter()).getCurrentLabel());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pastPeriod:
                changePeriod(PAST);
                break;
            case R.id.nextPeriod:
                changePeriod(NEXT);
                break;
            case R.id.modMonth:
                changeModView(MONTH_MODE);
                break;
            case R.id.modWeek:
                changeModView(WEEK_MODE);
                break;
        }
    }

} // 321 - 15h29    =>  261 - 15h38