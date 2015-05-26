package com.nag.android.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity{

	public static final String ASSERT_URL = "file:///android_asset/";
	public static final String PARAM_URL = "url";
	private WebView webview = null;

	public static void showByURL(Context context, String url){
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra(PARAM_URL, url);
		context.startActivity(intent);
	}

	public static void showByAsset(Context context, String url){
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra(PARAM_URL, ASSERT_URL + url);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String url = getIntent().getStringExtra(PARAM_URL);
		webview = new WebView(this);
		webview.setWebViewClient(new WebViewClient());
		setContentView(webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl( url );
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK &&  webview.canGoBack() ) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
