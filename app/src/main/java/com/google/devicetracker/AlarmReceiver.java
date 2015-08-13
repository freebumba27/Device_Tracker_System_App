package com.google.devicetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

        ReuseableClass.saveInPreference("name", "nothing", context);
        ReuseableClass.saveInPreference("email_id", "nothing", context);
        ReuseableClass.saveInPreference("mobile_no", "nothing", context);
        ReuseableClass.saveInPreference("From", "AlarmReceiver", context);

        Intent i = new Intent(context, RegistrationService.class);
        context.startService(i);
    }
}
