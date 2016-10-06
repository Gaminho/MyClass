package com.example.la.myclass.activities;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.activities.course.ActivityCourse;
import com.example.la.myclass.activities.devoir.ActivityDevoir;
import com.example.la.myclass.activities.pupil.ActivityPupil;
import com.example.la.myclass.adapters.AdapterSpinner;
import com.example.la.myclass.adapters.RecyclerViewCourses;
import com.example.la.myclass.adapters.RecyclerViewDevoirs;
import com.example.la.myclass.adapters.RecyclerViewPupil;
import com.example.la.myclass.adapters.SpinnerPupilWithPixAdapter;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.database.PupilsBDD;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gaminho on 29/09/2016.
 */
public abstract class AbstractFragmentList extends Fragment implements Animation.AnimationListener,
        View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        DatePickerDialog.OnDateSetListener{


    protected static final String FILTERG_WEEK = "Cette semaine";
    protected static final String FILTERG_MONTH = "Ce mois";
    protected static final String FILTERG_YEAR = "Cette année";
    protected static final String FILTERG_ALL = "Tous";

    /**
     * Common views
     */
    protected RecyclerView mListView;
    protected TextView mTextViewNoItem;
    protected ViewGroup mLLFiltersContainer, mCustomFilters;
    protected Button mOpenFilters;
    protected LinearLayout mContentView;
    protected TextView mButtonAddAnItem;
    protected TextView mFilteringCriteriaView;
    protected Button mDateFrom, mDateTo;
    protected DatePickerDialog mDatePickerDialog;
    protected LinearLayout mCustomDateLayout;
    protected CardView mCVRemoveFilters;

    /**
     * Manage animations to show / hide filters' layout
     */
    protected Animation mASlideDown, mASlideUp, mCustomDateSlideDown, mCustomDateSlideUp;

    /**
     * Used to determine the child type (course, pupil, devoir)
     */
    protected int mDataType;

    /**
     * Used to fill the listView
     */
    protected List<?> mList;

    /**
     * Used to filter items by date
     */
    protected Calendar mCalendar;

    /**
     * Default value for filtering dates
     */
    protected long mFilterDateValue =-1, mFilterBetweenDate = -1;

    /**
     * Default value for filtering criteria
     */
    protected List<String> mFilteringLabels;
    protected String mStringCriterias;

    /**
     * Flag to make difference between picking min date and max date
     */
    boolean isFromDate = true;

    /**
     * Used to stock min and max date (custom filtering)
     */
    protected long mFromDate, mToDate;


    /**
     * Fragment Life cycle
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mASlideUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_up);
        mASlideUp.setAnimationListener(this);
        mASlideDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_down);
        mASlideDown.setAnimationListener(this);
        mCustomDateSlideDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_down);
        mCustomDateSlideUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_up);
        mCustomDateSlideDown.setAnimationListener(this);
        mCustomDateSlideUp.setAnimationListener(this);
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        mFilteringLabels = new ArrayList<>();
        mFilteringLabels.add(0, FILTERG_ALL);
        mToDate = mFromDate = new Date().getTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_fragment_list, container, false);
        getAllViews(view);
        mContentView = (LinearLayout) setFilterContent(getActivity(), mCustomFilters);
        mCustomFilters.addView(mContentView);
        return view;
    }

    /**
     * Utils methods
     * @param view
     */
    protected void getAllViews(View view){
        mListView = (RecyclerView) view.findViewById(R.id.listItems);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTextViewNoItem = (TextView) view.findViewById(R.id.noItems);
        mTextViewNoItem.setVisibility(View.GONE);
        mLLFiltersContainer = (LinearLayout) view.findViewById(R.id.layFilters);
        mLLFiltersContainer.setVisibility(View.GONE);
        mCustomFilters = (LinearLayout) view.findViewById(R.id.customFilters);
        mOpenFilters = (Button) view.findViewById(R.id.moreFilter);
        mOpenFilters.setOnClickListener(this);
        mButtonAddAnItem = (TextView) view.findViewById(R.id.addAnItem);
        mButtonAddAnItem.setOnClickListener(this);
        ((RadioGroup) view.findViewById(R.id.rGroupDate)).setOnCheckedChangeListener(this);
        mFilteringCriteriaView = (TextView) view.findViewById(R.id.filtersList);
        mDateFrom = (Button) view.findViewById(R.id.pickFrom);
        mDateFrom.setOnClickListener(this);
        mDateTo = (Button) view.findViewById(R.id.pickTo);
        mDateTo.setOnClickListener(this);
        mCustomDateLayout = (LinearLayout) view.findViewById(R.id.customDateLayout);
        mCustomDateLayout.setVisibility(View.GONE);
        mCVRemoveFilters = (CardView) view.findViewById(R.id.removeFilter);
        mCVRemoveFilters.setOnClickListener(this);
        mCVRemoveFilters.setVisibility(View.GONE);
    }

    public void manageListView(List<?> list, RecyclerView listView, TextView textView){

        if(list.size()>0){
            listView.setVisibility(View.VISIBLE);
            listView.getAdapter().notifyDataSetChanged();
            textView.setVisibility(View.GONE);
        }

        else{
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }

        mStringCriterias = "";
        mCVRemoveFilters.setVisibility(View.GONE);
        if(mFilteringLabels.size() > 0) {
            for (String criteria : mFilteringLabels) {
                if(!"".equals(criteria))
                    mStringCriterias += criteria + " / ";
            }
        }
        else
            mStringCriterias = "Tous / ";

        if(!"Tous / ".equals(mStringCriterias))
            mCVRemoveFilters.setVisibility(View.VISIBLE);

        mStringCriterias = mStringCriterias.substring(0, mStringCriterias.lastIndexOf(" / "));
        mStringCriterias = String.format("%s (%d)", mStringCriterias, list.size());
        mFilteringCriteriaView.setText(mStringCriterias);
    }
    protected abstract void fillListView(RecyclerView recyclerView, TextView textViewNoItem);
    protected void removeFilters(){
        mList = getFilterList();
        manageListView(mList, mListView, mTextViewNoItem);
        ((RadioButton)mLLFiltersContainer.findViewById(R.id.rdAnywhen)).setChecked(true);
    };
    protected abstract View setFilterContent(Context context, ViewGroup container);
    protected abstract List<?> getFilterList();
    protected void setSpinnerContent(Spinner spinner, int idStringArray, AdapterView.OnItemSelectedListener listener){
        String[] spinnerContent = getResources().getStringArray(idStringArray);
        spinner.setAdapter(new AdapterSpinner(getActivity(), R.layout.adapter_cell_spinner_course, spinnerContent));
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(listener);
    }
    public void fillSpinnerPupils(final Spinner spinner, AdapterView.OnItemSelectedListener listener){
        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        List<Pupil> listPupils = pupilsBDD.getAllPupils();
        pupilsBDD.close();
        listPupils.add(0, new Pupil("Tous", 0, 0, 0, 0, "", 0, 0, 0, 0, ""));
        spinner.setAdapter(new SpinnerPupilWithPixAdapter(getActivity(), listPupils, true, getActivity().getResources().getColor(R.color.white)));
        spinner.setOnItemSelectedListener(listener);
    }

    /**
     * Interface animations
     * @param animation : the animation of openning/closing filters' layout
     */

    @Override
    public void onAnimationStart(Animation animation) {
        if(animation == mASlideDown) {
            mLLFiltersContainer.setVisibility(View.VISIBLE);
            mOpenFilters.setText("{fa-chevron-up}");
        }
        if(animation == mCustomDateSlideDown) {
            mCustomDateLayout.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onAnimationEnd(Animation animation) {
        if(animation == mASlideUp) {
            mLLFiltersContainer.setVisibility(View.GONE);
            mOpenFilters.setText("{fa-chevron-down}");
        }
        if(animation == mCustomDateSlideUp) {
            mCustomDateLayout.setVisibility(View.GONE);
        }
    }
    @Override
    public void onAnimationRepeat(Animation animation) {
        Log.e("ANIM", "onAnimationRepeat");
    }

    /**
     * Interface OnClick
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.moreFilter:
                if(mLLFiltersContainer.getVisibility() == View.VISIBLE) {
                    mLLFiltersContainer.startAnimation(mASlideUp);
                } else {
                    mLLFiltersContainer.setVisibility(View.VISIBLE);
                    mLLFiltersContainer.startAnimation(mASlideDown);
                }
                break;
            case R.id.addAnItem :
                //Intent intent = null;
                //if(mDataType == TYPE_COURSE) {
                    //if (mDataType == TYPE_COURSE) {
                    //    intent = new Intent(getActivity(), ActivityCourse.class);
                    //    intent.putExtra(ActivityCourse.COURSE_ACTION, ActivityCourse.ADDING);
                    //}
                    //if(mDataType == TYPE_PUPIL) {
                    //    intent = new Intent(getActivity(),ActivityPupil.class);
                    //    intent.putExtra(ActivityPupil.PUPIL_ACTION, ActivityPupil.ADDING);
                    //}
                    //if (mDataType == TYPE_DEVOIR) {
                    //    intent = new Intent(getActivity(), ActivityDevoir.class);
                    //    intent.putExtra(ActivityDevoir.DEVOIR_ACTION, ActivityDevoir.ADDING);
                    //}
                    //startActivity(intent);
                //}
                break;
            case R.id.pickFrom:
                isFromDate = true;
                mCalendar.setTimeInMillis(mFromDate);
                mDatePickerDialog = new DatePickerDialog(
                        getActivity(), this,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.getDatePicker().setMaxDate(mToDate);
                mDatePickerDialog.show();
                break;
            case R.id.pickTo:
                isFromDate = false;
                mCalendar.setTimeInMillis(mToDate);
                mDatePickerDialog = new DatePickerDialog(
                        getActivity(), this,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.getDatePicker().setMinDate(mFromDate);
                mDatePickerDialog.show();
                break;
            case R.id.removeFilter:
                removeFilters();
                break;
        }
    }

    /**
     * Interface OnCheckedCHangeListener
     * @param radioGroup
     * @param radioButtonID
     */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
        if(radioGroup.getId() == R.id.rGroupDate){
            Animation animation;
            String filterDateLabel;

            mCalendar.setTimeInMillis(new Date().getTime());
            mCalendar.set(Calendar.HOUR_OF_DAY, 0);
            mCalendar.set(Calendar.MINUTE, 0);
            mCalendar.set(Calendar.SECOND, 0);

            animation = mCustomDateSlideUp;
            mFilterBetweenDate = 1;
            mFilterDateValue = -1;

            switch(radioButtonID) {
                case R.id.rdWeek:
                    mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    mFromDate = mCalendar.getTimeInMillis();
                    mCalendar.add(Calendar.DAY_OF_YEAR, 7);
                    mToDate = mCalendar.getTimeInMillis();
                    mFilterBetweenDate = 1;
                    mFilterDateValue = -1;
                    filterDateLabel = FILTERG_WEEK;
                    break;
                case R.id.rdMonth:
                    mCalendar.set(Calendar.DAY_OF_MONTH, 1);
                    mFromDate = mCalendar.getTimeInMillis();
                    mCalendar.add(Calendar.MONTH, 1);
                    mToDate = mCalendar.getTimeInMillis();
                    mFilterBetweenDate = 1;
                    mFilterDateValue = -1;
                    filterDateLabel = FILTERG_MONTH;
                    break;
                case R.id.rdYear:
                    mCalendar.set(Calendar.MONTH, Calendar.JULY);
                    mCalendar.set(Calendar.DAY_OF_MONTH, 31);
                    mFromDate = mCalendar.getTimeInMillis();
                    mCalendar.add(Calendar.YEAR, 1);
                    mCalendar.add(Calendar.DAY_OF_YEAR,-1);
                    mToDate = mCalendar.getTimeInMillis();
                    mFilterBetweenDate = 1;
                    mFilterDateValue = -1;
                    filterDateLabel = FILTERG_YEAR;
                    break;
                case R.id.rdCustom:
                    animation = mCustomDateSlideDown;
                    filterDateLabel = "Perso";
                    mToDate = mFromDate = new Date().getTime();
                    mDateTo.setText(C.formatDate(mToDate, C.DD_MM_YY));
                    mDateFrom.setText(C.formatDate(mFromDate, C.DD_MM_YY));
                    break;
                default:
                    animation = mCustomDateSlideUp;
                    mCalendar.set(Calendar.YEAR, 0);
                    mFilterDateValue = mCalendar.getTimeInMillis();
                    mFilterBetweenDate = -1;
                    filterDateLabel = "Tous";
                    break;
            }

            mFilteringLabels.set(0, filterDateLabel);
            mCustomDateLayout.startAnimation(animation);
            mList = getFilterList();
            manageListView(mList, mListView, mTextViewNoItem);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH,monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String labelButton = String.format("%02d/%02d/%02d", dayOfMonth, monthOfYear + 1, year - 2000);
        if(isFromDate){
            mDateFrom.setText(labelButton);
            mFromDate = calendar.getTimeInMillis();
        }
        else{
            mDateTo.setText(labelButton);
            mToDate = calendar.getTimeInMillis();
        }

        mList = getFilterList();
        manageListView(mList, mListView, mTextViewNoItem);
        mDatePickerDialog.dismiss();
    }

}
