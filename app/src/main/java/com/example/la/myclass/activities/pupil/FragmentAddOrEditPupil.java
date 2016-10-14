package com.example.la.myclass.activities.pupil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.la.myclass.activities.AbstractFragmentAddOrEdit;
import com.example.la.myclass.adapters.AdapterSpinner;
import com.example.la.myclass.beans.Pupil;
import com.example.la.myclass.database.PupilsBDD;

import java.io.File;
import java.io.IOException;

/**
 * Created by Léa on 28/09/2015.
 */
public class FragmentAddOrEditPupil extends AbstractFragmentAddOrEdit implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    // 01h25

    /**
     * BUNDLE VARIABLES
     */
    protected static final int EDITING = 1;
    protected static final int ADDING = 0;

    /**
     * Variables de classe
     */
    protected Pupil mPupil;
    protected int mCurrentMod;
    protected FragmentDetailsPupil.PupilDetailsInterface mListener;

    /**
     * Fragment Lide Cycle
     * @return
     */
    public static FragmentAddOrEditPupil newInstance() {
        FragmentAddOrEditPupil fragmentAddOrEditPupil = new FragmentAddOrEditPupil();
        return fragmentAddOrEditPupil;
    }
    public static FragmentAddOrEditPupil newInstance(int pupilID) {
        FragmentAddOrEditPupil fragmentAddOrEditPupil = new FragmentAddOrEditPupil();
        Bundle args = new Bundle();
        args.putInt(ActivityPupil.PUPIL_ID, pupilID);
        fragmentAddOrEditPupil.setArguments(args);
        return fragmentAddOrEditPupil;
    }
    public FragmentAddOrEditPupil() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPupil = new Pupil();

        if (getArguments() != null && getArguments().getInt(ActivityPupil.PUPIL_ID,-1) != -1){
            mPupil = FragmentDetailsPupil.getPupilWithID(getActivity(), getArguments().getInt(ActivityPupil.PUPIL_ID));
            mCurrentMod = EDITING;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == C.RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(selectedImage, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, C.RESULT_PIC_CROP);

        }

        if (requestCode == C.RESULT_PIC_CROP && resultCode == getActivity().RESULT_OK && null != data) {

            if (data != null) {

                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                selectedBitmap = C.getRoundedCroppedBitmap(selectedBitmap);

                try {
                    mPupil.setImgPath( C.copyPicture(selectedBitmap, mPupil.getId()));
                } catch (IOException e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }

                ((ImageView) mContentView.findViewById(R.id.avatar)).setImageBitmap(selectedBitmap);
            }
        }
    }

    /**
     * Implementing abstracts functions from AbstractFragmentList
     */
   @Override
    protected View setContent(Context context, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pupil_fragment_add_or_edit, container, false);
        Spinner spinner =  (Spinner) view.findViewById(R.id.spinnerClass);
        setSpinnerContent(spinner, R.array.classes, this);
        view.findViewById(R.id.loadNewPix).setOnClickListener(this);
        ((RadioGroup) view.findViewById(R.id.rGroupSexe)).setOnCheckedChangeListener(this);
        ((RadioGroup) view.findViewById(R.id.rGroupFrequency)).setOnCheckedChangeListener(this);
        ((RadioGroup) view.findViewById(R.id.rGroupPaiementType)).setOnCheckedChangeListener(this);

       if(mCurrentMod == EDITING) {
           spinner.setSelection(mPupil.getLevel());
           EditText editText = ((EditText) view.findViewById(R.id.firstname));
           editText.setText(mPupil.getName().split(C.STRING_SEPARATOR)[0]);
           editText = ((EditText) view.findViewById(R.id.lastname));
           editText.setText(mPupil.getName().split(C.STRING_SEPARATOR)[1]);

           if(!"".equals(mPupil.getImgPath()) && new File(mPupil.getImgPath()).exists())
               ((ImageView) view.findViewById(R.id.avatar)).setImageBitmap(BitmapFactory.decodeFile(mPupil.getImgPath()));

           switch(mPupil.getSex()){
               case Pupil.SEXE_FEMALE:
                   ((RadioButton)view.findViewById(R.id.rbFemale)).setChecked(true);
                   break;
               default:
                   ((RadioButton)view.findViewById(R.id.rbMale)).setChecked(true);
                   break;
           }

           editText = ((EditText) view.findViewById(R.id.tel1));
           if(mPupil.getTel1() > 0)
               editText.setText(String.format("%010d", mPupil.getTel1()));
           editText = ((EditText) view.findViewById(R.id.tel2));
           if(mPupil.getTel2() > 0)
            editText.setText(String.format("%010d", mPupil.getTel2()));
           editText = ((EditText) view.findViewById(R.id.adress));
           editText.setText(mPupil.getAdress());
           editText = ((EditText) view.findViewById(R.id.price));
           editText.setText(String.format("%.2f", mPupil.getPrice()));

           switch(mPupil.getFrequency()){
               case Pupil.TEMPORALY:
                   ((RadioButton)view.findViewById(R.id.rbTemporaly)).setChecked(true);
                   break;
               case Pupil.OCCASIONALY:
                   ((RadioButton)view.findViewById(R.id.rbOccasionaly)).setChecked(true);
                   break;
               default:
                   ((RadioButton)view.findViewById(R.id.rbRegular)).setChecked(true);
                   break;
           }

           switch(mPupil.getType()){
               case Pupil.AGENCY:
                   ((RadioButton)view.findViewById(R.id.rbAgency)).setChecked(true);
                   break;
               default:
                   ((RadioButton)view.findViewById(R.id.rbBlack)).setChecked(true);
                   break;
           }


       }

        return view;
    }

    @Override
    protected void setLeftAction(LinearLayout leftLayout, TextView leftLabel, TextView leftIcon) {
        if(mCurrentMod == EDITING){
            leftLayout.setVisibility(View.VISIBLE);
            leftLabel.setText("Supprimer");
            leftIcon.setText("{fa-trash-o} ");
            leftLayout.setOnClickListener(this);
        }
        else
            leftLayout.setVisibility(View.GONE);
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
     * @param pupil the pupil meant to be insert into database
     */
    public void addPupilToBdd(Pupil pupil){
        PupilsBDD pbdd = new PupilsBDD(getActivity());
        pbdd.open();
        pbdd.insertPupils(pupil);
        pbdd.close();

        Toast.makeText(getActivity(), "Elève ajouté", Toast.LENGTH_SHORT).show();
    }

    public void updatePupilInBdd(Pupil pupil){
        PupilsBDD pbdd = new PupilsBDD(getActivity());
        pbdd.open();
        pbdd.updatePupil(pupil.getId(), pupil);
        pbdd.close();

        Toast.makeText(getActivity(), "Elève mis à jour", Toast.LENGTH_SHORT).show();
    }

    public void removePupil(Pupil pupil, int pupilID){
        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        pupil.setState(Pupil.DESACTIVE);
        pupilsBDD.updatePupil(pupilID, pupil);
        pupilsBDD.close();
        mListener.goBack();
    }

    public void setDialogDeletePupil() {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View adv = factory.inflate(R.layout.dialog_delete, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(adv);

        final AlertDialog createDialog = adb.create();
        createDialog.show();


        ((TextView) createDialog.findViewById(R.id.messageDialog)).setText(getString(R.string.dialog_delete_pupil));

        createDialog.findViewById(R.id.valid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePupil(mPupil, mPupil.getId());
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

        String toast = "";
        EditText editText, editText1;

        editText = ((EditText) mContentView.findViewById(R.id.adress));
        if(editText.getText().toString().equals(""))
            toast = "Précisez l'adresse";
        else
            mPupil.setAdress(editText.getText().toString());

        editText = ((EditText) mContentView.findViewById(R.id.price));
        if(editText.getText().toString().equals("") || Double.parseDouble(editText.getText().toString().replace(",",".")) < 1)
            toast = "Précisez le tarif horaire";
        else
            mPupil.setPrice(Double.parseDouble(editText.getText().toString().replace(",",".")));

        editText = ((EditText) mContentView.findViewById(R.id.tel1));
        if(!editText.getText().toString().equals("") && editText.getText().length() != 10)
            toast = "Téléphone personnel incorrect";
        else if("".equals(editText.getText().toString()))
            mPupil.setTel1(0);
        else
            mPupil.setTel1(Long.parseLong(editText.getText().toString()));

        editText = ((EditText) mContentView.findViewById(R.id.tel2));
        if(!editText.getText().toString().equals("") && editText.getText().length() != 10)
                toast = "Téléphone des parents incorrect";
        else if("".equals(editText.getText().toString()))
            mPupil.setTel2(0);
        else
            mPupil.setTel2(Long.parseLong(editText.getText().toString()));

        editText = ((EditText) mContentView.findViewById(R.id.firstname));
        editText1 = ((EditText) mContentView.findViewById(R.id.lastname));
        if(editText.getText().toString().equals("") || editText1.getText().toString().equals(""))
            toast = "Précisez le nom";
        else
            mPupil.setName(String.format("%s%s%s", editText.getText().toString(), C.STRING_SEPARATOR, editText1.getText().toString()));


        if(mPupil.getType() == -1)
            toast = "Précisez le type de paiement";

        if(mPupil.getFrequency() == -1)
            toast = "Précisez la fréquence des cours";

        if(mPupil.getLevel() < 1)
            toast = "Précisez la classe";


        if (toast.equals(""))
            return true;

        else{
            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    protected void setSpinnerContent(Spinner spinner, int idStringArray, AdapterView.OnItemSelectedListener listener){
        String[] spinnerContent = getResources().getStringArray(idStringArray);
        spinner.setAdapter(new AdapterSpinner(getActivity(), R.layout.adapter_cell_spinner_course, spinnerContent, getResources().getColor(R.color.textColor)));
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(listener);
    }

    /**
     * Implementing Interfaces methods
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.leftAction:
                setDialogDeletePupil();
                break;
            case R.id.rightAction:
                if(areInformationValid()) {
                    if(mCurrentMod == ADDING)
                        addPupilToBdd(mPupil);
                    else if(mCurrentMod == EDITING)
                        updatePupilInBdd(mPupil);
                    mListener.goBack();
                }
                break;
            case R.id.loadNewPix:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, C.RESULT_LOAD_IMAGE);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
        if(radioGroup.getId() == R.id.rGroupFrequency){
            switch(radioButtonID){
                case R.id.rbRegular:
                    mPupil.setFrequency(Pupil.REGULIAR);
                    break;
                case R.id.rbOccasionaly:
                    mPupil.setFrequency(Pupil.OCCASIONALY);
                    break;
                case R.id.rbTemporaly:
                    mPupil.setFrequency(Pupil.TEMPORALY);
                    break;
            }
        }
        if(radioGroup.getId() == R.id.rGroupPaiementType){
            switch(radioButtonID){
                case R.id.rbAgency:
                    mPupil.setType(Pupil.AGENCY);
                    break;
                case R.id.rbBlack:
                    mPupil.setType(Pupil.BLACK);
                    break;
            }
        }
        if(radioGroup.getId() == R.id.rGroupSexe){
            switch(radioButtonID){
                case R.id.rbMale:
                    mPupil.setSex(Pupil.SEXE_MALE);
                    if(mCurrentMod == ADDING || "".equals(mPupil.getImgPath()))
                        ((ImageView) mContentView.findViewById(R.id.avatar)).setImageDrawable(getResources().getDrawable(R.drawable.man));
                    break;
                case R.id.rbFemale:
                    mPupil.setSex(Pupil.SEXE_FEMALE);
                    if(mCurrentMod == ADDING || "".equals(mPupil.getImgPath()))
                        ((ImageView) mContentView.findViewById(R.id.avatar)).setImageDrawable(getResources().getDrawable(R.drawable.woman));
                    break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinnerClass)
            mPupil.setLevel(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }
}
