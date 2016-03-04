package com.example.mycoolweather.util;
//根据http地址抓取json数据
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				//String response = null;
				try {
					/*HttpClient httpClient = new DefaultHttpClient();
    				HttpGet httpGet = new HttpGet(address);
    				HttpResponse httpResponse = httpClient.execute(httpGet);
    				if(httpResponse.getStatusLine().getStatusCode()==200){
    					HttpEntity entity =httpResponse.getEntity();
    					response =EntityUtils.toString(entity,"utf-8");
    				}*/
					//String s = new String(address.getBytes(),"gbk");
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");//希望从服务器那里得到数据
					connection.setConnectTimeout(8000);//超时连接
					InputStream in = connection.getInputStream();//
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while((line = reader.readLine())!=null){
						response.append(line);
					}
					if(listener !=null){
						listener.onFinish(response.toString());
					}
				}catch(Exception e){
					//e.printStackTrace();
					if(listener != null){
						listener.onError(e);
					}
				}finally{
					if (connection != null){
						connection.disconnect();
					}
				}
			}
			
		}).start();
	}

}
