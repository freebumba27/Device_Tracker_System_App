package com.google.devicetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Dream on 17-Jun-15.
 */
public class ReuseableClass {

    public static String baseUrl = "http://bumba27.byethost16.com/";
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
    //Preference variable
    //===================================================================================================================================

}
