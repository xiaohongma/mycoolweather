package com.example.mycoolweather.activity;

import com.example.mycoolweather.R;
import com.example.mycoolweather.util.HttpCallbackListener;
import com.example.mycoolweather.util.HttpUtil;
import com.example.mycoolweather.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity implements OnClickListener{
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView updateText;
	private TextView currentTempText;
	private TextView currentWeatherText;
	private TextView dayTempText;
	private TextView dayWeatherText;
	private Button switchCity;
	private Button refreshWeather;
	
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
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		String cityCode = getIntent().getStringExtra("city_code");
		if(!TextUtils.isEmpty(cityCode)){
			updateText.setText("同步中。。。");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(cityCode);
			
		}else{
			//showWeather();
			Toast.makeText(WeatherActivity.this, "无此城市数据", Toast.LENGTH_LONG).show();
		}
		
	}
	private void queryWeatherCode(String cityCode){
		String address = "https://api.heweather.com/x3/weather?cityid="+cityCode+"&key=81d897f0b93a4ccb8fc3da62bbb4387f";
		queryFromServer(address,"cityCode");
	}
	
	private void queryFromServer(final String address,final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Utility.handleWeatherResponse(WeatherActivity.this, response);
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
						cityNameText.setText(prefs.getString("city_name", ""));
						updateText.setText(prefs.getString("update_time", "")+" 发布");
						currentTempText.setText(prefs.getString("now_temp", ""));
						currentWeatherText.setText(prefs.getString("now_weather", ""));
						dayTempText.setText(prefs.getString("day_temp", ""));
						dayWeatherText.setText(prefs.getString("day_weather", ""));
						weatherInfoLayout.setVisibility(View.VISIBLE);
						cityNameText.setVisibility(View.VISIBLE);
					}
					
				});
				
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						updateText.setText("加载失败了");
					}
					
				});
			}
			
		});
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.switch_city :
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			updateText.setText("更新中。。。");
			
		}
		
	}
	
	/*private void showWeather(){
		
	}*/

}
