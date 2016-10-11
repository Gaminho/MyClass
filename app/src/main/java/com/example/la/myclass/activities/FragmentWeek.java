package com.example.la.myclass.activities;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.adapters.AdapterGridViewMonth;
import com.example.la.myclass.adapters.AdapterGridViewWeek;
import com.example.la.myclass.adapters.RecyclerViewWeek;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.periodic.Week;
import com.example.la.myclass.database.DevoirBDD;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Léa on 27/09/2015.
 */
public class FragmentWeek extends Fragment implements View.OnClickListener {

    // Views
    protected RecyclerView mListViewWeekDays;
    protected TextView mTextViewActualWeek, mTextViewNextWeek, mTextViewLastWeek, mTextViewInformationWeek;


    // NEW
    protected TextView mTVMonthLabel, mTVModWeek, mTVModMonth;
    protected int mCurrentOffset;
    protected static final int NEXT = 1;
    protected static final int PAST = -1;
    protected static final int MONTH_MODE = 0;
    protected static final int WEEK_MODE = 1;
    protected GridView mGVMonth;
    protected Calendar mCalendar;
    protected View mRootView;
    protected float mMeanCourse, mMeanDevoir;
    protected double mMeanMoney;
    protected int mCurrentMod;

    // Variables de classe
    protected Week mWeek;
    protected List<Course> mListCourse, mCurrentWeekCourse;


    // Fragment life cycle
    public static FragmentWeek newInstance() {
        return new FragmentWeek();
    }

