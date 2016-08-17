package com.example.theme;

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
import com.example.Sy.Sy;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class ThemeContext extends Activity {
	private String id, title, context;
	private int user_id;
	private ListView contextList;
	private SimpleAdapter simpleAdapter = null;
	private ImageView addTheme,back;
	// 加载显示项目
	private CustomProgressDialog dialog = null;
	private int begin = 0, ItemLong = 15, lastItem, success, currentPage = 1;
	private JSONParser jsonParser = new JSONParser();
	private String url = Welcome.URL + "/dasidog/theme_nr.php", type = "2",load_type="1";
	private JSONArray data = null;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ThemeContentAdapter mgs = null;
	private TextView theme_title, theme_content;
	private Button btn_add;
	private LinearLayout mLinearLayout;
	// 加载更多项目
	private TextView loadinfo;
	private LinearLayout loadLayout;
	private LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT, 100);

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.themecontext);
		Intent it = super.getIntent();
		id = it.getStringExtra("id");
		title = it.getStringExtra("title");
		context = it.getStringExtra("nr");
		user_id = it.getIntExtra("user_id", 0);
		if(it.getStringExtra("load_type")!=null){
			load_type=it.getStringExtra("load_type");
			System.out.print(load_type);
		}
		addTheme = (ImageView) findViewById(R.id.add_theme);
		back=(ImageView) findViewById(R.id.theme_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ThemeContext.this.finish();
			}
		});
		contextList = (ListView) this.findViewById(R.id.themeList);
		addTheme.setOnClickListener(new AddOnClick());
		LayoutInflater inflater = getLayoutInflater();
		View headerView = inflater.inflate(
				R.layout.bgz_job_companylist_title_layout, null);
		// super.setContentView(headerView);
		theme_title = (TextView) headerView.findViewById(R.id.t_theme_title);
		theme_content = (TextView) headerView.findViewById(R.id.t_theme_nr);
		mLinearLayout=(LinearLayout) headerView.findViewById(R.id.title_layout);
		contextList.addHeaderView(headerView);
		theme_title.setText("#" + title + "#");
		theme_content.setText(context);
		btn_add=(Button) headerView.findViewById(R.id.public_btn);
		btn_add.setOnClickListener(new AddOnClick());
		// 加载更多
		this.loadLayout = new LinearLayout(this);
		this.loadinfo = new TextView(this);
		this.loadinfo.setText("加载更多中..");
		this.loadinfo.setTextColor(ThemeContext.this.getResources().getColor(
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
		initTitleBg(id);
		if(load_type.equals("1")){
			new ThemeLoad().execute();
		}else if(load_type.equals("2")){
			new RefreshLoad().execute();
		}
		mgs = new ThemeContentAdapter(list, R.layout.themecontentitem,
				ThemeContext.this, user_id);
	}
	private void initTitleBg(String id){
		int idInt=Integer.parseInt(id);
		switch(idInt){
		case 1:
			mLinearLayout.setBackgroundResource(R.drawable.theme_title_bg1);
			break;
		case 2:
			mLinearLayout.setBackgroundResource(R.drawable.theme_title_bg2);

			break;
		case 3:
			mLinearLayout.setBackgroundResource(R.drawable.theme_title_bg3);
			break;
		case 4:
			mLinearLayout.setBackgroundResource(R.drawable.theme_title_bg4);
			break;
		case 5:
			mLinearLayout.setBackgroundResource(R.drawable.theme_title_bg5);
			break;
		case 6:
			mLinearLayout.setBackgroundResource(R.drawable.theme_title_bg6);
			break;
		case 7:
			mLinearLayout.setBackgroundResource(R.drawable.theme_title_bg7);
			break;
		}
	}
	private class RefreshLoad extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ThemeContext.this.dialog = CustomProgressDialog
					.createDialog(ThemeContext.this);
			ThemeContext.this.dialog.setMessage("刷新中...");
			ThemeContext.this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			ThemeContext.this.begin = (ThemeContext.this.currentPage - 1)
					* ThemeContext.this.ItemLong;
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("user_id", String
					.valueOf(user_id)));
			params1.add(new BasicNameValuePair("theme_id", String.valueOf(id)));
			params1.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			params1.add(new BasicNameValuePair("type", type));
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params1,
					ThemeContext.this);
			try {
				success = json.getInt("success");
				if (success == 1) {
					data = json.getJSONArray("theme_content");
					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						String content = c.getString("content");
						String date = c.getString("date");
						String avatar_id = c.getString("avatar");
						String id = c.getString("id");
						String writer = c.getString("writer");
						map.put("content", content);
						map.put("date", date);
						map.put("avatar_id", avatar_id);
						map.put("id", id);
						map.put("writer", writer);
						ThemeContext.this.list.add(map);
					}
				} else {
					ThemeContext.this.loadinfo.setText("没有更多了~");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			runOnUiThread(new Runnable() {
				public void run() {
					ThemeContext.this.contextList.addFooterView(loadLayout);
					ThemeContext.this.contextList
							.setOnScrollListener(new ListOnScroll());
					ThemeContext.this.contextList.setAdapter(mgs);

				}
			});

			ThemeContext.this.dialog.dismiss();
		}
	}
	private class ThemeLoad extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ThemeContext.this.dialog = CustomProgressDialog
					.createDialog(ThemeContext.this);
			ThemeContext.this.dialog.setMessage("加载中...");
			ThemeContext.this.dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			ThemeContext.this.begin = (ThemeContext.this.currentPage - 1)
					* ThemeContext.this.ItemLong;
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("user_id", String
					.valueOf(user_id)));
			params1.add(new BasicNameValuePair("theme_id", String.valueOf(id)));
			params1.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			params1.add(new BasicNameValuePair("type", type));
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params1,
					ThemeContext.this);
			try {
				success = json.getInt("success");
				if (success == 1) {
					data = json.getJSONArray("theme_content");
					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						String content = c.getString("content");
						String date = c.getString("date");
						String avatar_id = c.getString("avatar");
						String id = c.getString("id");
						String writer = c.getString("writer");
						map.put("content", content);
						map.put("date", date);
						map.put("avatar_id", avatar_id);
						map.put("id", id);
						map.put("writer", writer);
						ThemeContext.this.list.add(map);
					}
				} else {
					ThemeContext.this.loadinfo.setText("没有更多了~");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			runOnUiThread(new Runnable() {
				public void run() {
					ThemeContext.this.contextList.addFooterView(loadLayout);
					ThemeContext.this.contextList
							.setOnScrollListener(new ListOnScroll());
					ThemeContext.this.contextList.setAdapter(mgs);

				}
			});

			ThemeContext.this.dialog.dismiss();
		}
	}

	private class ListOnScroll implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (lastItem == mgs.getCount()
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				currentPage++;
				ThemeContext.this.contextList.setSelection(lastItem);
				ThemeContext.this.appendData();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			lastItem = firstVisibleItem + visibleItemCount - 2;
		}

	}

	private void appendData() {
		ThemeContext.this.begin = (ThemeContext.this.currentPage - 1)
				* ThemeContext.this.ItemLong;
		List<NameValuePair> params1 = new ArrayList<NameValuePair>();
		params1.add(new BasicNameValuePair("user_id", String.valueOf(user_id)));
		params1.add(new BasicNameValuePair("theme_id", String.valueOf(id)));
		params1.add(new BasicNameValuePair("begin", String.valueOf(begin)));
		params1.add(new BasicNameValuePair("type", type));
		JSONObject json = jsonParser.makeHttpRequest(url, "POST", params1,
				ThemeContext.this);
		try {
			success = json.getInt("success");
			if (success == 1) {
				data = json.getJSONArray("theme_content");
				for (int i = 0; i < data.length(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					JSONObject c = data.getJSONObject(i);
					String content = c.getString("content");
					String date = c.getString("date");
					String avatar_id = c.getString("avatar");
					String id = c.getString("id");
					String writer = c.getString("writer");
					map.put("content", content);
					map.put("date", date);
					map.put("avatar_id", avatar_id);
					map.put("id", id);
					map.put("writer", writer);
					ThemeContext.this.list.add(map);
					ThemeContext.this.mgs.notifyDataSetChanged();
				}
			} else {
				ThemeContext.this.loadinfo.setText("没有更多了~");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class AddOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent it_add = new Intent(ThemeContext.this, AddTheme.class);
			it_add.putExtra("user_id", user_id);
			it_add.putExtra("theme_id", id);
			it_add.putExtra("title", title);
			it_add.putExtra("nr", context);
			ThemeContext.this.startActivity(it_add);
			ThemeContext.this.finish();
		}

	}

}
