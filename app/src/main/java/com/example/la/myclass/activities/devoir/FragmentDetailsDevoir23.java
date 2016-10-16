package com.example.la.myclass.activities.devoir;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.database.DevoirBDD;
import com.example.la.myclass.beans.Devoir;

import java.util.Locale;

/**
 * 05/10/2015.
 */
public class FragmentDetailsDevoir23 extends Fragment implements View.OnClickListener{

    // BUNDLE
    static final String DEVOIR_ID = "devoir_id";


    // Views
    protected TextView mTVPupilName, mTVDate, mTVNote, mTVTheme, mTVMemo, mTVType;
    protected LinearLayout mLLEditDevoir;
    protected CardView mCVRemarque;
    protected View mRootView;

    //Variables de classe
    protected Devoir mDevoir;


    // Fragment life cycle

    public static FragmentDetailsDevoir23 newInstance(int id) {
        FragmentDetailsDevoir23 fragmentDetailsDevoir = new FragmentDetailsDevoir23();
        Bundle args = new Bundle();
        args.putInt(DEVOIR_ID, id);
        fragmentDetailsDevoir.setArguments(args);
        return fragmentDetailsDevoir;
    }

    public FragmentDetailsDevoir23() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().get(DEVOIR_ID) != null)
            mDevoir = getDevoirWithId(getActivity(), getArguments().getInt(DEVOIR_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.devoir_fragment_details, container, false);
        getAllViews(mRootView);

        if(mDevoir != null)
            fillDevoirDetails();

        return mRootView;
    }


    // Utils

    public void getAllViews(View view){

        mTVPupilName = (TextView) view.findViewById(R.id.pupilName);
        mTVDate = (TextView) view.findViewById(R.id.date);
        mTVNote = (TextView) view.findViewById(R.id.note);
        mTVMemo = (TextView) view.findViewById(R.id.memo);
        mTVTheme = (TextView) view.findViewById(R.id.theme);
        mTVType = (TextView) view.findViewById(R.id.bareme);
        mCVRemarque = (CardView) view.findViewById(R.id.remarquesCV);
        mLLEditDevoir = (LinearLayout) view.findViewById(R.id.edit);
        mLLEditDevoir.setOnClickListener(this);

    }

    public void fillDevoirDetails(){
        mTVPupilName.setText(mDevoir.getPupil().getFullName());
        mTVDate.setText(C.formatDate(mDevoir.getDate(), C.DAY_DATE_D_MONTH_YEAR));
        mTVTheme.setText(mDevoir.getTheme());
        mTVMemo.setText(mDevoir.getCommentaire());

        if(mDevoir.getNote() > 0)
            mTVNote.setText(String.format(Locale.FRANCE,"%.2f/%02d", mDevoir.getNote(), mDevoir.getBarem()));
        else
            mTVNote.setText(String.format(Locale.FRANCE,"-/%02d", mDevoir.getBarem()));

        mTVType.setText(mDevoir.getTypeLabel(mDevoir.getType()));

    }


    public static Devoir getDevoirWithId(Context context, int id){
        DevoirBDD devoirBDD = new DevoirBDD(context);
        devoirBDD.open();
        Devoir devoir = devoirBDD.getDevoirWithId(id);
        Log.e("LOGIGI","DEVOIR="+devoir.toString());
        devoirBDD.close();
        return devoir;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.leftAction:
                getFragmentManager().beginTransaction().replace(R.id.default_container, FragmentAddOrEditDevoir.newInstance(mDevoir.getId())).commit();
                break;
        }
    }

    // Interface

    public interface DevoirDetailsInterface{
        void goBack();
    }
}
