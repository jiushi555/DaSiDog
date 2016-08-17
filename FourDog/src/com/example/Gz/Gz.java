package com.example.Gz;

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
import com.example.ProgressDialog.CustomProgressDialog;
import com.example.Sy.Sy;
import com.example.Gz.Gz;
import com.example.Gz.GzAdapter;

import com.example.add.Add_Gz;
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

public class Gz extends Activity {
	private ImageView add;
	private int user_id;
	private ImageView gz_writer;
	private static boolean isExit = false;
	private TextView version_native;
	private String re_type="no";
	// 加载显示项目
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog progressDialog = null;
	private int success;
	private int begin;
	private TextView Gz_ce;
	private int currentPage = 1;
	private int linesize = 15;
	private int lastItem;
	private String url_show = Welcome.URL + "/dasidog/gz_nr.php";
	private JSONArray data = null;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ListView datalist;
	private GzAdapter mgs;
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
		setContentView(R.layout.gz);
		Intent super_it = super.getIntent();
		user_id = super_it.getIntExtra("user_id", 0);
		if(super_it.getStringExtra("re_type")!=null){
			re_type="yes";
		}
		add = (ImageView) findViewById(R.id.img_set);
		gz_writer = (ImageView) findViewById(R.id.gz_writer);
		gz_writer.setOnClickListener(new writerOnClick());
		version_native=(TextView) findViewById(R.id.set_notive);
		if(Welcome.version_boolean){
			version_native.setVisibility(View.VISIBLE);
		}
		add.setOnClickListener(new setOnClick());
		// 加载显示项目
		datalist = (ListView) this.findViewById(R.id.gz_listview);
		// 加载更多显示卡
		this.loadLayout = new LinearLayout(this);
		this.loadinfo = new TextView(this);
		this.loadinfo.setText("加载更多中..");
		this.loadinfo.setTextColor(Gz.this.getResources().getColor(
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
			new Gz_().execute();
		}else{
			new Gz_Refresh().execute();
		}
		
		mgs = new GzAdapter(list, R.layout.sy_list, Gz.this, user_id);
	}

	private void appendData() {
		Gz.this.begin = (Gz.this.currentPage - 1) * Gz.this.linesize;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
		JSONObject json = jsonParser.makeHttpRequest(url_show, "POST", params,Gz.this);
		Log.d("Create Response", json.toString());
		try {
			Gz.this.success = json.getInt("success");
			if (success == 1) {
				data = json.getJSONArray("gz");
				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					int id = c.getInt("id");
					int writer_id = c.getInt("writer_id");
					String article = c.getString("article");
					int pl_num = c.getInt("pl_num");
					String date = c.getString("fb_date");
					String username = c.getString("writer_name");
					String now =c.getString("now");
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
					Gz.this.list.add(map);
					Gz.this.mgs.notifyDataSetChanged();
				}
			} else {
				Gz.this.loadinfo.setText("没有更多了~");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	class Gz_Refresh extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			Gz.this.begin = (Gz.this.currentPage - 1) * Gz.this.linesize;

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			JSONObject json = jsonParser.makeHttpRequest(url_show, "POST",
					params,Gz.this);
			Log.d("Create Response", json.toString());
			try {
				Gz.this.success = json.getInt("success");
				if (success == 1) {

					data = json.getJSONArray("gz");

					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						int writer_id = c.getInt("writer_id");
						String article = c.getString("article");
						int pl_num = c.getInt("pl_num");
						String date = c.getString("fb_date");
						String username = c.getString("writer_name");
						String now=c.getString("now");
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
						Gz.this.list.add(map);
					}
				} else {
					Gz.this.loadinfo.setText("没有更多了~");
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
					 * map_.put("name", data_[x][1]); Gz.this.list.add(map_); }
					 * sim = new SimpleAdapter(Gz.this, Gz.this.list,
					 * R.layout.Gz_list_c, new String[] { "_id", "name" }, new
					 * int[] { R.id.title_Gz, R.id.nr_Gz });
					 */

					// updating listview
					Gz.this.datalist.setVerticalScrollBarEnabled(true);
					Gz.this.datalist.addFooterView(Gz.this.loadLayout,null,false);

					Gz.this.datalist
							.setOnScrollListener(new OnScrollListenerlmpl());
					// Gz.this.Gzlayout.addView(Gz.this.datalist);

					Gz.this.datalist.setAdapter(Gz.this.mgs);
				}
			});

		}
	}
	class Gz_ extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			Gz.this.begin = (Gz.this.currentPage - 1) * Gz.this.linesize;

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			JSONObject json = jsonParser.makeHttpRequest(url_show, "POST",
					params,Gz.this);
			Log.d("Create Response", json.toString());
			try {
				Gz.this.success = json.getInt("success");
				if (success == 1) {

					data = json.getJSONArray("gz");

					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						int writer_id = c.getInt("writer_id");
						String article = c.getString("article");
						int pl_num = c.getInt("pl_num");
						String date = c.getString("fb_date");
						String username = c.getString("writer_name");
						String now=c.getString("now");
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
						Gz.this.list.add(map);
					}
				} else {
					Gz.this.loadinfo.setText("没有更多了~");
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
					 * map_.put("name", data_[x][1]); Gz.this.list.add(map_); }
					 * sim = new SimpleAdapter(Gz.this, Gz.this.list,
					 * R.layout.Gz_list_c, new String[] { "_id", "name" }, new
					 * int[] { R.id.title_Gz, R.id.nr_Gz });
					 */

					// updating listview
					Gz.this.datalist.setVerticalScrollBarEnabled(true);
					Gz.this.datalist.addFooterView(Gz.this.loadLayout,null,false);

					Gz.this.datalist
							.setOnScrollListener(new OnScrollListenerlmpl());
					// Gz.this.Gzlayout.addView(Gz.this.datalist);

					Gz.this.datalist.setAdapter(Gz.this.mgs);
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
				Gz.this.datalist.setSelection(lastItem);
				Gz.this.appendData();
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
			Intent add_gz = new Intent(Gz.this, Add_Gz.class);
			add_gz.putExtra("user_id", user_id);
			Gz.this.startActivity(add_gz);

		}

	}

	class setOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent it = new Intent(Gz.this, Set.class);
			it.putExtra("user_id", user_id);
			Gz.this.startActivity(it);
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
