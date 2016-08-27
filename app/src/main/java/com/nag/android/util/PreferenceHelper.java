package com.nag.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {
	private static PreferenceHelper instance;
//	private final SharedPreferences pref;

	public static PreferenceHelper getInstance(Context context){//Due to design change
		if(instance==null){
			instance=new PreferenceHelper();
		}
		return instance;
	}

	private SharedPreferences pref(Context context){return PreferenceManager.getDefaultSharedPreferences(context);}

	public boolean getBoolean(Context context, String key, boolean defValue){
		return pref(context).getBoolean(key, defValue);
	}

	public int getInt(Context context, String key, int defValue){
		return pref(context).getInt(key, defValue);
	}
	public long getLong(Context context, String key, long defValue){
		return pref(context).getLong(key, defValue);
	}

	public double getDouble(Context context, String key, double defValue){
		return Double.valueOf(pref(context).getString(key, String.valueOf(defValue)));
	}

	public String getString(Context context, String key, String defValue){
		return pref(context).getString(key, defValue);
	}

	public void putBoolean(Context context, String key, boolean value){
		SharedPreferences.Editor edit=pref(context).edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public void putInt(Context context, String key, int value){
		SharedPreferences.Editor edit=pref(context).edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public void putLong(Context context, String key, long value){
		SharedPreferences.Editor edit=pref(context).edit();
		edit.putLong(key, value);
		edit.commit();
	}

	public void putString(Context context, String key, String value){
		SharedPreferences.Editor edit=pref(context).edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	public void putDouble(Context context, String key, double value){
		SharedPreferences.Editor edit=pref(context).edit();
		edit.putString(key, String.valueOf(value));
		edit.commit();
	}
	
	public void remove(Context context, String key){
		SharedPreferences.Editor edit=pref(context).edit();
		edit.remove(key);
		edit.commit();
	}
}
