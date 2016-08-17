package com.example.set;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.DasiDog.DataOperate;
import com.example.DasiDog.DatabaseHelper;
import com.example.DasiDog.JSONParser;
import com.example.DasiDog.Login;
import com.example.DasiDog.R;
import com.example.DasiDog.Sy_Tab;
import com.example.DasiDog.Welcome;
import com.example.ProgressDialog.CustomProgressDialog;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ResetPwd_1 extends Activity {
	private TextView reset_next;
	private EditText old_pwd;
	private String old_pwd_txt;
	private String url = Welcome.URL + "/dasidog/resetpwd_1.php";
	private int user_id;
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog ProgressDialog;
	private int success;
	private int id;
	private CheckBox xspwd;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resetpwd_1);
		reset_next = (TextView) findViewById(R.id.ret_next);
		old_pwd = (EditText) findViewById(R.id.oldetPassword);
		Intent super_it = super.getIntent();
		user_id = super_it.getIntExtra("user_id", 0);
		Log.d("user_id=", String.valueOf(user_id));
		xspwd=(CheckBox) findViewById(R.id.cbPassword);
		reset_next.setOnClickListener(new nextClickListener());
		xspwd.setOnClickListener(new xsPwdOnClick());
		back=(ImageView) findViewById(R.id.ret_pwd1_back);
		back.setOnClickListener(new backListener());
	}
	class backListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			ResetPwd_1.this.finish();
		}
		
	}
	class xsPwdOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (xspwd.isChecked()) {
				old_pwd.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
			} else {
				old_pwd.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
			}
			
		}
		
	}
	class nextClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			old_pwd_txt = old_pwd.getText().toString();
			new SignupUser().execute();
		}

	}

	class SignupUser extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog = CustomProgressDialog.createDialog(ResetPwd_1.this);
			ProgressDialog.setMessage("请稍等...");
			ResetPwd_1.this.ProgressDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("old_pwd", old_pwd_txt));
			params.add(new BasicNameValuePair("user_id", String
					.valueOf(user_id)));
			
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params,ResetPwd_1.this);
			Log.d("Create Response", json.toString());
			try {
				ResetPwd_1.this.success = json.getInt("success");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			ProgressDialog.dismiss();
			if (ResetPwd_1.this.success == 0) {
				Intent next=new Intent(ResetPwd_1.this,ResetPwd_2.class);
				next.putExtra("user_id", user_id);
				ResetPwd_1.this.startActivity(next);
				ResetPwd_1.this.finish();
			}
			if (ResetPwd_1.this.success == 1) {
				Toast.makeText(ResetPwd_1.this, "旧密码错误", Toast.LENGTH_SHORT)
						.show();
			}

			if (ResetPwd_1.this.success == 2) {
				Toast.makeText(ResetPwd_1.this, "验证失败，刷新试试", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

}
