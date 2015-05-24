package com.nag.mannertimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WidgetIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
		WidgetProviderImpl.handleReceive(context, intent);
    }
    
//	private void showDialog(Context context){
//		context.startActivity(new Intent(context, MainActivity.class));
//	}
}
