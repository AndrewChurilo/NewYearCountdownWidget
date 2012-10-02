package by.chaba.widget.newyear;

/*
 * Copyright (C) 2012 The Android Open Source Project 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */


import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.text.DecimalFormat;


/**
 * This class is designed to perform background operations to update the contents of the widget.
 * 
 * @author Andrei Churila
 * 
 */
public class WidgetUpdateService extends Service{
    private Calendar mNextNewYear;  
    private RemoteViews mWidgetViews;   
    private AppWidgetManager mAppWidgetManager;
    private ComponentName mNewYearWidget;    
    
    private boolean mIsActive = false;
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // If service is already running
        if(mIsActive == true){
            if(intent.hasExtra(ThemesActivity.THEME_DATA_TAG)){
                // In intent placed id of theme
                Integer id = intent.getIntExtra(ThemesActivity.THEME_DATA_TAG, R.color.black_alfa);
                changeBackgraund(id);
            }
            return super.onStartCommand(intent, Service.START_NOT_STICKY, startId);
        }
        mIsActive = true;
        
        initializeClassVariables();     
        // Set onClickPendingIntent which opens ThemesActivity      
        setOnClick();       
        //Start CountDownTimer
        startCountDownTimer();
        return super.onStartCommand(intent, flags, startId);
    }
    
    private void initializeClassVariables(){
        mWidgetViews = new RemoteViews(this.getPackageName(), R.layout.widget);     
        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mNewYearWidget = new ComponentName(this, WidgetProvider.class);
        mNextNewYear = Calendar.getInstance();
        // Seting JANUARY 1 00:00:00 next years
        mNextNewYear.set(mNextNewYear.get(Calendar.YEAR) + 1, Calendar.JANUARY , 1, 0, 0, 00);
    }
    
    private void setOnClick(){
        Intent launchAppIntent = new Intent(this, ThemesActivity.class);
        PendingIntent launchAppPendingIntent = PendingIntent.getActivity(this, 0, launchAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mWidgetViews.setOnClickPendingIntent(R.id.Widget, launchAppPendingIntent);
    }
    
    private void startCountDownTimer(){
        new CountDownTimer(calculateRemainingTime(), 1000) {
            public void onTick(long millisUntilFinished) {
                mWidgetViews.setTextViewText(R.id.time,  formTimeFormat());
                mAppWidgetManager.updateAppWidget(mNewYearWidget, mWidgetViews);
            }
            public void onFinish() {
                // TODO: Think up scenario onset of the New Year
            }
        }.start();
    }

    /**
     * 
     * @param themeId
     *      Resource id you want to install to background.
     */
    private void changeBackgraund(Integer themeId){
        mWidgetViews.setImageViewResource(R.id.background, themeId);
        mAppWidgetManager.updateAppWidget(mNewYearWidget, mWidgetViews);
    }
    
    /**
     * 
     * @return
     *      The time remaining until the new year in milliseconds.
     */
    private long calculateRemainingTime(){
        return mNextNewYear.getTimeInMillis() - System.currentTimeMillis();
    }
    
    /**
     * 
     * @return
     *      Time remaining until the new year in string formaat.
     */     
    private String formTimeFormat(){            
        /**
         *  Before I used millisUntilFinished(parameter of onTick CountDownTimer class) 
         *  in this function. But using it, may lead to incorrect results. 
         *  If user changes time, widget can't change its state.
         */
        Calendar calendar = Calendar.getInstance();
        int mounth = Calendar.DECEMBER - calendar.get(Calendar.MONTH);
        int days = calendar.getMaximum(Calendar.DAY_OF_MONTH) - 
                calendar.get(Calendar.DAY_OF_MONTH) - 1;
        int hours = calendar.getMaximum(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.getMaximum(Calendar.MINUTE) - calendar.get(Calendar.MINUTE);
        int sec = calendar.getMaximum(Calendar.SECOND) - calendar.get(Calendar.SECOND);
        DecimalFormat timeFormat = new DecimalFormat("00");
        return Integer.toString(mounth) + "\t\t\t" + Integer.toString(days) + "\n" +
            timeFormat.format(hours) + ":" + timeFormat.format(min) + ":" + timeFormat.format(sec);     
    }
    
    @Override
    public void onDestroy() {
        mIsActive = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent theme) {
        // We don't provide binding, so return null
        return null;
    }
}
