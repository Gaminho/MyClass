package com.example.la.myclass.activities.devoir;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.database.DevoirBDD;
import com.example.la.myclass.beans.Devoir;

/**
 * Created by Léa on 05/10/2015.
 */
public class FragmentDetailsDevoir extends Fragment implements View.OnClickListener{

    // BUNDLE
    static final String DEVOIR_ID = "devoir_id";


    // Views
    protected TextView mTextViewPupilName, mTextViewDate, mTextViewNote, mTextViewTheme, mTextViewMemo;
    protected LinearLayout mLLEditDevoir;
    protected CardView mCVRemarque;

    //Variables de classe
    protected Devoir mDevoir;


    // Fragment life cycle

    public static FragmentDetailsDevoir newInstance(int id) {
        FragmentDetailsDevoir fragmentDetailsDevoir = new FragmentDetailsDevoir();
        Bundle args = new Bundle();
        args.putInt(DEVOIR_ID, id);
        fragmentDetailsDevoir.setArguments(args);
        return fragmentDetailsDevoir;
    }

    public FragmentDetailsDevoir() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().get(DEVOIR_ID) != null)
            mDevoir = getDevoirWithId(getActivity(), getArguments().getInt(DEVOIR_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.devoir_fragment_details, container, false);
        getAllViews(view);

        if(mDevoir != null)
            fillDevoirDetails();

        return view;
    }


    // Utils

    public void getAllViews(View view){

        mTextViewPupilName = (TextView) view.findViewById(R.id.pupilName);
        mTextViewDate = (TextView) view.findViewById(R.id.date);
        mTextViewNote = (TextView) view.findViewById(R.id.note);
        mTextViewTheme = (TextView) view.findViewById(R.id.theme);
        mTextViewMemo = (TextView) view.findViewById(R.id.memo);
        mCVRemarque = (CardView) view.findViewById(R.id.remarquesCV);
        mLLEditDevoir = (LinearLayout) view.findViewById(R.id.edit);
        mLLEditDevoir.setOnClickListener(this);

    }

    public void fillDevoirDetails(){
        mTextViewPupilName.setText(mDevoir.getPupil().getFullName());
        mTextViewDate.setText(C.formatDate(mDevoir.getDate(), C.DAY_DATE_D_MONTH_YEAR));
        mTextViewTheme.setText(mDevoir.getTheme());
        mTextViewMemo.setText(mDevoir.getCommentaire());
        mCVRemarque.setVisibility(View.GONE);

        if(mDevoir.getNote() > 0)
            mTextViewNote.setText(mDevoir.getNote()+"/20");
        else
            mTextViewNote.setText("-/20");

        if(!"".equals(mDevoir.getCommentaire()))
            mCVRemarque.setVisibility(View.VISIBLE);


    }


    public static Devoir getDevoirWithId(Context context, int id){
        DevoirBDD devoirBDD = new DevoirBDD(context);
        devoirBDD.open();
        Devoir devoir = devoirBDD.getDevoirWithId(id);
        devoirBDD.close();
        return devoir;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.edit:
                //getFragmentManager().beginTransaction().replace(R.id.default_container, FragmentEditingDevoir.newInstance(mDevoir.getId())).commit();
                getFragmentManager().beginTransaction().replace(R.id.default_container, FragmentAddOrEditDevoir.newInstance(mDevoir.getId())).commit();
                break;
        }
    }

    // Interface

    public interface DevoirDetailsInterface{
        void goBack();
    }
}
