package com.rubus.ITF25ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Choreographer;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    Switch mSwitchShowBar;
    BarInterleaved25View mBar25View;
    private TextView mScreenUpdateFps;
    private TextView mBarcodeUpdateFps;

    SeekBar mSeekBarEveryFrames;
    private int mSeekBarMin = 1;
    private int mUpdateBarCodeEveryFrames = 1;
    private int mFramesCount = 0;
    private long mStartShowBarCodeTime;
    private int mUpdateBarcodeCount = 0;

    private boolean mEnableUpdateCodeBar = false;
    private int mBarCodeInteger = 0;

    // frame callback
    Choreographer.FrameCallback mFrameCallback;

    // TextView array of every x frames update
    Vector<TextView> mEveryXFrameUpdateNumber;
    int[] mEveryXFrameUpdateEachInteger;
    int mEveryFrameUpdateDisplayIndex = 0;
    private int mCurrentDrawTextColor = Color.parseColor("#303030");
    private int mDisplayedTextColor = Color.parseColor("#708090");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSeekBarEveryFrames = (SeekBar) findViewById(R.id.seekbar_every_frames);
        mBar25View = (BarInterleaved25View)findViewById(R.id.bar25code_view);
        mSwitchShowBar = (Switch) findViewById(R.id.switch_show_bar);
        mScreenUpdateFps = (TextView) findViewById(R.id.text_screen_fps);
        mBarcodeUpdateFps = (TextView) findViewById(R.id.text_barcode_update_fps);

        mEveryXFrameUpdateNumber = new Vector<TextView>();
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count1));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count2));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count3));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count4));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count5));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count6));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count7));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count8));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count9));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count10));

        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count11));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count12));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count13));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count14));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count15));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count16));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count17));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count18));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count19));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count20));

        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count21));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count22));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count23));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count24));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count25));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count26));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count27));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count28));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count29));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count30));

        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count31));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count32));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count33));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count34));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count35));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count36));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count37));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count38));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count39));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count40));

        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count41));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count42));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count43));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count44));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count45));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count46));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count47));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count48));
        mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count49));
        // mEveryXFrameUpdateNumber.add((TextView) findViewById(R.id.tex_every_x_franes_count50));

        mEveryXFrameUpdateEachInteger = new int[mEveryXFrameUpdateNumber.size()];

        mSwitchShowBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mEnableUpdateCodeBar = b;
                if (mEnableUpdateCodeBar) {
                    StartShowBarCodes();
                    Choreographer.getInstance().postFrameCallback(mFrameCallback);
                } else
                    StopShowBarCodes();
            }
        });

        mSeekBarEveryFrames.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mUpdateBarCodeEveryFrames = mSeekBarMin + i;
                TextView txtView = (TextView) findViewById(R.id.text_update_every_frames);
                String sz = String.format("Update barcode every %d frames.", mUpdateBarCodeEveryFrames);
                txtView.setText(sz);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        mFrameCallback = lFrameTimeNs -> {
            if (mEnableUpdateCodeBar) {
                OnEachFrameCallback();

                // register callback for next frame
                Choreographer.getInstance().postFrameCallback(mFrameCallback);
            }
        };
    }

    private void StartShowBarCodes() {
        mFramesCount = 0;
        mUpdateBarcodeCount = 0;
        mStartShowBarCodeTime = System.currentTimeMillis();

        mSeekBarEveryFrames.setEnabled(false);
    }
    private void StopShowBarCodes() {
        mSeekBarEveryFrames.setEnabled(true);
    }
    private void OnEachFrameCallback() {
        mFramesCount++;
        // update screen fps
        float duration = (float)(System.currentTimeMillis() - mStartShowBarCodeTime) / 1000.0f;
        float screenFps = (float)(mFramesCount / duration);
        String sz = String.format("%5.2f", screenFps);
        mScreenUpdateFps.setText(sz);

        if (mFramesCount % mUpdateBarCodeEveryFrames == 0) {
            float updateBarcodeFps = (float)(mUpdateBarcodeCount / duration);
            String fps = String.format("%5.2f", updateBarcodeFps);
            mBarcodeUpdateFps.setText(fps);

            // update barcode
            mBar25View.SetBarNumber(++mBarCodeInteger);
            mBar25View.InvalidateView();
            mUpdateBarcodeCount++;

            // update every x frames update
            OnEveryXFramesUpdate();
        }
    }

    private void OnEveryXFramesUpdate() {
        boolean displayEachCx = true;
        if (displayEachCx) {
            int index = mEveryFrameUpdateDisplayIndex % mEveryXFrameUpdateNumber.size();
            mEveryXFrameUpdateEachInteger[index]++;
            int val = mEveryXFrameUpdateEachInteger[index];

            TextView txtView = mEveryXFrameUpdateNumber.elementAt(index);
            // String z = String.format("%d", val);
            String z = String.format("%d", mEveryFrameUpdateDisplayIndex);
            txtView.setTextColor(mCurrentDrawTextColor);
            txtView.setText(z);

            // last text view
            TextView lastTxtView = mEveryXFrameUpdateNumber.elementAt(index == 0 ? mEveryXFrameUpdateNumber.size() - 1 : index - 1);
            String s1 = lastTxtView.getText().toString();
            lastTxtView.setTextColor(mDisplayedTextColor);

            mEveryFrameUpdateDisplayIndex++;
            if (mEveryFrameUpdateDisplayIndex > 9999) mEveryFrameUpdateDisplayIndex = 0;
        } else {
            int index = mUpdateBarcodeCount % mEveryXFrameUpdateNumber.size();
            TextView txtView = mEveryXFrameUpdateNumber.elementAt(index);
            String z = String.format("%d", mUpdateBarcodeCount);
            txtView.setText(z);
        }
    }
}