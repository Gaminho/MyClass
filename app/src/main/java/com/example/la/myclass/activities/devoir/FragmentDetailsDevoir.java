package com.example.la.myclass.activities.devoir;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.activities.AbstractFragmentDetails;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.database.DevoirBDD;

import java.util.Locale;

/**
 * 17/10/2016.
 */

public class FragmentDetailsDevoir extends AbstractFragmentDetails {

    /**
     * Variables de classe
     */
    protected Devoir mDevoir;
    protected FragmentDetailsDevoir.DevoirDetailsInterface mListener;

    /**
     * Fragment Life Cycle
     */
    public static FragmentDetailsDevoir newInstance(int id) {
        FragmentDetailsDevoir fragmentDetailsDevoir = new FragmentDetailsDevoir();
        Bundle args = new Bundle();
        args.putInt(ActivityDevoir.DEVOIR_ID, id);
        fragmentDetailsDevoir.setArguments(args);
        return fragmentDetailsDevoir;
    }
    public FragmentDetailsDevoir() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().get(ActivityDevoir.DEVOIR_ID) != null)
            mDevoir = DevoirBDD.getDevoirWithId(getActivity(),getArguments().getInt(ActivityDevoir.DEVOIR_ID));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentDetailsDevoir.DevoirDetailsInterface) activity;
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
        View view = inflater.inflate(R.layout.devoir_fragment_details, container, false);
        // TODO : clean up view
        ((TextView) view.findViewById(R.id.pupilName)).setText(mDevoir.getPupil().getFullName());
        ((TextView) view.findViewById(R.id.date)).setText(C.formatDate(mDevoir.getDate(), C.DAY_DATE_D_MONTH_YEAR));

        if(mDevoir.getNote() > 0)
            ((TextView) view.findViewById(R.id.note)).setText(String.format(Locale.FRANCE,"%.2f/%02d", mDevoir.getNote(), mDevoir.getBarem()));
        else
            ((TextView) view.findViewById(R.id.note)).setText(String.format(Locale.FRANCE,"-/%02d", mDevoir.getBarem()));

        ((TextView) view.findViewById(R.id.memo)).setText(mDevoir.getCommentaire());
        ((TextView) view.findViewById(R.id.theme)).setText(mDevoir.getTheme());
        ((TextView) view.findViewById(R.id.bareme)).setText(mDevoir.getTypeLabel(mDevoir.getType()));

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
        rightLayout.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.leftAction:
                getFragmentManager().beginTransaction().replace(R.id.default_container, FragmentAddOrEditDevoir.newInstance(mDevoir.getId())).commit();
                break;
        }
    }

    public interface DevoirDetailsInterface{
        void goBack();
    }
}
