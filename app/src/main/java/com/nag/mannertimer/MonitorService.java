package com.nag.mannertimer;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.nag.android.util.PreferenceHelper;

public class MonitorService extends Service {
	private static final String PREF_NOTIFY_ON_RECEIVE_CALL = "notify_on_receive_call";
	private int id = 0;

	public static boolean isNotifyOnReceiveCall(Context context){
		return PreferenceHelper.getInstance(context).getBoolean(PREF_NOTIFY_ON_RECEIVE_CALL, false);
	}

	public static void setNotifyOnReceiveCall(Context context, boolean flag){
		PreferenceHelper.getInstance(context).putBoolean(PREF_NOTIFY_ON_RECEIVE_CALL, flag);
	}

	public static void start(Context context){
		context.startService(new Intent(context, MonitorService.class));
	}

	public static void stop(Context context) {
		context.stopService(new Intent(context, MonitorService.class));
	}

	private void notification(String number){

		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
		builder.setSmallIcon(R.drawable.ic_launcher);

		builder.setContentTitle("MannerTimer"); // 1行目
		builder.setContentText("Receive call : " + number); // 2行目
		builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)), 0));

		NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
		manager.notify(++id, builder.build());
		AppPreference.saveHasCall(getApplicationContext(), true);
	}
	private void initReceivePhone() {
		((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).listen
				(new PhoneStateListener() {
					@Override
					public void onCallStateChanged(int state, String number) {
						switch (state) {
							case TelephonyManager.CALL_STATE_RINGING:
								if(isNotifyOnReceiveCall(getApplicationContext()) && Executor.getRingerMode(getApplicationContext()) != AudioManager.RINGER_MODE_NORMAL) {
									notification(number);
								}
								break;
						}
					}
				}
						, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initReceivePhone();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
