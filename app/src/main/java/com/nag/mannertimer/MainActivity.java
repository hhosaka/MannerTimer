package com.nag.mannertimer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.nag.android.util.PreferenceHelper;
import com.nag.android.util.WebViewActivity;
import com.nag.mannertimer.R;

public class MainActivity extends Activity {

	class StatusMonitor extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
				Executor.update(context, intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1));
				((Button)findViewById(R.id.buttonSelect)).setText(TimerSelector.getLabel(context));
			}
		}
	}
	BroadcastReceiver status_monitor =null;

//	private void resetButton(){
//		((TimerSelector)findViewById(R.id.buttonSelect)).setLabel();
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Executor.initialize(this);
		init();
		((Button)findViewById(R.id.buttonSelect)).setOnClickListener(new TimerSelector(this));
		{
			CheckBox cb = (CheckBox) findViewById(R.id.checkBoxNotification);
			cb.setChecked(MonitorService.isNotifyOnReceiveCall(this));
			cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					MonitorService.setNotifyOnReceiveCall(MainActivity.this, isChecked);
					buttonView.setChecked(isChecked);
				}
			});
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
		registerReceiver(status_monitor =new StatusMonitor(),filter);
	}


	private Context getContext(){
		return this;
	}

	private int getMannerModeId(){
		switch(AppPreference.loadMannerMode(this)){
			case AudioManager.RINGER_MODE_SILENT:
				return R.id.radioMannerModeSilent;
			case AudioManager.RINGER_MODE_VIBRATE:
				if(AppPreference.loadIsIgnoreSilent(this)) {
					return R.id.radioMannerModeVibrateNoSilent;
				}else{
					return R.id.radioMannerModeVibrate;
				}
		}
		throw new UnsupportedOperationException();
	}

	private void init() {
		((RadioGroup)findViewById(R.id.radioGroupMannerModeSetting)).check(getMannerModeId());
		((RadioGroup)findViewById(R.id.radioGroupMannerModeSetting))
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radiogroup, int id) {
						switch (id) {
							case R.id.radioMannerModeVibrate:
								AppPreference.saveMannerMode(getContext(), AudioManager.RINGER_MODE_VIBRATE);
								AppPreference.saveIsIgnoreSilent(getContext(), false);
								break;
							case R.id.radioMannerModeVibrateNoSilent:
								AppPreference.saveMannerMode(getContext(), AudioManager.RINGER_MODE_VIBRATE);
								AppPreference.saveIsIgnoreSilent(getContext(), true);
								if(Executor.getRingerMode(MainActivity.this)==AudioManager.RINGER_MODE_SILENT){
									Executor.setRingerMode(MainActivity.this, AudioManager.RINGER_MODE_VIBRATE);
								}
								break;
							case R.id.radioMannerModeSilent:
								AppPreference.saveMannerMode(getContext(), AudioManager.RINGER_MODE_SILENT);
								AppPreference.saveIsIgnoreSilent(getContext(), false);
								break;
							default:
								assert (false);
								break;
						}
					}
				});
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(status_monitor !=null){
			unregisterReceiver(status_monitor);
			status_monitor =null;
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() )
		{
		case R.id.action_help:
			WebViewActivity.showByURL(this, getString(R.string.url_help));
//			WebViewActivity.showByURL(this, "https://sites.google.com/site/bsmatchmakerhelpjp/");
			break;
		default:
			assert( false );
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
