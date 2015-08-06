package com.google.devicetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        ReuseableClass.saveInPreference("name", "nothing", MainActivity.this);
        ReuseableClass.saveInPreference("email_id", "nothing", MainActivity.this);
        ReuseableClass.saveInPreference("mobile_no", "nothing", MainActivity.this);

        Intent i = new Intent(this, RegistrationService.class);
        finish();
        startService(i);

        super.onStop();
    }

    public void saveInfo(View view)
    {
        if(EditTextName.getText().toString().trim().length()>0 && EditTextEmailId.getText().toString().trim().length()>0
                && EditTextPhoneNo.getText().toString().trim().length()>0)
        {
            ReuseableClass.saveInPreference("name", EditTextName.getText().toString(), MainActivity.this);
            ReuseableClass.saveInPreference("email_id", EditTextEmailId.getText().toString(), MainActivity.this);
            ReuseableClass.saveInPreference("mobile_no", EditTextPhoneNo.getText().toString(), MainActivity.this);

            Intent i = new Intent(this, RegistrationService.class);
            finish();
            startService(i);
        }
        else
        {
            Toast.makeText(this, "All fields are mandatory !!",Toast.LENGTH_LONG).show();;
        }
    }

}
