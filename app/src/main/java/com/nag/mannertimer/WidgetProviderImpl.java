package com.nag.mannertimer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Button;
import android.widget.RemoteViews;

public class WidgetProviderImpl extends AppWidgetProvider {
	private static final String ACTION_UPDATE_WIDGET = "UPDATE_WIDGET";
	public static class WidgetIntentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			handleReceive(context, intent);
		}
	}

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		PendingIntent pi = buildShowActivityIntent(context);
    	RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
    	remoteViews.setOnClickPendingIntent(R.id.textViewWidget, pi);
		remoteViews.setOnClickPendingIntent(R.id.buttonWidget, pi);
		remoteViews.setTextViewText(R.id.textViewWidget,TimerSelector.getShortLabel(context));
    	appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

	private PendingIntent buildShowActivityIntent(Context context){
		return PendingIntent.getActivity(context, 0, new Intent(context, TimerSelectorParentActivity.class), 0);
	}
	public static void handleReceive(Context context, Intent intent){
		if (intent.getAction().equals(ACTION_UPDATE_WIDGET)) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
			remoteViews.setTextViewText(R.id.textViewWidget, TimerSelector.getShortLabel(context));
			pushWidgetUpdate(context, remoteViews);
		}else if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
			Executor.update(context, intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1));
		}

	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context, WidgetProviderImpl.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}

//	public static PendingIntent buildUpdateWidget(Context context) {
//		Intent intent = new Intent();
//		intent.setAction("UPDATE_WIDGET");
//		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//	}

	public static void updateWidget(Context context){
		Intent intent = new Intent();
		intent.setAction(ACTION_UPDATE_WIDGET);
		context.sendBroadcast(intent);
	}

}
