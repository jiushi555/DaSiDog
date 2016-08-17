package com.example.xx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.DasiDog.JSONParser;
import com.example.DasiDog.R;
import com.example.DasiDog.Welcome;
import com.example.ProgressDialog.CustomProgressDialog;
import com.example.set.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Xx extends Activity {
	private int user_id;
	private ImageView add;
	private RelativeLayout message_layout;
	private RelativeLayout notion_layout;
	private TextView version_native;
	// 加载显示项目
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog progressDialog = null;
	private int success;
	private String url_show = Welcome.URL + "/dasidog/Xx_jz.php";
	private JSONArray data = null;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ListView datalist;
	private XxAdapter mgs;
	private String message;
	private String notion;
	private TextView message_txt;
	private TextView notion_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xx);
		Intent it = super.getIntent();
		this.user_id = it.getIntExtra("user_id", 0);
		Log.e("aaaid", String.valueOf(this.user_id));
		this.message_layout = (RelativeLayout) findViewById(R.id.message);
		this.message_layout.setOnClickListener(new messageOnClick());
		this.notion_layout = (RelativeLayout) findViewById(R.id.notice);
		this.notion_layout.setOnClickListener(new notionOnClick());
		this.message_txt = (TextView) findViewById(R.id.messageCount);
		this.notion_txt = (TextView) findViewById(R.id.noticeCount);
		this.version_native=(TextView) findViewById(R.id.set_notive);
		if(Welcome.version_boolean){
			this.version_native.setVisibility(View.VISIBLE);
		}
		add = (ImageView) findViewById(R.id.xx_img_set);
		add.setOnClickListener(new setOnClick());
		new Xx_().execute();
	}
	class setOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent it = new Intent(Xx.this, Set.class);
			it.putExtra("user_id", user_id);
			Xx.this.startActivity(it);
		}

	}

	class Xx_ extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", String
					.valueOf(user_id)));
			JSONObject json = jsonParser.makeHttpRequest(url_show, "POST",
					params,Xx.this);
			Log.d("Create Response", json.toString());
			try {
				Xx.this.success = json.getInt("success");
				if (success == 1) {
					Xx.this.message = json.getString("message");
					Xx.this.notion = json.getString("notion");

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				public void run() {
					if (success == 1) {
						if (Xx.this.message.equals("0")) {
							Xx.this.message_txt.setVisibility(View.GONE);
						} else {
							Xx.this.message_txt.setText(Xx.this.message);
						}
						if (Xx.this.notion.equals("0")) {
							Xx.this.notion_txt.setVisibility(View.GONE);
						} else {
							Xx.this.notion_txt.setText(Xx.this.notion);
						}
					}
				}
			});

		}
	}

	private class messageOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent it_message = new Intent(Xx.this, Message.class);
			it_message.putExtra("user_id", Xx.this.user_id);
			it_message.putExtra("message", Xx.this.message);
			Xx.this.startActivity(it_message);
			Xx.this.message_txt.setVisibility(View.GONE);
		}

	}

	private class notionOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent it_notion = new Intent(Xx.this, Notion.class);
			it_notion.putExtra("user_id", Xx.this.user_id);
			it_notion.putExtra("notion", Xx.this.notion);
			Xx.this.startActivity(it_notion);
		}

	}
}
