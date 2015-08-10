package com.google.devicetracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by anirban on 03-08-2015.
 */
public class BootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(ReuseableClass.getFromPreference("registration_screen_opened", context).equalsIgnoreCase("") ||
                ReuseableClass.getFromPreference("registration_screen_opened", context).equalsIgnoreCase("NO"))
        {
            ReuseableClass.saveInPreference("registration_screen_opened", "YES", context);
            Intent myIntent = new Intent(context, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
        else if(ReuseableClass.getFromPreference("registration_screen_opened", context).equalsIgnoreCase("YES"))
        {
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 777, alarmIntent, 0);

            AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            int interval = ReuseableClass.intervalTime;

            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            Log.d("TAG", "Alarm set !!");
        }
    }
}