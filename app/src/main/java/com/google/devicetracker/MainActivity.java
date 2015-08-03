package com.google.devicetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    String tag = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Disable application icon
        //PackageManager pm = getApplicationContext().getPackageManager();
        //pm.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(tag, "Home is called");
        Intent i = new Intent(this, RegistrationService.class);
        finish();
        startService(i);
    }
}
