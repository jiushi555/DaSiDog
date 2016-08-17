package com.example.Ky;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.DasiDog.JSONParser;
import com.example.DasiDog.R;
import com.example.DasiDog.Welcome;
import com.example.Ky.Ky;
import com.example.Ky.KyAdapter;
import com.example.ProgressDialog.CustomProgressDialog;
import com.example.add.Add_Gz;
import com.example.add.Add_Ky;
import com.example.set.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;

public class Ky extends Activity {
	private ImageView add;
	private int user_id;
	private ImageView writer;
	private static boolean isExit = false;
	private TextView version_native;
	private String re_type="no";
	// 加载显示项目
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog progressDialog = null;
	private int success;
	private int begin;
	private int currentPage = 1;
	private int linesize = 15;
	private int lastItem;
	private String url_show = Welcome.URL + "/dasidog/ky_nr.php";
	private JSONArray data = null;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ListView datalist;
	private KyAdapter mgs;
	// 加载更多显示卡
	private TextView loadinfo = null;
	private LinearLayout loadLayout = null;
	private LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT, 100);

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ky);
		Intent super_it = super.getIntent();
		user_id = super_it.getIntExtra("user_id", 0);
		add = (ImageView) findViewById(R.id.img_set);
		writer = (ImageView) findViewById(R.id.ky_writer);
		writer.setOnClickListener(new writerOnClick());
		add.setOnClickListener(new setOnClick());
		version_native=(TextView) findViewById(R.id.set_notive);
		if(Welcome.version_boolean){
			version_native.setVisibility(View.VISIBLE);
		}
		if(super_it.getStringExtra("re_type")!=null){
			re_type="yes";
		}
		// 加载显示项目
		datalist = (ListView) this.findViewById(R.id.ky_listview);
		// 加载更多显示卡
		this.loadLayout = new LinearLayout(this);
		this.loadinfo = new TextView(this);
		this.loadinfo.setText("加载更多中..");
		this.loadinfo.setTextColor(Ky.this.getResources().getColor(
				R.color.loadtext_color));
		this.loadinfo.setGravity(Gravity.CENTER);
		this.loadinfo.setTextSize(20.0f);
		this.loadLayout.addView(loadinfo, this.layoutParams);
		this.loadLayout.setGravity(Gravity.CENTER);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		if(re_type.equals("no")){
			new Ky_().execute();
		}else{
			new Ky_Refresh().execute();
		}
		
		mgs = new KyAdapter(list, R.layout.sy_list, Ky.this, user_id);
	}
	class Ky_Refresh extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			Ky.this.begin = (Ky.this.currentPage - 1) * Ky.this.linesize;

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			JSONObject json = jsonParser.makeHttpRequest(url_show, "POST",
					params,Ky.this);
			Log.d("Create Response", json.toString());
			try {
				Ky.this.success = json.getInt("success");
				if (success == 1) {

					data = json.getJSONArray("ky");

					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						int writer_id = c.getInt("writer_id");
						String article = c.getString("article");
						int pl_num = c.getInt("pl_num");
						String date = c.getString("fb_date");
						String username = c.getString("writer_name");
						String now = c.getString("now");
						String user_tx=c.getString("user_tx");
						String path=c.getString("path");
						String pathX=c.getString("pathX");
						// String path = c.getString("tx");

						map.put("id", String.valueOf(id));
						map.put("writer_id", String.valueOf(writer_id));
						map.put("article", article);
						map.put("pl_num", String.valueOf(pl_num));
						map.put("date", date);
						map.put("username", username);
						map.put("now", now);
						map.put("user_tx", user_tx);
						map.put("path", path);
						map.put("pathX", pathX);
						// adding HashList to ArrayList
						Ky.this.list.add(map);
					}
				} else {
					Ky.this.loadinfo.setText("没有更多了~");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					/*
					 * String[][] data_ = new String[][] { { "01", "我是一" }, {
					 * "02", "我是二" }, { "03", "我是三" } }; List<Map<String,
					 * String>> list = new ArrayList<Map<String, String>>();
					 * SimpleAdapter sim = null; for (int x = 0; x <
					 * data_.length; x++) { Map<String, String> map_ = new
					 * HashMap<String, String>(); map_.put("_id", data_[x][0]);
					 * map_.put("name", data_[x][1]); Ky.this.list.add(map_); }
					 * sim = new SimpleAdapter(Ky.this, Ky.this.list,
					 * R.layout.Ky_list_c, new String[] { "_id", "name" }, new
					 * int[] { R.id.title_Ky, R.id.nr_Ky });
					 */

					// updating listview
					Ky.this.datalist.addFooterView(Ky.this.loadLayout,null,false);

					Ky.this.datalist
							.setOnScrollListener(new OnScrollListenerlmpl());
					// Ky.this.Kylayout.addView(Ky.this.datalist);

					Ky.this.datalist.setAdapter(Ky.this.mgs);
				}
			});

		}
	}
	private void appendData() {
		Ky.this.begin = (Ky.this.currentPage - 1) * Ky.this.linesize;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
		JSONObject json = jsonParser.makeHttpRequest(url_show, "POST", params,Ky.this);
		Log.d("Create Response", json.toString());
		try {
			Ky.this.success = json.getInt("success");
			if (success == 1) {
				data = json.getJSONArray("ky");
				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					int id = c.getInt("id");
					int writer_id = c.getInt("writer_id");
					String article = c.getString("article");
					int pl_num = c.getInt("pl_num");
					String date = c.getString("fb_date");
					String username = c.getString("writer_name");
					String now = c.getString("now");
					String user_tx=c.getString("user_tx");
					String path=c.getString("path");
					String pathX=c.getString("pathX");
					// String path=c.getString("tx");
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", String.valueOf(id));
					map.put("writer_id", String.valueOf(writer_id));
					map.put("article", article);
					map.put("pl_num", String.valueOf(pl_num));
					map.put("date", date);
					map.put("username", username);
					map.put("now", now);
					map.put("user_tx", user_tx);
					map.put("path", path);
					map.put("pathX", pathX);
					Ky.this.list.add(map);
					Ky.this.mgs.notifyDataSetChanged();
				}
			} else {
				Ky.this.loadinfo.setText("没有更多了~");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class Ky_ extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			Ky.this.begin = (Ky.this.currentPage - 1) * Ky.this.linesize;

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			JSONObject json = jsonParser.makeHttpRequest(url_show, "POST",
					params,Ky.this);
			Log.d("Create Response", json.toString());
			try {
				Ky.this.success = json.getInt("success");
				if (success == 1) {

					data = json.getJSONArray("ky");

					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						int writer_id = c.getInt("writer_id");
						String article = c.getString("article");
						int pl_num = c.getInt("pl_num");
						String date = c.getString("fb_date");
						String username = c.getString("writer_name");
						String now = c.getString("now");
						String user_tx=c.getString("user_tx");
						String path=c.getString("path");
						String pathX=c.getString("pathX");
						// String path = c.getString("tx");

						map.put("id", String.valueOf(id));
						map.put("writer_id", String.valueOf(writer_id));
						map.put("article", article);
						map.put("pl_num", String.valueOf(pl_num));
						map.put("date", date);
						map.put("username", username);
						map.put("now", now);
						map.put("user_tx", user_tx);
						map.put("path", path);
						map.put("pathX", pathX);
						// adding HashList to ArrayList
						Ky.this.list.add(map);
					}
				} else {
					Ky.this.loadinfo.setText("没有更多了~");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					/*
					 * String[][] data_ = new String[][] { { "01", "我是一" }, {
					 * "02", "我是二" }, { "03", "我是三" } }; List<Map<String,
					 * String>> list = new ArrayList<Map<String, String>>();
					 * SimpleAdapter sim = null; for (int x = 0; x <
					 * data_.length; x++) { Map<String, String> map_ = new
					 * HashMap<String, String>(); map_.put("_id", data_[x][0]);
					 * map_.put("name", data_[x][1]); Ky.this.list.add(map_); }
					 * sim = new SimpleAdapter(Ky.this, Ky.this.list,
					 * R.layout.Ky_list_c, new String[] { "_id", "name" }, new
					 * int[] { R.id.title_Ky, R.id.nr_Ky });
					 */

					// updating listview
					Ky.this.datalist.addFooterView(Ky.this.loadLayout,null,false);

					Ky.this.datalist
							.setOnScrollListener(new OnScrollListenerlmpl());
					// Ky.this.Kylayout.addView(Ky.this.datalist);

					Ky.this.datalist.setAdapter(Ky.this.mgs);
				}
			});

		}
	}

	private class OnScrollListenerlmpl implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (lastItem == mgs.getCount()
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				currentPage++;
				Ky.this.datalist.setSelection(lastItem);
				Ky.this.appendData();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			lastItem = firstVisibleItem + visibleItemCount - 1;
		}

	}

	class writerOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent it_add = new Intent(Ky.this, Add_Ky.class);
			it_add.putExtra("user_id", user_id);
			Ky.this.startActivity(it_add);

		}

	}

	class setOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent it = new Intent(Ky.this, Set.class);
			it.putExtra("user_id", user_id);
			Ky.this.startActivity(it);
		}

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); 
		}
		return false;
	}
	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, "再按就退出喽~", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; 
				}
			}, 1000);

		} else {

			android.os.Process.killProcess(android.os.Process.myPid()); 
			System.exit(0);
		}
	}
	
	
}
