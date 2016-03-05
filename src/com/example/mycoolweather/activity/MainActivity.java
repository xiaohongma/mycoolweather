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
					//cityName ="�����������������";
				//String cityName2 = cityName.substring(0, 2);
				String cityName2 = HandleCityName.handleCityName(cityName);
				updateWeather(cityName2);
				Intent i = new Intent(MainActivity.this,AutoUpdateService.class);
				i.putExtra("city_name", cityName2);
				startService(i);//������̨�������
				//updateWeather(cityName);
				if(client.isStarted()){
				client.unRegisterLocationListener(this);
				client.stop();
				}
			//String	cityCode = location.getCityCode();
				//locationClient.stop();
				//Log.d("MainActivity", Integer.toString(a));
				}else{
					//Log.d("MainActivity", "��λ����");
					updateText.setText("�õ�����ǰ����,���ֶ�ѡ��");
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
			startService(i);//������̨�������
			//updateWeather(cityName);
			client.stop();
		}else{
			updateText.setText("�õ�����ǰ����,���ֶ�ѡ��");
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
								updateText.setText(prefs.getString("update_time", "")+" ����");
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
);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
        option.setCoorType("bd09ll");//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
        int span=1000;
        option.setScanSpan(span);//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
        option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
        option.setOpenGps(true);//��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
        option.setLocationNotify(true);//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
        option.setIsNeedLocationDescribe(false);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
        option.setIsNeedLocationPoiList(true);//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
option.setIgnoreKillProcess(false);//��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��  
        option.SetIgnoreCacheException(false);//��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
option.setEnableSimulateGps(false);//��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
       client.setLocOption(option);
    }
	
		

	
}
