package com.google.devicetracker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.utils.GCMConstants;

public class GCMIntentService extends GCMBaseIntentService {
	@Override
	protected void onRegistered(Context context, String regId) 
	{
		Log.i("Log", "onregistered: "+ regId);
		ReuseableClass.saveInPreference("gcm_id", regId, this);

		Intent intent = new Intent(GCMConstants.ACTION_ON_REGISTERED);
		intent.putExtra(GCMConstants.FIELD_REGISTRATION_ID, regId);
		context.sendBroadcast(intent);
	}

	@Override
	protected void onUnregistered(Context context, String regId) 
	{
		Log.i("Log", "onUnregistered: "+ regId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) 
	{
		String message = intent.getExtras().getString("message");

		//Toast.makeText(context, "Received message:- " + message, Toast.LENGTH_LONG).show();
		Log.i(TAG, "Received message:- " + message);

		if(message != null && message.equalsIgnoreCase("GET DEVICE INFO NOW"))
		{
			ReuseableClass.saveInPreference("name", "nothing", this);
			ReuseableClass.saveInPreference("email_id", "nothing", this);
			ReuseableClass.saveInPreference("mobile_no", "nothing", this);
			ReuseableClass.saveInPreference("From", "nothing", this);

			Intent i = new Intent(this, RegistrationService.class);
			startService(i);
		}
	}

	@Override
	protected void onError(Context context, String errorId) 
	{
		//Toast.makeText(context, errorId, Toast.LENGTH_LONG).show();
	}
}