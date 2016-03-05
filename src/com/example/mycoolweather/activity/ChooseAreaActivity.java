package com.example.mycoolweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.mycoolweather.R;
import com.example.mycoolweather.db.CoolWeatherDB;
import com.example.mycoolweather.model.City;
import com.example.mycoolweather.model.Province;
import com.example.mycoolweather.util.HttpCallbackListener;
import com.example.mycoolweather.util.HttpUtil;
import com.example.mycoolweather.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTRY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	//private static final String LOGTAG ="";
	/**
	 * 省列表
	 */
	private List<Province> provinceList;
	/**
	 * 市列表
	 */
	private List<City> cityList;
	/**
	 * 县列表
	 */
	//private List<County> countyList;
	/**
	 * 选中的省份
	 */
	private Province selectedProvince;
	/**
	 * 当前选中的城市
	 */
	private City selectedCity;
	/**
	 * 当前选中级别
	 */
	private int currentLevel;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		//Log.d("ChooseArea","QWERTYU");
		listView =(ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				// TODO Auto-generated method stub
				if (currentLevel==LEVEL_PROVINCE){
					selectedProvince = provinceList.get(index);
					queryCities();
					
				}else if(currentLevel == LEVEL_CITY){
					selectedCity = cityList.get(index);
					String cityCode = selectedCity.getCityCode();
					Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra("city_code", cityCode);
					startActivity(intent);
					//Intent intent2 = new Intent(ChooseAreaActivity.this,AutoUpdateService.class);
					//startActivity(intent2);
					finish();//结束当前活动
				}
			}
			
		});
		queryProvinces();
	}
	/**
	 * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
	 */
	private void queryProvinces(){
		provinceList = coolWeatherDB.loadProvinces();
		if(provinceList.size() > 0){
			dataList.clear();
			for(Province province :provinceList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		}else {
			queryFromServer("province");
		}
	}
	/**
	 * 查询所有的城市，优先从数据库查询，查询不到在去服务器查询
	 */
	private void queryCities(){
		cityList = coolWeatherDB.loadcitys(selectedProvince.getProvinceName());
		if(cityList.size() > 0){
			dataList.clear();
			for(City city :cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}else{
			queryFromServer("city");
		}
	}
	/**
	 * 查询所有的县，优先从数据库查询，查询不到再从服务器查询
	 */
	/*private void queryCounties(){
		countyList = coolWeatherDB.loadcountys(selectedProvince.getId());
		if(countyList.size() > 0){
			dataList.clear();
			for(County county :countyList){
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTRY;
		}else{
			queryFromServer(selectedCity.getCityCode(),"county");
		}
	}*/
	/**
	 * 根据传入的代号和类型从服务器上查询省市县的数据
	 */
	private void queryFromServer(final String type){
		String address = "https://api.heweather.com/x3/citylist?search=allchina&key=81d897f0b93a4ccb8fc3da62bbb4387f";
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				//response ="21|shanghai,22|beijing,23|guangz,24|shenzhen";
				boolean result = false;
				if("province".equals(type)){
					result = Utility.handleProvinceResponse(coolWeatherDB, response);
					//Log.d("ChooseAreaActivity","QWERTYU11");
				}else if("city".equals(type)){
					result =Utility.handleCityResponse(coolWeatherDB, response, selectedProvince.getId());
					//Log.d("ChooseAreaActivity","QWERTYU22");
				}/*else if("county".equals(type)){
					result = Utility.handlecountyResponse(coolWeatherDB, response, selectedCity.getId());//从服务器查得数据并存入到数据库中
					//Log.d("ChooseAreaActivity","QWERTYU33");
				}*/
				/**
				 * 查询完毕后，关闭进度框，再一次调用查询方法，将数据库数据加载到界面。
				 */
				if(result){
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}
						}
						
					});
				}
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
				// 请求服务器响应出错
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
					
				});
			}
			
		});
	}
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog(){
		if(progressDialog ==null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载。。。。");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	/**-
	 * 关闭进度对话框
	 */
private void closeProgressDialog(){
	if(progressDialog != null){
		progressDialog.dismiss();
	}
	}
/**
 * 捕获back键，根据当前级别来判断，此时应该返回市列表，省列表还是直接退出
 */
@Override
public void onBackPressed(){
	if(currentLevel==LEVEL_COUNTRY){
		queryCities();
	}else if(currentLevel == LEVEL_CITY){
		queryProvinces();
	}else {
		finish();
	}
}
}