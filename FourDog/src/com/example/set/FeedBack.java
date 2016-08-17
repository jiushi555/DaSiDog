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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedBack extends Activity {
	private ImageView back;
	private EditText feed;
	private String feed_txt;
	private String moblie_txt;
	private EditText moblie;
	private TextView btnsend;
	private int success;
	private CustomProgressDialog ProgressDialog;
	private String url = Welcome.URL + "/dasidog/feedback.php";
	JSONParser jsonParser = new JSONParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		back = (ImageView) findViewById(R.id.btnBack);
		feed = (EditText) findViewById(R.id.etContent);
		moblie = (EditText) findViewById(R.id.etMobile);
		btnsend = (TextView) findViewById(R.id.btnRight);
		btnsend.setOnClickListener(new btnOnClick());
		back.setOnClickListener(new backOnClick());
	}

	class btnOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			feed_txt = feed.getText().toString();
			moblie_txt = moblie.getText().toString();
			new SignupUser().execute();
		}

	}

	class SignupUser extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog = CustomProgressDialog.createDialog(FeedBack.this);
			ProgressDialog.setMessage("发送中...");
			FeedBack.this.ProgressDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("feedback", feed_txt));
			params.add(new BasicNameValuePair("moblie", moblie_txt));
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params,FeedBack.this);
			Log.d("Create Response", json.toString());
			try {
				FeedBack.this.success = json.getInt("success");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			ProgressDialog.dismiss();
			if (FeedBack.this.success == 0) {
				Toast.makeText(FeedBack.this, "感谢您的建议！", Toast.LENGTH_SHORT)
						.show();

			}
			if (FeedBack.this.success == 1) {
				Toast.makeText(FeedBack.this, "发送失败，刷新试试？", Toast.LENGTH_SHORT)
						.show();

			}
			if (FeedBack.this.success == 2) {
				Toast.makeText(FeedBack.this, "发送失败，刷新试试？", Toast.LENGTH_SHORT)
						.show();

			}
		}
	}

	class backOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			FeedBack.this.finish();

		}

	}

}
