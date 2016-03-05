package com.example.mycoolweather.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.example.mycoolweather.R;
import com.example.mycoolweather.service.AutoUpdateService;
import com.example.mycoolweather.util.HttpCallbackListener;
import com.example.mycoolweather.util.HttpUtil;
import com.example.mycoolweather.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
	private String cityName="" ;
	private LocationClient client ;

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
		//Intent i = new Intent(MainActivity.this,AutoUpdateService.class);
		//i.putExtra("city_name", cityName);
		
	//	LocationServer locationServer = new LocationServer();
		//cityName = locationServer.getCity(getApplicationContext());
		
	     client = new LocationClient(MainActivity.this);
	     initLocation();
	     client.registerLocationListener(new BDLocationListener(){

			@Override
			public void onReceiveLocation(BDLocation location) {
				if(location != null){
				
				cityName = location.getCity();
					//cityName ="香格里拉傣族自治州";
				//String cityName2 = cityName.substring(0, 2);
				String cityName2 = HandleCityName.handleCityName(cityName);
				updateWeather(cityName2);
				Intent i = new Intent(MainActivity.this,AutoUpdateService.class);
				i.putExtra("city_name", cityName2);
				startService(i);//启动后台服务服务
				//updateWeather(cityName);
				if(client.isStarted()){
				client.unRegisterLocationListener(this);
				client.stop();
				}
			//String	cityCode = location.getCityCode();
				//locationClient.stop();
				//Log.d("MainActivity", Integer.toString(a));
				}else{
					//Log.d("MainActivity", "定位不到");
					updateText.setText("得到不当前城市,请手动选择");
					weatherInfoLayout.setVisibility(View.INVISIBLE);
					cityNameText.setVisibility(View.INVISIBLE);
				}
			}

		
	     });
		 client.start();
		
	//	cityName = getLocation();
		if(client.isStarted()){
			Intent i = new Intent(MainActivity.this,AutoUpdateService.class);
			i.putExtra("city_name", cityName);
			startService(i);//启动后台服务服务
			//updateWeather(cityName);
			client.stop();
		}else{
			updateText.setText("得到不当前城市,请手动选择");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
		}
		
		
	}
	

	


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
								weatherInfoLayout.setVisibility(View.VISIBLE);
								cityNameText.setVisibility(View.VISIBLE);
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
	private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy
);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
       client.setLocOption(option);
    }
	
		

	
}
