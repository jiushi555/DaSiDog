package com.example.set;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.DasiDog.JSONParser;
import com.example.DasiDog.R;
import com.example.DasiDog.Welcome;
import com.example.ProgressDialog.CustomProgressDialog;
import com.example.set.ResetPwd_1.SignupUser;

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

public class ResetPwd_2 extends Activity {
	private int user_id;
	private EditText new_pwd;
	private TextView change;
	private CheckBox xspwd;
	private String new_pwd_txt;
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog ProgressDialog;
	private int success;
	private String url=Welcome.URL + "/dasidog/resetpwd_2.php";
	private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resetpwd_2);
		Intent super_it = super.getIntent();
		user_id = super_it.getIntExtra("user_id", 0);
		new_pwd = (EditText) findViewById(R.id.newetPassword);
		change = (TextView) findViewById(R.id.ret_change);
		xspwd = (CheckBox) findViewById(R.id.cbPassword);
		xspwd.setOnClickListener(new xspwdOnClick());
		change.setOnClickListener(new changeOnClick());
		back=(ImageView) findViewById(R.id.ret_pwd2_back);
		back.setOnClickListener(new backListener());
	}
	class backListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			ResetPwd_2.this.finish();
			
		}
		
	}
	class changeOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			new_pwd_txt = new_pwd.getText().toString();
			new SignupUser().execute();
		}

	}

	class SignupUser extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog = CustomProgressDialog.createDialog(ResetPwd_2.this);
			ProgressDialog.setMessage("修改中...");
			ResetPwd_2.this.ProgressDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("new_pwd", new_pwd_txt));
			params.add(new BasicNameValuePair("user_id", String
					.valueOf(user_id)));

			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params,ResetPwd_2.this);
			Log.d("Create Response", json.toString());
			try {
				ResetPwd_2.this.success = json.getInt("success");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			ProgressDialog.dismiss();
			if (ResetPwd_2.this.success == 0) {
				Toast.makeText(ResetPwd_2.this, "修改成功", Toast.LENGTH_SHORT).show();
				
				ResetPwd_2.this.finish();
			}
			if (ResetPwd_2.this.success == 1) {
				Toast.makeText(ResetPwd_2.this, "修改失败，刷新试试", Toast.LENGTH_SHORT)
						.show();
			}
			if (ResetPwd_2.this.success == 2) {
				Toast.makeText(ResetPwd_2.this, "修改失败，刷新试试", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	class xspwdOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (xspwd.isChecked()) {
				new_pwd.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
			} else {
				new_pwd.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
			}

		}

	}
}
