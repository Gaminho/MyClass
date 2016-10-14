package com.example.la.myclass.activities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.activities.course.ActivityCourse;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.database.CoursesBDD;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 24/08/2016.
 */
public class FragmentDay extends Fragment implements View.OnClickListener{

    /**
     * Bundle Variables
     */
    private static final String DATE_DAY = "date_day";


    /**
     * Variables de classe
     */
    private Date mDay;
    private List<Course> mListCourses;

    /**
     * Views
     */
    private TextView mTextViewTitle;
    private View mViewRoot;


    /**
     * Fragment Life Cycle
     * @param day : the timestamp of the current day
     */
    public static FragmentDay newInstance(long day) {
        FragmentDay fragmentDay = new FragmentDay();
        Bundle args = new Bundle();
        args.putLong(DATE_DAY, day);
        fragmentDay.setArguments(args);
        return fragmentDay;
    }

    public FragmentDay() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDay = new Date(getArguments().getLong(DATE_DAY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewRoot = inflater.inflate(R.layout.fragment_day, container, false);
        getAllViews(mViewRoot);
        fillViews();

        return mViewRoot;
    }


    /**
     * Utils
     * @param view : the root view
     */

    private void getAllViews(View view){
        mTextViewTitle = (TextView) view.findViewById(R.id.actualDay);
        view.findViewById(R.id.lastDay).setOnClickListener(this);
        view.findViewById(R.id.nextDay).setOnClickListener(this);
        view.findViewById(R.id.addACourse).setOnClickListener(this);
    }

    private void fillViews(){

        if(mDay != null) {
            mTextViewTitle.setText(C.formatDate(mDay.getTime(), C.DAY_DATE_D_MONTH_YEAR));
            CoursesBDD coursesBDD = new CoursesBDD(getActivity());
            coursesBDD.open();
            mListCourses = coursesBDD.getListCourseForADay(mDay.getTime());
            coursesBDD.close();
        }

        ((ViewGroup)mViewRoot.findViewById(R.id.timeline)).removeAllViews();

        if(mListCourses != null) {
            for(Course course : mListCourses)
                putCourseOnTimeline(course, mViewRoot);
        }
    }

    private void putCourseOnTimeline(final Course course, final View view){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(course.getDate()));
        final int hBegin = calendar.get(Calendar.HOUR_OF_DAY);
        final int mBegin = calendar.get(Calendar.MINUTE);
        calendar.add(Calendar.MINUTE, course.getDuration());

        if(hBegin < 10)
            return;

        final View beginingHourView = view.findViewWithTag("h" + hBegin + "Hour");

        ViewTreeObserver vto = beginingHourView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onGlobalLayout() {
                // Récupération des positions
                float x = beginingHourView.getX(), y = beginingHourView.getY();

                beginingHourView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                float offset = (float) mBegin / 60;
                int yBeginning = (int) (y + offset * beginingHourView.getHeight());
                int height = (int) (((float) course.getDuration() / 60 ) * beginingHourView.getHeight());


                View v = null;

                if(getActivity() != null) {
                    LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.cell_day_timeline_course, null);
                }

                if (v != null) {
                    ((TextView) v.findViewById(R.id.pupilName)).setText(course.getPupil().getFullName());
                    ((TextView) v.findViewById(R.id.coursesHours)).setText(course.getHoursSlot());
                }


                TextView state;
                if (v != null) {
                    state = (TextView) v.findViewById(R.id.state);
                    switch(course.getState()){
                        case Course.FORESEEN :
                            state.setText(C.FA_COURSE_FORESSEN);
                            state.setTextColor(getActivity().getResources().getColor(R.color.red500));
                            break;
                        case Course.WAITING_FOT_VALIDATION:
                            state.setText(C.FA_COURSE_WAITING_FOR_VALIDATION);
                            state.setTextColor(getActivity().getResources().getColor(R.color.them700));
                            break;
                        case Course.VALIDATED:
                            state.setText(C.FA_COURSE_VALIDATED);
                            state.setTextColor(getActivity().getResources().getColor(R.color.green500));
                            break;
                    }

                    v.setX(x);
                    v.setY(yBeginning);

                    ((ViewGroup)view.findViewById(R.id.timeline)).addView(v, ViewGroup.LayoutParams.MATCH_PARENT, height);

                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ActivityCourse.class);
                            intent.putExtra("course_id", course.getId());
                            getActivity().startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private void changeDay(int mod){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDay);
        calendar.add(Calendar.DAY_OF_YEAR, mod);
        mDay = calendar.getTime();
        fillViews();
    }


    // Interface

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nextDay :
                changeDay(1);
                break;
            case R.id.lastDay:
                changeDay(-1);
                break;
            case R.id.addACourse :
                Intent intent = new Intent(getActivity(), ActivityCourse.class);
                intent.putExtra(ActivityCourse.COURSE_ACTION, ActivityCourse.ADDING);
                intent.putExtra(ActivityCourse.COURSE_DAY, mDay.getTime());
                startActivity(intent);
                break;
        }
    }
}


