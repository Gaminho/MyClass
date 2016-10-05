package com.example.la.myclass.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.la.myclass.R;

/**
 * Created by Gaminho on 23/09/2016.
 */
public class Podium2 extends ViewGroup {

    // Statics
    String TAG = "PODIUM2";
    public static final int TITLE_POSITION_LEFT = 0;
    public static final int TITLE_POSITION_RIGHT = 1;
    private static final double COEFF_HEIGHT_GOLD = 0.05;
    private static final double COEFF_HEIGHT_SILVER = 0.20;
    private static final double COEFF_HEIGHT_BRONZE = 0.35;
    private static final int INDEX_GOLD = 1;
    private static final int INDEX_SILVER = 0;
    private static final int INDEX_BRONZE = 2;


    // Attributes
    private int mPositionTitle;
    private float mPaddingTitle;
    private boolean mShowTitle = true;
    private boolean mShowLabel = true;
    private boolean mShowSubtitle = true;
    private boolean mShowDetails = true;
    private String mPodiumTitle = "Titre du podium";
    private int mNbOfParts = 4;
    private int mMinHeight = 100;
    private String mHeight;


    // Views
    private LinearLayout mViewRoot;
    private TextView mTVLegend;
    private LinearLayout mSilverView, mGoldView, mBronzeView;

    // Color
    private static final int COLOR_GOLD = 0x88FFD700;
    private static final int COLOR_SILVER = 0x88cecece;
    private static final int COLOR_BRONZE = 0x88B36700;

    // Variables de classe
    private Item[] mData = {new Item(INDEX_SILVER), new Item(INDEX_GOLD), new Item(INDEX_BRONZE)};
    private boolean mEnoughtSpaceForSubtitle = false;
    private boolean mEnoughtSpaceForDetails = false;


    // Constructeurs

    public Podium2(Context context) {
        super(context);
        init();
    }

    public Podium2(Context context, AttributeSet attrs) {
        super(context, attrs);



        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Podium2, 0, 0);

        try {
            mPaddingTitle = a.getDimension(R.styleable.Podium2_titlePadding, 10.0f);
            mShowTitle = a.getBoolean(R.styleable.Podium2_showTitle, true);
            mShowDetails = a.getBoolean(R.styleable.Podium2_showDetails, true);
            mShowLabel = a.getBoolean(R.styleable.Podium2_showLabel, true);
            mShowSubtitle = a.getBoolean(R.styleable.Podium2_showSubtitle, true);

            if(!mShowTitle)
                mNbOfParts = 3;


            if(a.getString(R.styleable.Podium2_titlePodium) != null && !a.getString(R.styleable.Podium2_titlePodium).isEmpty())
                mPodiumTitle = a.getString(R.styleable.Podium2_titlePodium);

            mPositionTitle = a.getInteger(R.styleable.Podium2_titlePosition, TITLE_POSITION_LEFT);
            mHeight = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        } finally {
            a.recycle();
        }

