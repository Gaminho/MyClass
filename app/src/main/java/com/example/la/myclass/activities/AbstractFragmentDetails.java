package com.example.la.myclass.activities;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.la.myclass.R;

/**
 * 16/10/2016.
 */

public abstract class AbstractFragmentDetails extends Fragment implements View.OnClickListener {

    /**
     * Common views
     */
    protected ScrollView mScrollView;
    protected LinearLayout mContentView;
    protected LinearLayout mLeftAction, mRightAction;
    protected TextView mLeftActionLabel, mLeftActionIcon, mRightActionLabel, mRightActionIcon;

    /**
     * Fragment Life cycle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_fragment_adding, container, false);
        getAllViews(view);
        mContentView = (LinearLayout) setContent(getActivity(), mScrollView);
        mScrollView.addView(mContentView);
        setLeftAction(mLeftAction, mLeftActionLabel, mLeftActionIcon);
        setRightAction(mRightAction, mRightActionLabel, mRightActionIcon);
        return view;
    }

    /**
     * Utils methods
     */
    protected void getAllViews(View view){
        mScrollView = (ScrollView) view.findViewById(R.id.scrollViewContent);
        mLeftActionLabel = (TextView) view.findViewById(R.id.leftActionLabel);
        mLeftActionIcon = (TextView) view.findViewById(R.id.leftActionIcon);
        mRightActionLabel = (TextView) view.findViewById(R.id.rightActionLabel);
        mRightActionIcon = (TextView) view.findViewById(R.id.rightActionIcon);
        mLeftAction = (LinearLayout) view.findViewById(R.id.leftAction);
        mRightAction = (LinearLayout) view.findViewById(R.id.rightAction);
        mLeftAction.setOnClickListener(this);
        mRightAction.setOnClickListener(this);
    }
    protected abstract View setContent(Context context, ViewGroup container);
    protected abstract void setLeftAction(LinearLayout leftLayout, TextView leftLabel, TextView leftIcon);
    protected abstract void setRightAction(LinearLayout rightLayout, TextView rightLabel, TextView rightIcon);
}
