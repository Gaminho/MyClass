package com.example.la.myclass.activities.pupil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.activities.MainActivity;
import com.example.la.myclass.adapters.RecyclerViewPupil;
import com.example.la.myclass.database.PupilsBDD;
import com.example.la.myclass.beans.Pupil;

import java.io.File;

/**
 * Created by Léa on 28/09/2015.
 */
public class FragmentDetailsPupil extends Fragment implements View.OnClickListener {

    // Views
    protected TextView mTextViewName, mTextViewClass, mTextViewType, mTextViewFrequency, mTextViewPrice, mTextViewAdress;
    protected TextView mTextViewSince, mTextViewTel1, mTextViewTel2;
    protected LinearLayout mButtonEdit, mButtonSuivi;
    protected ImageView mImageViewAvatar;
    protected CardView mCVActivePupil;
    protected TextView mTextViewCall1, mTextViewCall2, mTextViewGoTo, mTextViewCall, mTextViewResults;

    //Interface
    protected PupilDetailsInterface mListener;

    
    //Variables de classe
    protected Pupil mPupil;


    // Fragment life cycle

    public static FragmentDetailsPupil newInstance(int pupilID) {
        FragmentDetailsPupil fragmentDetailsPupil = new FragmentDetailsPupil();
        Bundle args = new Bundle();
        args.putInt(ActivityPupil.PUPIL_ID, pupilID);
        fragmentDetailsPupil.setArguments(args);
        return fragmentDetailsPupil;
    }
    public FragmentDetailsPupil() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().get(ActivityPupil.PUPIL_ID) != null)
            mPupil = getPupilWithID(getActivity(), getArguments().getInt(ActivityPupil.PUPIL_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pupil_fragment_details, container, false);
        getAllViews(view);

        if(mPupil != null)
            fillPupilDetails();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PupilDetailsInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement PupilDetailsInterface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    // Utils

    public void getAllViews(View view) {

        mButtonEdit = (LinearLayout) view.findViewById(R.id.edit);
        mButtonEdit.setOnClickListener(this);

        mButtonSuivi = (LinearLayout) view.findViewById(R.id.suivi);
        mButtonSuivi.setOnClickListener(this);

        mImageViewAvatar = (ImageView) view.findViewById(R.id.avatar);
        mTextViewName = (TextView) view.findViewById(R.id.name);
        mTextViewClass = (TextView) view.findViewById(R.id.level);
        mTextViewSince = (TextView) view.findViewById(R.id.since);


        mTextViewAdress = (TextView) view.findViewById(R.id.adress);
        mTextViewTel1 = (TextView) view.findViewById(R.id.tel1);
        mTextViewTel1.setVisibility(View.GONE);
        mTextViewTel2 = (TextView) view.findViewById(R.id.tel2);
        mTextViewTel2.setVisibility(View.GONE);

        mTextViewCall1 = (TextView) view.findViewById(R.id.call1);
        mTextViewCall1.setOnClickListener(this);
        mTextViewCall2 = (TextView) view.findViewById(R.id.call2);
        mTextViewCall2.setOnClickListener(this);
        mTextViewGoTo = (TextView) view.findViewById(R.id.goTo);
        mTextViewGoTo.setOnClickListener(this);


        mTextViewType = (TextView) view.findViewById(R.id.type);
        mTextViewFrequency = (TextView) view.findViewById(R.id.frequency);
        mTextViewPrice = (TextView) view.findViewById(R.id.price);

        mCVActivePupil = (CardView) view.findViewById(R.id.isActive);
        mCVActivePupil.setOnClickListener(this);
    }

    public void fillPupilDetails(){

        mTextViewName.setText(mPupil.getFullName());
        mTextViewClass.setText(getActivity().getResources().getStringArray(R.array.classes)[mPupil.getLevel()]);
        mTextViewSince.setText(String.format("Depuis %s", C.formatDate(mPupil.getSinceDate(), C.MONTH_YEAR)));

        if(!"".equals(mPupil.getImgPath()) && new File(mPupil.getImgPath()).exists())
            mImageViewAvatar.setImageBitmap(BitmapFactory.decodeFile(mPupil.getImgPath()));
        else{
            if(mPupil.getSex() == Pupil.SEXE_FEMALE)
                mImageViewAvatar.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.woman));
            else
                mImageViewAvatar.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.man));
        }


        if(mPupil.getTel1() != 0) {
            mTextViewTel1.setVisibility(View.VISIBLE);
            mTextViewTel1.setText(String.format("%010d",mPupil.getTel1()));
        }

        if(mPupil.getTel2() != 0) {
            mTextViewTel2.setVisibility(View.VISIBLE);
            mTextViewTel2.setText(String.format("%010d",mPupil.getTel2()));
        }

        mTextViewAdress.setText(mPupil.getAdress());

        mTextViewType.setText(getActivity().getResources().getStringArray(R.array.types)[mPupil.getType()]);
        mTextViewFrequency.setText(getActivity().getResources().getStringArray(R.array.frequencies)[mPupil.getFrequency()]);
        mTextViewPrice.setText(String.format("%.2f €", mPupil.getPrice()));


        if(mPupil.getState() == Pupil.DESACTIVE)
            mCVActivePupil.setBackgroundColor(getResources().getColor(R.color.red500));

    }

    public static Pupil getPupilWithID(Context context, int id){
        PupilsBDD pupilsBDD = new PupilsBDD(context);
        pupilsBDD.open();
        Pupil pupil = pupilsBDD.getPupilWithId(id);
        pupilsBDD.close();
        return pupil;
    }

    public void setDialogDeleteDevoir(final Pupil pupil) {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View adv = factory.inflate(R.layout.dialog_delete, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(adv);

        final AlertDialog createDialog = adb.create();
        createDialog.show();

        String message = "";
        int color=0;

        if(pupil.getState() == Pupil.ACTIVE) {
            mPupil.setState(Pupil.DESACTIVE);
            color = getActivity().getResources().getColor(R.color.red500);
            message = "En désactivant cet élève, vous ne pourrez plus le sélectionner lors de l'ajout de cours.\n\nSouhaitez-vous continuer ?";
        }

        else {
            mPupil.setState(Pupil.ACTIVE);
            color = getActivity().getResources().getColor(R.color.green500);
            message = "En réactivant cet élève, vous pourrez à nouveau le sélectionner lors de l'ajout de cours.\n\nSouhaitez-vous continuer ?";
        }

        final int finalColor = color;
        createDialog.findViewById(R.id.valid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
                pupilsBDD.open();
                pupilsBDD.updatePupil(mPupil.getId(),mPupil);
                pupilsBDD.close();
                mCVActivePupil.setBackgroundColor(finalColor);
                createDialog.dismiss();
            }
        });

        createDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog.dismiss();
            }
        });

        ((TextView) createDialog.findViewById(R.id.messageDialog)).setText(message);

    }

    // Interface

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.isActive:
                setDialogDeleteDevoir(mPupil);
                break;
            case R.id.cancel :
                mListener.goBack();
                break;
            case R.id.edit :
                getFragmentManager().beginTransaction().replace(R.id.default_container, FragmentAddOrEditPupil.newInstance(mPupil.getId())).commit();
                break;
            case R.id.suivi :
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(MainActivity.NAVIGATION_POSITION,4);
                intent.putExtra(MainActivity.PUPIL_ID, mPupil.getId());
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.call1 :
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mTextViewTel1.getText().toString()));
                startActivity(intent);
                break;
            case R.id.call2 :
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mTextViewTel2.getText().toString()));
                startActivity(intent);
                break;
            case R.id.call :
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mTextViewTel1.getText().toString()));
                startActivity(intent);
                break;
            case R.id.goTo :
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+mTextViewAdress.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
        }
    }


    // Interface

    public interface PupilDetailsInterface{
        void goBack();
    }


}
