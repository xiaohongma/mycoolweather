package com.example.mycoolweather.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class HandleCityName {
	public static String handleCityName(String cityName ){
	
		//List<String> list= new ArrayList<String>();
		String[] keyword = new String[13];
		keyword[0] = "市";
		keyword[1] = "地区";
		keyword[2] ="自治州";
		keyword[3] = "特别行政区";
		keyword[4] = "盟";
		keyword[5] ="苗族";
		keyword[6] ="藏族";
		keyword[7] = "彝族";
		keyword[8] = "回族";
		keyword[9] = "傣族";
		keyword[10] = "土家族";
		keyword[11] = "羌族";
		keyword[12] = "景颇族";
		
 		/*StringBuffer list  = new StringBuffer();
		
		list.append("市");
		list.append("地区");
		list.append("自治州");
		list.append("地区");
		list.append("地区特别行政区");*/
		
		for(int i =0;i<keyword.length;i++){
		Pattern pattern = Pattern.compile(keyword[i]);//添加数据
		Matcher matcher = pattern.matcher(cityName);
		if(matcher.find()){
		cityName =	cityName.replace(keyword[i],"");
			Log.d("Main", cityName);
			
		
			
		}
		}
		
		return cityName;
		
	}
	

}
