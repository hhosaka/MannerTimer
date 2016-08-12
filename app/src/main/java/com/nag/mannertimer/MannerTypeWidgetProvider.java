package com.nag.mannertimer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

public class MannerTypeWidgetProvider extends AppWidgetProvider {
	private static final String ACTION_INCREASE_MANNER_MODE = "INCREASE_MANNER_MODE";

	public static class WidgetIntentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			handleReceive(context, intent);
		}
	}

	private PendingIntent onClickWidget(Context context){
		Intent intent = new Intent();
		intent.setAction(ACTION_INCREASE_MANNER_MODE);
		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		PendingIntent pi = onClickWidget(context);
    	RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.manner_type_widget);
    	remoteViews.setOnClickPendingIntent(R.id.textViewWidget, pi);
		remoteViews.setOnClickPendingIntent(R.id.buttonWidget, pi);
		String label = "";
		switch(AppPreference.loadMannerModeId(context)){
			case R.id.radioMannerModeVibrate:
				label = "R.id.radioMannerModeVibrate";
				break;
			case R.id.radioMannerModeVibrateNoSilent:
				label = "radioMannerModeVibrateNoSilent";
				break;
			case R.id.radioMannerModeSilent:
				label = "radioMannerModeSilent";
				break;
			default:
				throw new UnsupportedOperationException();
		}
		remoteViews.setTextViewText(R.id.textViewWidget, label);
    	appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

//	private PendingIntent buildShowActivityIntent(Context context){
//		return PendingIntent.getActivity(context, 0, new Intent(context, TimerSelectorParentActivity.class), 0);
//	}
	public static void handleReceive(Context context, Intent intent){
		if (intent.getAction().equals(ACTION_INCREASE_MANNER_MODE)) {
			switch(AppPreference.loadMannerModeId(context)){
				case R.id.radioMannerModeVibrate:
					AppPreference.saveMannerModeId(context, R.id.radioMannerModeVibrateNoSilent);
					break;
				case R.id.radioMannerModeVibrateNoSilent:
					AppPreference.saveMannerModeId(context, R.id.radioMannerModeSilent);
					break;
				case R.id.radioMannerModeSilent:
					AppPreference.saveMannerModeId(context, R.id.radioMannerModeVibrate);
					break;
				default:
					throw new UnsupportedOperationException();
			}
		}
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.manner_type_widget);
//			remoteViews.setTextViewText(R.id.textViewWidget, TimerSelector.getShortLabel(context));
			pushWidgetUpdate(context, remoteViews);
//		}else if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
//			Executor.update(context, intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1));
//		}
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context, MannerTypeWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}

//	public static PendingIntent buildUpdateWidget(Context context) {
//		Intent intent = new Intent();
//		intent.setAction("UPDATE_WIDGET");
//		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//	}

//	public static void updateWidget(Context context){
//		Intent intent = new Intent();
//		intent.setAction(ACTION_UPDATE_WIDGET);
//		context.sendBroadcast(intent);
//	}
}
