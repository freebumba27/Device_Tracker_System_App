package com.google.devicetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * Created by Dream on 17-Jun-15.
 */
public class ReuseableClass {

    public static String baseUrl = "http://bumba27.byethost16.com/";
    public static int intervalTime = 10000;

    //===================================================================================================================================
    //Preference variable
    //===================================================================================================================================

    //--------------------------------------------
    // method to save variable in preference
    //--------------------------------------------
    public static void saveInPreference(String name, String content, Context myActivity) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(myActivity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name, content);
        editor.commit();
    }

    //--------------------------------------------
    // getting content from preferences
    //--------------------------------------------
    public static String getFromPreference(String variable_name, Context myActivity) {
        String preference_return;
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(myActivity);
        preference_return = preferences.getString(variable_name, "");

        return preference_return;
    }


    //===================================================================================================================================
    //check Mobile data and wifi
    //===================================================================================================================================
    public static boolean haveNetworkConnection(Context con) {
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    //====================================================================================================================================
    //checking Mobile data and wifi END
    //====================================================================================================================================

}
