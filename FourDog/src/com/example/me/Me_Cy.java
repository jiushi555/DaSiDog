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
import com.example.ProgressDialog.CustomProgressDialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;

public class Me_Cy extends Activity {
	private int user_id;
	// 加载显示项目
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog progressDialog = null;
	private int success;
	private int begin;
	private int currentPage = 1;
	private int linesize = 15;
	private int lastItem;
	private String url_show = Welcome.URL + "/dasidog/my_info_cy.php";
	private String url_delete = Welcome.URL + "/dasidog/delete_cy.php";
	private JSONArray data = null;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ListView datalist;
	private CyAdapter mgs;
	private int type = 1;
	// 加载更多显示卡
	private TextView loadinfo = null;
	private LinearLayout loadLayout = null;
	private LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT, 100);
	private String sc_id;
	private String sc_where;
	private String sc_where_id;
	private CustomProgressDialog dialog;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_cy);
		Intent it = super.getIntent();
		user_id = it.getIntExtra("user_id", 0);
		// 加载显示项目
		datalist = (ListView) this.findViewById(R.id.cy_list);
		// 加载更多显示卡
		this.loadLayout = new LinearLayout(this);
		this.loadinfo = new TextView(this);
		this.loadinfo.setText("加载更多中..");
		this.loadinfo.setTextColor(Me_Cy.this.getResources().getColor(
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
		new Cy().execute();
		mgs = new CyAdapter(list, R.layout.me_cy_item, Me_Cy.this, user_id);
	}

	private void appendData() {
		Me_Cy.this.begin = (Me_Cy.this.currentPage - 1) * Me_Cy.this.linesize;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
		params.add(new BasicNameValuePair("user_id", String.valueOf(user_id)));
		params.add(new BasicNameValuePair("type", String.valueOf(type)));
		JSONObject json = jsonParser.makeHttpRequest(url_show, "POST", params,Me_Cy.this);
		Log.d("Create Response", json.toString());
		try {
			Me_Cy.this.success = json.getInt("success");
			if (success == 1) {
				data = json.getJSONArray("cy");
				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					int id = c.getInt("id");
					String article = c.getString("nr");
					String date = c.getString("date");
					String pl_where = c.getString("pl_where");
					String for_nr = c.getString("for_nr");
					String from_name = c.getString("from_username");
					String to_name = c.getString("to_username");
					String where_id = c.getString("where_id");
					String user_tx=c.getString("from_tx");
					// String path=c.getString("tx");
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", String.valueOf(id));
					map.put("article", article);
					map.put("date", date);
					map.put("pl_where", pl_where);
					map.put("for_nr", for_nr);
					map.put("from_name", from_name);
					map.put("to_name", to_name);
					map.put("where_id", where_id);
					map.put("from_tx", user_tx);
					Me_Cy.this.list.add(map);
					Me_Cy.this.mgs.notifyDataSetChanged();
				}
			} else {
				Me_Cy.this.loadinfo.setText("没有更多了~");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class Cy extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			

		}

		protected String doInBackground(String... args) {
			Me_Cy.this.begin = (Me_Cy.this.currentPage - 1)
					* Me_Cy.this.linesize;

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			params.add(new BasicNameValuePair("user_id", String
					.valueOf(user_id)));
			params.add(new BasicNameValuePair("type", String.valueOf(type)));
			JSONObject json = jsonParser.makeHttpRequest(url_show, "POST",
					params,Me_Cy.this);
			Log.d("Create Response", json.toString());
			try {
				Me_Cy.this.success = json.getInt("success");
				if (success == 1) {

					data = json.getJSONArray("cy");

					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						String article = c.getString("nr");
						String date = c.getString("date");
						String pl_where = c.getString("pl_where");
						String for_nr = c.getString("for_nr");
						String from_name = c.getString("from_username");
						String to_name = c.getString("to_username");
						String where_id = c.getString("where_id");
						String user_tx=c.getString("from_tx");
						// String path = c.getString("tx");
						map.put("id", String.valueOf(id));
						map.put("article", article);
						map.put("date", date);
						map.put("pl_where", pl_where);
						map.put("from_name", from_name);
						map.put("to_name", to_name);
						map.put("for_nr", for_nr);
						map.put("where_id", where_id);
						map.put("from_tx", user_tx);
						// adding HashList to ArrayList
						Me_Cy.this.list.add(map);
					}
				} else {
					Me_Cy.this.loadinfo.setText("没有更多了~");
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
						 * Me_Cy.this.list.add(map_); } sim = new
						 * SimpleAdapter(Me_Cy.this, Me_Cy.this.list,
						 * R.layout.Me_Cy_list_c, new String[] { "_id", "name"
						 * }, new int[] { R.id.title_Me_Cy, R.id.nr_Me_Cy });
						 */

						Me_Cy.this.datalist
								.addFooterView(Me_Cy.this.loadLayout);

						Me_Cy.this.datalist
								.setOnScrollListener(new OnScrollListenerlmpl());
						// Me_Cy.this.Me_Cylayout.addView(Me_Cy.this.datalist);
						Me_Cy.this.datalist
								.setOnItemClickListener(new ListOnClick());
						Me_Cy.this.datalist
								.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

									@Override
									public void onCreateContextMenu(
											ContextMenu menu, View v,
											ContextMenuInfo menuInfo) {
										menu.setHeaderTitle("请选择操作：");
										menu.add(Menu.NONE, 1, Menu.NONE, "删除");
									}
								});
						Me_Cy.this.datalist.setAdapter(Me_Cy.this.mgs);
					}
				});
			} else if (success == 2) {
				Me_Cy.this.setContentView(R.layout.no_item);
			}

			
		}
	}

	private class ListOnClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Me_Cy.this.sc_id = mgs.getTextId(arg2, arg1, datalist);
			Me_Cy.this.sc_where = mgs.getWhere(arg2, arg1, datalist);
			Me_Cy.this.sc_where_id = mgs.getWhereId(arg2, arg1, datalist);
			arg1.showContextMenu();

		}

	}

	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			new CyDelete().execute();
		}
		return false;
	}

	private class CyDelete extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			dialog = CustomProgressDialog.createDialog(Me_Cy.this);
			dialog.setMessage("删除中...");
			Me_Cy.this.dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> params_ = new ArrayList<NameValuePair>();
			params_.add(new BasicNameValuePair("text_id", Me_Cy.this.sc_id));
			params_.add(new BasicNameValuePair("fb_where", Me_Cy.this.sc_where));
			params_.add(new BasicNameValuePair("where_id",
					Me_Cy.this.sc_where_id));
			JSONObject json = jsonParser.makeHttpRequest(url_delete, "POST",
					params_,Me_Cy.this);
			Log.d("Create Response", json.toString());
			try {
				Me_Cy.this.success = json.getInt("success");

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (Me_Cy.this.success == 1) {
				Toast.makeText(Me_Cy.this, "删除成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Me_Cy.this, "删除失败", Toast.LENGTH_SHORT).show();
			}
			refresh();
			dialog.dismiss();
		}

	}
	private void refresh(){
		Me_Cy.this.finish();
		Intent it=new Intent(Me_Cy.this,Me_Main.class);
		it.putExtra("user_id", user_id);
		it.putExtra("re_type", "yes");
		it.putExtra("page", "1");
		Me_Cy.this.startActivity(it);
	} 
	private class OnScrollListenerlmpl implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (lastItem == mgs.getCount()
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				currentPage++;
				Me_Cy.this.datalist.setSelection(lastItem);
				Me_Cy.this.appendData();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			lastItem = firstVisibleItem + visibleItemCount - 1;
		}

	}
}
