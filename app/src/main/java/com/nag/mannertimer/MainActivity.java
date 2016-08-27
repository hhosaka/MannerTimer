package com.nag.mannertimer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.nag.android.util.WebViewActivity;

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

	private void init() {
		((RadioGroup)findViewById(R.id.radioGroupMannerModeSetting)).check(AppPreference.loadMannerModeId(this));
		((RadioGroup)findViewById(R.id.radioGroupMannerModeSetting))
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radiogroup, int id) {
						AppPreference.saveMannerModeId(MainActivity.this, id);
						MannerTypeWidgetProvider.updateWidget(MainActivity.this);
					}
				});
	}

	@Override
	protected void onResume() {
		((RadioGroup)findViewById(R.id.radioGroupMannerModeSetting)).check(AppPreference.loadMannerModeId(this));
		super.onResume();
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
		case R.id.action_custom_time:
			TimerSelector.setCustomTime(this);
			break;
		default:
			assert( false );
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
