package com.example.Sy;

import java.io.Writer;
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
import com.example.Gz.Gz;
import com.example.ProgressDialog.CustomProgressDialog;
import com.example.add.Add_Gz;
import com.example.add.Add_Sy;
import com.example.set.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("NewApi")
public class Sy extends Activity {
	private ImageView img_set;
	private int user_id;
	private ImageView writer;
	private static boolean isExit = false;
	private TextView version_notive;
	private String re_type="no";
	// 加载显示项目
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog progressDialog = null;
	private int success;
	private int begin;
	private TextView sy_ce;
	private int currentPage = 1;
	private int linesize = 15;
	private int lastItem;
	private String url_show = Welcome.URL + "/dasidog/sy_nr.php";
	private JSONArray data = null;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private SimpleAdapter simpleAdapter = null;
	private ListView datalist;
	private SyAdapter mgs;
	// 加载更多显示卡
	private TextView loadinfo = null;
	private LinearLayout loadLayout = null;
	private LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT, 100);

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sy);

		Intent it_super = super.getIntent();
		user_id = it_super.getIntExtra("user_id", 0);
		if(it_super.getStringExtra("re_type")!=null){
			re_type="yes";
		}
		img_set = (ImageView) findViewById(R.id.img_set);

		img_set.setOnClickListener(new setOnClick());
		writer = (ImageView) findViewById(R.id.writer);
		writer.setOnClickListener(new writerOnClick());
		version_notive=(TextView) findViewById(R.id.set_notive);
		// 加载显示项目
		datalist = (ListView) this.findViewById(R.id.sy_listview);
		// 加载更多显示卡
		this.loadLayout = new LinearLayout(this);
		this.loadinfo = new TextView(this);
		this.loadinfo.setText("加载更多中..");
		this.loadinfo.setTextColor(Sy.this.getResources().getColor(
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
			new Sy_().execute();
		}else{
			new SyRefresh().execute();
		}
		
		mgs = new SyAdapter(list, R.layout.sy_list_c, Sy.this, user_id);
	}

	private void appendData() {
		Sy.this.begin = (Sy.this.currentPage - 1) * Sy.this.linesize;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
		JSONObject json = jsonParser.makeHttpRequest(url_show, "POST", params,Sy.this);
		Log.d("Create Response", json.toString());
		try {
			Sy.this.success = json.getInt("success");
			if (success == 1) {
				data = json.getJSONArray("sy");
				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					int id = c.getInt("id");
					int writer_id = c.getInt("writer_id");
					String article = c.getString("article");
					int pl_num = c.getInt("pl_num");
					String date = c.getString("fb_date");
					String username = c.getString("writer_name");
					int f_where = c.getInt("fb_where");
					int where_id = c.getInt("where_id");
					String now=c.getString("now");
					String tx=c.getString("user_tx");
					String path=c.getString("path");
					String pathX=c.getString("pathX");
					// String path=c.getString("tx");
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", String.valueOf(id));
					map.put("writer_id", String.valueOf(writer_id));
					map.put("article", article);
					map.put("pl_num", String.valueOf(pl_num));
					map.put("date", date);
					map.put("now", now);
					map.put("username", username);
					map.put("fb_where", String.valueOf(f_where));
					map.put("where_id", String.valueOf(where_id));
					map.put("user_tx", tx);
					map.put("path", path);
					map.put("pathX", pathX);
					Sy.this.list.add(map);
					Sy.this.mgs.notifyDataSetChanged();
				}
			} else {
				Sy.this.loadinfo.setText("没有更多了~");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	class SyRefresh extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		protected String doInBackground(String... args) {
			Sy.this.begin = (Sy.this.currentPage - 1) * Sy.this.linesize;

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			JSONObject json = jsonParser.makeHttpRequest(url_show, "POST",
					params,Sy.this);
			Log.d("Create Response", json.toString());
			try {
				Sy.this.success = json.getInt("success");
				Welcome.new_version=json.getString("version");
				Welcome.new_url=json.getString("url");
				if (success == 1) {

					data = json.getJSONArray("sy");

					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						int writer_id = c.getInt("writer_id");
						String article = c.getString("article");
						int pl_num = c.getInt("pl_num");
						String date = c.getString("fb_date");
						String username = c.getString("writer_name");
						// String path = c.getString("tx");
						int f_where = c.getInt("fb_where");
						int where_id = c.getInt("where_id");
						String now=c.getString("now");
						String tx=c.getString("user_tx");
						String path=c.getString("path");
						String pathX=c.getString("pathX");
						
						map.put("id", String.valueOf(id));
						map.put("writer_id", String.valueOf(writer_id));
						map.put("article", article);
						map.put("pl_num", String.valueOf(pl_num));
						map.put("date", date);
						map.put("now", now);
						map.put("username", username);
						map.put("fb_where", String.valueOf(f_where));
						map.put("where_id", String.valueOf(where_id));
						map.put("user_tx", tx);
						map.put("path", path);
						map.put("pathX", pathX);
						// adding HashList to ArrayList
						Sy.this.list.add(map);
					}
				} else {
					Sy.this.loadinfo.setText("没有更多了~");
				}
			} catch (JSONException e) {
				Toast.makeText(Sy.this, "加载失败，请检查一下网络",Toast.LENGTH_LONG).show();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			if(Sy.this.success==1){
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
						 * map_.put("name", data_[x][1]); Sy.this.list.add(map_); }
						 * sim = new SimpleAdapter(Sy.this, Sy.this.list,
						 * R.layout.sy_list_c, new String[] { "_id", "name" }, new
						 * int[] { R.id.title_sy, R.id.nr_sy });
						 */
						Sy.this.simpleAdapter = new SimpleAdapter(Sy.this,
								Sy.this.list, R.layout.sy_list, new String[] {
										"id", "writer_id", "article", "username",
										"date", "pl_num" }, new int[] {
										R.id.sy_txt_id, R.id.sy_writer_id,
										R.id.sy_nr, R.id.sy_writer_username,
										R.id.sy_date, R.id.sy_pl_num_ });

						// updating listview
						Sy.this.datalist.addFooterView(Sy.this.loadLayout,null, false);

						Sy.this.datalist
								.setOnScrollListener(new OnScrollListenerlmpl());
						// Sy.this.Sylayout.addView(Sy.this.datalist);
						Sy.this.datalist
								.setOnItemClickListener(new SyOnItemClickListener());
						Sy.this.datalist
								.setOnItemLongClickListener(new SyLongClick());
						Sy.this.datalist.setAdapter(mgs);
						progressDialog.dismiss();
					}
				});
				if(Welcome.new_version.equals(String.valueOf(Welcome.version_id))){
					Welcome.version_boolean=false;
					Sy.this.version_notive.setVisibility(View.GONE);

				}else{
					Welcome.version_boolean=true;
					Sy.this.version_notive.setVisibility(View.VISIBLE);
				}
				System.out.println(Welcome.new_version);
				System.out.println(Welcome.version);
				System.out.println(Welcome.version);

				System.out.println(Welcome.version_boolean);
			}
			
			
		}
	}
	class Sy_ extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = CustomProgressDialog.createDialog(Sy.this);
			progressDialog.setMessage("加载中...");
			Sy.this.progressDialog.show();
		}

		protected String doInBackground(String... args) {
			Sy.this.begin = (Sy.this.currentPage - 1) * Sy.this.linesize;

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			JSONObject json = jsonParser.makeHttpRequest(url_show, "POST",
					params,Sy.this);
			Log.d("Create Response", json.toString());
			try {
				Sy.this.success = json.getInt("success");
				Welcome.new_version=json.getString("version");
				Welcome.new_url=json.getString("url");
				if (success == 1) {

					data = json.getJSONArray("sy");

					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						int writer_id = c.getInt("writer_id");
						String article = c.getString("article");
						int pl_num = c.getInt("pl_num");
						String date = c.getString("fb_date");
						String username = c.getString("writer_name");
						// String path = c.getString("tx");
						int f_where = c.getInt("fb_where");
						int where_id = c.getInt("where_id");
						String now=c.getString("now");
						String tx=c.getString("user_tx");
						String path=c.getString("path");
						String pathX=c.getString("pathX");
						
						map.put("id", String.valueOf(id));
						map.put("writer_id", String.valueOf(writer_id));
						map.put("article", article);
						map.put("pl_num", String.valueOf(pl_num));
						map.put("date", date);
						map.put("now", now);
						map.put("username", username);
						map.put("fb_where", String.valueOf(f_where));
						map.put("where_id", String.valueOf(where_id));
						map.put("user_tx", tx);
						map.put("path", path);
						map.put("pathX", pathX);
						// adding HashList to ArrayList
						Sy.this.list.add(map);
					}
				} else {
					Sy.this.loadinfo.setText("没有更多了~");
				}
			} catch (JSONException e) {
				Toast.makeText(Sy.this, "加载失败，请检查一下网络",Toast.LENGTH_LONG).show();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			if(Sy.this.success==1){
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
						 * map_.put("name", data_[x][1]); Sy.this.list.add(map_); }
						 * sim = new SimpleAdapter(Sy.this, Sy.this.list,
						 * R.layout.sy_list_c, new String[] { "_id", "name" }, new
						 * int[] { R.id.title_sy, R.id.nr_sy });
						 */
						Sy.this.simpleAdapter = new SimpleAdapter(Sy.this,
								Sy.this.list, R.layout.sy_list, new String[] {
										"id", "writer_id", "article", "username",
										"date", "pl_num" }, new int[] {
										R.id.sy_txt_id, R.id.sy_writer_id,
										R.id.sy_nr, R.id.sy_writer_username,
										R.id.sy_date, R.id.sy_pl_num_ });

						// updating listview
						Sy.this.datalist.addFooterView(Sy.this.loadLayout,null,false);

						Sy.this.datalist
								.setOnScrollListener(new OnScrollListenerlmpl());
						// Sy.this.Sylayout.addView(Sy.this.datalist);
						Sy.this.datalist
								.setOnItemClickListener(new SyOnItemClickListener());
						Sy.this.datalist
								.setOnItemLongClickListener(new SyLongClick());
						Sy.this.datalist.setAdapter(mgs);
						progressDialog.dismiss();
					}
				});
				if(Welcome.new_version.equals(String.valueOf(Welcome.version_id))){
					Welcome.version_boolean=false;
				}else{
					Welcome.version_boolean=true;
					Sy.this.version_notive.setVisibility(View.VISIBLE);
				}
				System.out.println(Welcome.new_version);
				System.out.println(Welcome.version);
				System.out.println(Welcome.version_boolean);
			}else{
				progressDialog.dismiss();
				Toast.makeText(Sy.this, "加载失败，请检查一下网络",Toast.LENGTH_LONG).show();
			}
			
			
		}
	}

	private class SyOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			mgs.getView(arg2, arg1, datalist);

		}

	}

	private class SyLongClick implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			/*
			 * // TODO Auto-generated method stub nr=mgs.getText(arg2, arg1,
			 * datalist); jb_text_id=mgs.getTextId(arg2, arg1, datalist);
			 * jb_writer_id=mgs.getWriterId(arg2, arg1,datalist);
			 */
			return false;
		}

	}

	private class OnScrollListenerlmpl implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (lastItem == mgs.getCount()
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				currentPage++;
				Sy.this.datalist.setSelection(lastItem);
				Sy.this.appendData();
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
			Intent it_writer = new Intent(Sy.this, Add_Sy.class);
			it_writer.putExtra("user_id", user_id);
			Sy.this.startActivity(it_writer);
		}

	}

	class setOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent it = new Intent(Sy.this, Set.class);
			it.putExtra("user_id", user_id);
			Sy.this.startActivity(it);
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