        init();
    }


    // Getters & Setters


    public float getPaddingTitle() {
        return mPaddingTitle;
    }


    public void setPaddingTitle(float mPaddingTitle) {
        this.mPaddingTitle = mPaddingTitle;
        onDataChanged();
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        Log.e(TAG, "onLayout");

        mTVLegend.setText(mPodiumTitle);
        mTVLegend.setPadding((int) mPaddingTitle, 0, (int) mPaddingTitle, 0);

        int xOffset = 0;
        int itemWidth = mViewRoot.getWidth() / mNbOfParts;

        if(mShowTitle &&  mPositionTitle != TITLE_POSITION_RIGHT) {
            mTVLegend.layout(xOffset, 0, xOffset + itemWidth, mViewRoot.getHeight());
            xOffset += itemWidth;
        }

        mSilverView.layout(xOffset, (int) (COEFF_HEIGHT_SILVER * mViewRoot.getHeight()), xOffset + itemWidth, mViewRoot.getHeight());
        xOffset += itemWidth;
        mGoldView.layout(xOffset, (int) (COEFF_HEIGHT_GOLD * mViewRoot.getHeight()), xOffset + itemWidth, mViewRoot.getHeight());
        xOffset += itemWidth;
        mBronzeView.layout(xOffset, (int) (COEFF_HEIGHT_BRONZE * mViewRoot.getHeight()), xOffset + itemWidth, mViewRoot.getHeight());
        xOffset += itemWidth;

        if(mShowTitle && mPositionTitle == TITLE_POSITION_RIGHT)
            mTVLegend.layout(xOffset, 0, xOffset + itemWidth, mViewRoot.getHeight());

        for(int j = (mData.length -1) ; j >=0 ; j--){
            Item it = mData[j];
            LinearLayout linearLayout = null;
            TextView tv;

            switch (it.mPosition){
                case INDEX_BRONZE:
                    linearLayout = mBronzeView;
                    break;
                case INDEX_SILVER :
                    linearLayout = mSilverView;
                    break;
                case INDEX_GOLD:
                    linearLayout = mGoldView;
                    break;
            }

            int yOffset = (int) (0.05 * linearLayout.getHeight());
            int heightText = getMinTVHeight(getContext(), 100, linearLayout.getWidth(),14,it.mLabel);

            // ADDING LABEL
            tv = new TextView(getContext());
            tv.layout(0, yOffset, linearLayout.getWidth(), yOffset + heightText);
            tv.setText(it.mLabel);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(14);
            tv.setTypeface(null, Typeface.BOLD);
            if(mShowLabel)
                linearLayout.addView(tv);

            // ADDING SUBTITLE
            tv = new TextView(getContext());
            yOffset += heightText;
            heightText = getMinTVHeight(getContext(), 100, linearLayout.getWidth(),12,it.mSubTitle);
            tv.layout(0, yOffset, linearLayout.getWidth(), yOffset + heightText);
            tv.setText(it.mSubTitle);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(12);
            if(yOffset + heightText < linearLayout.getHeight() && it.mPosition == INDEX_BRONZE)
                mEnoughtSpaceForSubtitle = true;

            if(mEnoughtSpaceForSubtitle && mShowSubtitle)
                linearLayout.addView(tv);


            // ADDING DATES
            tv = new TextView(getContext());
            yOffset += heightText;
            heightText = getMinTVHeight(getContext(), 100, linearLayout.getWidth(),10,it.mDetails);
            tv.layout(0,yOffset,linearLayout.getWidth(), yOffset + heightText);
            tv.setText(it.mDetails);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(10);
            tv.setTypeface(null, Typeface.ITALIC);

            if(yOffset + heightText < linearLayout.getHeight() && it.mPosition == INDEX_BRONZE)
                mEnoughtSpaceForDetails = true;

            if(mEnoughtSpaceForDetails && mShowDetails)
                linearLayout.addView(tv);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure");

        int minw = getPaddingLeft() + getPaddingRight();
        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));


        int yOffset = getPaddingBottom() + getPaddingTop();
        int heightOfBronze = (int) ((1-COEFF_HEIGHT_BRONZE) * heightMeasureSpec);

        int h;
        if(mHeight.equals(String.valueOf(LayoutParams.WRAP_CONTENT)))
            h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mMinHeight, getContext().getResources().getDisplayMetrics());
        else
            h = MeasureSpec.getSize(heightMeasureSpec);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged");

        //
        // Set dimensions for text, pie chart, etc
        //
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        mViewRoot.layout(0, 0, (int) ww, (int) hh);
        //mTVTitle.layout(0, 0, mViewRoot.getWidth(), ((int) (COEFF_HEIGHT_TITLE * mViewRoot.getHeight())));

        onDataChanged();
    }


    private void init() {
        Log.e(TAG, "init");

        mViewRoot = new LinearLayout(getContext());
        //mViewRoot.setBackgroundColor(0x99b3e5fc);

        mTVLegend = new TextView(getContext());
        //mTVLegend.setBackgroundColor(0x99b3e5fc);
        mTVLegend.setTextColor(Color.BLACK);
        mTVLegend.setTextSize(10);
        mTVLegend.setPadding((int) mPaddingTitle, 0, (int) mPaddingTitle, 0);
        mViewRoot.addView(mTVLegend);

        mSilverView = new LinearLayout(getContext());
        mSilverView.setBackgroundColor(COLOR_SILVER);
        mViewRoot.addView(mSilverView);

        mGoldView = new LinearLayout(getContext());
        mGoldView.setBackgroundColor(COLOR_GOLD);
        mViewRoot.addView(mGoldView);

        mBronzeView = new LinearLayout(getContext());
        mBronzeView.setBackgroundColor(COLOR_BRONZE);
        mViewRoot.addView(mBronzeView);

        //mTVTitle = new TextView(getContext());
        //mTVTitle.setBackgroundColor(0x9981d4fa);
        //mTVTitle.setGravity(mPositionTitle);
        //mTVTitle.setTextColor(Color.WHITE);
        //mTVTitle.setPadding((int) mPaddingTitle, 0, (int) mPaddingTitle, 0);
        //mViewRoot.addView(mTVTitle);

        addView(mViewRoot);
    }

    private void onDataChanged() {
        Log.e(TAG, "onDataChanged");
        mTVLegend.setPadding((int) mPaddingTitle, 0, (int) mPaddingTitle, 0);
    }








    private int getMinTVHeight(Context context, int height, int width, int fontSize, String text){
        TextView tv = new TextView(context);
        tv.layout(0, 0, width, height);
        tv.setTextSize(fontSize);
        tv.setText(text);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(tv.getWidth(), View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(tv.getHeight(), MeasureSpec.UNSPECIFIED);
        tv.measure(widthMeasureSpec, heightMeasureSpec);

        return tv.getMeasuredHeight();
    }




    public int addGoldItem(String label, String subTitle, String details) {
        Item it = mData[INDEX_GOLD];
        it.mLabel = label;
        it.mSubTitle = subTitle;
        it.mDetails = details;
        it.mPosition = INDEX_GOLD;
        mData[INDEX_GOLD]= it;
        onDataChanged();

        return mData.length;
    }

    public int addSilverItem(String label, String subTitle, String details) {
        Item it = mData[INDEX_SILVER];
        it.mLabel = label;
        it.mSubTitle = subTitle;
        it.mDetails = details;
        mData[INDEX_SILVER]= it;
        onDataChanged();

        return mData.length;
    }

    public int addBronzeItem(String label, String subTitle, String details) {
        Item it = mData[INDEX_BRONZE];
        it.mLabel = label;
        it.mSubTitle = subTitle;
        it.mDetails = details;
        it.mPosition = INDEX_BRONZE;
        mData[INDEX_BRONZE]= it;
        onDataChanged();

        return mData.length;
    }








    // Inner class
    private class Item {
        public int mHeight;
        public int mColor;
        public int mStartOffset;
        public int mEndOffset;
        public String mLabel;
        public String mSubTitle;
        public String mDetails;
        public int mPosition;

        public Item(int position){
            this.mLabel = "";
            this.mSubTitle = "";
            this.mDetails = "";
            this.mPosition = position;
            this.mColor = Color.BLACK;
        }

        public Item(){}

        @Override
        public String toString() {
            return "Item{" +
                    "mHeight=" + mHeight +
                    ", mColor=" + mColor +
                    ", mStartOffset=" + mStartOffset +
                    ", mEndOffset=" + mEndOffset +
                    ", mLabel='" + mLabel + '\'' +
                    ", mSubTitle='" + mSubTitle + '\'' +
                    ", mDetails='" + mDetails + '\'' +
                    ", mPosition=" + mPosition +
                    '}';
        }
    }

}
