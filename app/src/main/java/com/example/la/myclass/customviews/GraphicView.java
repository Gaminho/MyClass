package com.example.la.myclass.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.la.myclass.C;
import com.example.la.myclass.R;
import com.example.la.myclass.beans.Devoir;
import com.example.la.myclass.beans.Month;
import com.example.la.myclass.beans.PeriodicItem;
import com.example.la.myclass.beans.Week;
import com.example.la.myclass.beans.Year;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaminho on 24/09/2016.
 */
public class GraphicView extends View {

    // Statics
    private static final String TAG = "Graphic";
    private static final String TYPE_MONTH = "mois";
    private static final String TYPE_WEEK = "semaines";
    private static final String TYPE_YEAR = "années";
    private static final String TYPE_DEVOIR = "devoirs";
    private static final int MOD_LEFT = 0;
    private static final int MOD_RIGHT = 1;
    private static final int MOD_MIDDLE = 2;
    private static final double COEFF_HEIGHT_BARS = 0.09;
    private static final double COEFF_WIDTH_LEGENDS = 0.10;
    private static final int DEFAULT_BACKGROUND     = 0x99ececed;
    private static final int DEFAULT_LEFT_LEGEND    = 0x9903a9f4;
    private static final int DEFAULT_RIGHT_LEGEND   = 0x99ffc107;


    // Attributes
    private int mNbOfItemsToDisplay;
    private int mBackgroundColor;
    private int mLeftLegendColor;
    private int mRightLegendColor;
    private int mNbOfLegendSteps;
    private boolean mShowLeftLegend;
    private boolean mShowRightLegend;
    private boolean mEnableNavigation;
    private boolean mEnableSwitchMod;
    private String mDataType;


    // Vues
    private RectF mGraphicsBounds = new RectF();
    private Canvas mRootCanvas;
    private Paint mPaint = new Paint();
    private TextPaint mTitlePaint = new TextPaint();
    private RectF mTopLeftRect = new RectF(), mTopRightRect = new RectF(), mTopTitle = new RectF();
    private RectF mTop2LeftRect = new RectF(), mTop2RightRect = new RectF(), mTop2MiddleRect = new RectF();
    private RectF mBottomLeftRect = new RectF(), mBottomRightRect = new RectF(), mBottomMiddleRect = new RectF();
    private RectF mLeftLegend = new RectF(), mRightLegend = new RectF();
    private RectF mGraphRect = new RectF();

    // Variables de classe
    private int mCurrentModView = MOD_MIDDLE;
    private int mCurrentWeekIndex = 0;
    private List<Object> mData = new ArrayList<>();


    public GraphicView(Context context) {
        super(context);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mTitlePaint.setTextAlign(Paint.Align.CENTER);
        init();
    }

    public GraphicView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint.setAntiAlias(true);


