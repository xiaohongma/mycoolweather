package com.example.mycoolweather.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.mycoolweather.db.CoolWeatherDB;
import com.example.mycoolweather.model.City;
import com.example.mycoolweather.model.Province;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

//�����������ص����ݴ��뵽���ݿ���

public class Utility {
	/**
	 * �����ʹ�����������ص�ʡ������
	 */
	public synchronized static boolean handleProvinceResponse (CoolWeatherDB coolWeatherDB,String response){
		if( !TextUtils.isEmpty(response)){
			try{
			JSONObject json = new JSONObject(response);
			
			JSONArray jsonArray = json.getJSONArray("city_info");
			List<String> list = new ArrayList<String>();
			//Gson gson = new Gson();
			//List<Province> province =gson.fromJson(json, classOfT)
			for(int i = 0;i<jsonArray.length();i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String provinceName = jsonObject.optString("prov");
				list.add(provinceName);
			}
			HashSet<String> hs = new HashSet<String>(list);
	       list.clear();
	       list.addAll(hs);
	        for(String provinceName:list){
	        	Province province = new Province();
	        	province.setProvinceName(provinceName);
				coolWeatherDB.saveProvince(province);
	        	
	        }
	        
			
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * �����ʹ�����������ص��м�����
	 */
	public synchronized static boolean handleCityResponse (CoolWeatherDB coolWeatherDB,String response,int provinceId){
		
		if( !TextUtils.isEmpty(response)){
			try{
			JSONObject json = new JSONObject(response);
			
			JSONArray jsonArray = json.getJSONArray("city_info");
			for(int i = 0;i<jsonArray.length();i++){
				City city = new City();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String cityname =jsonObject.optString("city");
				String country = jsonObject.optString("cnty");
				String cityCode = jsonObject.optString("id");
				String cityLat = jsonObject.optString("lat");
				String cityLon = jsonObject.optString("lon");
				String provinceName = jsonObject.optString("prov");
				city.setCityName(cityname);
				city.setCityCode(cityCode);
				city.setCityLocation(cityLat, cityLon);
				city.setCountry(country);
				city.setProvinceName(provinceName);
				coolWeatherDB.saveCity(city);
				
			}
			
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}
		
		/*if( !TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if( allCities !=null && allCities.length >0){
				for(String c : allCities){
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityCode(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * �����ʹ�����������ص��ؼ�����
	 */
	/*public synchronized static boolean handlecountyResponse (CoolWeatherDB coolWeatherDB,String response,int cityId){
		if( !TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if( allCounties !=null && allCounties.length >0){
				for(String c : allCounties){
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyCode(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}*/
	public static void handleWeatherResponse(Context context,String response){
		try{
			JSONObject jsonObject = new JSONObject(response);
			JSONArray weatherData = jsonObject.getJSONArray("HeWeather data service 3.0");//�Ѵ���Ŀ�µ����ݴ���JSONArray������
			JSONObject aqi = weatherData.getJSONObject(0);
			//JSONObject status = weatherData.getJSONObject(1);
			//JSONObject suggesttion = weatherData.getJSONObject(2);
			//JSONObject city          = aqi.getJSONObject("city");
			JSONArray daliy_forecast  = aqi.getJSONArray("daily_forecast");//�б��д���Ž��쵽δ��������������
			JSONObject basic         = aqi.getJSONObject("basic");
			
			//JSONObject hourly_forecast = aqi.getJSONObject("hourly_forecast");
			JSONObject now           = aqi.getJSONObject("now");
			/**
			 * �����ĳ�����Ϣ
			 */
			String cityName = basic.getString("city");
			//String cnty = basic.getString("cnty");
			String cityId = basic.getString("id");
			JSONObject update = basic.getJSONObject("update");
			String updateTime = update.getString("loc");
			/**
			 * δ��һ��ÿ�������״��
			 */
			JSONObject today = daliy_forecast.getJSONObject(0);
			//JSONObject tomorrow = daliy_forecast.getJSONObject(1);
			JSONObject temp = today.getJSONObject("tmp");
			JSONObject cond = today.getJSONObject("cond");
			String max_temp = temp.getString("max");
			String min_temp = temp.getString("min");
			String txt_d = cond.getString("txt_d");
			String txt_n = cond.getString("txt_n");
			String day_weather = "���� "+txt_d+"   ���� "+txt_n;//��������ϵ�����
			String day_temp = "�����¶�  "+min_temp+"��"+"  ~  "+max_temp+"��";
			/**
			 * ��ǰ�ļ�����
			 */
			String now_temp = now.getString("tmp")+"��";//���ڵ��¶�
			JSONObject now_cond = now.getJSONObject("cond");
			String now_weather = now_cond.getString("txt");
			
			saveWeatherData(context,cityName,cityId,updateTime,day_temp,day_weather,now_temp,now_weather);
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void saveWeatherData(Context context,String cityName,String cityId,String updateTime,
			String day_temp,String day_weather,String now_temp,String now_weather){
		SimpleDateFormat sdf = new SimpleDateFormat("yyy��M��d��",Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("city_id",cityId);
		editor.putString("update_time", updateTime);
		//editor.putString("max_temp", max_temp);
		editor.putString("day_temp", day_temp);
		editor.putString("day_weather", day_weather);
		editor.putString("now_temp", now_temp);
		editor.putString("now_weather", now_weather);
		editor.putString("now_time", sdf.format(new Date()));
		editor.commit();
	}

	}
