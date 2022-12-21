package com.rubus.ITF25ProgressBar;

import android.content.Context;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Vector;

public class ITF25ProgressBarView extends View {
    class Bar {
        public boolean isBlackBar = false;
        public boolean isBigBar = false;
    }
    private final int EMPTY_LEFT_BIG_BARS = 1;
    private final int EMPTY_RIGHT_BIG_BARS = 1;

    // private int mBigBarSmallBarWidthRatio = 3;
    private int mBigBarSmallBarWidthRatio = 2;
    private Vector<Bar> mBars;

    // small bar width
    private int mSmallBarWidth = 21;
    private boolean mIsVerticalBar = true;
    private boolean mBarsInitialized = false;
    private int mValidSmallBars = 0;
    private int mValidBigBars = 0;

    // bar pos index
    private int mProgressBarStartIndex = 0;
    private int mProgressBarEndIndex = 0;
    private int mPos = 0;

    // padding bars
    private int mBarPaddingHead = 0;
    private int mBarPaddingTail = 0;

    // paint
    Paint mBlackPaint;
    Paint mWhitePaint;
    Paint mBarHeaderSpacePaint;
    Paint mPaddingBarsPaint;
    Paint mTextPaint;

    // 在java代码里new的时候会用到
    // @param context
    public ITF25ProgressBarView(Context context) { super(context); onBarInterleaved25ViewInit(context, null); }

    // 在xml布局文件中使用时自动调用
    // @param context
    public ITF25ProgressBarView(Context context, @Nullable AttributeSet attrs) { super(context, attrs); onBarInterleaved25ViewInit(context, attrs); }

