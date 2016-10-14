package com.example.la.myclass.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.adapters.RecyclerViewWeek;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.database.DevoirBDD;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.periodic.Week;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Creation : 02/10/2015.
 */
public class FragmentHome extends Fragment {

    /**
     * Views
     */
    protected RecyclerView mListViewWeekDays;
    protected TextView mTextViewCours, mTextViewDevoirs, mTextViewMoney, mTextViewCountDown;


    /**
     * Variables de classe
     */
    protected Week mActualWeek;
    protected List<Course> mListCourses;
    protected CountDownTimer mCountDownTimer;


    /**
     * Fragment Life Cycle
     */
    public static FragmentHome newInstance() {
        return new FragmentHome();
    }
    public FragmentHome() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActualWeek = new Week();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getAllViews(view);

        fillListWeekViews();
        fillBottomBar();
        setUpTimer();

        return view;
    }





    // Utils

    public void getAllViews(View view){

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mListViewWeekDays = (RecyclerView) view.findViewById(R.id.weekList);
        mListViewWeekDays.setLayoutManager(llm);

        mTextViewCours = (TextView) view.findViewById(R.id.cours);
        mTextViewDevoirs = (TextView) view.findViewById(R.id.devoirs);
        mTextViewMoney = (TextView) view.findViewById(R.id.money);
        mTextViewCountDown = (TextView) view.findViewById(R.id.timer);

    }

    public void fillBottomBar(){


        mTextViewCours.setText(String.format(Locale.FRANCE, "%d", mListCourses.size()));

        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        mTextViewDevoirs.setText(String.format(Locale.FRANCE, "%d",
                devoirBDD.getListDevoirForAWeek(System.currentTimeMillis()).size()));
        devoirBDD.close();

        double money = 0;
        for(Course course : mListCourses)
            money +=  course.getMoney();

        mTextViewMoney.setText(String.format(Locale.FRANCE, "%.2f", money));

    }




    public void fillListWeekViews() {
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        mListCourses = coursesBDD.getListCourseForAWeek(System.currentTimeMillis());
        coursesBDD.close();
        mListViewWeekDays.setAdapter(new RecyclerViewWeek(getActivity(), mActualWeek, mListCourses));
    }

    public void setUpTimer() {

        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        List<Course> list = coursesBDD.getCoursesWithState(Course.FORESEEN);
        coursesBDD.close();

        if (list .size()>0) {
            Course course = list.get(0);

            long milliseconds = course.getDate() - new Date().getTime();

            mCountDownTimer = new CountDownTimer(milliseconds, C.SECOND) {
                @Override
                public void onTick(long milliseconds) {
                    mTextViewCountDown.setText(C.formatDate(milliseconds, C.dd_HH_mm_ss));
                }

                @Override
                public void onFinish() {
                    mTextViewCountDown.setText("-");
                }
            }.start();
        }
    }

}
