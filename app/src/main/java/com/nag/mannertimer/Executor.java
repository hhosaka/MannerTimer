package com.nag.mannertimer;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class Executor extends BroadcastReceiver {
	private static PendingIntent getIntent(Context context){
		return PendingIntent.getBroadcast(context.getApplicationContext(), 0, new Intent(context, Executor.class), 0);
	}
	private static AlarmManager getManager(Context context){
		return (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	}

	private static AudioManager getAudioManager(Context context){
		return (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
	}

	public static int getRingerMode(Context context){
		return getAudioManager(context).getRingerMode();
	}

	public static void setRingerMode(Context context, int mode){
		getAudioManager(context).setRingerMode(mode);
	}

	public static void initialize(Context context){
		if(AppPreference.loadRegisteredTime(context)>System.currentTimeMillis()) {
			Executor.start(context, AppPreference.loadMannerMode(context), AppPreference.loadRegisteredTime(context));
		}else{
			Executor.stop(context);
		}
		MonitorService.start(context);
	}

	public static void start(Context context, int mode, long time) {
		AppPreference.saveRegisteredTime(context, time);
		setRingerMode(context, mode);
		getManager(context).set(AlarmManager.RTC_WAKEUP, time, getIntent(context));
		showNotify(context, TimerSelector.getLabel(context));
		WidgetProviderImpl.updateWidget(context);
	}

	private static void showNotify(Context context, String subject){
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pi = PendingIntent.getActivity(context,0, new Intent(context, MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT );
		Notification notification = new Notification.Builder(context)
				.setContentTitle(context.getString(R.string.app_name))
				.setContentText(subject)
				.setSmallIcon(R.drawable.ic_launcher)
				.setOngoing(true)
				.setContentIntent(pi)
				.getNotification();
		manager.notify(R.string.app_name, notification);
	}

	private static void cancelNotify(Context context) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(R.string.app_name);
	}

	public static void stop(Context context){
		if(AppPreference.loadRegisteredTime(context)!=0 && AppPreference.loadHasCall(context)){
			DoRingActivity.invoke(context);
		}
		AppPreference.saveHasCall(context, false);
		AppPreference.saveRegisteredTime(context, 0L);
		if(getRingerMode(context)!=AudioManager.RINGER_MODE_NORMAL) {
			setRingerMode(context, AudioManager.RINGER_MODE_NORMAL);
		}
		getManager(context).cancel(getIntent(context));
		cancelNotify(context);
		WidgetProviderImpl.updateWidget(context);
	}

	public static void update(Context context, int mode){
		switch(mode){
			case AudioManager.RINGER_MODE_NORMAL:
				stop(context);
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				break;
			case AudioManager.RINGER_MODE_SILENT:
				if(AppPreference.loadIsIgnoreSilent(context)){
					setRingerMode(context, AudioManager.RINGER_MODE_VIBRATE);
				}
				break;
			default:
				throw new UnsupportedOperationException();
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		stop(context);
	}
}
