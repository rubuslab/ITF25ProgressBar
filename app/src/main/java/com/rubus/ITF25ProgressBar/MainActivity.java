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

    private TextView mScreenUpdateFps;
    private TextView mBarcodeUpdateFps;

    // ITF25ProgressBarView
    ITF25ProgressBarView mItf25ProgressBar;

    SeekBar mSeekBarEveryFrames;
    private int mSeekBarMin = 1;
    private int mUpdateBarCodeEveryFrames = 1;
    private int mFramesCount = 0;
    private long mStartShowBarCodeTime;
    private int mUpdateBarcodeCount = 0;

    private boolean mEnableUpdateCodeBar = false;

    // frame callback
    Choreographer.FrameCallback mFrameCallback;

    // TextView array of every x frames update
    private int mCurrentDrawTextColor = Color.parseColor("#303030");
    private int mDisplayedTextColor = Color.parseColor("#708090");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSeekBarEveryFrames = (SeekBar) findViewById(R.id.seekbar_every_frames);
        mSwitchShowBar = (Switch) findViewById(R.id.switch_show_bar);
        mScreenUpdateFps = (TextView) findViewById(R.id.text_screen_fps);
        mBarcodeUpdateFps = (TextView) findViewById(R.id.text_barcode_update_fps);
        mItf25ProgressBar = (ITF25ProgressBarView)findViewById(R.id.itf25_progress_bar_view);

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

            // update progress bar
            mItf25ProgressBar.SetProgressBarIncreaseOne();
            mItf25ProgressBar.InvalidateView();

            mUpdateBarcodeCount++;

            // update every x frames update
            OnEveryXFramesUpdate();
        }
    }

    private void OnEveryXFramesUpdate() {
    }
}