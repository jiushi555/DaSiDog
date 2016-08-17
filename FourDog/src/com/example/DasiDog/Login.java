package com.example.DasiDog;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.DasiDog.Zc_2.SignupUser;
import com.example.ProgressDialog.CustomProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	private TextView go_zc;
	private CheckBox cdpwd;
	private EditText pwd;
	private EditText username;
	private TextView login;
	private String username_txt;
	private String pwd_txt;
	private ProgressDialog pDialog;
	private CustomProgressDialog ProgressDialog;
	private String url = Welcome.URL + "/dasidog/login.php";
	JSONParser jsonParser = new JSONParser();
	private int success;
	private int id;
	private int now;
	private DatabaseHelper helper = null;
	private DataOperate mytab = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		go_zc = (TextView) findViewById(R.id.go_zc);
		cdpwd = (CheckBox) findViewById(R.id.cbPassword);
		pwd = (EditText) findViewById(R.id.etPassword);
		login=(TextView) findViewById(R.id.tvLogin);
		username=(EditText) findViewById(R.id.etUserName);
		login.setOnClickListener(new loginOnClickListener());
		go_zc.setOnClickListener(new gozcClickListener());
		cdpwd.setOnClickListener(new xsPwdOnClick());
	}
	class loginOnClickListener implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			username_txt=username.getText().toString();
			pwd_txt=pwd.getText().toString();
			if(Login.this.username_txt.equals("")){
				Toast.makeText(Login.this,"请输入用户名",Toast.LENGTH_SHORT).show();
			}else if(Login.this.pwd_txt.equals("")){
				Toast.makeText(Login.this,"请输入密码",Toast.LENGTH_SHORT).show();
			}else{
				new SignupUser().execute();
			}
			
		}
		
	}
	class SignupUser extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog=CustomProgressDialog.createDialog(Login.this);
			ProgressDialog.setMessage("登录中...");
			Login.this.ProgressDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username_txt));
			params.add(new BasicNameValuePair("password", pwd_txt));
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params,Login.this);
			Log.d("Create Response", json.toString());
			try {
				Login.this.success = json.getInt("success");
				Login.this.id = json.getInt("id");
				Login.this.now=json.getInt("now");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			ProgressDialog.dismiss();
			if (Login.this.success == 0) {
				Toast.makeText(Login.this, "用户名不存在", Toast.LENGTH_SHORT).show();
			}
			if (Login.this.success == 2) {
				Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
				Login.this.helper = new DatabaseHelper(Login.this);
				Login.this.mytab = new DataOperate(
						Login.this.helper.getWritableDatabase());
				int id = mytab.select_id();
				if (id == 0) {
					mytab.insert(Login.this.id,username_txt,now
							);
				} else {
					mytab.update(Login.this.id, username_txt,now);
				}
				Intent it_go = new Intent(Login.this, Sy_Tab.class);
				it_go.putExtra("user_id", Login.this.id);
				startActivity(it_go);
				Login.this.finish();

			}
			if (Login.this.success == 1) {
				Toast.makeText(Login.this, "密码错误", Toast.LENGTH_SHORT)
						.show();
			}
			if (Login.this.success == 3) {
				Toast.makeText(Login.this, "登陆失败，刷新试试", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}
	class xsPwdOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (cdpwd.isChecked()) {
				pwd.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
			} else {
				pwd.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
			}

		}

	}

	class gozcClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent it = new Intent(Login.this, Zc_1.class);
			startActivity(it);
			Login.this.finish();
		}

	}
	protected void onDestroy()   
	{  
	    super.onDestroy();  
	    if(helper!= null)   
	    {  
	    	helper.close();
	    	}
	}  
}
