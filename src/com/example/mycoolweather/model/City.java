package com.example.mycoolweather.model;

public class City {
		private int id;
		private String cityName;
		private String cityCode;
		private String provinceName;
		private String location;
		private String country;
		
		public int getId(){
			return id;
		}
		public void setId(int id){
			this.id = id;
		}
		public String getCityName(){
			return cityName;
		}
		public void setCityName(String cityName){
			this.cityName = cityName;
		}
		public  String getCityCode(){
			return cityCode;
		}
		public void setCityCode(String cityCode){
			this.cityCode = cityCode;
		}
		public String  getProvinceName(){
			return provinceName;
		}
		public void setProvinceName(String provinceName){
			this.provinceName = provinceName;
		}
		public String getCityLocation(){
			return location;
		}
		public void setCityLocation(String lat,String lon){
			//location[1] = Double.parseDouble(lat);
			//location[2]= Double.parseDouble(lon);
			location = lat+","+lon;
	
		}
		public String getCountry (){
			return country;
		}
		public void setCountry(String country){
			this.country = country;
		}

}
