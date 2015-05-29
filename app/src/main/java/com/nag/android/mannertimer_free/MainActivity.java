package com.nag.android.mannertimer_free;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.nag.mannertimer.R;

/**
 * Created by hosaka-hitoshi on 2015/05/26.
 */
public class MainActivity extends com.nag.mannertimer.MainActivity {
	private AdView adView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adView = new AdView(this);
		adView.setAdUnitId(this.getResources().getString(R.string.id_admob));

		adView.setAdSize(AdSize.BANNER);
		FrameLayout.LayoutParams adParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		adParams.gravity = (Gravity.TOP|Gravity.CENTER);
		addContentView(adView, adParams);
		adView.loadAd(new AdRequest.Builder().build());
	}
}
