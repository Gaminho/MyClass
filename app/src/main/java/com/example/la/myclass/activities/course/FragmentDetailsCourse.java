package com.example.la.myclass.activities.course;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.beans.Course;

import java.util.Date;

/**
 * Created by Léa on 01/10/2015.
 */
public class FragmentDetailsCourse extends Fragment implements View.OnClickListener {

    /**
     * BUNDLE VARIABLES
     */
    // TODO : make it extend @AbstractFragmentDetails
    static final String COURSE_ID = "course_id";

    // Views
    protected TextView mTextViewPupilName, mTextViewDate, mTextViewHour, mTextViewMoney;
    protected TextView mTextViewTheme, mTextViewMemo;
    protected LinearLayout mButtonEditCourse, mButtonValidCourse;
    protected LinearLayout mLinearLayoutRemarques, mLinearLayoutChapitre, mLinearLayoutRemar;

    //Variables de classe
    protected Course mCourse;

    //Interface
    protected CourseDetailsInterface mListener;


    // Fragment life cycle

    public static FragmentDetailsCourse newInstance(int id) {
        FragmentDetailsCourse fragmentDetailsCourse = new FragmentDetailsCourse();
        Bundle args = new Bundle();
        args.putInt(COURSE_ID, id);
        fragmentDetailsCourse.setArguments(args);
        return fragmentDetailsCourse;
    }
    public FragmentDetailsCourse() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().get(COURSE_ID) != null) {
            mCourse = getCourseWithId(getActivity(), getArguments().getInt(COURSE_ID));
            Log.e("DETAILS COURSE", "" + new Date(mCourse.getDate()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.course_fragment_details, container, false);
        getAllViews(view);
//      TODO : clean up xml view
        if(mCourse != null)
            fillCourseDetails(mCourse);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CourseDetailsInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement CourseDetailsInterface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    // Utils

    public void getAllViews(View view){
        mTextViewPupilName = (TextView) view.findViewById(R.id.pupilName);
        mTextViewDate = (TextView) view.findViewById(R.id.date);
        mTextViewMoney = (TextView) view.findViewById(R.id.money);
        mTextViewHour = (TextView) view.findViewById(R.id.hour);

        mButtonEditCourse = (LinearLayout) view.findViewById(R.id.edit);
        mButtonEditCourse.setOnClickListener(this);

        mButtonValidCourse = (LinearLayout) view.findViewById(R.id.validCourse);
        mButtonValidCourse.setOnClickListener(this);

        mTextViewTheme = (TextView) view.findViewById(R.id.theme);
        mTextViewMemo = (TextView) view.findViewById(R.id.memo);

        mLinearLayoutRemarques = (LinearLayout) view.findViewById(R.id.remarque);

        mLinearLayoutChapitre = (LinearLayout) view.findViewById(R.id.chapitreLayout);
        mLinearLayoutChapitre.setVisibility(View.GONE);
        mLinearLayoutRemar = (LinearLayout) view.findViewById(R.id.remarqueLayout);
        mLinearLayoutRemar.setVisibility(View.GONE);

    }

    public void fillCourseDetails(Course course){

        mTextViewPupilName.setText(course.getPupil().getFullName());
        mTextViewDate.setText(C.formatDate(course.getDate(), C.DAY_DATE_D_MONTH_YEAR));
        mTextViewHour.setText(course.getHoursSlot());
        mTextViewMoney.setText(String.format("%2.2f €", course.getMoney()));

        if(!"".equals(course.getTheme())) {
            mTextViewTheme.setText(course.getTheme());
            mLinearLayoutChapitre.setVisibility(View.VISIBLE);
        }

        if(!"".equals(course.getMemo())) {
            mTextViewMemo.setText(course.getMemo());
            mLinearLayoutRemar.setVisibility(View.VISIBLE);
        }

        if("".equals(course.getMemo()) && "".equals(course.getTheme()))
            mLinearLayoutRemarques.setVisibility(View.GONE);

        if(course.getState() != Course.WAITING_FOT_VALIDATION)
            mButtonValidCourse.setVisibility(View.GONE);
    }


    public static Course getCourseWithId(Context context, int id){
        CoursesBDD coursesBDD = new CoursesBDD(context);
        coursesBDD.open();
        Course course = coursesBDD.getCourseWithId(id);
        coursesBDD.close();
        return course;
    }


    // Interface

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.edit :
                getFragmentManager().beginTransaction().replace(R.id.default_container, FragmentAddOrEditCourse.newInstance(mCourse.getId())).commit();
                break;
            case R.id.validCourse :
                updateCourseInBdd(mCourse);
                Toast.makeText(getActivity(), "Cours validé", Toast.LENGTH_SHORT).show();
                mListener.goBack();
                break;
            case R.id.cancel :
                mListener.goBack();
                break;
            }
    }

    public void updateCourseInBdd(Course course){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        mCourse.setState(Course.VALIDATED);
        coursesBDD.updateCourse(mCourse.getId(), course);
        coursesBDD.close();
    }

    // Interface

    public interface CourseDetailsInterface{
        void goBack();
    }



}
