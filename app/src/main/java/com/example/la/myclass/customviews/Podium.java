/* Copyright (C) 2012 The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.example.la.myclass.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;


import com.example.la.myclass.R;

import java.lang.Math;
import java.lang.Override;
import java.lang.String;

/**
 * Custom view that shows a pie chart and, optionally, a label.
 */
public class Podium extends ViewGroup {

    // Statics
    private static final int COLOR_GOLD = 0x88FFD700;
    private static final int COLOR_SILVER = 0x88cecece;
    private static final int COLOR_BRONZE = 0x88B36700;
    private static final int SPACE_BETWEEN_TEXTS = 10;


    // Variables de classe

    private PodiumView mPodiumView;
    private Item[] mData = {new Item(INDEX_SILVER), new Item(INDEX_GOLD), new Item(INDEX_BRONZE)};
    private RectF mPodiumBounds = new RectF();
    private Paint mPodiumPaint;
    private Paint mTextLabelPaint;
    private Paint mTextSubtitlePaint;
    private Paint mTextDetailsPaint;

    // Dimensions
    private float mMaxLegendWidth;
    private float mMaxSubTitleWidth;
    private float mMaxDetailsWidth;
    private static final double COEFF_HEIGHT_GOLD = 0.95;
    private static final double COEFF_HEIGHT_SILVER = 0.8;
    private static final double COEFF_HEIGHT_BRONZE = 0.65;
    private static final int INDEX_GOLD = 1;
    private static final int INDEX_SILVER = 0;
    private static final int INDEX_BRONZE = 2;

    //Color
    private int mColorGold;
    private int mColorSilver;
    private int mColorBronze;
    private int mTextLabelColor;
    private int mTextSubtitleColor;
    private int mTextDetailsColor;


    // Constructeur


    /**
     * Class constructor taking only a context. Use this constructor to create
     * {@link PieChart} objects from your own code.
     *
     * @param context
     */
    public Podium(Context context) {
        super(context);
        init();
    }

    /**
     * Class constructor taking a context and an attribute set. This constructor
     * is used by the layout engine to construct a {@link PieChart} from a set of
     * XML attributes.
     *
     * @param context
     * @param attrs   An attribute set which can contain attributes from
     *                {@link com.example.la.myclass.customviews.Podium} as well as attributes inherited
     *                from {@link android.view.View}.
     */
    public Podium(Context context, AttributeSet attrs) {
        super(context, attrs);

        // attrs contains the raw values for the XML attributes
        // that were specified in the layout, which don't include
        // attributes set by styles or themes, and which may have
        // unresolved references. Call obtainStyledAttributes()
        // to get the final values for each attribute.
        //
        // This call uses R.styleable.PieChart, which is an array of
        // the custom attributes that were declared in attrs.xml.
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Podium, 0, 0);

        try {
            // Retrieve the values from the TypedArray and store into
            // fields of this class.
            //
            // The R.styleable.Podium_* constants represent the index for
            // each custom attribute in the R.styleable.PieChart array.
            mColorGold          = a.getColor(R.styleable.Podium_goldColor,          COLOR_GOLD);
            mColorSilver        = a.getColor(R.styleable.Podium_silverColor,        COLOR_SILVER);
            mColorBronze        = a.getColor(R.styleable.Podium_bronzeColor,        COLOR_BRONZE);
            mTextLabelColor     = a.getColor(R.styleable.Podium_textLabelColor,     Color.BLACK);
            mTextSubtitleColor  = a.getColor(R.styleable.Podium_textSubtitleColor,  Color.BLACK);
            mTextDetailsColor   = a.getColor(R.styleable.Podium_textDetailsColor,   Color.BLACK);
        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }

