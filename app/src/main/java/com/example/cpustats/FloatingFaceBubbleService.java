package com.example.cpustats;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class FloatingFaceBubbleService extends Service {
    private WindowManager windowManager;
    private ImageView floatingFaceBubble;
    private Random random;
    private int UUID;
    private TextView floatingTextView;
    private Handler handler = new Handler();
    private final String STATS = "MY STATS";
    public void onCreate() {
        super.onCreate();
        random = new Random();
        UUID = random.nextInt(1000);
        Log.d("Floating","UUID="+UUID);
        floatingFaceBubble = new ImageView(this);
        floatingTextView = new TextView(this);
        //a face floating bubble as imageView
        floatingFaceBubble.setImageResource(R.mipmap.ic_launcher_round);

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        floatingTextView.setTextColor(Color.WHITE);
        floatingTextView.setText(STATS);
        //here is all the science of params
        final LayoutParams myParams = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_PHONE,
                LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x=0;
        myParams.y=100;
        // add a floatingfacebubble icon in window
        //windowManager.addView(floatingFaceBubble, myParams);
        windowManager.addView(floatingTextView, myParams);
        try{
            //for moving the picture on touch and slide
            //floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {                  //CHANGES HERE
            floatingTextView.setOnTouchListener(new View.OnTouchListener(){
                WindowManager.LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //remove face bubble on long press

                    if(System.currentTimeMillis()-touchStartTime>ViewConfiguration.getLongPressTimeout() && initialTouchX== event.getRawX()){
                        //windowManager.removeView(floatingFaceBubble);
                        windowManager.removeView(floatingTextView);
                       // Log.d("Floating","Inside hold down listener");
                        stopSelf();
                        return false;
                    }
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            touchStartTime = System.currentTimeMillis();
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            Log.d("onTouch","initialTouchX = "+initialTouchX);
                            Log.d("onTouch","event.getX() = "+event.getX());
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, myParams);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("Floating", "Inside onDestroy of Floating class");
        Log.d("Floating","OnDestroy UUID="+UUID);
        windowManager.removeView(floatingTextView);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}