    // 不会自动调用，如果有默认style时，在第二个构造函数中调用
    // @param context
    // @param attrs
    // @param defStyleAttr
    public ITF25ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); onBarInterleaved25ViewInit(context, attrs); }

    // 只有在API版本>21时才会用到
    // 不会自动调用，如果有默认style时，在第二个构造函数中调用
    // @param context
    // @param attrs
    // @param defStyleAttr
    // @param defStyleRes
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ITF25ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); onBarInterleaved25ViewInit(context, attrs); }

    private void onBarInterleaved25ViewInit(Context context, AttributeSet attrs) {
        // get special attributes
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ITF25ProgressBarView);
        int textSize = ta.getInteger(R.styleable.ITF25ProgressBarView_textSize, 5);
        int textColor = ta.getColor(R.styleable.ITF25ProgressBarView_textColor, Color.DKGRAY);
        int barColor = ta.getColor(R.styleable.ITF25ProgressBarView_barColor, Color.BLACK);
        int barHeaderSpaceColor = ta.getColor(R.styleable.ITF25ProgressBarView_barHeaderSpaceColor, Color.YELLOW);

        // padding bars
        mBarPaddingHead = ta.getInteger(R.styleable.ITF25ProgressBarView_barPaddingHead, 0);
        mBarPaddingTail = ta.getInteger(R.styleable.ITF25ProgressBarView_barPaddingTail, 0);
        int paddingBarColor = ta.getColor(R.styleable.ITF25ProgressBarView_barPaddingBarColor, Color.CYAN);
        ta.recycle();

        mBlackPaint = new Paint();
        mBlackPaint.setColor(barColor);
        mBlackPaint.setStyle(Paint.Style.FILL);

        mWhitePaint = new Paint();
        mWhitePaint.setColor(Color.WHITE);
        mWhitePaint.setStyle(Paint.Style.FILL);

        mBarHeaderSpacePaint = new Paint();
        mBarHeaderSpacePaint.setColor(barHeaderSpaceColor);
        mBarHeaderSpacePaint.setStyle(Paint.Style.FILL);

        mPaddingBarsPaint = new Paint();
        mPaddingBarsPaint.setColor(paddingBarColor);
        mPaddingBarsPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTypeface(Typeface.MONOSPACE);
        mTextPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ///< 2.进行绘制

        DrawBars(canvas);
    }

    private void CheckBarsCapacity(int totalBars) {
        if (mBars == null) mBars = new Vector<Bar>(totalBars);
        if (mBars.size() < totalBars) mBars.setSize(totalBars);
    }

    private void UpdateWhiteSmallBar(int index) { UpdateBarByChar(index, '-'); }
    private void UpdateWhiteBigBar(int index) { UpdateBarByChar(index, '='); }
    private void UpdateBlackSmallBar(int index) { UpdateBarByChar(index, '0'); }
    private void UpdateBlackBigBar(int index) { UpdateBarByChar(index, '='); }
    private void UpdateBar(int index, boolean isBlackBar, boolean isBigBar) {
        Bar b = mBars.elementAt(index);
        if (b == null) { b = new Bar(); mBars.setElementAt(b, index);}
        b.isBlackBar = isBlackBar;
        b.isBigBar = isBigBar;
    }

    // '0', black colored small bar
    // '1', black colored big bar
    // '-', white colored small bar
    // '=', white colored big bar
    private void UpdateBarByChar(int index, char c) {
        if (c == '0') UpdateBar(index, true, false);
        if (c == '1') UpdateBar(index, true, true);
        if (c == '-') UpdateBar(index, false, false);
        if (c == '=') UpdateBar(index, false, true);
    }

    // get base bars count
    private void InitializeBars() {
        // step 1: Initialize
        mBarsInitialized = false;

        // step 2: calculate total bars
        // left start: 0-1-1-0, 0 small black bar, - space white bar, 1 big black bar
        // right end:  0-1-0-1, 0 small black bar, - space white bar, 1 big black bar
        mValidSmallBars = 2 + 3;  // start: 2 black small bars, 3 white small bars, 2 big black bars,
        mValidBigBars = 2;
        mValidSmallBars += (2 + 3);// end: 2 small black bars, 3 white small bars, 2 big black bars.
        mValidBigBars += 2;

        mValidBigBars += (EMPTY_LEFT_BIG_BARS + EMPTY_RIGHT_BIG_BARS);

        int space = mIsVerticalBar ? getWidth() : getHeight();
        int leftRightBarsWidth = (mValidSmallBars + mValidBigBars * mBigBarSmallBarWidthRatio) * mSmallBarWidth;
        int progressSmalleBars = (space - leftRightBarsWidth) / mSmallBarWidth;
        // need odd-number-x progress small bars: II-1-1-1-1-II
        progressSmalleBars = (progressSmalleBars % 2 == 0) ? progressSmalleBars - 1 : progressSmalleBars;

        mValidSmallBars += progressSmalleBars;
        CheckBarsCapacity(mValidSmallBars + mValidBigBars);

        // step 3: encode bars
        // left empty big bars
        int index = -1;
        for (int i = 0; i < EMPTY_LEFT_BIG_BARS; ++i) { UpdateWhiteBigBar(++index); }

        // start bars, 0-1-1-0
        UpdateBarByChar(++index, '0');
        UpdateBarByChar(++index, '-');
        UpdateBarByChar(++index, '1');
        UpdateBarByChar(++index, '-');
        UpdateBarByChar(++index, '1');
        UpdateBarByChar(++index, '-');
        UpdateBarByChar(++index, '0');

        // progress small white bars
        mProgressBarStartIndex = index + 1;
        for (int i = 0; i < progressSmalleBars; ++i) { UpdateBarByChar(++index, '-'); }
        mProgressBarEndIndex = index - 1;  // skip last space small white bar

        mPos = 0;
        UpdateBarByChar(mProgressBarStartIndex + 1, '0');

        // end bars
        // 0-1-0-1
        UpdateBarByChar(++index, '0');
        UpdateBarByChar(++index, '-');
        UpdateBarByChar(++index, '1');
        UpdateBarByChar(++index, '-');
        UpdateBarByChar(++index, '0');
        UpdateBarByChar(++index, '-');
        UpdateBarByChar(++index, '1');

        // right empty big white bars
        for (int i = 0; i < EMPTY_RIGHT_BIG_BARS; ++i) { UpdateWhiteBigBar(++index); }

        mBarsInitialized = true;
    }
    private void CheckBarsInitialized() { if (!mBarsInitialized) InitializeBars(); }

    private void DrawBars(Canvas canvas) {
        CheckBarsInitialized();;
        int baseBarsCount = mValidSmallBars + mValidBigBars * mBigBarSmallBarWidthRatio;
        int validBars = mValidSmallBars + mValidBigBars;
        // single direction increase progress bar
        String posText = String.format("progress bar - %02d / %d", GetPos(), GetProgressMax());

        int startBars = EMPTY_LEFT_BIG_BARS + 6;
        if (mIsVerticalBar) {
            // vertical bars
            int baseBarWidth = (int)((float)getWidth() / (float)baseBarsCount);
            int left = 0;
            int textHeight = 60;
            int barHeight = getHeight() - textHeight - mBarPaddingHead - mBarPaddingTail;
            int bigBarWidth = baseBarWidth * mBigBarSmallBarWidthRatio;
            for (int i = 0; i < validBars; ++i) {
                Bar b = mBars.elementAt(i);
                int width = b.isBigBar ? bigBarWidth : baseBarWidth;

                Paint barPaint = b.isBlackBar ? mBlackPaint : mWhitePaint;

                // draw padding bars at header and tail of white space bar
                if (!b.isBlackBar && !b.isBigBar && i < startBars) {
                    barPaint = mBarHeaderSpacePaint;
                    canvas.drawRect(left, 0, left + width, mBarPaddingHead, mPaddingBarsPaint);
                    canvas.drawRect(left, mBarPaddingHead + barHeight, left + width, mBarPaddingHead + barHeight + mBarPaddingTail, mPaddingBarsPaint);
                }

                // draw bar
                // right: top + barHeight
                canvas.drawRect(left, mBarPaddingHead, left + width, mBarPaddingHead + barHeight, barPaint);
                left += width;
            }

            // draw text
            int textLeft = baseBarWidth * (EMPTY_LEFT_BIG_BARS * mBigBarSmallBarWidthRatio + 4) + 5;
            canvas.drawText(posText, textLeft, mBarPaddingHead + barHeight + mBarPaddingTail + textHeight / 2 + 20, mTextPaint);
        } else {
            // horizontal bars
            int baseBarWidth = (int)((float)getHeight() / (float)baseBarsCount);
            int top = 0;
            int textHeight = 60;
            int barHeight = getWidth() - textHeight;
            int bigBarWidth = baseBarWidth * mBigBarSmallBarWidthRatio;
            for (int i = 0; i < validBars; ++i) {
                Bar b = mBars.elementAt(i);
                int width = b.isBigBar ? bigBarWidth : baseBarWidth;
                canvas.drawRect(0, top, barHeight, top + width , b.isBlackBar ? mBlackPaint : mWhitePaint);
                top += width;
            }

            // draw text
            // int textLeft = barHeight + textHeight / 2 + 20;
            // int textLeft = baseBarWidth * (EMPTY_LEFT_BIG_BARS * mBigBarSmallBarWidthRatio + 4) + 5;
            canvas.drawText(posText, 0, 40, mTextPaint);
        }
    }

    public void SetSmallBarWidth(int width) { mSmallBarWidth = width; InitializeBars(); }
    public void SetBarDirection(boolean isVertical) { mIsVerticalBar = isVertical; };
    public void InvalidateView() { super.invalidate(); }
    public int SetProgressBarIncreaseOne() {
        int pos = GetPos();
        SetPos(++pos);
        pos = GetPos();
        return pos;
    }
    // 0-1-2-3...
    public int GetProgressMax() { return mBarsInitialized ? (mProgressBarEndIndex - mProgressBarStartIndex + 1) / 2 - 1 : 0; }
    public int GetPos() { return mPos; }
    public void SetPos(int pos) {
        if (!mBarsInitialized) return;
        if (pos < 0) pos = 0;
        int maxPos = GetProgressMax();
        if (pos > maxPos) pos = 0;

        // clean
        if (pos == 0) {
            for (int i = 1; i <= maxPos; ++i)
                mBars.elementAt(mProgressBarStartIndex + i * 2 + 1).isBlackBar = false;
        }

        // II-1-1-1-1-1-----II
        mBars.elementAt(mProgressBarStartIndex + pos * 2 + 1).isBlackBar = true;
        mPos = pos;
    }
}
