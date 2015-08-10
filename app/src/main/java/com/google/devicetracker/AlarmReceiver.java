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
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
