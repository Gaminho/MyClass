package com.example.la.myclass.activities.course;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.activities.FragmentAddOrEditDefault;
import com.example.la.myclass.adapters.SpinnerPupilWithPixAdapter;
import com.example.la.myclass.beans.Course;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.database.PupilsBDD;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Léa on 28/09/2015.
 */
public class FragmentAddOrEditCourse extends FragmentAddOrEditDefault implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    /**
     * BUNDLE VARIABLES
     */
    protected static final String COURSE_ID = "course_id";
    public static final String TIME_IN_MILLIS = "time_in_millis";

    /**
     * STATIC VARIABLES
     */
    protected static final int EDITING = 1;
    protected static final int ADDING = 0;

    /**
     * Variables de classe
     */
    private Course mCourse;
    private Calendar mCalendar;
    protected int mCurrentMod;
    protected FragmentDetailsCourse.CourseDetailsInterface mListener;

    /**
     * Fragment Life Cycle
     * @return
     */
    public static FragmentAddOrEditCourse newInstance() {
        return new FragmentAddOrEditCourse();
    }
    public static FragmentAddOrEditCourse newInstance(long dayInMillis) {
        FragmentAddOrEditCourse fragmentDay = new FragmentAddOrEditCourse();
        Bundle args = new Bundle();
        args.putLong(TIME_IN_MILLIS, dayInMillis);
        fragmentDay.setArguments(args);
        return fragmentDay;
    }
    public static FragmentAddOrEditCourse newInstance(int courseID) {
        FragmentAddOrEditCourse fragmentAddOrEditCourse = new FragmentAddOrEditCourse();
        Bundle args = new Bundle();
        args.putInt(COURSE_ID, courseID);
        fragmentAddOrEditCourse.setArguments(args);
        return fragmentAddOrEditCourse;
    }
    public FragmentAddOrEditCourse() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        mCourse = new Course();
        mCourse.setDate(mCalendar.getTimeInMillis());
        mCourse.setDuration(Course.DURATION_1H);
        mCourse.setState(Course.FORESEEN);
        mCurrentMod = ADDING;

        if (getArguments() != null) {
            if(getArguments().getLong(TIME_IN_MILLIS,-1) != -1)
                mCalendar.setTimeInMillis(getArguments().getLong(TIME_IN_MILLIS));

            if (getArguments().getInt(COURSE_ID,-1) != -1){
                mCourse = FragmentDetailsCourse.getCourseWithId(getActivity(), getArguments().getInt(COURSE_ID));
                mCalendar.setTimeInMillis(mCourse.getDate());
                mCurrentMod = EDITING;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setContent(getActivity(), mScrollView);
        return view;
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

    /**
     * Implementing abstracts functions from FragmentListDefault
     */
    @Override
    protected View setContent(Context context, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adding_course, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerPupils);
        fillSpinnerPupils(spinner, this, getResources().getColor(R.color.textColor));

        if(mCurrentMod == EDITING) {
            spinner.setSelection(((SpinnerPupilWithPixAdapter) spinner.getAdapter()).getSelectionWithPupilID(mCourse.getPupilID()));
            EditText editText = ((EditText) view.findViewById(R.id.money));
            editText.setText(String.format("%2.2f €", mCourse.getMoney()));
            editText = ((EditText) view.findViewById(R.id.memo));
            editText.setText(mCourse.getMemo());
            editText = ((EditText) view.findViewById(R.id.theme));
            editText.setText(mCourse.getTheme());
            view.findViewById(R.id.remarque).setVisibility(View.VISIBLE);

            switch(mCourse.getDuration()){
                case Course.DURATION_1H30:
                    ((RadioButton)view.findViewById(R.id.rb90)).setChecked(true);
                    break;
                case Course.DURATION_2H:
                    ((RadioButton)view.findViewById(R.id.rb120)).setChecked(true);
                    break;
                default:
                    ((RadioButton)view.findViewById(R.id.rb60)).setChecked(true);
                    break;
            }
        }

        view.findViewById(R.id.pickDate).setOnClickListener(this);
        view.findViewById(R.id.pickHour).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.day)).setText(C.formatDate(mCalendar.getTimeInMillis(), C.DAY_DATE_D_MONTH_YEAR));
        ((TextView) view.findViewById(R.id.hour)).setText(mCourse.getHoursSlot());
        ((RadioGroup) view.findViewById(R.id.rGroupDuration)).setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    protected void setLeftAction(LinearLayout leftLayout, TextView leftLabel, TextView leftIcon) {
        leftLayout.setVisibility(View.VISIBLE);
        leftLayout.setOnClickListener(this);
        if(mCurrentMod == ADDING) {
            leftLabel.setText("Commentaires");
            leftIcon.setText("{fa-bookmark} ");
        }
        else if(mCurrentMod == EDITING){
            leftLabel.setText("Supprimer");
            leftIcon.setText("{fa-trash-o} ");
        }
    }

    @Override
    protected void setRightAction(LinearLayout rightLayout, TextView rightLabel, TextView rightIcon) {
        rightLayout.setVisibility(View.VISIBLE);
        rightLayout.setOnClickListener(this);
        rightLabel.setText("Valider");
        rightIcon.setText("{fa-check} ");
    }

    /**
     * Utils
     * @param course the course meant to be insert into database
     */
    public void addCourseToBdd(Course course){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        coursesBDD.insertCourse(course);
        coursesBDD.close();
        Toast.makeText(getActivity(), "Cours ajouté", Toast.LENGTH_SHORT).show();
    }

    public void updateCourseInBdd(Course course){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        coursesBDD.updateCourse(course.getId(), course);
        coursesBDD.close();
        Toast.makeText(getActivity(), "Cours mis à jour", Toast.LENGTH_SHORT).show();
    }

    public void removeCourse(int id){
        CoursesBDD coursesBDD = new CoursesBDD(getActivity());
        coursesBDD.open();
        coursesBDD.removeCourseWithID(id);
        coursesBDD.close();
        mListener.goBack();
    }

    public void setDialogDeleteCourse() {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View adv = factory.inflate(R.layout.dialog_delete, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(adv);

        final AlertDialog createDialog = adb.create();
        createDialog.show();


        ((TextView) createDialog.findViewById(R.id.messageDialog)).setText("Voulez-vous vraiment supprimer ce cours ?");

        createDialog.findViewById(R.id.valid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCourse(mCourse.getId());
                createDialog.dismiss();
            }
        });

        createDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog.dismiss();
            }
        });
    }

    public double foressenPrice(){

            PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
            pupilsBDD.open();
            double price = pupilsBDD.getPupilWithId(mCourse.getPupilID()).getPrice();
            pupilsBDD.close();

            if (((RadioButton)mContentView.findViewById(R.id.rb90)).isChecked())
                return price * 1.5;
            else if (((RadioButton)mContentView.findViewById(R.id.rb120)).isChecked())
                return price * 2;
            else
                return price;
    }

    public boolean areInformationValid(){
        getCourseFromInformation();
        return true;
    }

    public Course getCourseFromInformation(){

        if(mCourse.getDate() > new Date().getTime())
            mCourse.setState(Course.FORESEEN);

        EditText editText;

        editText = (EditText) mContentView.findViewById(R.id.money);
        mCourse.setMoney(Double.parseDouble(editText.getText().toString().replace(",", ".")));

        editText = (EditText) mContentView.findViewById(R.id.memo);
        mCourse.setMemo(editText.getText().toString());

        editText = (EditText) mContentView.findViewById(R.id.theme);
        mCourse.setTheme(editText.getText().toString());

        return mCourse;
    }


    /**
     * Implementing Interfaces methods
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.leftAction:
                if(mCurrentMod == ADDING) {
                    if (mContentView.findViewById(R.id.remarque).getVisibility() == View.GONE)
                        mContentView.findViewById(R.id.remarque).setVisibility(View.VISIBLE);
                    else
                        mContentView.findViewById(R.id.remarque).setVisibility(View.GONE);
                }
                else if (mCurrentMod == EDITING)
                    setDialogDeleteCourse();
                break;
            case R.id.rightAction:
                if(areInformationValid()) {
                    if(mCurrentMod == ADDING)
                        addCourseToBdd(mCourse);
                    else if(mCurrentMod == EDITING)
                        updateCourseInBdd(mCourse);
                    mListener.goBack();
                }
                break;
            case R.id.pickDate :
                mDatePickerDialog = new DatePickerDialog(getActivity(), this,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
                break;
            case R.id.pickHour :
                mTimePickerDialog = new TimePickerDialog(getActivity(), this, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
                mTimePickerDialog.show();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        ((TextView) mContentView.findViewById(R.id.day)).setText(C.formatDate(mCalendar.getTimeInMillis(), C.DAY_DATE_D_MONTH));
        mCourse.setDate(mCalendar.getTimeInMillis());
        mDatePickerDialog.dismiss();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        mCourse.setDate(mCalendar.getTimeInMillis());

        ((TextView) mContentView.findViewById(R.id.hour)).setText(mCourse.getHoursSlot());
        mTimePickerDialog.dismiss();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
        if (radioGroup.getId() == R.id.rGroupDuration) {
            if (radioButtonID == R.id.rb90)
                mCourse.setDuration(Course.DURATION_1H30);
            if (radioButtonID == R.id.rb120)
                mCourse.setDuration(Course.DURATION_2H);
            if (radioButtonID == R.id.rb60)
                mCourse.setDuration(Course.DURATION_1H);

            ((EditText) mContentView.findViewById(R.id.money)).setText(String.format("%.2f", foressenPrice()));
            ((TextView) mContentView.findViewById(R.id.hour)).setText(mCourse.getHoursSlot());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinnerPupils) {
            Pupil pupil = (Pupil) adapterView.getAdapter().getItem(i);
            mCourse.setPupilID(pupil.getId());
            ((EditText) mContentView.findViewById(R.id.money)).setText(String.format("%.2f", foressenPrice()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}

