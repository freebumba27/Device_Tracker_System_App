package com.google.devicetracker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

public class RegistrationService extends Service {

    String tag = "RegistrationService";

    public RegistrationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(tag, "Start Registration from the service");


        //--------------------------
        // Get gmail ids
        //--------------------------
        try
        {
            Account[] accounts = AccountManager.get(this).getAccounts();
            for (Account account : accounts)
            {
                if(account.name.endsWith("@gmail.com") ||
                        account.name.endsWith("@yahoo.com")||
                        account.name.endsWith("@hotmail.com"))
                {
                    Log.i(tag, "Email id: " + account.name);
                }
            }
        }
        catch (Exception e)
        {
            Log.i("Exception", "Exception:" + e);
        }
        //--------------------------
        // Get gmail ids
        //--------------------------

        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String number = tm.getLine1Number();
        Log.i(tag, "Mobile No: " + number);

        //--------------------------
        // Get Mobile Nos
        //--------------------------


        return super.onStartCommand(intent, flags, startId);
    }
}
