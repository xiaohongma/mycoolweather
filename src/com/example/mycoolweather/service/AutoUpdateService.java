package com.example.mycoolweather.service;
//耗时的更新天气的操作放在子线程中，服务开始就启动。获取最新的天气数据送到SharedPreferences中，每隔8小时更新一次
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.example.mycoolweather.receiver.AutoUpdateReceiver;
import com.example.mycoolweather.util.HttpCallbackListener;
import com.example.mycoolweather.util.HttpUtil;
import com.example.mycoolweather.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class AutoUpdateService extends Service {
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int onStartCommand( Intent intent,int flags,int startId){
		 final String cityName = intent.getStringExtra("city_name");
		new Thread(new Runnable(){
			@Override
			public void  run(){
				updateWeather(cityName);//服务启动一次，子线程运行一次
				Log.d("MainActivity", "test");
			}
		}).start();
		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);//获取定时服务管理器
		int anHour = 8*60*60*1000;
		long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
		Intent i = new Intent(this,AutoUpdateReceiver.class);//启动接受服务
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);//等待触发活动
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);//设置为启动服务八小时后自动更新数据
		return super.onStartCommand(intent, flags, startId);
	}
	private void updateWeather(String cityName ){
		//SharedPreferences prefs = getSharedPreferences("data",0);
		//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String address = "";
		String cityURLName="" ;
		try {
			cityURLName =URLEncoder.encode(cityName,"utf-8");
			
		
			address = "https://api.heweather.com/x3/weather?city="+cityURLName+"&key=81d897f0b93a4ccb8fc3da62bbb4387f";
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				Utility.handleWeatherResponse(getApplicationContext(), response);
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stu
				e.printStackTrace();
			}
			
		});
	}
	

}
