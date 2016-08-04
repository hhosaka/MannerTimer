package com.nag.mannertimer;

import android.content.Context;
import android.media.AudioManager;

import com.nag.android.util.PreferenceHelper;

class AppPreference {
	private static final String PREF_REGISTERED_TIME = "registered_time";
	private static final String PREF_IS_IGNORE_SILENT="ignore_silent";
	private static final String PREF_MANNER_MODE="manner_mode";
	private static final String PREF_CONFIRM_ON_ALREADY_MANNER = "confirm_on_already_manner";
	private static final String PREF_HAS_CALL = "has_call";

	public static int loadMannerModeId(Context context){
		switch(AppPreference.loadMannerMode(context)){
			case AudioManager.RINGER_MODE_SILENT:
				return R.id.radioMannerModeSilent;
			case AudioManager.RINGER_MODE_VIBRATE:
				if(AppPreference.loadIsIgnoreSilent(context)) {
					return R.id.radioMannerModeVibrateNoSilent;
				}else{
					return R.id.radioMannerModeVibrate;
				}
		}
		throw new UnsupportedOperationException();
	}

	public static void saveMannerModeId(Context context, int mannerMode) {
		switch (mannerMode) {
			case R.id.radioMannerModeVibrate:
				saveMannerMode(context, AudioManager.RINGER_MODE_VIBRATE);
				saveIsIgnoreSilent(context, false);
				break;
			case R.id.radioMannerModeVibrateNoSilent:
				saveMannerMode(context, AudioManager.RINGER_MODE_VIBRATE);
				saveIsIgnoreSilent(context, true);
				if(Executor.getRingerMode(context)==AudioManager.RINGER_MODE_SILENT){
					Executor.setRingerMode(context, AudioManager.RINGER_MODE_VIBRATE);
				}
				break;
			case R.id.radioMannerModeSilent:
				saveMannerMode(context, AudioManager.RINGER_MODE_SILENT);
				saveIsIgnoreSilent(context, false);
				break;
			default:
				throw new UnsupportedOperationException();
		}
	}

	public static int loadMannerMode(Context context){
		return PreferenceHelper.getInstance(context).getInt(PREF_MANNER_MODE, AudioManager.RINGER_MODE_VIBRATE);
	}
	private static void saveMannerMode(Context context, int mode){
		PreferenceHelper.getInstance(context).putInt(PREF_MANNER_MODE, mode);
	}
	public static long loadRegisteredTime(Context context){
		return PreferenceHelper.getInstance(context).getLong(PREF_REGISTERED_TIME, 0L);
	}
	public static void saveRegisteredTime(Context context, long value){
		PreferenceHelper.getInstance(context).putLong(PREF_REGISTERED_TIME, value);
	}
	public static boolean loadIsIgnoreSilent(Context context){
		return PreferenceHelper.getInstance(context).getBoolean(PREF_IS_IGNORE_SILENT,false);
	}

	public static void saveIsIgnoreSilent(Context context, boolean value) {
		PreferenceHelper.getInstance(context).putBoolean(PREF_IS_IGNORE_SILENT, value);
	}

	public static boolean loadIsConfirmAlreadyOnManner(Context context){
		return PreferenceHelper.getInstance(context).getBoolean(PREF_CONFIRM_ON_ALREADY_MANNER, true);
	}

	public static void saveIsConfirmAlreadyOnManner(Context context, boolean value) {
		PreferenceHelper.getInstance(context).putBoolean(PREF_CONFIRM_ON_ALREADY_MANNER, value);
	}
	public static boolean loadHasCall(Context context){
		return PreferenceHelper.getInstance(context).getBoolean(PREF_HAS_CALL, false);
	}

	public static void saveHasCall(Context context, boolean value) {
		PreferenceHelper.getInstance(context).putBoolean(PREF_HAS_CALL, value);
	}

}
