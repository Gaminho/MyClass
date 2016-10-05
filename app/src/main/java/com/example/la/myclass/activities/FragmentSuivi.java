package com.example.la.myclass.activities;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.adapters.SpinnerPupilWithPixAdapter;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.customviews.GraphicView;
import com.example.la.myclass.database.CoursesBDD;
import com.example.la.myclass.database.DevoirBDD;
import com.example.la.myclass.database.PupilsBDD;

import java.io.File;
import java.util.List;

/**
 * Created by Gaminho on 28/09/2016.
 */
public class FragmentSuivi extends Fragment implements View.OnClickListener {

    // BUNDLE
    public static final String PUPIL_ID = "pupil_id";

    // Views
    protected Spinner mSpinnerPupil;
    protected GraphicView mGraphicView;

    // Variables de classe
    protected List<Devoir> mListDevoir;
    protected Pupil mPupil;
    protected TextView mTextViewNumberOfCourse, mTextViewSince, mTextViewNumberOfDevoirs;
    protected TextView mTVNoInfos;
    protected ImageView mIVAvatar;

    // Fragment life cycle
    public static FragmentSuivi newInstance() {
        return new FragmentSuivi();
    }
    public static FragmentSuivi newInstance(int pupilID) {
        FragmentSuivi fragmentSuivi = new FragmentSuivi();
        Bundle args = new Bundle();
        args.putInt(PUPIL_ID, pupilID);
        fragmentSuivi.setArguments(args);
        return fragmentSuivi;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().getInt(PUPIL_ID,-1) != -1)
            mPupil = getPupilWithID(getArguments().getInt(PUPIL_ID,-1));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_suivi, container, false);
        getAllViews(view);
        fillViews(mPupil);
        return view;
    }


    // Utils

    public void getAllViews(View v){
        mSpinnerPupil = (Spinner) v.findViewById(R.id.filterItem);
        mGraphicView = (GraphicView) v.findViewById(R.id.graph);
        mTextViewNumberOfCourse = (TextView) v.findViewById(R.id.numberOfCourses);
        mTextViewNumberOfDevoirs = (TextView) v.findViewById(R.id.numberOfDevoirs);
        mTextViewSince = (TextView) v.findViewById(R.id.since);
        mTVNoInfos = (TextView) v.findViewById(R.id.noInformation);
        mIVAvatar = (ImageView) v.findViewById(R.id.avatar);
        v.findViewById(R.id.openSpinner).setOnClickListener(this);
    }

    public void fillViews(Pupil pupil){
        fillSpinnerPupils(mSpinnerPupil, pupil);
        mSpinnerPupil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pupilID = (int) mSpinnerPupil.getAdapter().getItemId(position);
                mPupil = getPupilWithID(pupilID);
                mListDevoir = getDevoirWithPupilID(pupilID);
                fillGraphic(mListDevoir);
                fillPupilInfos(mPupil);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if(mPupil != null){
            mListDevoir = getDevoirWithPupilID(mPupil.getId());
            fillGraphic(mListDevoir);
            fillPupilInfos(mPupil);
        }
    }

    public void fillSpinnerPupils(Spinner spinner, Pupil pupil){

        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        List<Pupil> listPupils = pupilsBDD.getAllPupils();
        pupilsBDD.close();

        int selectedItem = 0;
        if(pupil != null) {
            for (int i = 0; i < listPupils.size(); i++) {
                if (pupil.getId() == listPupils.get(i).getId())
                    selectedItem = i;
            }
        }

        spinner.setAdapter(new SpinnerPupilWithPixAdapter(getActivity(), listPupils, false, getActivity().getResources().getColor(R.color.white)));
        spinner.setSelection(selectedItem);
    }

    public void fillGraphic(List<Devoir> devoirs){
        if(devoirs.size()>0) {
            mGraphicView.setListDevoirs(devoirs);
            mGraphicView.setVisibility(View.VISIBLE);
            mTVNoInfos.setVisibility(View.GONE);
        }
        else{
            mGraphicView.setVisibility(View.GONE);
            mTVNoInfos.setVisibility(View.VISIBLE);
        }

    }

    public void fillPupilInfos(Pupil pupil){
        CoursesBDD cbdd = new CoursesBDD(getActivity());
        cbdd.open();
        int nbCours = cbdd.getNbCoursesWithPupilID(pupil.getId());
        cbdd.close();
        mTextViewNumberOfCourse.setText(String.format("%d cours", nbCours));
        mTextViewNumberOfDevoirs.setText(String.format("%d devoirs", mListDevoir.size()));
        mTextViewSince.setText(String.format("Depuis %s", C.formatDate(pupil.getSinceDate(), C.MONTH_YEAR)));

        if(new File(mPupil.getImgPath()).exists())
            mIVAvatar.setImageBitmap(BitmapFactory.decodeFile(mPupil.getImgPath()));
        else {
            if (mPupil.getSex() == Pupil.SEXE_MALE)
                mIVAvatar.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.man));
            else
                mIVAvatar.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.woman));
        }
    }

    public Pupil getPupilWithID(int id){
        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        Pupil pupil = pupilsBDD.getPupilWithId(id);
        pupilsBDD.close();
        return pupil;
    }

    public List<Devoir> getDevoirWithPupilID(int pupilID){
        DevoirBDD devoirBDD = new DevoirBDD(getActivity());
        devoirBDD.open();
        List<Devoir> list = devoirBDD.getDevoirsWithPupilID(pupilID);
        devoirBDD.close();
        return list;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.openSpinner:
                mSpinnerPupil.performClick();
                break;
        }
    }
}
