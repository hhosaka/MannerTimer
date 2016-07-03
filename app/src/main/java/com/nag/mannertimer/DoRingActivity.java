package com.nag.mannertimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;


public class DoRingActivity extends Activity {

	public static void invoke(Context context){
		Intent intent = new Intent(context, DoRingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_do_ring);
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		final Ringtone rt = RingtoneManager.getRingtone(this, uri);
		rt.play();
		new AlertDialog.Builder(this)
				.setTitle(getString(R.string.label_missed_call))
				.setPositiveButton(R.string.label_confirmed,new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						rt.stop();
						finish();
					}
				})
				.show();

	}
}
