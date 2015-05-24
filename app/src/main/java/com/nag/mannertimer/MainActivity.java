package com.nag.mannertimer;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

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
		init();
		((Button)findViewById(R.id.buttonSelect)).setOnClickListener(new TimerSelector(this));

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
			WebViewActivity.showByURL(this, "http://www.yahoo.co.jp/");
//			WebViewActivity.showByURL(this, "https://sites.google.com/site/bsmatchmakerhelpjp/");
			break;
		default:
			assert( false );
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
