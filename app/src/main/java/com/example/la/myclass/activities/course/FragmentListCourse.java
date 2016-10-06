package com.example.la.myclass.activities.course;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.la.myclass.R;
import com.example.la.myclass.activities.AbstractFragmentList;
import com.example.la.myclass.adapters.RecyclerViewCourses;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.database.CoursesBDD;

import java.util.Date;
import java.util.List;

/**
 * Created by Léa on 27/09/2015.
 */
public class FragmentListCourse extends AbstractFragmentList implements AdapterView.OnItemSelectedListener {


    /**
     * Constantes du bundle
     */
    private static final String COURSES_STATE = "courses_state";

    /**
     * Variables de classe
     */
    protected int mState;   // Used to auto-fill listview with specified state (all by default)
    protected int mFilterPupilIDValue = -1, mFilterStateValue =-1;


    /**
     * Fragment Lide Cycle
     * @param state
     * @return
     */
    public static FragmentListCourse newInstance(int state) {
        FragmentListCourse fragmentListCourse = new FragmentListCourse();
        Bundle args = new Bundle();
        args.putInt(COURSES_STATE, state);
        fragmentListCourse.setArguments(args);
        return fragmentListCourse;
    }
    public FragmentListCourse() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mState = getArguments().getInt(COURSES_STATE, Course.ALL_COURSES);

        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> list = coursesBDD.getCoursesWithState(Course.FORESEEN);

        if(list.size()>0) {
            for (Course course : list) {
                if (new Date().getTime() > course.getDate()) {
                    course.setState(Course.WAITING_FOT_VALIDATION);
                    coursesBDD.updateCourse(course.getId(), course);
                }
            }
        }

        coursesBDD.close();
        mFilteringLabels.add(1, "");
        mFilteringLabels.add(2, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        fillListView(mListView, mTextViewNoItem);
        manageListView(getFilterList(), mListView, mTextViewNoItem);
        mButtonAddAnItem.setOnClickListener(this);
        return view;
    }


    /**
     * Implementing abstracts functions from AbstractFragmentList
     */
    @Override
    protected void fillListView(RecyclerView recyclerView, TextView textViewNoItem) {
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> list = coursesBDD.getAllCourses();
        coursesBDD.close();

        if(list.size() > 0)
            recyclerView.setAdapter(new RecyclerViewCourses(getActivity(), list));

        else{
            recyclerView.setVisibility(View.GONE);
            textViewNoItem.setVisibility(View.VISIBLE);
            textViewNoItem.setText("Aucun cours");
        }
    }

    @Override
    protected void removeFilters() {
        mFilteringLabels.set(1, "");
        mFilteringLabels.set(2, "");
        mFilterPupilIDValue = mFilterStateValue =-1;
        ((Spinner)super.mContentView.findViewById(R.id.spinnerPupils)).setSelection(0);
        ((Spinner)super.mContentView.findViewById(R.id.spinnerStates)).setSelection(0);
        super.removeFilters();
    }

    @Override
    protected View setFilterContent(Context context, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filters_courses, container, false);
        fillSpinnerPupils((Spinner) view.findViewById(R.id.spinnerPupils), this);
        setSpinnerContent((Spinner) view.findViewById(R.id.spinnerStates), R.array.filter_cours, this);
        mCBHide.setText("Afficher les cours annulés");
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    public List<?> getFilterList(){

        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        String criteria = "1";

        if(super.mFilterDateValue > -1)
            criteria += " AND " + CoursesBDD.COL_DATE + " >= " + super.mFilterDateValue;
        if(super.mFilterBetweenDate > -1){
            criteria += " AND " + CoursesBDD.COL_DATE + " >= " + super.mFromDate + " AND " + CoursesBDD.COL_DATE + " <= " + super.mToDate;
        }

        if(mFilterPupilIDValue > -1)
            criteria += " AND " + CoursesBDD.COL_PUPIL_ID + " = " + mFilterPupilIDValue;

        if(mFilterStateValue > -1) {
            criteria += " AND " + CoursesBDD.COL_STATE + " = " + mFilterStateValue;
            mFilteringLabels.set(2, Course.getStateLabel(mFilterStateValue));
        }
        else
            mFilteringLabels.set(2, "");

        if (!mDisplayOldItems)
            criteria += " AND " + CoursesBDD.COL_STATE + " != " + Course.CANCELED;

        Log.e("Criteria", criteria);
        return coursesBDD.getCourseWithCriteria(criteria);
    }

    @Override
    public void manageListView(List<?> list, RecyclerView listView, TextView textView) {
        ((RecyclerViewCourses) listView.getAdapter()).setmListCourses((List<Course>) list);
        textView.setText("Aucun cours");
        super.manageListView(list, listView, textView);
    }

    /**
     * Implementing Interfaces methods
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinnerStates){
            if(i>0)
                mFilterStateValue = i - 1;
            else
                mFilterStateValue = -1;
        }
        if(adapterView.getId() == R.id.spinnerPupils){
            if (i > 0) {
                Pupil pupil = (Pupil) adapterView.getAdapter().getItem(i);
                mFilterPupilIDValue = pupil.getId();
                mFilteringLabels.set(1,pupil.getFullName());
            }
            else {
                mFilterPupilIDValue = -1;
                mFilteringLabels.set(1,"");
            }
        }

        mList = getFilterList();
        manageListView(mList, mListView, mTextViewNoItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch(view.getId()){
            case R.id.addAnItem:
                Intent intent = new Intent(getActivity(), ActivityCourse.class);
                intent.putExtra(ActivityCourse.COURSE_ACTION, ActivityCourse.ADDING);
                startActivity(intent);
                break;
        }
    }

} //223 -> 187