package com.nag.mannertimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class Executor{
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

	public static void start(Context context, int mode, long time){
		AppPreference.saveRegisteredTime(context, time);
		setRingerMode(context, mode);
		getManager(context).set( AlarmManager.RTC_WAKEUP, time, getIntent(context));
	}

	public static void stop(Context context){
		AppPreference.saveRegisteredTime(context, 0L);
		if(getRingerMode(context)!=AudioManager.RINGER_MODE_NORMAL) {
			setRingerMode(context, AudioManager.RINGER_MODE_NORMAL);
		}
		getManager(context).cancel(getIntent(context));
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

}
