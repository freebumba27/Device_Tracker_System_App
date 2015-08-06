package com.google.devicetracker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        startService(new Intent(RegistrationService.this, CurrentLocationService.class));

        String name = ReuseableClass.getFromPreference("name", this);
        String emailId = ReuseableClass.getFromPreference("email_id", this);
        String mobileNo = ReuseableClass.getFromPreference("mobile_no", this);

        Log.d("TAG", "name: " + name);
        Log.d("TAG", "emailId: " + emailId);
        Log.d("TAG", "mobileNo: " + mobileNo);

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
                    emailId += "," + account.name;
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


        //--------------------------
        // Get Mobile Nos
        //--------------------------

        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String number = tm.getLine1Number();
        Log.i(tag, "Mobile No: " + number);
        mobileNo += number;

        //--------------------------
        // Get Mobile Nos
        //--------------------------


        //--------------------------
        // Get unique device id
        //--------------------------

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId         = telephonyManager.getDeviceId();
        String softwareVersion  = Build.VERSION.RELEASE;
        String DeviceName       = getDeviceName();

        Log.i(tag, "Device Id: " + deviceId);
        Log.i(tag, "Software Version: " + softwareVersion);
        Log.i(tag, "Device Name: " + DeviceName);

        //--------------------------
        // Get unique device id
        //--------------------------

        registeringUser(deviceId, name, emailId, mobileNo, softwareVersion, DeviceName);

        return super.onStartCommand(intent, flags, startId);
    }

    private void registeringUser(final String deviceId, final String name, final String emailId, final String mobileNo, final String softwareVersion, final String DeviceName)
    {
        Double lat = 0.0;
        Double lng = 0.0;
        if (!ReuseableClass.getFromPreference("nlat", RegistrationService.this).equalsIgnoreCase("")) {

            if (ReuseableClass.getFromPreference("glat", RegistrationService.this).equalsIgnoreCase("")) {
                lat = Double.parseDouble(ReuseableClass.getFromPreference("nlat", RegistrationService.this));
                lng = Double.parseDouble(ReuseableClass.getFromPreference("nlng", RegistrationService.this));
            } else {
                lat = Double.parseDouble(ReuseableClass.getFromPreference("glat", RegistrationService.this));
                lng = Double.parseDouble(ReuseableClass.getFromPreference("glng", RegistrationService.this));
            }

            if (lat != 0.0 && lng != 0.0) {
                if (ReuseableClass.haveNetworkConnection(this)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd.MM.yyyy");
                    String currentDatedTime = sdf.format(new Date());

                    new RegistrationTask().execute(deviceId, name, emailId, mobileNo, softwareVersion,
                            DeviceName, currentDatedTime, lat.toString(), lng.toString());
                }
            }
        } else {
            Log.d("TAG", "gLat, GLng is blank !!");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    registeringUser(deviceId, name, emailId, mobileNo, softwareVersion, DeviceName);
                }
            }, 1000);

        }
    }

    private class RegistrationTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... values) {
            String responseBody = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ReuseableClass.baseUrl + "deviceTracker/register_device.php");
            try
            {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(9);
                nameValuePairs.add(new BasicNameValuePair("deviceId", values[0]));
                nameValuePairs.add(new BasicNameValuePair("name", values[1]));
                nameValuePairs.add(new BasicNameValuePair("emailId", values[2]));
                nameValuePairs.add(new BasicNameValuePair("mobileNo", values[3]));
                nameValuePairs.add(new BasicNameValuePair("softwareVersion", values[4]));
                nameValuePairs.add(new BasicNameValuePair("deviceName", values[5]));
                nameValuePairs.add(new BasicNameValuePair("currentDatedTime", values[6]));
                nameValuePairs.add(new BasicNameValuePair("lat", values[7]));
                nameValuePairs.add(new BasicNameValuePair("lng", values[8]));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                int responseCode = response.getStatusLine().getStatusCode();
                if (responseCode == 200)
                {
                    responseBody = EntityUtils.toString(response.getEntity());
                    Log.d("TAG", "value: " + responseBody);
                }
            } catch (Exception t) {
                Log.e("TAG", "Error: " + t);
            }
            return responseBody;
        }

        protected void onPostExecute(String result)
        {
            Log.d("TAG", "value: " + result);
            stopSelf();
            stopService(new Intent(RegistrationService.this, CurrentLocationService.class));
        }
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
