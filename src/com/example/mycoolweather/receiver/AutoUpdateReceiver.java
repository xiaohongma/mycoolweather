package com.example.mycoolweather.receiver;

import com.example.mycoolweather.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context content, Intent intent) {
		Intent i = new Intent(content,AutoUpdateService.class);
		content.startService(i);

	}

}