        // attrs contains the raw values for the XML attributes
        // that were specified in the layout, which don't include
        // attributes set by styles or themes, and which may have
        // unresolved references. Call obtainStyledAttributes()
        // to get the final values for each attribute.
        //
        // This call uses R.styleable.PieChart, which is an array of
        // the custom attributes that were declared in attrs.xml.
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.GraphicView,
                0, 0
        );

        try {
            // Retrieve the values from the TypedArray and store into
            // fields of this class.
            //
            // The R.styleable.PieChart_* constants represent the index for
            // each custom attribute in the R.styleable.PieChart array.
            mNbOfLegendSteps = a.getInteger(R.styleable.GraphicView_legendSteps, 6);
            mNbOfItemsToDisplay = a.getInteger(R.styleable.GraphicView_nbItem, 7);
            int dataType = a.getInteger(R.styleable.GraphicView_dataType, 0);
            mDataType = TYPE_MONTH;

            if(dataType==1)
                mDataType = TYPE_WEEK;
            if(dataType==2)
                mDataType = TYPE_YEAR;
            if(dataType==3)
                mDataType = TYPE_DEVOIR;
            if (mDataType.isEmpty())
                mDataType = TYPE_WEEK;
            mShowLeftLegend = a.getBoolean(R.styleable.GraphicView_showLeftLegend, true);
            mShowRightLegend = a.getBoolean(R.styleable.GraphicView_showRightLegend, true);
            mEnableNavigation = a.getBoolean(R.styleable.GraphicView_enableNavigation, true);
            mEnableSwitchMod = a.getBoolean(R.styleable.GraphicView_enableSwitchMod, true);
            mBackgroundColor = a.getColor(R.styleable.GraphicView_colorBackground, DEFAULT_BACKGROUND);
            mLeftLegendColor = a.getColor(R.styleable.GraphicView_colorLeftLegend, DEFAULT_LEFT_LEGEND);
            mRightLegendColor = a.getColor(R.styleable.GraphicView_colorRightLegend, DEFAULT_RIGHT_LEGEND);

        } finally {
            a.recycle();
        }

        init();
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        Log.e(TAG, "onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRootCanvas = canvas;
        Log.e(TAG, "onDraw");

        // Tracer du graphique
        mPaint.setColor(mBackgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mGraphRect, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mGraphicsBounds, mPaint);
        mPaint.setStrokeWidth(2f);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(mTopLeftRect, mPaint);
        canvas.drawRect(mTopRightRect, mPaint);
        canvas.drawRect(mTopTitle, mPaint);
        canvas.drawRect(mTop2LeftRect, mPaint);
        canvas.drawRect(mTop2RightRect, mPaint);
        canvas.drawRect(mBottomLeftRect, mPaint);
        canvas.drawRect(mBottomRightRect, mPaint);
        canvas.drawRect(mBottomMiddleRect, mPaint);
        if(mShowLeftLegend)
            canvas.drawRect(mLeftLegend, mPaint);
        if(mShowRightLegend)
            canvas.drawRect(mRightLegend, mPaint);
        canvas.drawRect(mTop2MiddleRect, mPaint);
        canvas.drawRect(mGraphicsBounds, mPaint);


        // Draw Title
        if(mEnableNavigation)
            drawTopActionBar();

        if(mEnableSwitchMod)
            drawOngletBar();

        if(mData.size() > 0) {
            Log.e("Graphic", "List non vide " + mData);
            if(!mDataType.equals(TYPE_DEVOIR))
                drawLegends(getMaxLeftValue(), getMaxRightValue());
            else
                drawLegends(20,20);
            drawGraph();
            drawBottomBar();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure");

        int minw = getPaddingLeft() + getPaddingRight();

        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = getPaddingBottom() + getPaddingTop();
        int h = Math.max(MeasureSpec.getSize(heightMeasureSpec), minh);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged");

        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        mGraphicsBounds = new RectF(0.0f, 0.0f, ww, hh);
        mGraphicsBounds.offsetTo(getPaddingLeft(), getPaddingTop());

        float yOffset = 0;
        float xStart = 0;
        float xEnd = mGraphicsBounds.right;

        if(mEnableNavigation){
            mTopLeftRect = new RectF(0, yOffset, ww / 6, (float) (COEFF_HEIGHT_BARS * hh));
            mTopRightRect = new RectF(5 * ww / 6, 0.0f, ww, (float) (COEFF_HEIGHT_BARS * hh));
            mTopTitle = new RectF(ww / 6, 0.0f, 5 * ww / 4, (float) (COEFF_HEIGHT_BARS * hh));
            yOffset += (float) (COEFF_HEIGHT_BARS * hh);
        }

        if(mEnableSwitchMod){
            mTop2LeftRect = new RectF(0.0f, yOffset, ww / 3, (float) (yOffset + COEFF_HEIGHT_BARS * hh));
            mTop2MiddleRect = new RectF(ww / 3, yOffset, 2 * ww / 3, (float) (yOffset + COEFF_HEIGHT_BARS * hh));
            mTop2RightRect = new RectF(2 * ww / 3, yOffset, ww, (float) (yOffset + COEFF_HEIGHT_BARS * hh));
            yOffset += (float) (COEFF_HEIGHT_BARS * hh);
        }

        mBottomLeftRect = new RectF(0.0f, (float) ((1 - COEFF_HEIGHT_BARS) * hh), ww / 6, hh);
        mBottomMiddleRect = new RectF(ww / 6, (float) ((1 - COEFF_HEIGHT_BARS) * hh), 5 * ww / 6, hh);
        mBottomRightRect = new RectF(5 * ww / 6, (float) ((1 - COEFF_HEIGHT_BARS) * hh), ww, hh);

        if(mShowLeftLegend) {
            mLeftLegend = new RectF(xStart, yOffset, (float) (COEFF_WIDTH_LEGENDS * ww), (float) ((1 - COEFF_HEIGHT_BARS) * hh));
            xStart += (float) (COEFF_WIDTH_LEGENDS * ww);
        }

        if(mShowRightLegend) {
            mRightLegend = new RectF((float) ((1 - COEFF_WIDTH_LEGENDS) * ww), yOffset, ww, (float) ((1 - COEFF_HEIGHT_BARS) * hh));
            xEnd -= (float) (COEFF_WIDTH_LEGENDS * ww);
        }

        mGraphRect = new RectF(xStart, yOffset, xEnd, (float) ((1 - COEFF_HEIGHT_BARS) * hh));

        onDataChanged();
    }


    private void init() {
        Log.e(TAG, "init");
        if (this.isInEditMode()) {
            mData.add(new Item(184, "Lundi", 2));
            mData.add(new Item(135, "Mardi", 6));
            mData.add(new Item(120, "Mercredi", 5));
            mData.add(new Item(50, "Jeudi", 7));
            mData.add(new Item(30, "Vendredi", 2));
            mData.add(new Item(190, "Samedi", 1));
            mData.add(new Item(95, "Dimanche", 4));
            mData.add(new Item(184, "Lundi", 7));
            mData.add(new Item(135, "Mardi", 3));
            mData.add(new Item(120, "Mercredi", 5));
        }

        Log.e(TAG, mData.size() + "");

        if(mNbOfItemsToDisplay > mData.size() && mData.size() > 0)
            mNbOfItemsToDisplay = mData.size();

    }

    private void onDataChanged() {
        Log.e(TAG, "onDataChanged");
    }


    // Utils

    public void drawTopActionBar() {
        mTitlePaint.setTextAlign(Paint.Align.CENTER);
        mTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        mTitlePaint.setTextSize(60);
        mTitlePaint.setColor(Color.DKGRAY);


        int yPos = (int) ( mTopLeftRect.top + (mTopLeftRect.bottom - mTopLeftRect.top)/2 - ((mTitlePaint.descent() + mTitlePaint.ascent()) / 2));
        int xPos = (int) ((mTopTitle.right - mTopTitle.left) / 2);
        mRootCanvas.drawText(String.format("%d %s", mNbOfItemsToDisplay, mDataType), xPos, yPos, mTitlePaint);

        mTitlePaint.setTextSize(70);

        xPos = (int) (0.5 * mTopLeftRect.right);
        mRootCanvas.drawText(" - ", xPos, yPos, mTitlePaint);
        xPos = (int) (mTopRightRect.right - xPos);
        mRootCanvas.drawText(" + ", xPos, yPos, mTitlePaint);
    }

    public void drawOngletBar() {
        mTitlePaint.setTextAlign(Paint.Align.CENTER);
        mTitlePaint.setTextSize(55);
        mTitlePaint.setColor(Color.DKGRAY);
        mTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // Draw middle onglet
        int yPos = (int) (mTop2MiddleRect.top + (mTop2MiddleRect.bottom - mTop2MiddleRect.top)/2 - ((mTitlePaint.descent() + mTitlePaint.ascent()) / 2));
        int xPos = (int) (mTop2MiddleRect.left + (mTop2MiddleRect.right - mTop2MiddleRect.left) / 2);

        if (mCurrentModView == MOD_MIDDLE)
            mTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        mRootCanvas.drawText("Argent / Cours", xPos, yPos, mTitlePaint);
        mTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // Draw left onglet
        xPos = (int) (mTop2LeftRect.right / 2);
        if (mCurrentModView == MOD_LEFT)
            mTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        mRootCanvas.drawText("Argent", xPos, yPos, mTitlePaint);
        mTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        xPos = (int) (mTop2RightRect.right - xPos);
        if (mCurrentModView == MOD_RIGHT)
            mTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        mRootCanvas.drawText("Cours", xPos, yPos, mTitlePaint);
        mTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        return;
    }

    public void drawLegends(double maxLeft, int maxRight) {
        mTitlePaint.setTextAlign(Paint.Align.CENTER);
        mTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        mTitlePaint.setTextSize(60);
        mTitlePaint.setColor(Color.BLACK);

        int heightOfStep = (int) ( mGraphRect.bottom - mGraphRect.top) / mNbOfLegendSteps;
        int xPos = (int) (0.5 * mLeftLegend.right);
        int yPos = (int) (mGraphRect.top + heightOfStep);

        // Lines Legend
        for (int i = 0; i < mNbOfLegendSteps; i++) {
            RectF rectF = new RectF(mGraphRect.left, yPos, mGraphRect.right, yPos + 2);
            mPaint.setColor(Color.DKGRAY);
            mPaint.setStyle(Paint.Style.FILL);
            mRootCanvas.drawRect(rectF, mPaint);
            yPos += heightOfStep;
        }

        // Left Legend
        if (mShowLeftLegend) {
            RectF rectF = new RectF(mGraphRect.left, mGraphRect.top, mGraphRect.left + 5, mGraphRect.bottom);
            mPaint.setColor(Color.BLACK);
            mPaint.setStyle(Paint.Style.FILL);
            mRootCanvas.drawRect(rectF, mPaint);

            xPos = (int) (0.5 * mLeftLegend.right);
            yPos = (int) (mLeftLegend.top + heightOfStep);
            mTitlePaint.setColor(mLeftLegendColor);
            if(!mDataType.equals(TYPE_DEVOIR))
                for (int i = 0; i < mNbOfLegendSteps; i++) {
                    mRootCanvas.drawText("" + Math.round((maxLeft - i * (maxLeft / (mNbOfLegendSteps - 1)))/10)*10, xPos, yPos, mTitlePaint);
                    yPos += heightOfStep;
                }
            else
                for (int i = 1; i < mNbOfLegendSteps; i++) {
                    mRootCanvas.drawText("" + Math.round(maxLeft - i * ((float) maxLeft / (float) (mNbOfLegendSteps))), xPos, yPos, mTitlePaint);
                    yPos += heightOfStep;
                }

        }

        // Right Legend
        if (mShowRightLegend) {
            RectF rectF = new RectF(mGraphRect.right, mGraphRect.top, mGraphRect.right + 5, mGraphRect.bottom);
            mPaint.setColor(Color.BLACK);
            mPaint.setStyle(Paint.Style.FILL);
            mRootCanvas.drawRect(rectF, mPaint);

            xPos = (int) (mGraphicsBounds.right - 0.5 * mLeftLegend.right);
            yPos = (int) (mLeftLegend.top + heightOfStep);
            mTitlePaint.setColor(mRightLegendColor);
            for (int i = 0; i < mNbOfLegendSteps; i++) {
                mRootCanvas.drawText("" + Math.round(maxRight - i * ((float) maxRight / (float) (mNbOfLegendSteps - 1))), xPos, yPos, mTitlePaint);
                yPos += heightOfStep;
            }
        }

        RectF rectF = new RectF(mGraphRect.left, mGraphRect.bottom - 5, mGraphRect.right, mGraphRect.bottom);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mRootCanvas.drawRect(rectF, mPaint);

    }

    public void drawBottomBar(){
        mTitlePaint.setTextAlign(Paint.Align.CENTER);
        mTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        mTitlePaint.setColor(Color.DKGRAY);
        mTitlePaint.setTextSize(70);

        int yPos = (int) (mBottomLeftRect.top + (mBottomLeftRect.bottom - mBottomLeftRect.top)/2 - ((mTitlePaint.descent() + mTitlePaint.ascent()) / 2));
        int xPos = (int) (0.5 * mBottomLeftRect.right);

        mRootCanvas.drawText(" - ", xPos, yPos, mTitlePaint);
        xPos = (int) (mBottomRightRect.right - xPos);
        mRootCanvas.drawText(" + ", xPos, yPos, mTitlePaint);

        mTitlePaint.setTextSize(50);
        mTitlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        yPos = (int) (mBottomLeftRect.top + (mBottomLeftRect.bottom - mBottomLeftRect.top)/2 - ((mTitlePaint.descent() + mTitlePaint.ascent()) / 2));
        xPos = (int) (mGraphicsBounds.right / 2);
        mRootCanvas.drawText(String.format("%s - %s", ((Item) mData.get(mCurrentWeekIndex)).mLabel, ((Item) mData.get(mCurrentWeekIndex + mNbOfItemsToDisplay - 1)).mLabel), xPos, yPos, mTitlePaint);
    }

    public void drawGraph() {
        mPaint.setStyle(Paint.Style.FILL);

        int xOffset = (int) mGraphRect.left+5;
        int yOffset = (int) mGraphRect.bottom-5;
        int widthOfItem = (int) (mGraphRect.right - mGraphRect.left-5)/ mNbOfItemsToDisplay;
        float coeffLeftEchelle, coeffRightEchelle;
        if(!mDataType.equals(TYPE_DEVOIR)) {
            coeffLeftEchelle = (mGraphRect.bottom - mGraphRect.top - 5) / ((float) (getMaxLeftValue() + getMaxLeftValue() / mNbOfLegendSteps));
            coeffRightEchelle = (mGraphRect.bottom - mGraphRect.top - 5) / ((float) getMaxRightValue() + getMaxRightValue() / mNbOfLegendSteps);
        }
        else {
            coeffLeftEchelle =  (mGraphRect.bottom - mGraphRect.top - 5)  / ( (float) 20);
            coeffRightEchelle = (mGraphRect.bottom - mGraphRect.top - 5)  / ( (float) 20);
        }
        double mean = 0;

        if(mCurrentModView == MOD_LEFT) {
            mPaint.setColor(mLeftLegendColor);
            for (int i = mCurrentWeekIndex ; i < mCurrentWeekIndex + mNbOfItemsToDisplay; i++) {
                Item it = (Item) mData.get(i);
                RectF rectF = new RectF(xOffset + 3, (float) (yOffset - coeffLeftEchelle * it.mValueLeft), xOffset + widthOfItem - 3, yOffset);
                mRootCanvas.drawRect(rectF, mPaint);
                xOffset += widthOfItem;
                mean += it.mValueLeft;
            }
            mean /= mNbOfItemsToDisplay;
            yOffset = (int) (yOffset - coeffLeftEchelle*mean)-3;
            RectF rectF = new RectF(mGraphRect.left+5, yOffset,mGraphRect.right-5,yOffset+6);
            mPaint.setColor(mRightLegendColor);
            mRootCanvas.drawRect(rectF, mPaint);

            yOffset = (int) (mGraphRect.top + 50);
            xOffset = (int) (mGraphRect.left + 30);
            mTitlePaint.setColor(Color.BLACK);
            mTitlePaint.setTextSize(45);
            mTitlePaint.setTextAlign(Paint.Align.LEFT);
            String meanLegend = "Moyenne : ";
            if(!mDataType.equals(TYPE_DEVOIR))
                meanLegend = String.format("%s %.2f€",meanLegend, mean);
            else
                meanLegend = String.format("%s %.2f/20",meanLegend, mean);
            mRootCanvas.drawText(meanLegend, xOffset, yOffset, mTitlePaint);

        }

        else if (mCurrentModView == MOD_RIGHT){
            mPaint.setColor(mRightLegendColor);
            for(int i = mCurrentWeekIndex ; i < mCurrentWeekIndex + mNbOfItemsToDisplay; i++){
                Item it = (Item) mData.get(i);
                RectF rectF = new RectF(xOffset+3, (float) (yOffset-coeffRightEchelle*it.mValueRight),xOffset+widthOfItem-3,yOffset);
                mRootCanvas.drawRect(rectF, mPaint);
                xOffset += widthOfItem;mean += it.mValueRight;
            }
            mean /= mNbOfItemsToDisplay;
            yOffset = (int) (yOffset - coeffRightEchelle*mean) -3;
            RectF rectF = new RectF(mGraphRect.left+5, yOffset,mGraphRect.right-5,yOffset+6);
            mPaint.setColor(mLeftLegendColor);
            mRootCanvas.drawRect(rectF, mPaint);

            yOffset = (int) (mGraphRect.top + 50);
            xOffset = (int) (mGraphRect.left + 30);
            mTitlePaint.setColor(Color.BLACK);
            mTitlePaint.setTextSize(45);
            mTitlePaint.setTextAlign(Paint.Align.LEFT);
            mRootCanvas.drawText(String.format("Moyenne : %.1f cours", mean), xOffset, yOffset, mTitlePaint);
        }

        else{
            widthOfItem /= 2;
            Log.e("Graphic", "nbItem : " + mNbOfItemsToDisplay + " - CurrentIndex : " + mCurrentWeekIndex);
            for (int i = mCurrentWeekIndex ; i < mCurrentWeekIndex + mNbOfItemsToDisplay; i++) {
                Item it = (Item) mData.get(i);
                Log.e("Graphic", it.toString());
                RectF rectF = new RectF(xOffset + 3, (float) (yOffset - coeffLeftEchelle * it.mValueLeft), xOffset + widthOfItem, yOffset);
                mPaint.setColor(mLeftLegendColor);
                mRootCanvas.drawRect(rectF, mPaint);
                xOffset += widthOfItem;
                rectF = new RectF(xOffset, (float) (yOffset-coeffRightEchelle*it.mValueRight),xOffset+widthOfItem-3,yOffset);
                mPaint.setColor(mRightLegendColor);
                mRootCanvas.drawRect(rectF, mPaint);
                xOffset += widthOfItem;
            }
        }
    }

    public double getMaxLeftValue(){
        double leftValue = 0;
        Item it;
        for(int i = mCurrentWeekIndex ; i < mCurrentWeekIndex + mNbOfItemsToDisplay; i++) {
            it = (Item) mData.get(i);
            if (it.mValueLeft > leftValue)
                leftValue = it.mValueLeft;
        }
        return leftValue;
    }

    public int getMaxRightValue(){
        int rightValue = 0;
        Item it;
        for(int i = mCurrentWeekIndex ; i < mCurrentWeekIndex + mNbOfItemsToDisplay; i++) {
            it = (Item) mData.get(i);
            if (it.mValueRight > rightValue)
                rightValue = it.mValueRight;
        }
        return rightValue;
    }


    public void setListWeeks(List<PeriodicItem> listData){
        for(PeriodicItem week : listData){
            mData.add(new Item(
                    week.getMoney(),
                    week.getLabel(),
                    week.getNbCourse()));
        }
        if(mNbOfItemsToDisplay > mData.size() && mData.size() > 0)
            mNbOfItemsToDisplay = mData.size();
        mDataType = TYPE_WEEK;
        invalidate();
    }

    public void setListMonths(List<PeriodicItem> listData){
        for(PeriodicItem month : listData){
            mData.add(new Item(
                    month.getMoney(),
                    month.getLabel(),
                    month.getNbCourse()));
        }
        if(mNbOfItemsToDisplay > mData.size() && mData.size() > 0)
            mNbOfItemsToDisplay = mData.size();
        mDataType = TYPE_MONTH;
        invalidate();
    }

    public void setListYears(List<PeriodicItem> listData){
        for(PeriodicItem year : listData){
            mData.add(new Item(
                    year.getMoney(),
                    year.getLabel(),
                    year.getNbCourse()));
        }
        if(mNbOfItemsToDisplay > mData.size() && mData.size() > 0)
            mNbOfItemsToDisplay = mData.size();
        mDataType = TYPE_YEAR;
        invalidate();
    }

    public void setListDevoirs(List<Devoir> listDevoirs){
        mData.clear();
        mNbOfItemsToDisplay = 10;
        for(Devoir devoir : listDevoirs){
            mData.add(new Item(
                    devoir.getNote(),
                    C.formatDate(devoir.getDate(),C.DD_MM_YY),
                    0));
        }
        if(mNbOfItemsToDisplay > mData.size() && mData.size() > 0)
            mNbOfItemsToDisplay = mData.size();

        mCurrentModView = MOD_LEFT;
        mShowRightLegend = false;
        mDataType = TYPE_DEVOIR;
        mNbOfLegendSteps = 10;
        invalidate();
    }




    // Updating View

    public int addAWeek() {
        if(mCurrentWeekIndex + mNbOfItemsToDisplay < mData.size()) {
            mNbOfItemsToDisplay += 1;
        }
        else if(mCurrentWeekIndex + mNbOfItemsToDisplay >= mData.size() && mCurrentWeekIndex > 0) {
            mCurrentWeekIndex -= 1;
            mNbOfItemsToDisplay += 1;
        }

        invalidate();
        return mNbOfItemsToDisplay;
    }

    public int removeAWeek() {
        if(mNbOfItemsToDisplay > 1) {
            mNbOfItemsToDisplay -= 1;
            invalidate();
        }

        return mNbOfItemsToDisplay;
    }

    public int changeModView(int modView) {
        Log.e(TAG, "changeModView" + modView);
        mCurrentModView = modView;
        switch (modView) {
            case MOD_LEFT:
                mShowLeftLegend = true;
                mShowRightLegend = false;
                mGraphRect.right = mGraphicsBounds.right;
                mGraphRect.left = mLeftLegend.right;
                break;
            case MOD_RIGHT:
                mShowLeftLegend = false;
                mShowRightLegend = true;
                mGraphRect.left = mGraphicsBounds.left;
                mGraphRect.right = mRightLegend.left;
                break;
            default:
                mShowLeftLegend = true;
                mShowRightLegend = true;
                mGraphRect.left = mLeftLegend.right;
                mGraphRect.right = mRightLegend.left;
                break;
        }
        invalidate();
        return modView;
    }

    public void changeWeekIndex(int offset){
        if( offset > 0 && mCurrentWeekIndex + mNbOfItemsToDisplay < mData.size())
            mCurrentWeekIndex += offset;
        else if( offset < 0 && mCurrentWeekIndex > 0)
            mCurrentWeekIndex += offset;
        invalidate();
    }


    // Interface

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mTopLeftRect.contains(event.getX(), event.getY())) {
            removeAWeek();
        } else if (mTopRightRect.contains(event.getX(), event.getY())) {
            addAWeek();
        } else if (mTop2LeftRect.contains(event.getX(), event.getY())) {
            changeModView(MOD_LEFT);
        } else if (mTop2RightRect.contains(event.getX(), event.getY())) {
            changeModView(MOD_RIGHT);
        } else if (mTop2MiddleRect.contains(event.getX(), event.getY())) {
            changeModView(MOD_MIDDLE);
        } else if (mBottomLeftRect.contains(event.getX(), event.getY())) {
            changeWeekIndex(-1);
        } else if (mBottomRightRect.contains(event.getX(), event.getY())) {
            changeWeekIndex(1);
        }

        return false;
    }


    // Inner class
    private class Item {
        public double mValueLeft;
        public String mLabel;
        public int mValueRight;

        public Item(double mValueLeft, String mLabel, int mValueRight) {
            this.mValueLeft = mValueLeft;
            this.mLabel = mLabel.split(" - ")[0];
            this.mValueRight = mValueRight;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "mValueLeft=" + mValueLeft +
                    ", mLabel='" + mLabel + '\'' +
                    ", mValueRight=" + mValueRight +
                    '}';
        }
    }
}
