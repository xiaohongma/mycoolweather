package com.example.mycoolweather.db;
//建立数据库和城市表
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	//Province
	public static final String CREATE_PROVINCE = "create table Province ("+
					"id integer primary key autoincrement," +
					"province_name text)" ;
	//City
	public static final String CREATE_CITY = "create table City ("+
				"id integer primary key autoincrement,"+
				"city_name text,"+
				"city_code text,"+
				"city_location text,"+
				"country text,"+
				"province_name text)";
	//County
	/*public static final String CREATE_COUNTY = "create table County("+
				"id integer primary key autoincrement,"+
				"county_name text , "+
				"county_code text , "+
				"city_id interger)";*/

	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		//db.execSQL(CREATE_COUNTY);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
