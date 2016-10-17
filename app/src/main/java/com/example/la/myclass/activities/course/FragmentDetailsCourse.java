package com.example.la.myclass.activities.course;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.activities.AbstractFragmentDetails;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.database.CoursesBDD;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * 17/10/2016.
 */

public class FragmentDetailsCourse extends AbstractFragmentDetails implements View.OnClickListener {

    /**
     * Variables de classe
     */
    protected Course mCourse;
    protected FragmentDetailsCourse.CourseDetailsInterface mListener;

    /**
     * Fragment Life Cycle
     */
    public static FragmentDetailsCourse newInstance(int courseID) {
        FragmentDetailsCourse fragmentDetailsCourse = new FragmentDetailsCourse();
        Bundle args = new Bundle();
        args.putInt(ActivityCourse.COURSE_ID, courseID);
        fragmentDetailsCourse.setArguments(args);
        return fragmentDetailsCourse;
    }
    public FragmentDetailsCourse() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().get(ActivityCourse.COURSE_ID) != null)
            mCourse = CoursesBDD.getCourseWithId(getActivity(), getArguments().getInt(ActivityCourse.COURSE_ID));

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentDetailsCourse.CourseDetailsInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement CourseDetailsInterface.");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    protected View setContent(Context context, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.course_fragment_details, container, false);

        ((TextView) view.findViewById(R.id.pupilName)).setText(mCourse.getPupil().getFullName());
        ((TextView) view.findViewById(R.id.date)).setText(C.formatDate(mCourse.getDate(), C.DAY_DATE_D_MONTH_YEAR));
        ((TextView) view.findViewById(R.id.money)).setText(mCourse.getHoursSlot());
        ((TextView) view.findViewById(R.id.hour)).setText(String.format(Locale.FRANCE, "%2.2f €", mCourse.getMoney()));

        ((TextView) view.findViewById(R.id.theme)).setText(String.format(Locale.FRANCE, "%2.2f €", mCourse.getMoney()));
        ((TextView) view.findViewById(R.id.memo)).setText(String.format(Locale.FRANCE, "%2.2f €", mCourse.getMoney()));


        view.findViewById(R.id.chapitreLayout).setVisibility(View.GONE);
        view.findViewById(R.id.remarqueLayout).setVisibility(View.GONE);

        if(!"".equals(mCourse.getTheme())) {
            ((TextView) view.findViewById(R.id.theme)).setText(mCourse.getTheme());
            view.findViewById(R.id.chapitreLayout).setVisibility(View.VISIBLE);
        }

        if(!"".equals(mCourse.getMemo())) {
            ((TextView) view.findViewById(R.id.memo)).setText(mCourse.getMemo());
            view.findViewById(R.id.remarqueLayout).setVisibility(View.VISIBLE);
        }

        if("".equals(mCourse.getMemo()) && "".equals(mCourse.getTheme()))
            view.findViewById(R.id.remarque).setVisibility(View.GONE);

        if(mCourse.getState() != Course.WAITING_FOT_VALIDATION)
            mRightAction.setVisibility(View.GONE);

        return view;
    }

    @Override
    protected void setLeftAction(LinearLayout leftLayout, TextView leftLabel, TextView leftIcon) {
        leftLayout.setVisibility(View.VISIBLE);
        leftLabel.setText("Modifier");
        leftIcon.setText("{fa-edit} ");
        leftLayout.setOnClickListener(this);
    }

    @Override
    protected void setRightAction(LinearLayout rightLayout, TextView rightLabel, TextView rightIcon) {
        rightLayout.setVisibility(View.VISIBLE);
        rightLabel.setText("Valider");
        rightIcon.setText("{fa-check} ");
        rightLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.leftAction :
                getFragmentManager().beginTransaction().replace(R.id.default_container, FragmentAddOrEditCourse.newInstance(mCourse.getId())).commit();
                break;
            case R.id.rightAction :
                CoursesBDD.changeCourseState(getActivity(), mCourse, Course.VALIDATED);
                Toast.makeText(getActivity(), "Cours validé", Toast.LENGTH_SHORT).show();
                mListener.goBack();
                break;
        }
    }

    public interface CourseDetailsInterface{
        void goBack();
    }
}
