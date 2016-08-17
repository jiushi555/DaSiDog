package com.example.DasiDog;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.ProgressDialog.CustomProgressDialog;
import com.example.tx.Tx_zc;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class Zc_2 extends Activity {
	private TextView zc_btn;
	private int now=0;
	private RadioGroup now_radio;
	private RadioButton dsg;
	private RadioButton yby;
	private String username;
	private String pwd;
	private TextView zc_dl;
	private String url = Welcome.URL + "/dasidog/zc.php";
	private ProgressDialog pDialog;
	private int id;
	private int success;
	private DatabaseHelper helper = null;
	private DataOperate mytab = null;
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zc_2);
		Intent it_get = super.getIntent();
		username = it_get.getStringExtra("zc_username");
		pwd = it_get.getStringExtra("zc_pwd");
		zc_btn = (TextView) findViewById(R.id.zc_btn);
		now_radio = (RadioGroup) findViewById(R.id.now);
		dsg = (RadioButton) findViewById(R.id.dsg);
		yby = (RadioButton) findViewById(R.id.yby);
		now_radio.setOnCheckedChangeListener(new nowCheckListener());
		zc_btn.setOnClickListener(new zcOnClickListener());
		zc_dl=(TextView) findViewById(R.id.zc_dl_2);
		zc_dl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it=new Intent(Zc_2.this,Login.class);
				Zc_2.this.startActivity(it);
				Zc_2.this.finish();
			}
		});
	}

	

	class nowCheckListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (dsg.getId() == checkedId) {
				now = 1;
			}
			if (yby.getId() == checkedId) {
				now = 2;
			}

		}

	}
	class zcOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(now==0){
				Toast.makeText(Zc_2.this, "请选择现在的状态",Toast.LENGTH_SHORT).show();
			}else{
				new SignupUser().execute();
			}

		}

	}
	class SignupUser extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = CustomProgressDialog.createDialog(Zc_2.this);
	    	progressDialog.setMessage("注册中...");
			Zc_2.this.progressDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", pwd));
			params.add(new BasicNameValuePair("now", String.valueOf(now)));
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params,Zc_2.this);
			Log.d("Create Response", json.toString());
			try {
				Zc_2.this.success = json.getInt("success");
				Zc_2.this.id = json.getInt("id");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			progressDialog.dismiss();
			if (Zc_2.this.success == 0) {
				Toast.makeText(Zc_2.this, "用户名已存在", Toast.LENGTH_SHORT).show();
				Intent it_back=new Intent(Zc_2.this,Zc_1.class);
				startActivity(it_back);
				Zc_2.this.finish();
			}
			if (Zc_2.this.success == 1) {
				Toast.makeText(Zc_2.this, "注册成功", Toast.LENGTH_SHORT).show();
				Zc_2.this.helper = new DatabaseHelper(Zc_2.this);
				Zc_2.this.mytab = new DataOperate(
						Zc_2.this.helper.getWritableDatabase());
				int id = mytab.select_id();
				if (id == 0) {
					mytab.insert(Zc_2.this.id,username,now
							);
				} else {
					mytab.update(Zc_2.this.id, username,now);
				}
				Intent it = new Intent(Zc_2.this, Tx_zc.class);
				it.putExtra("user_id", Zc_2.this.id);
				startActivity(it);
				Zc_2.this.finish();

			}
			if (Zc_2.this.success == 3) {
				Toast.makeText(Zc_2.this, "注册失败，刷新试试？", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}
}
