package com.nag.mannertimer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProviderImpl extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	Intent intent = new Intent(context, TimerSelectorParentActivity.class);
    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    	RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
    	remoteViews.setOnClickPendingIntent(R.id.button, pendingIntent);
    	appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
//        remoteViews.setOnClickPendingIntent(R.id.button, clickButton(context));
//        pushWidgetUpdate(context, remoteViews);
    }

    public static PendingIntent clickButton(Context context) {
        Intent intent = new Intent();
        intent.setAction("UPDATE_WIDGET");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, WidgetProviderImpl.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}
