package com.heima.chat.base;

import android.app.IntentService;

import com.heima.chat.ChatApplication;

public abstract class BaseIntentService extends IntentService {

	public BaseIntentService(String name) {
		super(name);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		((ChatApplication) getApplication()).addService(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		((ChatApplication) getApplication()).removeService(this);
	}
}
