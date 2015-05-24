package com.nag.mannertimer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProviderImpl extends AppWidgetProvider {

	private static final String ACTION_PRESS_WIDGET = "PRESS_WIDGET";
	private static final String ACTION_UPDATE_WIDGET = "PRESS_WIDGET";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	Intent intent = new Intent(context, TimerSelectorParentActivity.class);
    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    	RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
    	remoteViews.setOnClickPendingIntent(R.id.textViewWidget, pendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.buttonWidget, pendingIntent);
		remoteViews.setTextViewText(R.id.textViewWidget,TimerSelector.getLabel(context));
    	appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

	public static void handleReceive(Context context, Intent intent){
		if (intent.getAction().equals(WidgetProviderImpl.ACTION_PRESS_WIDGET)) {
			context.startActivity(new Intent(context, TimerSelectorParentActivity.class));
		}else if (intent.getAction().equals(WidgetProviderImpl.ACTION_UPDATE_WIDGET)) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		}
	}

    private static PendingIntent pushWidget(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_PRESS_WIDGET);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

//	public static PendingIntent updateWidget(Context context) {
//		Intent intent = new Intent();
//		intent.setAction("UPDATE_WIDGET");
//		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//	}

//    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
//        ComponentName myWidget = new ComponentName(context, WidgetProviderImpl.class);
//        AppWidgetManager manager = AppWidgetManager.getInstance(context);
//        manager.updateAppWidget(myWidget, remoteViews);
//    }
}
