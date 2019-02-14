package com.robot.brainhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.robot.brainhelperlib.BrainHelper;

public class MainActivity extends AppCompatActivity {



    private BrainHelper brainHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        brainHelper = new BrainHelper(this);
        brainHelper.regist();

        brainHelper.setOnTtsListener(new BrainHelper.OnTtsListener() {
            @Override
            public void onStart() {
                Log.d("klog","tts_start");
            }

            @Override
            public void onStop() {
                Log.d("klog","tts_stop");

            }
        });
    }


    public void tts(View view){
        brainHelper.startTts("大脑助手");
    }
}
