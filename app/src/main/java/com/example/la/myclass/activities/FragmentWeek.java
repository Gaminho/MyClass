package com.example.la.myclass.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.adapters.AdapterGridViewMonth;
import com.example.la.myclass.adapters.RecyclerViewWeek;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.periodic.Week;

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
    protected TextView mTVMonthLabel;
    protected int mCurrentOffsetMonth;
    protected static final int NEXT = 1;
    protected static final int PAST = -1;
    protected GridView mGVMonth;
    protected Calendar mCalendar;

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

        View view = inflater.inflate(R.layout.fragment_week, container, false);
        getAllViews(view);
        fillViews();

        return view;
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

    }

    public void fillViews() {

        mListViewWeekDays.setAdapter(new RecyclerViewWeek(getActivity(), mWeek, mCurrentWeekCourse));
        mTextViewActualWeek.setText(mWeek.getLabel());

        double money = 0;
        for(Course course : mCurrentWeekCourse)
            money = money + course.getMoney();

        mTextViewInformationWeek.setText(String.format("Cours : %d ; Argent : %.2f €", mCurrentWeekCourse.size(), money));



        mGVMonth.setAdapter(new AdapterGridViewMonth(getActivity(), mCurrentOffsetMonth));
        mTVMonthLabel.setText(String.format("%s %d",
                C.MONTHS[mCalendar.get(Calendar.MONTH)], mCalendar.get(Calendar.YEAR)));
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
        mCurrentOffsetMonth += mode;
        mGVMonth.setAdapter(new AdapterGridViewMonth(getActivity(), mCurrentOffsetMonth));
        mCalendar.add(Calendar.MONTH, mode);
        mTVMonthLabel.setText(
                String.format("%s %d",
                        C.MONTHS[mCalendar.get(Calendar.MONTH)],
                        mCalendar.get(Calendar.YEAR)
                )
        );
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
        }
    }

}