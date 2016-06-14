package com.heima.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.heima.chat.service.BackgroundService;
import com.heima.chat.utils.CommonUtil;

public class ConnectedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// 判断网络是否连接
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			if (CommonUtil.isNetConnected(context)) {
				// 网络已经连接
				context.startService(new Intent(context,
						BackgroundService.class));
			}
		}

	}

}
