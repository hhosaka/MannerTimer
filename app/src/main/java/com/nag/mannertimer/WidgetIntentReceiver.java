package com.nag.mannertimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetIntentReceiver extends BroadcastReceiver {
    public static int clickCount = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("UPDATE_WIDGET")) {
        	context.startActivity(new Intent(context, TimerSelectorParentActivity.class));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            remoteViews.setOnClickPendingIntent(R.id.button, WidgetProviderImpl.clickButton(context));

            WidgetProviderImpl.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
        }
    }
    
	private void showDialog(Context context){
		context.startActivity(new Intent(context, MainActivity.class));
	}
}