    public FragmentWeek() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeek = new Week();
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        mListCourse = coursesBDD.getAllCourses();
        mCurrentWeekCourse = coursesBDD.getCoursesBetweenTwoDates(mWeek.getBeginning(), mWeek.getEnding());
        coursesBDD.close();
        mCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_week, container, false);
        getAllViews(mRootView);
        fillViews();
        fillMeanStats(mCurrentMod);
        fillMonthStats();

        return mRootView;
    }


    // Utils

    public void getAllViews(View view) {

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mListViewWeekDays = (RecyclerView) view.findViewById(R.id.weekList);
        mListViewWeekDays.setLayoutManager(llm);

        mTextViewActualWeek = (TextView) view.findViewById(R.id.actualWeek);
        mTextViewLastWeek = (TextView) view.findViewById(R.id.lastWeek);
        mTextViewLastWeek.setOnClickListener(this);
        mTextViewNextWeek = (TextView) view.findViewById(R.id.nextWeek);
        mTextViewNextWeek.setOnClickListener(this);
        mTextViewInformationWeek = (TextView) view.findViewById(R.id.informationWeek);
        mGVMonth = (GridView) view.findViewById(R.id.gridView);

        view.findViewById(R.id.nextMonth).setOnClickListener(this);
        view.findViewById(R.id.pastMonth).setOnClickListener(this);
        mTVMonthLabel = (TextView) view.findViewById(R.id.monthLabel);

        mTVModMonth = (TextView) view.findViewById(R.id.modMonth);
        view.findViewById(R.id.modMonth).setOnClickListener(this);
        mTVModWeek = (TextView) view.findViewById(R.id.modWeek);
        view.findViewById(R.id.modWeek).setOnClickListener(this);
    }

    public void fillViews() {

        mListViewWeekDays.setAdapter(new RecyclerViewWeek(getActivity(), mWeek, mCurrentWeekCourse));
        mTextViewActualWeek.setText(mWeek.getLabel());

        double money = 0;
        for(Course course : mCurrentWeekCourse)
            money = money + course.getMoney();

        mTextViewInformationWeek.setText(String.format("Cours : %d ; Argent : %.2f €", mCurrentWeekCourse.size(), money));

        if(mCurrentMod == MONTH_MODE) {
            mGVMonth.setAdapter(new AdapterGridViewMonth(getActivity(), mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewMonth)mGVMonth.getAdapter()).getCurrentLabel());
        }
        else if(mCurrentMod == WEEK_MODE){
            mGVMonth.setAdapter(new AdapterGridViewWeek(getActivity(), mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewWeek)mGVMonth.getAdapter()).getCurrentLabel());
        }

        mGVMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),
                        mGVMonth.getAdapter().getItem(i).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fillMonthStats(){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> courses = coursesBDD.getListCourseForAMonth(mCalendar.getTimeInMillis());
        coursesBDD.close();

        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        List<Devoir> devoirs = devoirBDD.getListDevoirForAMonth(mCalendar.getTimeInMillis());
        devoirBDD.close();

        ((TextView)mRootView.findViewById(R.id.nbCours)).setText(String.format("%d", courses.size()));
        double money = 0;
        for(Course course : courses)
            money += course.getMoney();
        ((TextView)mRootView.findViewById(R.id.nbDevoir)).setText(String.format("%d", devoirs.size()));
        ((TextView)mRootView.findViewById(R.id.money)).setText(String.format("%.2f€", money));

        int pixColor = R.color.green500;
        int trendDrawable = R.drawable.ic_keyboard_arrow_up_white_48dp;

        if(courses.size() < mMeanCourse) {
            pixColor = R.color.red500;
            trendDrawable = R.drawable.ic_keyboard_arrow_down_white_48dp;
        }
        ((ImageView)mRootView.findViewById(R.id.trendCourse)).setImageDrawable(getResources().getDrawable(trendDrawable));
        ((ImageView)mRootView.findViewById(R.id.trendCourse)).setColorFilter(getResources().getColor(pixColor));

        pixColor = R.color.green500;
        trendDrawable = R.drawable.ic_keyboard_arrow_up_white_48dp;
        if(devoirs.size() < mMeanDevoir) {
            pixColor = R.color.red500;
            trendDrawable = R.drawable.ic_keyboard_arrow_down_white_48dp;
        }
        ((ImageView)mRootView.findViewById(R.id.trendDevoir)).setImageDrawable(getResources().getDrawable(trendDrawable));
        ((ImageView)mRootView.findViewById(R.id.trendDevoir)).setColorFilter(getResources().getColor(pixColor));

        pixColor = R.color.green500;
        trendDrawable = R.drawable.ic_keyboard_arrow_up_white_48dp;
        if(money < mMeanMoney) {
            pixColor = R.color.red500;
            trendDrawable = R.drawable.ic_keyboard_arrow_down_white_48dp;
        }
        ((ImageView)mRootView.findViewById(R.id.trendMoney)).setImageDrawable(getResources().getDrawable(trendDrawable));
        ((ImageView)mRootView.findViewById(R.id.trendMoney)).setColorFilter(getResources().getColor(pixColor));

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

    public void changeWeek(int mode) {
        Week week = mWeek.getWeekWithOffset(mWeek, mode);
        if (!week.isTheOldest(mListCourse) && !week.isTheMostRecent(mListCourse)) {
            mWeek = week;
            CoursesBDD coursesBDD = new CoursesBDD(getActivity());
            coursesBDD.open();
            mCurrentWeekCourse = coursesBDD.getCoursesBetweenTwoDates(mWeek.getBeginning(), mWeek.getEnding());
            coursesBDD.close();
            fillViews();
        } else
            Toast.makeText(getActivity(), "Aucun cours", Toast.LENGTH_SHORT).show();
    }

    public void changeMonth(int mode){
        mCurrentOffset += mode;
        if(mCurrentMod == MONTH_MODE){
            mGVMonth.setAdapter(new AdapterGridViewMonth(getActivity(),mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewMonth)mGVMonth.getAdapter()).getCurrentLabel());
        }
        else if(mCurrentMod == WEEK_MODE){
            mGVMonth.setAdapter(new AdapterGridViewWeek(getActivity(), mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewWeek)mGVMonth.getAdapter()).getCurrentLabel());
        }
        mCalendar.add(Calendar.MONTH, mode);
        fillMonthStats();
    }

    public void changeModView(int mod){
        mCurrentMod = mod;
        fillMeanStats(mod);
        mCurrentOffset = 0;

        if(mod == WEEK_MODE){
            mTVModMonth.setTextColor(getActivity().getResources().getColor(R.color.white));
            mTVModMonth.setTypeface(null, Typeface.NORMAL);
            mTVModWeek.setTypeface(null, Typeface.BOLD);
            mTVModWeek.setTextColor(getActivity().getResources().getColor(R.color.unthem300));
            mGVMonth.setAdapter(new AdapterGridViewWeek(getActivity(), mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewWeek)mGVMonth.getAdapter()).getCurrentLabel());
        }
        else if(mod == MONTH_MODE){
            mTVModWeek.setTextColor(getActivity().getResources().getColor(R.color.white));
            mTVModWeek.setTypeface(null, Typeface.NORMAL);
            mTVModMonth.setTypeface(null, Typeface.BOLD);
            mTVModMonth.setTextColor(getActivity().getResources().getColor(R.color.unthem300));
            mGVMonth.setAdapter(new AdapterGridViewMonth(getActivity(), mCurrentOffset));
            mTVMonthLabel.setText( ((AdapterGridViewMonth)mGVMonth.getAdapter()).getCurrentLabel());
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.nextWeek:
                changeWeek(Week.MOD_NEXT);
                break;
            case R.id.lastWeek:
                changeWeek(Week.MOD_LAST);
                break;
            case R.id.pastMonth:
                changeMonth(PAST);
                break;
            case R.id.nextMonth:
                changeMonth(NEXT);
                break;
            case R.id.modMonth:
                changeModView(MONTH_MODE);
                break;
            case R.id.modWeek:
                changeModView(WEEK_MODE);
                break;
        }
    }

}