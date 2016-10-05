package com.example.la.myclass.activities.devoir;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.database.DevoirBDD;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Léa on 06/10/2015.
 */
public class FragmentAddOrEditDevoir extends FragmentAddOrEditDefault implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    /**
     * BUNDLE VARIABLES
     */
    protected static final String DEVOIR_ID = "devoir_id";

    /**
     * STATIC VARIABLES
     */
    protected static final int EDITING = 1;
    protected static final int ADDING = 0;

    /**
     * Variables de classe
     */
    protected Devoir mDevoir;
    protected Calendar mCalendar;
    protected FragmentDetailsDevoir.DevoirDetailsInterface mListener;
    protected int mCurrentMod;

    /**
     * Fragment Life Cycle
     * @return
     */
    public static FragmentAddOrEditDevoir newInstance() {
        return new FragmentAddOrEditDevoir();
    }
    public static FragmentAddOrEditDevoir newInstance(int devoirID) {
        FragmentAddOrEditDevoir fragmentAddOrEditDevoir = new FragmentAddOrEditDevoir();
        Bundle args = new Bundle();
        args.putInt(DEVOIR_ID, devoirID);
        fragmentAddOrEditDevoir.setArguments(args);
        return fragmentAddOrEditDevoir;
    }
    public FragmentAddOrEditDevoir() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        mDevoir = new Devoir();
        mDevoir.setDate(mCalendar.getTimeInMillis());

        if(getArguments() != null && getArguments().getInt(DEVOIR_ID,-1) != -1) {
            mDevoir = FragmentDetailsDevoir.getDevoirWithId(getActivity(), getArguments().getInt(DEVOIR_ID));
            mCalendar.setTimeInMillis(mDevoir.getDate());
            mCurrentMod = EDITING;
        }
        else
            mCurrentMod = ADDING;
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
            mListener = (FragmentDetailsDevoir.DevoirDetailsInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement DevoirDetailsInterface.");
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
        View view = inflater.inflate(R.layout.adding_devoir, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerPupils);
        fillSpinnerPupils(spinner, this, getResources().getColor(R.color.textColor));
        ((RadioGroup) view.findViewById(R.id.rGroupTypeDevoir)).setOnCheckedChangeListener(this);

        if(mCurrentMod == EDITING) {
            spinner.setSelection(((SpinnerPupilWithPixAdapter) spinner.getAdapter()).getSelectionWithPupilID(mDevoir.getPupilID()));
            EditText editText = ((EditText) view.findViewById(R.id.theme));
            editText.setText(mDevoir.getTheme());
            editText = ((EditText) view.findViewById(R.id.memo));
            editText.setText(mDevoir.getCommentaire());
            editText = (EditText)view.findViewById(R.id.note);

            if(mDevoir.getNote()>0)
                editText.setText(String.format("%.2f", mDevoir.getNote()));

            editText = ((EditText) view.findViewById(R.id.bareme));
            editText.setText(String.format("%2d", mDevoir.getBarem()));

        }

        switch(mDevoir.getType()){
            case Devoir.DM:
                ((RadioButton)view.findViewById(R.id.rbDM)).setChecked(true);
                break;
            case Devoir.INTERRO:
                ((RadioButton)view.findViewById(R.id.rbInterro)).setChecked(true);
                break;
            default:
                ((RadioButton)view.findViewById(R.id.rbDST)).setChecked(true);
                break;
        }

        view.findViewById(R.id.pickDate).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.day)).setText(C.formatDate(mCalendar.getTimeInMillis(), C.DAY_DATE_D_MONTH_YEAR));
        return view;
    }

    @Override
    protected void setLeftAction(LinearLayout leftLayout, TextView leftLabel, TextView leftIcon) {
        if(mCurrentMod == EDITING){
            leftLabel.setText("Supprimer");
            leftIcon.setText("{fa-trash-o} ");
            leftLayout.setOnClickListener(this);
            leftLayout.setVisibility(View.VISIBLE);
        }
        else
            leftLayout.setVisibility(View.GONE);
    }

    @Override
    protected void setRightAction(LinearLayout rightLayout, TextView rightLabel, TextView rightIcon) {
        if(mCurrentMod == EDITING) {
            rightLayout.setVisibility(View.VISIBLE);
            rightLayout.setOnClickListener(this);
            rightLabel.setText("Valider");
            rightIcon.setText("{fa-check} ");
        }
    }


    /**
     * Utils
     * @param devoir the devoir meant to be insert into database
     */
    public void addDevoirToBdd(Devoir devoir){
        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        devoirBDD.insertDevoir(devoir);
        devoirBDD.close();

        Toast.makeText(getActivity(), "Devoir ajouté", Toast.LENGTH_SHORT).show();
    }

    public void updateDevoirInBdd(Devoir devoir) {
        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        devoirBDD.updateDevoir(devoir.getId(), devoir);
        devoirBDD.close();

        Toast.makeText(getActivity(), "Devoir mis à jour", Toast.LENGTH_SHORT).show();
    }

    public void removeCourse(int id){
        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        devoirBDD.removeDevoirWithID(id);
        devoirBDD.close();
        mListener.goBack();
    }

    public void setDialogDeleteDevoir() {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View adv = factory.inflate(R.layout.dialog_delete, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(adv);

        final AlertDialog createDialog = adb.create();
        createDialog.show();


        ((TextView) createDialog.findViewById(R.id.messageDialog)).setText("Voulez-vous vraiment supprimer ce devoir ?");

        createDialog.findViewById(R.id.valid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCourse(mDevoir.getId());
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

    public boolean areInformationValid(){

        getDevoirFromInformation();
        EditText editText = ((EditText) mContentView.findViewById(R.id.theme));
        if(editText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Précisez le chapitre", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            mDevoir.setTheme(editText.getText().toString());

        editText = ((EditText) mContentView.findViewById(R.id.bareme));
        if("".equals(editText.getText().toString())) {
            Toast.makeText(getActivity(), "Précisez le barême", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            mDevoir.setBarem(Integer.parseInt(editText.getText().toString()));

        return true;
    }

    public Devoir getDevoirFromInformation(){

        mDevoir.setNote(0);

        if(mDevoir.getDate() < new Date().getTime())
            mDevoir.setState(Devoir.STATE_DONE);

        EditText editText;
        editText = (EditText)mContentView.findViewById(R.id.theme);

        mDevoir.setTheme(editText.getText().toString());

        editText = (EditText)mContentView.findViewById(R.id.memo);
        mDevoir.setCommentaire(editText.getText().toString());

        editText = (EditText)mContentView.findViewById(R.id.note);
        if(!editText.getText().toString().equals(""))
            mDevoir.setNote(Double.parseDouble(editText.getText().toString()));

        return mDevoir;

    }

    /**
     * Implementing Interfaces methods
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.leftAction:
                setDialogDeleteDevoir();
                break;
            case R.id.rightAction:
                if(areInformationValid()) {
                    Log.e("LOGYGOL", "Mode : " + mCurrentMod + " x " + mDevoir.toString());
                    if(mCurrentMod == ADDING)
                        addDevoirToBdd(mDevoir);
                    else if(mCurrentMod == EDITING)
                        updateDevoirInBdd(mDevoir);
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
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        ((TextView) mContentView.findViewById(R.id.day)).setText(C.formatDate(mCalendar.getTimeInMillis(), C.DAY_DATE_D_MONTH_YEAR));
        mDevoir.setDate(mCalendar.getTimeInMillis());
        mDatePickerDialog.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinnerPupils) {
            Pupil pupil = (Pupil) adapterView.getAdapter().getItem(i);
            mDevoir.setPupilID(pupil.getId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
        if(radioGroup.getId() == R.id.rGroupTypeDevoir){
            switch(radioButtonID){
                case R.id.rbInterro:
                    mDevoir.setType(Devoir.INTERRO);
                    break;
                case R.id.rbDM:
                    mDevoir.setType(Devoir.DM);
                    break;
                default:
                    mDevoir.setType(Devoir.DST);
                    break;
            }
        }
    }
}