        init();
    }


    // Getters and setters

    public int getGoldColor() {
        return mColorGold;
    }

    public int getSilverColor() {
        return mColorSilver;
    }

    public int getBronzeColor() {
        return mColorBronze;
    }

    public int getmTextLabelColor() {
        return mTextLabelColor;
    }

    public void setGoldColor(int mColorGold) {
        Item it = mData[INDEX_GOLD];
        it.mColor = mColorGold;
        mData[INDEX_GOLD] = it;
        invalidate();
    }

    public void setSilverColor(int mColorSilver) {
        Item it = mData[INDEX_SILVER];
        it.mColor = mColorSilver;
        mData[INDEX_SILVER] = it;
        invalidate();
    }

    public void setBronzeColor(int mColorBronze) {
        Item it = mData[INDEX_BRONZE];
        it.mColor = mColorBronze;
        mData[INDEX_BRONZE] = it;
        invalidate();
    }

    public void setTextLabelColor(int mTextLabelColor) {
        this.mTextLabelColor = mTextLabelColor;
        mTextLabelPaint.setColor(mTextLabelColor);
        invalidate();
    }

    public void setTextSubtitleColor(int mTextSubtitleColor) {
        this.mTextSubtitleColor = mTextSubtitleColor;
        mTextSubtitlePaint.setColor(mTextSubtitleColor);
        invalidate();
    }

    public void setTextDetailsColor(int mTextDetailsColor) {
        this.mTextDetailsColor = mTextDetailsColor;
        mTextDetailsPaint.setColor(mTextDetailsColor);
        invalidate();
    }

    // Getters and Setters
    // NOT FORGET TO SET INVALIDATE AFTER SETTING DATA


    // Utils







    // Interface


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // Do nothing. Do not call the superclass method--that would start a layout pass
        // on this view's children. PieChart lays out its children in onSizeChanged().
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    //
    // Measurement functions. This example uses a simple heuristic: it assumes that
    // the pie chart should be at least as wide as its label.
    //
    @Override
    protected int getSuggestedMinimumWidth() {
        return 0;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 0;
    }


    private void setLayerToSW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void setLayerToHW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }


    // My actions

    private class PodiumView extends View{

        // Used for SDK < 11
        private float mRotation = 0;
        private Matrix mTransform = new Matrix();
        private PointF mPivot = new PointF();

        /**
         * Construct a PieView
         *
         * @param context
         */
        public PodiumView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.e("TEST-PODIUM", "onDraw");
            if (Build.VERSION.SDK_INT < 11) {
                mTransform.set(canvas.getMatrix());
                mTransform.preRotate(mRotation, mPivot.x, mPivot.y);
                canvas.setMatrix(mTransform);
            }



            for (Item it : mData) {

                if(it != null) {
                    /**
                     * Draw rectangles of podium
                     */
                    mPodiumPaint.setColor(it.mColor);
                    canvas.drawRect(it.mStartOffset, mPodiumBounds.bottom - it.mHeight, it.mEndOffset, mPodiumBounds.bottom, mPodiumPaint);

                    /**
                     * Write Labels
                     */
                    int xText = it.mStartOffset + (it.mEndOffset - it.mStartOffset) / 2;
                    int yText = (int) (mPodiumBounds.bottom - it.mHeight / 2);

                    calibrateTextSize(mTextLabelPaint, it.mLabel, 20, 200, mMaxLegendWidth);
                    Rect result = new Rect();
                    mTextLabelPaint.getTextBounds(it.mLabel, 0, it.mLabel.length(), result);
                    int height = result.height();
                    canvas.drawText(it.mLabel, xText, yText, mTextLabelPaint);

                    /**
                     * Write Subtitles
                     */
                    if(isSubtitleWritable()){
                        yText = yText + height + SPACE_BETWEEN_TEXTS;
                        calibrateTextSize(mTextSubtitlePaint, it.mSubTitle, 10, 180, mMaxSubTitleWidth);
                        mTextSubtitlePaint.getTextBounds(it.mSubTitle, 0, it.mSubTitle.length(), result);
                        canvas.drawText(it.mSubTitle, xText, yText, mTextSubtitlePaint);

                        if(areDetailsWritable()){
                            //yText = yText + height + SPACE_BETWEEN_TEXTS;
                            yText = (int) mPodiumBounds.bottom;
                            String[] details = it.mDetails.split("\n");
                            calibrateTextSize(mTextDetailsPaint, details[0], 10, 150, mMaxDetailsWidth);
                            mTextSubtitlePaint.getTextBounds(details[0], 0, details[0].length(), result);
                            //yText -= result0.height();
                            yText -= 2* SPACE_BETWEEN_TEXTS;
                            canvas.drawText(details[1], xText, yText, mTextDetailsPaint);
                            yText -= result.height();
                            calibrateTextSize(mTextDetailsPaint, details[0], 10, 150, mMaxDetailsWidth);
                            canvas.drawText(details[0], xText, yText, mTextDetailsPaint);
                        }
                    }

                }
            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mBounds = new RectF(0, 0, w, h);
        }

        RectF mBounds;

    }


    /**
     * Initialize the control. This code is in a separate method so that it can be
     * called from both constructors.
     */
    private void init() {
        Log.e("TEST-PODIUM", "init");
        // Force the background to software rendering because otherwise the Blur
        // filter won't work.
        setLayerToSW(this);

        mPodiumView = new PodiumView(getContext());
        addView(mPodiumView);

        mPodiumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPodiumPaint.setStyle(Paint.Style.FILL);

        mTextLabelPaint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG);
        mTextLabelPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextLabelPaint.setTextAlign(Paint.Align.CENTER);
        mTextLabelPaint.setColor(mTextLabelColor);

        mTextSubtitlePaint = new Paint(Paint.LINEAR_TEXT_FLAG);
        mTextSubtitlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextSubtitlePaint.setTextAlign(Paint.Align.CENTER);
        mTextSubtitlePaint.setColor(mTextSubtitleColor);

        mTextDetailsPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
        mTextDetailsPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextDetailsPaint.setTextAlign(Paint.Align.CENTER);
        mTextDetailsPaint.setColor(mTextDetailsColor);

        // In edit mode it's nice to have some demo data, so add that here.
        if (this.isInEditMode()) {
            addSilverItem("125,00€", "5 cours", "16/05/16\n22/05/16");
            addGoldItem("184,00€", "7 cours", "11/04/16\n17/04/16");
            addBronzeItem("120,00€", "7 cours", "02/11/15\n08/11/15");
        }

    }


    /**
     * Add a new data item to this view. Adding an item adds a slice to the pie whose
     * size is proportional to the item's value. As new items are added, the size of each
     * existing slice is recalculated so that the proportions remain correct.
     *
     * @return The index of the newly added item.
     */
    public int addGoldItem(String label, String subTitle, String details) {
        Item it = mData[INDEX_GOLD];
        it.mColor = mColorGold;
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
        it.mColor = mColorSilver;
        it.mLabel = label;
        it.mSubTitle = subTitle;
        it.mDetails = details;
        mData[INDEX_SILVER]= it;
        onDataChanged();

        return mData.length;
    }

    public int addBronzeItem(String label, String subTitle, String details) {
        Item it = mData[INDEX_BRONZE];
        it.mColor = mColorBronze;
        it.mLabel = label;
        it.mSubTitle = subTitle;
        it.mDetails = details;
        it.mPosition = INDEX_BRONZE;

        mData[INDEX_BRONZE]= it;
        onDataChanged();

        return mData.length;
    }


    /**
     * Maintains the state for a data item.
     */
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
            if(position == INDEX_GOLD)
                this.mColor = COLOR_GOLD;
            else if(position == INDEX_BRONZE)
                this.mColor = COLOR_BRONZE;
            else if(position == INDEX_SILVER)
                this.mColor = COLOR_SILVER;
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

    /**
     * Do all of the recalculations needed when the data array changes.
     */
    private void onDataChanged() {
        Log.e("TEST-PODIUM", "onDataChanged");
        // When the data changes, we have to recalculate all widths
        for (int i = 0 ; i < mData.length ; i++) {
            Item it = mData[i];
            if(it != null) {
                it.mStartOffset = (int) (it.mPosition * mPodiumBounds.right / mData.length);
                it.mEndOffset = it.mStartOffset + (int) (mPodiumBounds.right / mData.length);
                if (it.mPosition == INDEX_BRONZE)
                    it.mHeight = (int) (COEFF_HEIGHT_BRONZE * mPodiumBounds.bottom);
                else if (it.mPosition == INDEX_SILVER)
                    it.mHeight = (int) (COEFF_HEIGHT_SILVER * mPodiumBounds.bottom);
                else
                    it.mHeight = (int) (COEFF_HEIGHT_GOLD * mPodiumBounds.bottom);

                Log.e("TEST-PODIUM", "Rectangle " + i + " : " + it.toString());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("TEST-PODIUM", "onMeasure");
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight();

        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = getPaddingBottom() + getPaddingTop();
        int h = Math.max(MeasureSpec.getSize(heightMeasureSpec), minh);

        Log.e("TEST-PODIUM", "onMeasure - w : " + w + " x h : " + h);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("TEST-PODIUM", "onSizeChanged");

        //
        // Set dimensions for text, pie chart, etc
        //
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        mPodiumBounds = new RectF(0.0f,0.0f,ww,hh);
        mPodiumBounds.offsetTo(getPaddingLeft(), getPaddingTop());


        // Lay out the child view that actually draws the pie.
        mPodiumView.layout((int) mPodiumBounds.left, (int) mPodiumBounds.top,
                (int) mPodiumBounds.right, (int) mPodiumBounds.bottom);

        mMaxLegendWidth = (float) (0.8* (mPodiumBounds.right / mData.length));
        mMaxSubTitleWidth = (float) (0.65* (mPodiumBounds.right / mData.length));
        mMaxDetailsWidth = (float) (0.6* (mPodiumBounds.right / mData.length));

        onDataChanged();
    }

    /**
     * Calibrates this paint's text-size to fit the specified text within the specified width.
     * @param paint     The paint to calibrate.
     * @param max       The maximum text size to use.
     * @param boxWidth  The width of the space in which the text has to fit.
     * @param text      The text to calibrate for.
     * @param min       The minimum text size to use.
     */

    public static void calibrateTextSize(Paint paint,String text, float min, float max, float boxWidth) {
            paint.setTextSize(10);
            paint.setTextSize(Math.max(Math.min((boxWidth/paint.measureText(text))*10, max), min));
        }

    public boolean isSubtitleWritable(){

        Rect result0 = new Rect();

        int yText = (int) (mPodiumBounds.bottom - (COEFF_HEIGHT_BRONZE * mPodiumBounds.bottom / 2));
        calibrateTextSize(mTextLabelPaint, mData[INDEX_BRONZE].mLabel, 20, 200, mMaxLegendWidth);
        mTextLabelPaint.getTextBounds(mData[INDEX_BRONZE].mLabel, 0, mData[INDEX_BRONZE].mLabel.length(), result0);
        int heightLabel = result0.height();

        calibrateTextSize(mTextSubtitlePaint, mData[INDEX_BRONZE].mSubTitle, 20, 200, mMaxSubTitleWidth);
        mTextSubtitlePaint.getTextBounds(mData[INDEX_BRONZE].mSubTitle, 0, mData[INDEX_BRONZE].mSubTitle.length(), result0);
        int heightSubtitle = result0.height();

        yText += heightLabel + SPACE_BETWEEN_TEXTS + heightSubtitle + SPACE_BETWEEN_TEXTS;

        if(yText < mPodiumBounds.bottom)
            return true;

        return false;
    }

    public boolean areDetailsWritable(){

        Rect result0 = new Rect();

        int yText = (int) (mPodiumBounds.bottom - (COEFF_HEIGHT_BRONZE * mPodiumBounds.bottom / 2));
        calibrateTextSize(mTextLabelPaint, mData[INDEX_BRONZE].mLabel, 20, 200, mMaxLegendWidth);
        mTextLabelPaint.getTextBounds(mData[INDEX_BRONZE].mLabel, 0, mData[INDEX_BRONZE].mLabel.length(), result0);
        yText += result0.height() + SPACE_BETWEEN_TEXTS;

        calibrateTextSize(mTextSubtitlePaint, mData[INDEX_BRONZE].mSubTitle, 20, 200, mMaxSubTitleWidth);
        mTextSubtitlePaint.getTextBounds(mData[INDEX_BRONZE].mSubTitle, 0, mData[INDEX_BRONZE].mSubTitle.length(), result0);
        yText += result0.height() + SPACE_BETWEEN_TEXTS;


        String[] details = mData[INDEX_BRONZE].mDetails.split("\n");
        calibrateTextSize(mTextDetailsPaint, details[0], 10, 150, mMaxDetailsWidth);
        mTextSubtitlePaint.getTextBounds(details[0], 0, details[0].length(), result0);
        yText = yText + 2 * result0.height() + 4 * SPACE_BETWEEN_TEXTS;

        if(yText < mPodiumBounds.bottom) {
            return true;
        }

        return false;
    }

}
