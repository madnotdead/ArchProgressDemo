package com.madnotdead.archprogressdemo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.number_progress)
    TextView number_progress;

    @BindView(R.id.archProgressView)
    ArchProgressView archProgressView;

    @BindView(R.id.seekPercentage)
    SeekBar seekProgress;

    private  int _progressValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/AkzidGrtskProBolCnd.otf");

        number_progress.setTypeface(tf);

        seekProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i(TAG, "onProgressChanged: " + i);
                _progressValue = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick(R.id.startAnimation)
    void onButtonClick() {
        archProgressView.setProgressWithAnimation(_progressValue);
    }

}
