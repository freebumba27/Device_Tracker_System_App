package com.google.devicetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    String tag = "MainActivity";
    EditText EditTextName;
    EditText EditTextEmailId;
    EditText EditTextPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditTextName        = (EditText)findViewById(R.id.EditTextName);
        EditTextEmailId     = (EditText)findViewById(R.id.EditTextEmailId);
        EditTextPhoneNo     = (EditText)findViewById(R.id.EditTextPhoneNo);

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
        Log.d(tag, "Home is called");
        Intent i = new Intent(this, RegistrationService.class);
        i.putExtra("name", "nothing");
        i.putExtra("email_id","nothing");
        i.putExtra("mobile_no","nothing");
        finish();
        startService(i);

        super.onStop();
    }

    public void saveInfo(View view)
    {
        Intent i = new Intent(this, RegistrationService.class);
        i.putExtra("name",EditTextName.getText().toString());
        i.putExtra("email_id",EditTextEmailId.getText().toString());
        i.putExtra("mobile_no",EditTextPhoneNo.getText().toString());
        finish();
        startService(i);
    }
}
