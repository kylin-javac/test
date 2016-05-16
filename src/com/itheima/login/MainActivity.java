package com.itheima.login;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.itheima.httpclientlogin.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText et_username;
	private EditText et_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// [1]找到我们关心的控件
		
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);

	}

	// 点击按钮 进行get方式提交数据
	public void click1(View v) {
		
		new Thread(){public void run() {
			try {
				String name = et_username.getText().toString().trim();
				String pwd = et_password.getText().toString().trim();
				//[2.1]定义get方式要提交的路径    小细节 如果提交中文要对name 和 pwd 进行一个urlencode 编码 
					String path = "http://192.168.11.73:8080/login/LoginServlet?username="+URLEncoder.encode(name, "utf-8")+"&password="+URLEncoder.encode(pwd, "utf-8")+"";
					
					//[3]获取httpclient实例 
					DefaultHttpClient client = new DefaultHttpClient();
					//[3.1]准备get请求 定义 一个httpget实现 
					HttpGet get = new HttpGet(path);
					
					//[3.2]执行一个get请求  
					HttpResponse response = client.execute(get);
					
					//[4]获取服务器返回的状态码
					int code = response.getStatusLine().getStatusCode();
					if (code == 200) {
						//[5]获取服务器返回的数据   以流的形式返回 
						InputStream inputStream = response.getEntity().getContent();
						
						//[6]把流转换成字符串
					    String content = StreamTools.readStream(inputStream);	
					
					    //[7]展示结果
						showToast(content);
					}
					
					
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		};}.start();
		
		
		
	}

	// [1]点击按钮 进行post方式提交数据
	public void click2(View v) {

		
	new Thread(){public void run() {
			
			try {
				//[2]获取用户名和密码 
				String name = et_username.getText().toString().trim();
				String pwd = et_password.getText().toString().trim();
				String path = "http://192.168.11.73:8080/login/LoginServlet";
				
				//[3]以httpClient 方式进行post 提交 
				DefaultHttpClient client = new DefaultHttpClient();
				//[3.1]准备post 请求 
				HttpPost post = new HttpPost(path);

				//[3.1.0]准备parameters 
				List<NameValuePair> lists = new ArrayList<NameValuePair>();
				//[3.1.1]准备 NameValuePair 实际上就是我们要提交的用户名 和密码  key是服务器key :username
				BasicNameValuePair nameValuePair = new BasicNameValuePair("username",name);
				BasicNameValuePair pwdValuePair = new BasicNameValuePair("password",pwd);
				//[3.1.3] 把nameValuePair  和 pwdValuePair  加入到集合中
				lists.add(nameValuePair);
				lists.add(pwdValuePair);
				
				//[3.1.3]准备entity
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(lists);
				
				//[3.2]准备post方式提交的正文   以实体形式准备 (键值对形式 )
				post.setEntity(entity);
				
				
				HttpResponse response = client.execute(post);
				//[4]获取服务器返回的状态码
				int code = response.getStatusLine().getStatusCode();
				if (code == 200) {
					//[5]获取服务器返回的数据   以流的形式返回 
					InputStream inputStream = response.getEntity().getContent();
					
					//[6]把流转换成字符串
				    String content = StreamTools.readStream(inputStream);	
				    
				    //[7]展示结果
					showToast(content);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			
			
		};}.start();
		
		
	}
	
	
	
	//封装toast方法  该toast方法执行在主线程 
	public void showToast(final String content){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				//该方法一定是执行主线程 
				Toast.makeText(getApplicationContext(), content, 1).show();
				
				
			}
		});
		
	}

}
