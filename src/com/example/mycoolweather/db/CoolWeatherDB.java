package com.example.mycoolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.example.mycoolweather.model.City;
import com.example.mycoolweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	public static final String DB_NAME ="cool_weather";//name
	public static final int VERSION = 1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
		db = dbHelper.getWritableDatabase();
	}
	//构造CoolWeatherDB实例
	public synchronized static CoolWeatherDB getInstance (Context context){
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	//将Province存入数据库
	public void saveProvince(Province province){
		if(province !=null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			//values.put("privince_code",province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	//读取全国所有省份的信息
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null,null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				//province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor !=null){
			cursor.close();
		}
		return list;
	}
	
	public void saveCity(City city){
		if(city !=null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code",city.getCityCode());
			values.put("province_name", city.getProvinceName());
			values.put("city_location",city.getCityLocation());
			values.put("country", city.getCountry());
			db.insert("City", null, values);
		}
	}
	//读取全国所有省份的信息
	public List<City> loadcitys(String provinceName){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_name = ?", new String[] {provinceName}, null,null,null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor !=null){
			cursor.close();
		}
		return list;
	}
	
	/*public void saveCounty(County county){
		if(county !=null){
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyCode());
			values.put("privince_code",county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("county", null, values);
		}
	}
	//读取全国所有省份的信息
	public List<County> loadcountys(int cityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor =db.query("County", null, "city_id = ?", new String[] {String.valueOf(cityId)}, null,null, null);
		if(cursor.moveToFirst()){
			do{
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cursor.getInt(cursor.getColumnIndex("cityId")));
				list.add(county);
			}while(cursor.moveToNext());
		}
		if(cursor !=null){
			cursor.close();
		}
		return list;
	}*/

}
