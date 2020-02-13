package com.example.eggtimerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.BatchUpdateException;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    SeekBar seekBar;
    Button button;
    int max = 3600;
    int startPosition = 30;
    CountDownTimer countDownTimer;
    boolean isCountDownActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar5);
        seekBar.setMax(max);
        seekBar.setProgress(startPosition);
        displayTimer(startPosition);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("INFO", Integer.toString(progress));
                displayTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * displayTimer method is created to display the timer in MM:SS format
     * progress - current value of seekBar position (in seconds)
     * minute and seconds values are derived from progress by performing division and modulo by 60 respectively **/
    public void displayTimer(int progress){
        final int minute = (progress / 60);
        final int seconds = (progress % 60);

        String secondString = Integer.toString(seconds);
        if(seconds <= 9)
        {
            secondString = "0" + secondString;
        }
        textView = findViewById(R.id.textView4);

        //textView.setText(Integer.toString(minute) + ":" + Integer.toString(seconds));
        textView.setText(Integer.toString(minute) + ":" + secondString);
    }

    /**
     * clickStart is the onClick function
     * the countdown only begins when the START button is clicked
     * initial value of boolean isCountDownActive is set to False
     * when start button is clicked the boolean is set to True and the countdown begins
     * when stop button is clicked the boolean is set to False and resetTimer function is called**/
    public void clickStart(View view){
        button = findViewById(R.id.button4);

        if(isCountDownActive){
            resetTimer();
        }
        else{
            isCountDownActive = true;
            button.setText("STOP");
            seekBar.setEnabled(false); //when countdown starts the seekBar is grayed out

            /**
             * Setting millisInFuture value to the value of seekBar position because that's the exact time when countdown should begin
             * seekBar progress is multiplied by 1000 to convert into milliseconds
             * displayTimer method is called inside the onTick function, where the progress value is set to millisUntilFinsihed
             * by dividing by 1000 we convert milliseconds to seconds
             * when onTick function runs the countdown is displayed on screen
             * once the timer ends the onFinish function is called and the sound plays. Also all the values are set to default position **/
            countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000, 1000){
                public void onTick(long millisUntilFinished){
                    displayTimer((int) millisUntilFinished/1000);
                }
                public void onFinish(){
                    final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.rooster);
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Time's Up!", Toast.LENGTH_SHORT).show();
                    resetTimer();
                }
            }.start();
        }
    }

    /**
     * resetTimer function is called when timer ends or when the STOP button is clicked
     * this function sets the timer to default value (which in this case is 30 seconds), the seekBar is enabled
     * and the boolean isCountDownActive is set to False**/
    public void resetTimer(){
        button.setText("START");
        displayTimer(startPosition);
        seekBar.setEnabled(true);
        seekBar.setProgress(startPosition);
        countDownTimer.cancel();
        isCountDownActive = false;
    }
}