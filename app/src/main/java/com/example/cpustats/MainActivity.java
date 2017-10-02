package com.example.cpustats;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    Switch mToggleSwitch;
    boolean check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToggleSwitch = (Switch) findViewById(R.id.toggle_switch);
        check=false;
        mToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b && !check){
                    startService(new Intent(MainActivity.this, FloatingFaceBubbleService.class));

                    check = true;
                }
                else{
                    stopService(new Intent(MainActivity.this, FloatingFaceBubbleService.class));
                    check = false;
                }
            }
        });
    }
}
