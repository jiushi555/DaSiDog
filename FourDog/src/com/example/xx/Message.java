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
import com.example.Gz.GzArticle;
import com.example.Ky.KyArticle;
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
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class Message extends Activity {
	private int user_id;
	private String message;
	private ImageView back;
	// 加载显示项目
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog progressDialog = null;
	private int success;
	private String url_show = Welcome.URL + "/dasidog/Message_nr.php";
	private JSONArray data = null;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ListView datalist;
	private MgsAdapter mgs;
	private int begin;
	private int currentPage = 1;
	private int linesize = 5;
	private int lastItem;
	private String hf_to;
	private String hf_for;
	private String hf_where;
	private String hf_where_id;
	private String pl_name;
	private String for_name;
	private String for_date;
	private String for_num;
	private String for_writer;
	private String for_nr;
	private String for_tx;
	private String for_path;
	private String for_pathX;
	private String now;
	// 加载更多显示卡
	private TextView loadinfo = null;
	private LinearLayout loadLayout = null;
	private LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT, 100);

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent it = super.getIntent();
		user_id = it.getIntExtra("user_id", 0);
		message = it.getStringExtra("message");
		Message.this.setContentView(R.layout.message);
		initView();

	}

	public void initView() {
		back = (ImageView) findViewById(R.id.btnBack_message);
		back.setOnClickListener(new backOnClick());
		this.datalist = (ListView) findViewById(R.id.msg_list);
		// 加载更多显示卡
		this.loadLayout = new LinearLayout(this);
		this.loadinfo = new TextView(this);
		this.loadinfo.setText("加载更多中..");
		this.loadinfo.setTextColor(Message.this.getResources().getColor(
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
		new Message_Zj().execute();
		mgs = new MgsAdapter(list, R.layout.message_item, Message.this, user_id);
	}

	private class Message_Zj extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Message.this.progressDialog = CustomProgressDialog
					.createDialog(Message.this);
			Message.this.progressDialog.setMessage("请稍等");
			Message.this.progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			Message.this.begin = (Message.this.currentPage - 1)
					* Message.this.linesize;
			List<NameValuePair> params_ = new ArrayList<NameValuePair>();
			params_.add(new BasicNameValuePair("user_id", String
					.valueOf(user_id)));
			params_.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			JSONObject json = jsonParser.makeHttpRequest(url_show, "POST",
					params_, Message.this);
			Log.d("result", json.toString());
			try {
				Message.this.success = json.getInt("success");
				if (Message.this.success == 2) {

					data = json.getJSONArray("message");
					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						String fpid = c.getString("fpid");
						String pl_from = c.getString("pl_from");
						String pl_date = c.getString("pl_date");
						String pl_nr = c.getString("pl_nr");
						String pl_where = c.getString("pl_where");
						String where_id = c.getString("where_id");
						String pl_type = c.getString("pl_type");
						String pl_for = c.getString("pl_for");
						String from_name = c.getString("from_name");
						String from_tx = c.getString("from_tx");
						String from_now=c.getString("from_now");
						String for_pl_nr = null;
						if (c.getString("for_pl_nr") != null) {
							for_pl_nr = c.getString("for_pl_nr");
						}

						String for_nr = c.getString("for_nr");
						String for_writer = c.getString("for_writer");
						String for_num = c.getString("for_num");
						String for_name = c.getString("for_name");
						String for_date = c.getString("for_date");
						String for_tx = c.getString("for_tx");
						String for_now=c.getString("for_now");
						String for_path = c.getString("path");
						String for_pathX = c.getString("pathX");
						map.put("fpid", fpid);
						map.put("pl_from", pl_from);
						map.put("pl_date", pl_date);
						map.put("pl_date", pl_date);
						map.put("pl_nr", pl_nr);
						map.put("pl_where", pl_where);
						map.put("where_id", where_id);
						map.put("pl_type", pl_type);
						map.put("pl_for", pl_for);
						map.put("from_name", from_name);
						map.put("for_pl_nr", for_pl_nr);

						map.put("for_nr", for_nr);
						map.put("for_writer", for_writer);
						map.put("for_num", for_num);
						map.put("for_name", for_name);
						map.put("for_date", for_date);
						map.put("from_tx", from_tx);
						map.put("for_tx", for_tx);
						map.put("for_path", for_path);
						map.put("for_pathX", for_pathX);
						map.put("for_now", for_now);
						map.put("from_now", from_now);
						Message.this.list.add(map);
					}
				} else {

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (Message.this.success == 2) {
				runOnUiThread(new Runnable() {
					public void run() {

						/*
						 * Message.this.datalist
						 * .addFooterView(Message.this.loadLayout);
						 */
						Message.this.datalist
								.addFooterView(Message.this.loadLayout,null,false);
						Message.this.datalist
								.setOnScrollListener(new OnScrollListenerlmpl());
						Message.this.datalist
								.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

									@Override
									public void onCreateContextMenu(
											ContextMenu menu, View v,
											ContextMenuInfo menuInfo) {

										menu.add(Menu.NONE, 1, Menu.NONE, "回复");
										menu.add(Menu.NONE, 2, Menu.NONE, "查看");
									}
								});
						Message.this.datalist
								.setOnItemClickListener(new MgsOnItemClickListener());
						Message.this.datalist.setAdapter(mgs);
					}
				});

			} else {
				Message.this.setContentView(R.layout.message_no);
				back = (ImageView) findViewById(R.id.btnBack_message);
				back.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Message.this.finish();
					}
				});
			}
			progressDialog.dismiss();
		}

	}

	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			Intent it_hf = new Intent(Message.this, Message_pl.class);
			it_hf.putExtra("pl_name", pl_name);
			it_hf.putExtra("hf_to", hf_to);
			it_hf.putExtra("hf_for", hf_for);
			it_hf.putExtra("hf_where", hf_where);
			it_hf.putExtra("hf_where_id", hf_where_id);
			it_hf.putExtra("user_id", user_id);

			Message.this.startActivity(it_hf);
		}
		if (item.getItemId() == 2) {
			Intent it_ck = null;
			if (hf_where.equals("1")) {
				it_ck = new Intent(Message.this, GzArticle.class);
			}
			if (hf_where.equals("2")) {
				it_ck = new Intent(Message.this, KyArticle.class);
			}
			it_ck.putExtra("user_id", user_id);
			it_ck.putExtra("writer_id", for_writer);
			it_ck.putExtra("writer_name", for_name);
			it_ck.putExtra("text_id", hf_where_id);
			it_ck.putExtra("pl_num", for_num);
			it_ck.putExtra("nr", for_nr);
			it_ck.putExtra("date", for_date);
			it_ck.putExtra("user_tx", for_tx);
			it_ck.putExtra("path", for_path);
			it_ck.putExtra("pathX", for_pathX);
			it_ck.putExtra("now", now);
			Message.this.startActivity(it_ck);
		}
		return false;
	}

	private class MgsOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			hf_to = mgs.getTo(arg2, arg1, datalist);
			hf_for = mgs.getFor(arg2, arg1, datalist);
			hf_where = mgs.getWhere(arg2, arg1, datalist);
			hf_where_id = mgs.getWhere_id(arg2, arg1, datalist);
			pl_name = mgs.getFormName(arg2, arg1, datalist);
			// 查看
			for_name = mgs.getForName(arg2, arg1, datalist);
			for_writer = mgs.getWriter(arg2, arg1, datalist);
			for_date = mgs.getDate(arg2, arg1, datalist);
			for_num = mgs.getNum(arg2, arg1, datalist);
			for_nr = mgs.getForNr(arg2, arg1, datalist);
			for_tx = mgs.getTxUrl(arg2, arg1, datalist);
			for_path = mgs.getImgUrl(arg2, arg1, datalist);
			for_pathX = mgs.getImgXUrl(arg2, arg1, datalist);
			now=mgs.getNow(arg2, arg1, datalist);
			arg1.showContextMenu();

		}

	}

	private class OnScrollListenerlmpl implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (lastItem == mgs.getCount()
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				currentPage++;
				Message.this.datalist.setSelection(lastItem);
				Message.this.appendData();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			lastItem = firstVisibleItem + visibleItemCount - 1;
		}

	}

	private void appendData() {
		Message.this.begin = (Message.this.currentPage - 1)
				* Message.this.linesize;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
		JSONObject json = jsonParser.makeHttpRequest(url_show, "POST", params,
				Message.this);
		Log.d("Create Response", json.toString());
		try {
			Message.this.success = json.getInt("success");
			if (success == 2) {
				data = json.getJSONArray("message");
				for (int i = 0; i < data.length(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					JSONObject c = data.getJSONObject(i);
					String fpid = c.getString("fpid");
					String pl_from = c.getString("pl_from");
					String pl_date = c.getString("pl_date");
					String pl_nr = c.getString("pl_nr");
					String pl_where = c.getString("pl_where");
					String where_id = c.getString("where_id");
					String pl_type = c.getString("pl_type");
					String pl_for = c.getString("pl_for");
					String from_name = c.getString("from_name");
					String from_tx = c.getString("from_tx");
					String from_now=c.getString("from_now");
					String for_pl_nr = c.getString("for_pl_nr");
					String for_now=c.getString("for_now");

					String for_nr = c.getString("for_nr");
					String for_writer = c.getString("for_writer");
					String for_num = c.getString("for_num");
					String for_name = c.getString("for_name");
					String for_date = c.getString("for_date");
					String for_tx = c.getString("for_tx");
					String for_path = c.getString("path");
					String for_pathX = c.getString("pathX");

					map.put("fpid", fpid);
					map.put("pl_from", pl_from);
					map.put("pl_date", pl_date);
					map.put("pl_date", pl_date);
					map.put("pl_nr", pl_nr);
					map.put("pl_where", pl_where);
					map.put("where_id", where_id);
					map.put("pl_type", pl_type);
					map.put("pl_for", pl_for);
					map.put("from_name", from_name);
					map.put("for_pl_nr", for_pl_nr);

					map.put("for_nr", for_nr);
					map.put("for_writer", for_writer);
					map.put("for_num", for_num);
					map.put("for_name", for_name);
					map.put("for_date", for_date);
					map.put("from_tx", from_tx);
					map.put("for_tx", for_tx);
					map.put("for_path", for_path);
					map.put("for_pathX", for_pathX);
					map.put("from_now", from_now);
					map.put("for_now", for_now);
					Message.this.list.add(map);
					Message.this.mgs.notifyDataSetChanged();
				}
			} else {
				Message.this.loadinfo.setText("没有更多了~");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private class backOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Message.this.finish();
		}

	}
}
