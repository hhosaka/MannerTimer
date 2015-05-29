package com.nag.mannertimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nag.android.mannertimer_free.R;

class TimerSelector implements OnClickListener {

	private static final int TIME_UNIT = 60 * 1000;
	private final LabelImpl[] labels;
	private static class LabelImpl implements CharSequence{
		private String label;
		private int value;
		public LabelImpl(String label, Integer value) {
			this.label=label;
			this.value=value;
		}
		int getValue(){
			return value;
		}
		@Override
		public String toString(){
			return label;
		}
		@Override
		public char charAt(int index) {
			return toString().charAt(index);
		}

		@Override
		public int length() {
			return toString().length();
		}

		@Override
		public CharSequence subSequence(int start, int end) {
			return toString().subSequence(start, end);
		}
	}

	private int getCurrentItem(Context context){
		long time = AppPreference.loadRegisteredTime(context);
		if(time==0L){
			return 0;
		}else {
			time = (time - System.currentTimeMillis())/TIME_UNIT;
			for( int i=1; i< labels.length; ++i) {
				if (labels[i].getValue() > time) {
					return i;
				}
			}
			throw new UnsupportedOperationException();
		}
	}

	public TimerSelector(Context context) {
		final LabelImpl[] labels = {
				new LabelImpl(context.getString(R.string.label_smart_timer_deactivate),0),
				new LabelImpl(context.getString(R.string.label_smart_timer_option10),10),
				new LabelImpl(context.getString(R.string.label_smart_timer_option30),30),
				new LabelImpl(context.getString(R.string.label_smart_timer_option60),60),
				new LabelImpl(context.getString(R.string.label_smart_timer_option90),90),
				new LabelImpl(context.getString(R.string.label_smart_timer_option120),120),
				new LabelImpl(context.getString(R.string.label_smart_timer_option150),150),
				new LabelImpl(context.getString(R.string.label_smart_timer_option180),180),
				new LabelImpl(context.getString(R.string.label_smart_timer_option240),240)
		};
		this.labels = labels;
	}
	
	public static String getLabel(Context context) {
		long t = AppPreference.loadRegisteredTime(context);
		if (t == 0) {
			int mode = Executor.getRingerMode(context);
			switch (mode) {
				case AudioManager.RINGER_MODE_NORMAL:
					return context.getString(R.string.label_smart_timer_deactivating);
				case AudioManager.RINGER_MODE_VIBRATE:
					return context.getString(R.string.label_smart_timer_in_manner_vibration);
				case AudioManager.RINGER_MODE_SILENT:
					return context.getString(R.string.label_smart_timer_in_manner_silent);
				default:
					throw new RuntimeException();
			}
		} else {
			return context.getString(R.string.label_smart_timer_prefix) + DateFormat.format("kk:mm:ss", t).toString() + context.getString(R.string.label_smart_timer_postfix);
		}
	}

	public static String getShortLabel(Context context) {
		long t = AppPreference.loadRegisteredTime(context);
		if (t == 0) {
			int mode = Executor.getRingerMode(context);
			switch (mode) {
				case AudioManager.RINGER_MODE_NORMAL:
					return context.getString(R.string.label_short_smart_timer_deactivating);
				case AudioManager.RINGER_MODE_VIBRATE:
					return context.getString(R.string.label_short_smart_timer_in_manner_vibration);
				case AudioManager.RINGER_MODE_SILENT:
					return context.getString(R.string.label_short_smart_timer_in_manner_silent);
				default:
					throw new RuntimeException();
			}
		} else {
			return DateFormat.format("kk:mm:ss", t).toString();
		}
	}

	public void showDialog(final View v, final Activity activity, final Button button) {
		final Context context = v.getContext();
		new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.label_smart_timer))
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						if (activity != null) activity.finish();
						if(button!=null)button.setText(getLabel(context));
					}
				})
				.setSingleChoiceItems(labels, getCurrentItem(context)
						, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						execute(context, labels[which].getValue());
						dialog.dismiss();
						if (activity != null) activity.finish();
						if(button!=null)button.setText(getLabel(context));
					}
				}).create().show();
	}

	private void execute(Context context, int value) {
		if(value==0){
			Executor.stop(context);
		}else{
			long time = System.currentTimeMillis() + value * TIME_UNIT;
			((AudioManager)context.getSystemService(Context.AUDIO_SERVICE)).setRingerMode(AppPreference.loadMannerMode(context));
			Executor.start(context, AppPreference.loadMannerMode(context), time);
		}
	}

	@Override
	public void onClick(View v) {
		showDialog(v, null,(Button)v);
	}
}
