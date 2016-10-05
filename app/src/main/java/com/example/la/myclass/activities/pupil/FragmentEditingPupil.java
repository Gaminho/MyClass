package com.example.la.myclass.activities.pupil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.database.PupilsBDD;
import com.example.la.myclass.beans.Pupil;

import java.io.File;
import java.io.IOException;

/**
 * Created by Léa on 30/09/2015.
 */
public class FragmentEditingPupil extends Fragment implements View.OnClickListener, CheckBox.OnCheckedChangeListener {


    // EDITING
    static Pupil mPupil;


    //Statics
    static final int SEXE_MALE = 0;
    static final int SEXE_FEMALE = 1;
    static final int TYPE_REG = 0;
    static final int TYPE_OCCA = 1;
    static final int TYPE_TEMP = 2;
    static final int AGENCY = 0;
    static final int BLACK = 1;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_PIC_CROP = 2;

    //Views
    protected LinearLayout mButtonValid, mButtonRemove;
    protected TextView mEditTextFirstName, mEditTextLastName;
    protected EditText mEditTextAdress, mEditTextPrix, mEditTextTel1, mEditTextTel2;
    protected Spinner mSpinnerClass;
    protected RadioButton mRadioButtonMale, mRadioButtonFemale, mRadioButtonTypeReg, mRadioButtonTypeOcca, mRadioButonTypeTemp, mRadioButtonAgency, mRadioButtonBlack;
    protected ImageView mImageViewAvatar;
    protected Button mButtonChangePix;


    //Interface
    protected FragmentDetailsPupil.PupilDetailsInterface mListener;




    // Fragment life cycle

    public static FragmentEditingPupil newInstance(int pupilId) {
        FragmentEditingPupil fragmentEditingPupil = new FragmentEditingPupil();
        Bundle args = new Bundle();
        args.putInt(ActivityPupil.PUPIL_ID, pupilId);
        fragmentEditingPupil.setArguments(args);
        return fragmentEditingPupil;
    }


    public FragmentEditingPupil() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPupil = getPupilWithID(getArguments().getInt(ActivityPupil.PUPIL_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pupil_fragment_editing, container, false);
        getAllViews(view);
        fillAllViews(mPupil);

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

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
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
            startActivityForResult(cropIntent, RESULT_PIC_CROP);

        }

