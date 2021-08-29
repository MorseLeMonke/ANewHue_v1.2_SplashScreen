package com.example.anewhue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ToggleButton;

public class HomeActivity extends AppCompatActivity {

    private SharedMemory mSharedMemory;
    private ToggleButton mToggleButton;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar alpha, red, green, blue;
        alpha = findViewById(R.id.seek_alpha);
        red = findViewById(R.id.seek_red);
        green = findViewById(R.id.seek_green);
        blue = findViewById(R.id.seek_blue);

        getSupportActionBar().hide();

        mToggleButton = findViewById(R.id.startButton);

        mSharedMemory = new SharedMemory(this);

        SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mSharedMemory.setAlpha(alpha.getProgress());
                mSharedMemory.setRed(red.getProgress());
                mSharedMemory.setGreen(green.getProgress());
                mSharedMemory.setBlue(blue.getProgress());

                if (ANewHueFilterService.STATE == ANewHueFilterService.STATE_ACTIVE) {
                    Intent intent =new Intent(HomeActivity.this, ANewHueFilterService.class);
                    startService(intent);
                }

                mToggleButton.setChecked(ANewHueFilterService.STATE == ANewHueFilterService.STATE_ACTIVE);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        alpha.setOnSeekBarChangeListener(changeListener);
        red.setOnSeekBarChangeListener(changeListener);
        green.setOnSeekBarChangeListener(changeListener);
        blue.setOnSeekBarChangeListener(changeListener);

        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(HomeActivity.this, ANewHueFilterService.class);
                if (ANewHueFilterService.STATE == ANewHueFilterService.STATE_ACTIVE) {
                    stopService(i);
                } else {
                    startService(i);
                }
                refresh();
            }
        });
    }

    private void refresh() {
        if(mCountDownTimer != null)
            mCountDownTimer.cancel();

        mCountDownTimer = new CountDownTimer(100, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                mToggleButton.setChecked(ANewHueFilterService.STATE == ANewHueFilterService.STATE_ACTIVE);
            }
        };

        mCountDownTimer.start();
    }
}