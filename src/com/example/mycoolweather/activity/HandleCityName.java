package com.example.mycoolweather.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class HandleCityName {
	public static String handleCityName(String cityName ){
	
		//List<String> list= new ArrayList<String>();
		String[] keyword = new String[13];
		keyword[0] = "��";
		keyword[1] = "����";
		keyword[2] ="������";
		keyword[3] = "�ر�������";
		keyword[4] = "��";
		keyword[5] ="����";
		keyword[6] ="����";
		keyword[7] = "����";
		keyword[8] = "����";
		keyword[9] = "����";
		keyword[10] = "������";
		keyword[11] = "Ǽ��";
		keyword[12] = "������";
		
 		/*StringBuffer list  = new StringBuffer();
		
		list.append("��");
		list.append("����");
		list.append("������");
		list.append("����");
		list.append("�����ر�������");*/
		
		for(int i =0;i<keyword.length;i++){
		Pattern pattern = Pattern.compile(keyword[i]);//�������
		Matcher matcher = pattern.matcher(cityName);
		if(matcher.find()){
		cityName =	cityName.replace(keyword[i],"");
			Log.d("Main", cityName);
			
		
			
		}
		}
		
		return cityName;
		
	}
	

}
