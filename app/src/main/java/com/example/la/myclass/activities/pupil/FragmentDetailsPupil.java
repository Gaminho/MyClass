package com.example.la.myclass.activities.pupil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.activities.AbstractFragmentDetails;
import com.example.la.myclass.activities.MainActivity;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.database.PupilsBDD;

import java.io.File;

/**
 * Created by ariche on 16/10/2016.
 */

public class FragmentDetailsPupil extends AbstractFragmentDetails {

    /**
     * Variables de classe
     */
    protected Pupil mPupil;
    protected FragmentDetailsPupil.PupilDetailsInterface mListener;
    /**
     * Fragment Life Cycle
     */
    public static FragmentDetailsPupil newInstance(int pupilID) {
        FragmentDetailsPupil fragmentDetailsPupil = new FragmentDetailsPupil();
        Bundle args = new Bundle();
        args.putInt(ActivityPupil.PUPIL_ID, pupilID);
        fragmentDetailsPupil.setArguments(args);
        return fragmentDetailsPupil;
    }
    public FragmentDetailsPupil() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().get(ActivityPupil.PUPIL_ID) != null)
            mPupil = PupilsBDD.getPupileWithId(getActivity(),getArguments().getInt(ActivityPupil.PUPIL_ID));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentDetailsPupil.PupilDetailsInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement PupilDetailsInterface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Implementing abstracts functions from AbstractFragmentList
     */
    @Override
    protected View setContent(Context context, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pupil_fragment_details, container, false);

        ((TextView) view.findViewById(R.id.name)).setText(mPupil.getFullName());
        ((TextView) view.findViewById(R.id.level)).setText(getActivity().getResources().getStringArray(R.array.classes)[mPupil.getLevel()]);
        ((TextView) view.findViewById(R.id.since)).setText(String.format("Depuis %s", C.formatDate(mPupil.getSinceDate(), C.MONTH_YEAR)));
        ImageView imageView = (ImageView) view.findViewById(R.id.avatar);

        if(!"".equals(mPupil.getImgPath()) && new File(mPupil.getImgPath()).exists())
            imageView.setImageBitmap(BitmapFactory.decodeFile(mPupil.getImgPath()));
        else{
            if(mPupil.getSex() == Pupil.SEXE_FEMALE)
                imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.woman));
            else
                imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.man));
        }

        ((TextView) view.findViewById(R.id.adress)).setText(mPupil.getAdress());
        if(mPupil.getTel1() != 0) {
            ((TextView) view.findViewById(R.id.tel1)).setText(String.format("%010d", mPupil.getTel1()));
            view.findViewById(R.id.call1).setOnClickListener(this);
        }
        if(mPupil.getTel2() != 0) {
            ((TextView) view.findViewById(R.id.tel2)).setText(String.format("%010d", mPupil.getTel2()));
            view.findViewById(R.id.call2).setOnClickListener(this);
        }

        view.findViewById(R.id.goTo).setOnClickListener(this);
        view.findViewById(R.id.isActive).setOnClickListener(this);

        ((TextView) view.findViewById(R.id.type)).setText(getResources().getStringArray(R.array.types)[mPupil.getType()]);
        ((TextView) view.findViewById(R.id.frequency)).setText(getResources().getStringArray(R.array.frequencies)[mPupil.getFrequency()]);
        ((TextView) view.findViewById(R.id.price)).setText(String.format("%.2f €", mPupil.getPrice()));

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
        rightLabel.setText("Suivi");
        rightIcon.setText("{fa-line-chart} ");
        rightLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.leftAction:
                getFragmentManager().beginTransaction().replace(R.id.default_container, FragmentAddOrEditPupil.newInstance(mPupil.getId())).commit();
                break;
            case R.id.rightAction:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(MainActivity.NAVIGATION_POSITION,4);
                intent.putExtra(MainActivity.PUPIL_ID, mPupil.getId());
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.isActive:
                setDialogDeleteDevoir(mPupil);
                break;
            case R.id.call1 :
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+((TextView)mContentView.findViewById(R.id.tel1)).getText().toString()));
                startActivity(intent);
                break;
            case R.id.call2 :
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+((TextView)mContentView.findViewById(R.id.tel2)).getText().toString()));
                startActivity(intent);
                break;
            case R.id.goTo :
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+((TextView)mContentView.findViewById(R.id.adress)).getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
        }
    }

    /**
     * Utils
     */
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
                mContentView.findViewById(R.id.isActive).setBackgroundColor(finalColor);
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


    public interface PupilDetailsInterface{
        void goBack();
    }
}