        if (requestCode == RESULT_PIC_CROP && resultCode == getActivity().RESULT_OK && null != data) {

            if (data != null) {

                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                selectedBitmap = C.getRoundedCroppedBitmap(selectedBitmap);

                try {
                    mPupil.setImgPath( C.copyPicture(selectedBitmap, mPupil.getId()));
                } catch (IOException e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }

                mImageViewAvatar.setImageBitmap(selectedBitmap);
            }
        }
    }



    // Interface

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.valid :
                if(areInformationValid()) {
                    updatePupilInBdd(getPupilFromEditText());
                    getFragmentManager().beginTransaction().replace(R.id.default_container, new FragmentDetailsPupil().newInstance(mPupil.getId())).commit();
                }
                break;
            case R.id.removePupil :
                setDialogDeletePupil();
                break;
            case R.id.changePix:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;
        }

    }



    // Utils

    public void getAllViews(View view){

        // Actions
        mButtonValid = (LinearLayout) view.findViewById(R.id.valid);
        mButtonValid.setOnClickListener(this);
        mButtonRemove = (LinearLayout) view.findViewById(R.id.removePupil);
        mButtonRemove.setOnClickListener(this);

        // Informations générales
        mEditTextFirstName = (EditText) view.findViewById(R.id.firstname);
        mEditTextLastName = (EditText) view.findViewById(R.id.lastname);
        mSpinnerClass = (Spinner) view.findViewById(R.id.mySpinner);

        mRadioButtonFemale = (RadioButton) view.findViewById(R.id.rbFemale);
        mRadioButtonMale = (RadioButton) view.findViewById(R.id.rbMale);

        mRadioButtonFemale.setOnCheckedChangeListener(this);
        mRadioButtonMale.setOnCheckedChangeListener(this);

        // Coordonnées
        mEditTextTel1 = (EditText) view.findViewById(R.id.tel1);
        mEditTextTel2 = (EditText) view.findViewById(R.id.tel2);
        mEditTextAdress = (EditText) view.findViewById(R.id.adress);

        // Rémunération
        mEditTextPrix = (EditText) view.findViewById(R.id.price);
        mRadioButonTypeTemp = (RadioButton) view.findViewById(R.id.typeTemp);
        mRadioButtonTypeOcca = (RadioButton) view.findViewById(R.id.typeOcca);
        mRadioButtonTypeReg = (RadioButton) view.findViewById(R.id.typeRegular);
        mRadioButtonAgency = (RadioButton) view.findViewById(R.id.agency);
        mRadioButtonBlack = (RadioButton) view.findViewById(R.id.black);


        mSpinnerClass.setAdapter(new ArrayAdapter<>(
                getActivity(),
                R.layout.simple_spinner_item_2,
                getActivity().getResources().getStringArray(R.array.classes)
        ));

        mImageViewAvatar = (ImageView) view.findViewById(R.id.avatar);
        mButtonChangePix = (Button) view.findViewById(R.id.changePix);
    }

    public void fillAllViews(Pupil pupil){

        // Informations générales
        mEditTextFirstName.setText(pupil.getName().split(" ")[0]);
        mEditTextLastName.setText(pupil.getName().split(" ")[1]);
        if(pupil.getSex() == SEXE_FEMALE)
            mRadioButtonFemale.setChecked(true);
        else
            mRadioButtonMale.setChecked(true);

        // Classe
        mSpinnerClass.setSelection(pupil.getLevel());


        // Coordonnées
        if (mPupil.getTel1() != 0) {
            mEditTextTel1.setText("0" + pupil.getTel1());
        }

        if(pupil.getTel2() != 0) {
            mEditTextTel2.setText("0" + pupil.getTel2());
        }
        mEditTextAdress.setText(pupil.getAdress());

        // Rémunération
        mEditTextPrix.setText("" + pupil.getPrice());

        switch(pupil.getFrequency()){
            case TYPE_REG :
                mRadioButtonTypeReg.setChecked(true);
                break;
            case TYPE_OCCA :
                mRadioButtonTypeOcca.setChecked(true);
                break;
            default:
                mRadioButonTypeTemp.setChecked(true);
                break;
        }

        if(pupil.getType() == AGENCY)
            mRadioButtonAgency.setChecked(true);
        else
            mRadioButtonBlack.setChecked(true);


        if(!"".equals(mPupil.getImgPath()) && new File(mPupil.getImgPath()).exists())
            mImageViewAvatar.setImageBitmap(BitmapFactory.decodeFile(pupil.getImgPath()));

        else{
            if(mPupil.getSex() == Pupil.SEXE_FEMALE)
                mImageViewAvatar.setImageDrawable(getResources().getDrawable(R.drawable.woman));
            else
                mImageViewAvatar.setImageDrawable(getResources().getDrawable(R.drawable.man));
        }

        mButtonChangePix.setOnClickListener(this);
    }



    public Pupil getPupilFromEditText(){

        int classe = mSpinnerClass.getSelectedItemPosition();

        String name = mEditTextFirstName.getText().toString() + " " + mEditTextLastName.getText().toString();

        int sex = 12;
        if(mRadioButtonFemale.isChecked())
            sex = SEXE_FEMALE;
        else if(mRadioButtonMale.isChecked())
            sex = SEXE_MALE;

        int freq = 4;
        if(mRadioButtonTypeReg.isChecked())
            freq = TYPE_REG;
        else if(mRadioButtonTypeOcca.isChecked())
            freq = TYPE_OCCA;
        else if (mRadioButonTypeTemp.isChecked())
            freq = TYPE_TEMP;

        int type = 2;
        if(mRadioButtonBlack.isChecked())
            type = BLACK;
        else if (mRadioButtonAgency.isChecked())
            type = AGENCY;


        String adresse = mEditTextAdress.getText().toString();
        double price = Double.parseDouble(mEditTextPrix.getText().toString());

        long tel1 = mPupil.getTel1();
        long tel2 = mPupil.getTel2();

        if(!mEditTextTel1.getText().toString().equals(""))
            tel1 = Long.parseLong(mEditTextTel1.getText().toString());

        if(!mEditTextTel2.getText().toString().equals(""))
            tel2 = Long.parseLong(mEditTextTel2.getText().toString());


        return new Pupil(name, classe, type, sex, freq, adresse, price, mPupil.getSinceDate(), tel1, tel2, mPupil.getImgPath());

    }

    public void updatePupilInBdd(Pupil pupil){
        PupilsBDD pbdd = new PupilsBDD(getActivity());
        pbdd.open();
        pbdd.updatePupil(mPupil.getId(), pupil);
        pbdd.close();

        Toast.makeText(getActivity(), "Elève modifié", Toast.LENGTH_SHORT).show();
    }

    public boolean areInformationValid(){

        String toast = "";

        if(mEditTextFirstName.getText().toString().equals(""))
            toast = "Prénom manquant";

        if(mEditTextLastName.getText().toString().equals(""))
            toast = "Nom manquant";

        if(!mRadioButtonMale.isChecked() && !mRadioButtonFemale.isChecked())
            toast = "Sexe manquant";

        if(mEditTextAdress.getText().toString().equals(""))
            toast = "Adresse manquante";

        if(!mRadioButtonAgency.isChecked() && !mRadioButtonBlack.isChecked())
            toast = "Type de rémunération manquant";

        if(!mRadioButtonTypeReg.isChecked() && !mRadioButonTypeTemp.isChecked() && !mRadioButtonTypeOcca.isChecked())
            toast = "Fréquence de rémunération manquante";

        if (mSpinnerClass.getSelectedItemPosition() < 1)
            toast = "Classe manquante";

        if(mEditTextPrix.getText().toString().equals("") || Double.parseDouble(mEditTextPrix.getText().toString()) < 1)
            toast = "Tarif incorrect";

        if(!mEditTextTel1.getText().toString().equals("") && mEditTextTel1.getText().length() != 10)
            toast = "Téléphone incorrect";

        if(!mEditTextTel2.getText().toString().equals("") && mEditTextTel2.getText().length() != 10)
            toast = "Téléphone des parents incorrect";

        if (toast.equals(""))
            return true;

        else{
            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public Pupil getPupilWithID(int pupilID){
        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        Pupil pupil = pupilsBDD.getPupilWithId(pupilID);
        pupilsBDD.close();
        return pupil;
    }

    public void removePupil(int id){
        PupilsBDD pupilsBDD = new PupilsBDD(getActivity());
        pupilsBDD.open();
        pupilsBDD.removePupilWithID(id);
        pupilsBDD.close();

        Toast.makeText(getActivity(), mPupil.getName() + " a été supprimé.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.rbFemale:
                mImageViewAvatar.setImageDrawable(getResources().getDrawable(R.drawable.man));
                break;
            case R.id.rbMale:
                mImageViewAvatar.setImageDrawable(getResources().getDrawable(R.drawable.woman));
                break;
        }
    }








    // Dialog

    public void setDialogDeletePupil() {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View adv = factory.inflate(R.layout.dialog_delete, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(adv);

        final AlertDialog createDialog = adb.create();
        createDialog.show();


        ((TextView) createDialog.findViewById(R.id.messageDialog)).setText("Voulez-vous vraiment supprimer cet élève ?");

        createDialog.findViewById(R.id.valid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePupil(mPupil.getId());
                mListener.goBack();
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
}

