package com.nag.mannertimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	private static final String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if(AppPreference.loadRegisteredTime(context)>System.currentTimeMillis()) {
			Executor.start(context, AppPreference.loadMannerMode(context), AppPreference.loadRegisteredTime(context));
		}else{
			Executor.stop(context);
		}
	}
}
