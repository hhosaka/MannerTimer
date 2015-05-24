package com.nag.mannertimer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class TimerSelectorParentActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View v=new View(this);
		setContentView(v);
		new TimerSelector(this).showDialog(v, this, null);
	}
}
