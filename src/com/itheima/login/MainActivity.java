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

		// [1]�ҵ����ǹ��ĵĿؼ�
		
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);

	}

	// �����ť ����get��ʽ�ύ����
	public void click1(View v) {
		
		new Thread(){public void run() {
			try {
				String name = et_username.getText().toString().trim();
				String pwd = et_password.getText().toString().trim();
				//[2.1]����get��ʽҪ�ύ��·��    Сϸ�� ����ύ����Ҫ��name �� pwd ����һ��urlencode ���� 
					String path = "http://192.168.11.73:8080/login/LoginServlet?username="+URLEncoder.encode(name, "utf-8")+"&password="+URLEncoder.encode(pwd, "utf-8")+"";
					
					//[3]��ȡhttpclientʵ�� 
					DefaultHttpClient client = new DefaultHttpClient();
					//[3.1]׼��get���� ���� һ��httpgetʵ�� 
					HttpGet get = new HttpGet(path);
					
					//[3.2]ִ��һ��get����  
					HttpResponse response = client.execute(get);
					
					//[4]��ȡ���������ص�״̬��
					int code = response.getStatusLine().getStatusCode();
					if (code == 200) {
						//[5]��ȡ���������ص�����   ��������ʽ���� 
						InputStream inputStream = response.getEntity().getContent();
						
						//[6]����ת�����ַ���
					    String content = StreamTools.readStream(inputStream);	
					
					    //[7]չʾ���
						showToast(content);
					}
					
					
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		};}.start();
		
		
		
	}

	// [1]�����ť ����post��ʽ�ύ����
	public void click2(View v) {

		
	new Thread(){public void run() {
			
			try {
				//[2]��ȡ�û��������� 
				String name = et_username.getText().toString().trim();
				String pwd = et_password.getText().toString().trim();
				String path = "http://192.168.11.73:8080/login/LoginServlet";
				
				//[3]��httpClient ��ʽ����post �ύ 
				DefaultHttpClient client = new DefaultHttpClient();
				//[3.1]׼��post ���� 
				HttpPost post = new HttpPost(path);

				//[3.1.0]׼��parameters 
				List<NameValuePair> lists = new ArrayList<NameValuePair>();
				//[3.1.1]׼�� NameValuePair ʵ���Ͼ�������Ҫ�ύ���û��� ������  key�Ƿ�����key :username
				BasicNameValuePair nameValuePair = new BasicNameValuePair("username",name);
				BasicNameValuePair pwdValuePair = new BasicNameValuePair("password",pwd);
				//[3.1.3] ��nameValuePair  �� pwdValuePair  ���뵽������
				lists.add(nameValuePair);
				lists.add(pwdValuePair);
				
				//[3.1.3]׼��entity
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(lists);
				
				//[3.2]׼��post��ʽ�ύ������   ��ʵ����ʽ׼�� (��ֵ����ʽ )
				post.setEntity(entity);
				
				
				HttpResponse response = client.execute(post);
				//[4]��ȡ���������ص�״̬��
				int code = response.getStatusLine().getStatusCode();
				if (code == 200) {
					//[5]��ȡ���������ص�����   ��������ʽ���� 
					InputStream inputStream = response.getEntity().getContent();
					
					//[6]����ת�����ַ���
				    String content = StreamTools.readStream(inputStream);	
				    
				    //[7]չʾ���
					showToast(content);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			
			
		};}.start();
		
		
	}
	
	
	
	//��װtoast����  ��toast����ִ�������߳� 
	public void showToast(final String content){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				//�÷���һ����ִ�����߳� 
				Toast.makeText(getApplicationContext(), content, 1).show();
				
				
			}
		});
		
	}

}
