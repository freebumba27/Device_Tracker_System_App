package com.google.devicetracker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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

import java.util.ArrayList;
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

        String name     = intent.getStringExtra("name");
        String emailId  = intent.getStringExtra("email_id");
        String mobileNo = intent.getStringExtra("mobile_no");

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

        new RegistrationTask().execute(deviceId, name, emailId, mobileNo, softwareVersion, DeviceName);

        return super.onStartCommand(intent, flags, startId);
    }

    private class RegistrationTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... values) {
            String responseBody = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ReuseableClass.baseUrl + "deviceTracker/register_device.php");
            try
            {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                nameValuePairs.add(new BasicNameValuePair("deviceId", values[0]));
                nameValuePairs.add(new BasicNameValuePair("name", values[1]));
                nameValuePairs.add(new BasicNameValuePair("emailId", values[2]));
                nameValuePairs.add(new BasicNameValuePair("mobileNo", values[3]));
                nameValuePairs.add(new BasicNameValuePair("softwareVersion", values[4]));
                nameValuePairs.add(new BasicNameValuePair("deviceName", values[5]));

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
