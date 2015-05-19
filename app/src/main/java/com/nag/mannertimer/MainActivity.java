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

	private void init() {
		setIsIgnoreSilent(AppPreference.loadIsIgnoreSilent(this));
		setMannerMode(AppPreference.loadMannerMode(this));
		((CheckBox)findViewById(R.id.checkBoxIgnoreSilentMode))
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked && AppPreference.loadMannerMode(getContext()) == AudioManager.RINGER_MODE_SILENT) {
							setMannerMode(AudioManager.RINGER_MODE_VIBRATE);
							AppPreference.saveMannerMode(getContext(), AudioManager.RINGER_MODE_VIBRATE);
						}
						AppPreference.saveIsIgnoreSilent(getContext(), isChecked);
					}
				});
		((RadioGroup)findViewById(R.id.radioGroupMannerModeSetting))
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radiogroup, int id) {
						switch (id) {
							case R.id.radioMannerModeVibrate:
								AppPreference.saveMannerMode(getContext(), AudioManager.RINGER_MODE_VIBRATE);
								break;
							case R.id.radioMannerModeSilent:
								AppPreference.saveMannerMode(getContext(), AudioManager.RINGER_MODE_SILENT);
								if (AppPreference.loadIsIgnoreSilent(getContext())) {
									AppPreference.saveIsIgnoreSilent(getContext(), false);
									setIsIgnoreSilent(false);
								}
								break;
							default:
								assert (false);
								break;
						}
					}
				});

		setIsConfirmAlreadyOnManner(AppPreference.loadIsConfirmAlreadyOnManner(this));
		((CheckBox)findViewById(R.id.CheckBoxIsConfirmAlreadyOnManner))
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						AppPreference.saveIsConfirmAlreadyOnManner(getContext(), isChecked);
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

	private void setMannerMode(int value){
		assert(value==AudioManager.RINGER_MODE_VIBRATE||value==AudioManager.RINGER_MODE_SILENT);
		((RadioGroup)findViewById(R.id.radioGroupMannerModeSetting))
				.check(value==AudioManager.RINGER_MODE_VIBRATE?
						R.id.radioMannerModeVibrate:R.id.radioMannerModeSilent);
	}

	private void setIsIgnoreSilent(boolean value){
		((CheckBox)findViewById(R.id.checkBoxIgnoreSilentMode)).setChecked(value);
	}

	private void setIsConfirmAlreadyOnManner(boolean value){
		((CheckBox)findViewById(R.id.CheckBoxIsConfirmAlreadyOnManner)).setChecked(value);
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
			WebViewActivity.showByURL(this, "http://www.yahoo.co.jp");
			break;
		default:
			assert( false );
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
