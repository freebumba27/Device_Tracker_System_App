package com.google.devicetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
    boolean fromSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditTextName        = (EditText)findViewById(R.id.EditTextName);
        EditTextEmailId     = (EditText)findViewById(R.id.EditTextEmailId);
        EditTextPhoneNo     = (EditText)findViewById(R.id.EditTextPhoneNo);

        ReuseableClass.saveInPreference("registration_screen_opened", "YES", this);

        LocationManager lm = (LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            fromSettings = false;
        }
    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }

    @Override
    protected void onStop() {
        if(!fromSettings) {
            Log.d(tag, "Home is called");

            ReuseableClass.saveInPreference("name", "nothing", MainActivity.this);
            ReuseableClass.saveInPreference("email_id", "nothing", MainActivity.this);
            ReuseableClass.saveInPreference("mobile_no", "nothing", MainActivity.this);
            ReuseableClass.saveInPreference("From", "nothing", MainActivity.this);

            Intent i = new Intent(this, RegistrationService.class);
            finish();
            startService(i);
        }

        super.onStop();
    }

    public void saveInfo(View view)
    {
        LocationManager lm = (LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}


        if(!gps_enabled || !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(this.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    fromSettings = true;
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MainActivity.this.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(this.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                }
            });
            dialog.show();
        }
        else{

            if (ReuseableClass.haveNetworkConnection(this)) {
                if(EditTextName.getText().toString().trim().length()>0 && EditTextEmailId.getText().toString().trim().length()>0
                        && EditTextPhoneNo.getText().toString().trim().length()>0)
                {
                    ReuseableClass.saveInPreference("name", EditTextName.getText().toString(), MainActivity.this);
                    ReuseableClass.saveInPreference("email_id", EditTextEmailId.getText().toString(), MainActivity.this);
                    ReuseableClass.saveInPreference("mobile_no", EditTextPhoneNo.getText().toString(), MainActivity.this);
                    ReuseableClass.saveInPreference("From", "nothing", MainActivity.this);

                    Intent i = new Intent(this, RegistrationService.class);
                    finish();
                    startService(i);
                }
                else
                {
                    Toast.makeText(this, "All fields are mandatory !!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please check your internet connection !!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
