package com.google.devicetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
    }
}