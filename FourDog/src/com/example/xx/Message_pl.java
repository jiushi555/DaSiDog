package com.example.xx;

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


import android.app.Activity;
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

public class Message_pl extends Activity {
	private EditText fb_edt;
	private TextView fb_btn;
	private String pl_name;
	private String hf_where;
	private String hf_where_id;
	private String hf_for;
	private String type = "1";
	private int user_id;
	private String hf_to;
	private ImageView back;
	// 发布评论
	private String url_pl = null;
	private String pl_nr;
	private int success;
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog pDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_pl);
		Intent it = super.getIntent();
		pl_name = it.getStringExtra("pl_name");
		hf_where = it.getStringExtra("hf_where");
		hf_where_id = it.getStringExtra("hf_where_id");
		hf_for = it.getStringExtra("hf_for");
		user_id = it.getIntExtra("user_id", 0);
		hf_to = it.getStringExtra("hf_to");
		if(hf_where.equals("1")){
			url_pl=Welcome.URL + "/dasidog/gz_pl_fb.php";
		}
		if(hf_where.equals("2")){
			url_pl=Welcome.URL + "/dasidog/ky_pl_fb.php";
		}
		this.fb_edt = (EditText) findViewById(R.id.message_pl);
		this.fb_edt.setHint("回复"+pl_name);
		this.fb_btn = (TextView) findViewById(R.id.fb_btn);
		this.back=(ImageView) findViewById(R.id.btnBack_message_fb);
		this.back.setOnClickListener(new backOnClick());
		this.fb_btn.setOnClickListener(new fbOnClick());
	}
	private class backOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			Message_pl.this.finish();
		}
		
	}
	private class fbOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Message_pl.this.pl_nr = Message_pl.this.fb_edt.getText().toString();
			if (Message_pl.this.pl_nr.equals("")) {
				Toast.makeText(Message_pl.this, "请输入内容", Toast.LENGTH_SHORT)
						.show();
			} else {
				new Mgs_pl().execute();
			}

		}

	}
	private class Mgs_pl extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = CustomProgressDialog.createDialog(Message_pl.this);
			pDialog.setMessage("评论中...");
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("from", String.valueOf(user_id)));
			params.add(new BasicNameValuePair("to", hf_to));
			params.add(new BasicNameValuePair("txt_id", hf_where_id));
			params.add(new BasicNameValuePair("pl_nr", pl_nr));
			params.add(new BasicNameValuePair("pl_for",hf_for));
			params.add(new BasicNameValuePair("type", "1"));
			JSONObject json = jsonParser
					.makeHttpRequest(url_pl, "POST", params,Message_pl.this);
			Log.d("Create Response", json.toString());
			try {
				Message_pl.this.success = json.getInt("success");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done

			if (Message_pl.this.success == 1) {
				Toast.makeText(Message_pl.this, "评论成功", Toast.LENGTH_SHORT)
						.show();

			}
			if (Message_pl.this.success == 2) {
				Toast.makeText(Message_pl.this, "评论失败，刷新试试？", Toast.LENGTH_SHORT)
						.show();
			}

			pDialog.dismiss();
		}

	}

}
