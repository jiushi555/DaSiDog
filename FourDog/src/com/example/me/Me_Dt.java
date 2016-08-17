package com.example.me;

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
import com.example.Gz.GzArticle;
import com.example.ProgressDialog.CustomProgressDialog;
import com.example.Ky.KyArticle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class Me_Dt extends Activity {
	private int user_id;
	// 加载显示项目
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog progressDialog = null;
	private int success;
	private int begin;
	private TextView Me_Dt_ce;
	private int currentPage = 1;
	private int linesize = 15;
	private int lastItem;
	private String url_show = Welcome.URL + "/dasidog/my_info.php";
	private String url_delect = Welcome.URL + "/dasidog/delect_dt.php";
	private JSONArray data = null;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private SimpleAdapter simpleAdapter = null;
	private ListView datalist;
	private DtAdapter mgs;
	private int type = 0;
	// 加载更多显示卡
	private TextView loadinfo = null;
	private LinearLayout loadLayout = null;
	private LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT, 100);

	// 点击跳转
	private String tz_date;
	private String tz_name;
	private String tz_nr;
	private String tz_fb_where;
	private String tz_plnum;
	private String tz_id;
	private String tz_where_id;
	private String tz_tx;
	private String tz_path;
	private String tz_now;
	private String tz_pathX;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_dt);
		Intent it = super.getIntent();
		user_id = it.getIntExtra("user_id", 0);
		// 加载显示项目
		datalist = (ListView) this.findViewById(R.id.dt_list);
		// 加载更多显示卡
		this.loadLayout = new LinearLayout(this);
		this.loadinfo = new TextView(this);
		this.loadinfo.setText("加载更多中..");
		this.loadinfo.setTextColor(Me_Dt.this.getResources().getColor(
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
		new Dt().execute();
		mgs = new DtAdapter(list, R.layout.me_dt_item, Me_Dt.this, user_id);
	}

	private void appendData() {
		Me_Dt.this.begin = (Me_Dt.this.currentPage - 1) * Me_Dt.this.linesize;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(user_id)));
		params.add(new BasicNameValuePair("type", String.valueOf(type)));
		JSONObject json = jsonParser.makeHttpRequest(url_show, "POST", params,Me_Dt.this);
		Log.d("Create Response", json.toString());
		try {
			Me_Dt.this.success = json.getInt("success");
			if (success == 1) {
				data = json.getJSONArray("me_dt");
				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					int id = c.getInt("id");
					String article = c.getString("nr");
					int pl_num = c.getInt("pl_num");
					String date = c.getString("date");
					String username = c.getString("writer_name");
					String fb_where = c.getString("fb_where");
					String where_id = c.getString("where_id");
					String user_tx = c.getString("user_tx");
					String path = c.getString("path");
					// String path=c.getString("tx");
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", String.valueOf(id));
					map.put("article", article);
					map.put("pl_num", String.valueOf(pl_num));
					map.put("date", date);
					map.put("username", username);
					map.put("fb_where", fb_where);
					map.put("where_id", where_id);
					map.put("user_tx", user_tx);
					map.put("path", path);
					Me_Dt.this.list.add(map);
					Me_Dt.this.mgs.notifyDataSetChanged();
				}
			} else {
				Me_Dt.this.loadinfo.setText("没有更多了~");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class Dt extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = CustomProgressDialog.createDialog(Me_Dt.this);
			progressDialog.setMessage("加载中...");
			Me_Dt.this.progressDialog.show();

		}

		protected String doInBackground(String... args) {
			Me_Dt.this.begin = (Me_Dt.this.currentPage - 1)
					* Me_Dt.this.linesize;

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			params.add(new BasicNameValuePair("user_id", String
					.valueOf(user_id)));
			params.add(new BasicNameValuePair("type", String.valueOf(type)));
			JSONObject json = jsonParser.makeHttpRequest(url_show, "POST",
					params,Me_Dt.this);
			Log.d("Create Response", json.toString());
			try {
				Me_Dt.this.success = json.getInt("success");
				if (success == 1) {

					data = json.getJSONArray("me_dt");

					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						String article = c.getString("nr");
						int pl_num = c.getInt("pl_num");
						String date = c.getString("date");
						String username = c.getString("writer_name");
						String fb_where = c.getString("fb_where");
						String where_id = c.getString("where_id");
						String user_tx = c.getString("user_tx");
						String path = c.getString("path");
						String now=c.getString("now");
						String pathX=c.getString("pathX");
						// String path = c.getString("tx");
						map.put("id", String.valueOf(id));
						map.put("article", article);
						map.put("pl_num", String.valueOf(pl_num));
						map.put("date", date);
						map.put("username", username);
						map.put("fb_where", fb_where);
						map.put("where_id", where_id);
						map.put("user_tx", user_tx);
						map.put("path", path);
						map.put("now", now);
						map.put("pathX", pathX);
						Log.d("pathXX", pathX);
						// adding HashList to ArrayList
						Me_Dt.this.list.add(map);
					}
				} else {
					Me_Dt.this.loadinfo.setText("没有更多了~");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			if (success == 1) {
				runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed JSON data into ListView
						 * */
						/*
						 * String[][] data_ = new String[][] { { "01", "我是一" },
						 * { "02", "我是二" }, { "03", "我是三" } }; List<Map<String,
						 * String>> list = new ArrayList<Map<String, String>>();
						 * SimpleAdapter sim = null; for (int x = 0; x <
						 * data_.length; x++) { Map<String, String> map_ = new
						 * HashMap<String, String>(); map_.put("_id",
						 * data_[x][0]); map_.put("name", data_[x][1]);
						 * Me_Dt.this.list.add(map_); } sim = new
						 * SimpleAdapter(Me_Dt.this, Me_Dt.this.list,
						 * R.layout.Me_Dt_list_c, new String[] { "_id", "name"
						 * }, new int[] { R.id.title_Me_Dt, R.id.nr_Me_Dt });
						 */

						Me_Dt.this.datalist
								.addFooterView(Me_Dt.this.loadLayout);

						Me_Dt.this.datalist
								.setOnScrollListener(new OnScrollListenerlmpl());
						// Me_Dt.this.Me_Dtlayout.addView(Me_Dt.this.datalist);

						Me_Dt.this.datalist.setAdapter(Me_Dt.this.mgs);
						Me_Dt.this.datalist
								.setOnItemClickListener(new listOnItemListener());
						Me_Dt.this.datalist
								.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

									@Override
									public void onCreateContextMenu(
											ContextMenu menu, View v,
											ContextMenuInfo menuInfo) {
										menu.setHeaderTitle("请选择操作");
										menu.add(Menu.NONE, 1, Menu.NONE, "删除");
									}
								});
						Me_Dt.this.datalist
								.setOnItemLongClickListener(new listLongOnClick());
					}
				});
			} else if (success == 2) {
				Me_Dt.this.setContentView(R.layout.no_item);
			}

			Me_Dt.this.progressDialog.dismiss();
		}
	}

	private class listLongOnClick implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			Me_Dt.this.tz_id = mgs.getTxtId(arg2, arg1, datalist);
			Me_Dt.this.tz_fb_where = mgs.getWhere(arg2, arg1, datalist);
			Me_Dt.this.tz_where_id = mgs.getWhereId(arg2, arg1, datalist);
			Me_Dt.this.tz_tx = mgs.getTxUrl(arg2, arg1, datalist);
			Me_Dt.this.tz_path = mgs.getImgUrl(arg2, arg1, datalist);
			// arg1.showContextMenu();
			System.out.println(arg2);
			return false;
		}

	}

	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			new DtDelect().execute();
		}
		return false;
	}

	class DtDelect extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = CustomProgressDialog.createDialog(Me_Dt.this);
			progressDialog.setMessage("删除中...");
			Me_Dt.this.progressDialog.show();

		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("text_id", Me_Dt.this.tz_id));
			params.add(new BasicNameValuePair("fb_where",
					Me_Dt.this.tz_fb_where));
			params.add(new BasicNameValuePair("where_id",
					Me_Dt.this.tz_where_id));
			JSONObject json = jsonParser.makeHttpRequest(url_delect, "POST",
					params,Me_Dt.this);
			Log.d("Create Response", json.toString());
			try {
				Me_Dt.this.success = json.getInt("success");

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			if (success == 1) {
				Toast.makeText(Me_Dt.this, "删除成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Me_Dt.this, "删除失败", Toast.LENGTH_SHORT).show();
			}
			refresh();
			Me_Dt.this.progressDialog.dismiss();
		}

	}
	private void refresh(){
		Me_Dt.this.finish();
		Intent it=new Intent(Me_Dt.this,Me_Main.class);
		it.putExtra("user_id", user_id);
		it.putExtra("re_type", "yes");
		it.putExtra("page", "0");
		Me_Dt.this.startActivity(it);
	} 
	private class listOnItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Me_Dt.this.tz_id = mgs.getWhereId(arg2, arg1, datalist);
			Me_Dt.this.tz_name = mgs.getName(arg2, arg1, datalist);
			Me_Dt.this.tz_nr = mgs.getText(arg2, arg1, datalist);
			Me_Dt.this.tz_plnum = mgs.getPlnum(arg2, arg1, datalist);
			Me_Dt.this.tz_date = mgs.getDate(arg2, arg1, datalist);
			Me_Dt.this.tz_fb_where = mgs.getWhere(arg2, arg1, datalist);
			Me_Dt.this.tz_tx = mgs.getTxUrl(arg2, arg1, datalist);
			Me_Dt.this.tz_path = mgs.getImgUrl(arg2, arg1, datalist);
			Me_Dt.this.tz_now=mgs.getNow(arg2, arg1, datalist);
			Me_Dt.this.tz_pathX=mgs.getImgUrlX(arg2, arg1, datalist);
			Intent tz_it = null;
			if (Me_Dt.this.tz_fb_where.equals("1")) {
				tz_it = new Intent(Me_Dt.this, GzArticle.class);
			} else {
				tz_it = new Intent(Me_Dt.this, KyArticle.class);
			}
			tz_it.putExtra("text_id", tz_id);
			tz_it.putExtra("writer_id", String.valueOf(user_id));
			tz_it.putExtra("pl_num", tz_plnum);
			tz_it.putExtra("user_id", user_id);
			tz_it.putExtra("writer_name", tz_name);
			tz_it.putExtra("nr", tz_nr);
			tz_it.putExtra("date", tz_date);
			tz_it.putExtra("user_tx", tz_tx);
			tz_it.putExtra("path", tz_path);
			tz_it.putExtra("now", tz_now);
			tz_it.putExtra("pathX", tz_pathX);
			Me_Dt.this.startActivity(tz_it);

		}

	}

	private class OnScrollListenerlmpl implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (lastItem == mgs.getCount()
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				currentPage++;
				Me_Dt.this.datalist.setSelection(lastItem);
				Me_Dt.this.appendData();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			lastItem = firstVisibleItem + visibleItemCount - 1;
		}

	}
}