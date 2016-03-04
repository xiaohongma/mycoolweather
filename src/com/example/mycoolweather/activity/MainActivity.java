package com.example.mycoolweather.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.example.mycoolweather.R;
import com.example.mycoolweather.service.AutoUpdateService;
import com.example.mycoolweather.util.HttpCallbackListener;
import com.example.mycoolweather.util.HttpUtil;
import com.example.mycoolweather.util.Utility;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView updateText;
	private TextView currentTempText;
	private TextView currentWeatherText;
	private TextView dayTempText;
	private TextView dayWeatherText;
	private Button switchCity;
	private Button refreshWeather;
	private String cityName = "乌鲁木齐";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
		cityNameText = (TextView)findViewById(R.id.city_name);
		updateText = (TextView)findViewById(R.id.publish_text);
		currentTempText = (TextView)findViewById(R.id.current_temp);
		currentWeatherText = (TextView)findViewById(R.id.current_weather);
		dayTempText = (TextView)findViewById(R.id.day_temp);
		dayWeatherText = (TextView)findViewById(R.id.day_weather);
		switchCity = (Button)findViewById(R.id.switch_city);
		refreshWeather = (Button)findViewById(R.id.refresh_weather);
		switchCity.setText("Switch");
		refreshWeather.setText("Exit");
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		Intent i = new Intent(MainActivity.this,AutoUpdateService.class);
		i.putExtra("city_name", cityName);
		ComponentName b = startService(i);//启动后台服务服务
		updateWeather(cityName);
		
		
	}
	

	private String  getLocation() {
		return null;
		// TODO Auto-generated method stub
		
	}
	/*private void startService(String cityName){
		Intent i = new Intent(MainActivity.this,AutoUpdateService.class);
		i.putExtra("city_name", cityName);
		ComponentName b = startService(i);
		//String cityName = getLocation();
		//String cityName = "乌鲁木齐";
		if(b!=null){
		runOnUiThread(new Runnable(){

			@Override
			public void run() {
				
				showWeather();
			}
			
		});
		}*/
	

	//private void showWeather()  {
		/*SharedPreferences prefs =null;
		 try {
			Context ServiceContext = createPackageContext("com.example.mycoolweather.service.AutoUpdateService",
															Context.CONTEXT_IGNORE_SECURITY);
			 prefs = ServiceContext.getSharedPreferences("test",  MODE_MULTI_PROCESS);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//SharedPreferences prefs = getSharedPreferences("test",MODE_MULTI_PROCESS);
		//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		//SharedPreferences.Editor editor = prefs.edit();
		//editor.clear();
		//editor.putString("city_name", cityName);
		//editor.commit();
		
			//SharedPreferences prefs = getSharedPreferences("data",0);
		/*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	
		String CityName = prefs.getString("city_name", "");
		cityNameText.setText(CityName);
		updateText.setText(prefs.getString("update_time", "")+" 发布");
		currentTempText.setText(prefs.getString("now_temp", ""));
		currentWeatherText.setText(prefs.getString("now_weather", ""));
		dayTempText.setText(prefs.getString("day_temp", ""));
		dayWeatherText.setText(prefs.getString("day_weather", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
		
		
	}*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.switch_city:
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			finish();
			break;
		
		
		}
		
	}
	private void updateWeather(String cityName ){
		String address = "";
		String cityURLName="" ;
		//boolean b2 =false;
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
				 boolean b2 = Utility.handleWeatherResponse(getApplicationContext(), response);
				 if(b2){
					 runOnUiThread(new Runnable(){

							@Override
							public void run() {
								
								SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
							
								String CityName = prefs.getString("city_name", "");
								cityNameText.setText(CityName);
								updateText.setText(prefs.getString("update_time", "")+" 发布");
								currentTempText.setText(prefs.getString("now_temp", ""));
								currentWeatherText.setText(prefs.getString("now_weather", ""));
								dayTempText.setText(prefs.getString("day_temp", ""));
								dayWeatherText.setText(prefs.getString("day_weather", ""));
								//weatherInfoLayout.setVisibility(View.VISIBLE);
								//cityNameText.setVisibility(View.VISIBLE);
							}
							
						});
				 }
				 
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stu
				e.printStackTrace();
			}
			
		});
	}
		

	
}
