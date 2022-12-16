package com.rubus.ITF25ProgressBar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Vector;

public class BarInterleaved25View extends View {
    class Bar {
        public boolean isBlackBar = false;
        public boolean isBigBar = false;
    }
    private final int EMPTY_LEFT_BIG_BARS = 3;
    private final int EMPTY_RIGHT_BIG_BARS = 2;

    private int mBarNumber = 0;
    private String mBarNumberString;
    private int mBigBarSmallBarWidthRatio = 3;
    private Vector<Bar> mBars;
    private int mValidBars = 0;

    // 字符的编码细节，“00110” 这种
    // char, black bars detail
    byte[] mCharBlackBarsDetail = new byte[5];
    // char, white bars detail
    byte[] mCharWhiteBarsDetail = new byte[5];

    // paint
    Paint mBlackPaint;
    Paint mWhitePaint;
    Paint mTextPaint;

    // 在java代码里new的时候会用到
    // @param context
    public BarInterleaved25View(Context context) { super(context); onBarInterleaved25ViewInit(); }

    // 在xml布局文件中使用时自动调用
    // @param context
    public BarInterleaved25View(Context context, @Nullable AttributeSet attrs) { super(context, attrs); onBarInterleaved25ViewInit(); }

    // 不会自动调用，如果有默认style时，在第二个构造函数中调用
    // @param context
    // @param attrs
    // @param defStyleAttr
    public BarInterleaved25View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); onBarInterleaved25ViewInit(); }

    // 只有在API版本>21时才会用到
    // 不会自动调用，如果有默认style时，在第二个构造函数中调用
    // @param context
    // @param attrs
    // @param defStyleAttr
    // @param defStyleRes
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BarInterleaved25View(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); onBarInterleaved25ViewInit(); }

    private void onBarInterleaved25ViewInit() {
        mBlackPaint = new Paint();
        mBlackPaint.setColor(Color.BLACK);
        mBlackPaint.setStyle(Paint.Style.FILL);

        mWhitePaint = new Paint();
        mWhitePaint.setColor(Color.WHITE);
        mWhitePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.DKGRAY);
        mTextPaint.setTypeface(Typeface.MONOSPACE);
        mTextPaint.setTextSize(50);

        mBarNumberString = "";
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

    private void UpdateBar(int index, boolean isBlackBar, boolean isBigBar) {
        Bar b = mBars.elementAt(index);
        if (b == null) { b = new Bar(); mBars.setElementAt(b, index);}
        b.isBlackBar = isBlackBar;
        b.isBigBar = isBigBar;
    }

    private void GetBarCodeOfChar(char c, byte[] a) {
        if (c == '0') { a[0] = 0; a[1] = 0; a[2] = 1; a[3] = 1; a[4] = 0; } // "00110"
        if (c == '1') { a[0] = 1; a[1] = 0; a[2] = 0; a[3] = 0; a[4] = 1; } // "10001"
        if (c == '2') { a[0] = 0; a[1] = 1; a[2] = 0; a[3] = 0; a[4] = 1; } // "01001"
        if (c == '3') { a[0] = 1; a[1] = 1; a[2] = 0; a[3] = 0; a[4] = 0; } // "11000"
        if (c == '4') { a[0] = 0; a[1] = 0; a[2] = 1; a[3] = 0; a[4] = 1; } // "00101"

        if (c == '5') { a[0] = 1; a[1] = 0; a[2] = 1; a[3] = 0; a[4] = 0; } // "10100"
        if (c == '6') { a[0] = 0; a[1] = 1; a[2] = 1; a[3] = 0; a[4] = 0; } // "01100"
        if (c == '7') { a[0] = 0; a[1] = 0; a[2] = 0; a[3] = 1; a[4] = 1; } // "00011"
        if (c == '8') { a[0] = 1; a[1] = 0; a[2] = 0; a[3] = 1; a[4] = 0; } // "10010"
        if (c == '9') { a[0] = 0; a[1] = 1; a[2] = 0; a[3] = 1; a[4] = 0; } // "01010"

        //if (binCodes.equals("00110")) return "0";
        //if (binCodes.equals("10001")) return "1";
        //if (binCodes.equals("01001")) return "2";
        //if (binCodes.equals("11000")) return "3";
        //if (binCodes.equals("00101")) return "4";

        //if (binCodes.equals("10100")) return "5";
        //if (binCodes.equals("01100")) return "6";
        //if (binCodes.equals("00011")) return "7";
        //if (binCodes.equals("10010")) return "8";
        //if (binCodes.equals("01010")) return "9";
    }

    // get base bars count
    private int UpdateStringBars() {
        // step 1: 转成字符串，如果不是偶数，前面添0
        String numbers = String.format("%d", mBarNumber);
        if (numbers.length() % 2 == 1) numbers = "0" + numbers;
        mBarNumberString = numbers;

        // step 2: calculate total bars
        int smallBars = 4 + 2;  // start: 2 black small bars, 2 white small bars, end: 1 big black bar, 1 white bar, 1 black bar
        int bigBars = EMPTY_LEFT_BIG_BARS + 1 + EMPTY_RIGHT_BIG_BARS;  // end: 1 big bar, 1 white bar, 1 black bar
        smallBars += numbers.length() * 3;
        bigBars += numbers.length() * 2;
        mValidBars = smallBars + bigBars;
        int baseBars = smallBars + bigBars *  mBigBarSmallBarWidthRatio;
        CheckBarsCapacity(smallBars + bigBars);

        // step 3: encode bars
        // left empty big bars
        int index = -1;
        for (int i = 0; i < EMPTY_LEFT_BIG_BARS; ++i) {
            UpdateBar(++index, false, true);
        }

        // start bars
        UpdateBar(++index, true, false);
        UpdateBar(++index, false, false);
        UpdateBar(++index, true, false);
        UpdateBar(++index, false, false);

        // chars string
        int pairs = numbers.length() / 2;
        for (int i = 0; i < pairs; ++i) {
            char c1 = numbers.charAt(i * 2);
            GetBarCodeOfChar(c1, mCharBlackBarsDetail);
            char c2 = numbers.charAt(i * 2 + 1);
            GetBarCodeOfChar(c2, mCharWhiteBarsDetail);

            for (int j = 0; j < 5; ++j) {
                UpdateBar(++index, true, mCharBlackBarsDetail[j] == 1);
                UpdateBar(++index, false, mCharWhiteBarsDetail[j] == 1);
            }
        }

        // end bars
        UpdateBar(++index, true, true);
        UpdateBar(++index, false, false);
        UpdateBar(++index, true, false);

        // right empty big white bars
        for (int i = 0; i < EMPTY_RIGHT_BIG_BARS; ++i)
            UpdateBar(++index, false, true);

        return baseBars;
    }

    private void DrawBars(Canvas canvas) {
        int baseBarsCount = UpdateStringBars();

        boolean drawHorizontal = true;
        if (drawHorizontal) {
            int baseBarWidth = (int)((float)getWidth() / (float)baseBarsCount);
            int left = 0;
            int textHeight = 60;
            int barHeight = getHeight() - textHeight;
            for (int i = 0; i < mValidBars; ++i) {
                Bar b = mBars.elementAt(i);
                int width = b.isBigBar ? baseBarWidth * mBigBarSmallBarWidthRatio : baseBarWidth;
                // right: top + barHeight
                canvas.drawRect(left, 0, left + width, barHeight, b.isBlackBar ? mBlackPaint : mWhitePaint);
                left += width;
            }

            // draw text
            int textLeft = baseBarWidth * (EMPTY_LEFT_BIG_BARS * mBigBarSmallBarWidthRatio + 4) + 5;
            canvas.drawText(mBarNumberString, textLeft, barHeight + textHeight / 2 + 20, mTextPaint);
        } else {
            // vertical
            int baseBarWidth = (int)((float)getHeight() / (float)baseBarsCount);
            int top = 0;
            int textHeight = 60;
            int barHeight = getWidth() - textHeight;
            for (int i = 0; i < mValidBars; ++i) {
                Bar b = mBars.elementAt(i);
                int width = b.isBigBar ? baseBarWidth * mBigBarSmallBarWidthRatio : baseBarWidth;
                canvas.drawRect(0, top, barHeight, top + width , b.isBlackBar ? mBlackPaint : mWhitePaint);
                top += width;
            }

            // draw text
            // int textLeft = barHeight + textHeight / 2 + 20;
            // int textLeft = baseBarWidth * (EMPTY_LEFT_BIG_BARS * mBigBarSmallBarWidthRatio + 4) + 5;
            canvas.drawText(mBarNumberString, 0, 40, mTextPaint);
        }
    }

    public void SetBarNumber(int barNumber) { mBarNumber = barNumber; }
    public int GetBarNumber() { return mBarNumber; }
    public void InvalidateView() { super.invalidate(); }
}
