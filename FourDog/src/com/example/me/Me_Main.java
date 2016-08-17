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


import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Me_Main extends Activity {
	private ImageView back;
	private LinearLayout my_dt;
	private LinearLayout my_cy;
	private int user_id;
	private Context context;
	private ViewPager viewpager = null;
	private LocalActivityManager manager = null;
	private TextView dtnum, dttxt, cynum, cytxt;
	private CustomProgressDialog progressDialog;
	private JSONArray data = null;
	private String url_jz = Welcome.URL + "/dasidog/me_jz.php";
	JSONParser jsonParser = new JSONParser();
	private int success;
	private String username;
	private String now;
	private String dt_num;
	private String cy_num;
	private TextView name_txt;
	private TextView now_txt;
	private RelativeLayout reset_me;
	private ImageView user_tx;
	private String user_tx_url;
	private String re_type="no";
	private int page;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_my_info);
		Intent it = super.getIntent();
		user_id = it.getIntExtra("user_id", 0);
		if(it.getStringExtra("re_type")!=null){
			re_type="yes";
		}
		if(it.getStringExtra("page")!=null){
			if(it.getStringExtra("page").equals("0")){
				page=0;
			}
			if(it.getStringExtra("page").equals("1")){
				page=1;
			}
		}
		context = Me_Main.this;
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		initTextView();
		initPagerViewer();
		new Init().execute();
	}

	private class Init extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = CustomProgressDialog.createDialog(Me_Main.this);
			progressDialog.setMessage("加载中...");
			Me_Main.this.progressDialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> params_ = new ArrayList<NameValuePair>();
			params_.add(new BasicNameValuePair("user_id", String
					.valueOf(user_id)));
			JSONObject json = jsonParser.makeHttpRequest(url_jz, "POST",
					params_,Me_Main.this);
			Log.d("Create Response", json.toString());
			try {
				Me_Main.this.success = json.getInt("success");
				if (success == 1) {

					data = json.getJSONArray("user");

					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						Me_Main.this.username = c.getString("username");
						Me_Main.this.now = c.getString("now");
						Me_Main.this.dt_num = c.getString("dt_num");
						Me_Main.this.cy_num = c.getString("cy_num");
						Me_Main.this.user_tx_url=c.getString("user_tx");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			if (success == 1) {
				Me_Main.this.name_txt.setText(Me_Main.this.username);
				Me_Main.this.dtnum.setText(Me_Main.this.dt_num);
				Me_Main.this.cynum.setText(Me_Main.this.cy_num);
				if(Me_Main.this.now.equals("1")){
					Me_Main.this.now_txt.setText("大四狗");
				}
				if(Me_Main.this.now.equals("2")){
					Me_Main.this.now_txt.setText("已工作");
				}
				if(Me_Main.this.user_tx_url.equals("no")){
					Me_Main.this.user_tx.setImageResource(R.drawable.ic_portrait);
				}else{
					String url=Welcome.URL + "/dasidog/tx/"+user_tx_url;
					Me_Main.this.user_tx.setTag(url);
					Welcome.mimageLoader.getImageByAsyncTask(user_tx, url,1);
				}
			} else  {
				Me_Main.this.name_txt.setText("--");
				Me_Main.this.dtnum.setText("-");
				Me_Main.this.cynum.setText("-");
			}

			Me_Main.this.progressDialog.dismiss();
		}

	}

	private void initTextView() {
		back = (ImageView) findViewById(R.id.btnBack);
		back.setOnClickListener(new backOnClickListener());
		my_dt = (LinearLayout) findViewById(R.id.llDynamic);
		my_cy = (LinearLayout) findViewById(R.id.llMyAttention);
		name_txt = (TextView) findViewById(R.id.tvName);
		dtnum=(TextView) findViewById(R.id.dtnum);
		cynum=(TextView) findViewById(R.id.cynum);
		now_txt = (TextView) findViewById(R.id.tvContent);
		my_dt.setOnClickListener(new MyOnClickListener(0));
		my_cy.setOnClickListener(new MyOnClickListener(1));
		dttxt = (TextView) findViewById(R.id.dttxt);
		cytxt = (TextView) findViewById(R.id.cytxt);
		reset_me=(RelativeLayout) findViewById(R.id.rlInfo);
		reset_me.setOnClickListener(new resetOnClick());
		user_tx=(ImageView) findViewById(R.id.me_tx);
	}

	private void initPagerViewer() {
		viewpager = (ViewPager) findViewById(R.id.me_viewpage);
		final ArrayList<View> list = new ArrayList<View>();
		Intent intent = new Intent(context, com.example.me.Me_Dt.class);
		intent.putExtra("user_id", user_id);
		list.add(getView("A", intent));
		Intent intent2 = new Intent(context, com.example.me.Me_Cy.class);
		intent2.putExtra("user_id", user_id);
		list.add(getView("B", intent2));
		viewpager.setAdapter(new MyPagerAdapter(list));
		viewpager.setCurrentItem(page);
		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	private class resetOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent reset_it=new Intent(Me_Main.this,Reset_Me.class);
			reset_it.putExtra("user_id", String.valueOf(user_id));
			reset_it.putExtra("user_tx", user_tx_url);
			reset_it.putExtra("username", username);
			reset_it.putExtra("now", now);
			Me_Main.this.startActivity(reset_it);
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	public class MyPagerAdapter extends PagerAdapter {
		List<View> list = new ArrayList<View>();

		public MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				dtnum.setTextColor(getResources().getColor(R.color.res_cor3));
				dttxt.setTextColor(getResources().getColor(R.color.res_cor3));
				cynum.setTextColor(getResources().getColor(R.color.text_title));
				cytxt.setTextColor(getResources().getColor(R.color.text_title));
				break;
			case 1:
				dtnum.setTextColor(getResources().getColor(R.color.text_title));
				dttxt.setTextColor(getResources().getColor(R.color.text_title));
				cynum.setTextColor(getResources().getColor(R.color.res_cor3));
				cytxt.setTextColor(getResources().getColor(R.color.res_cor3));
				break;

			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewpager.setCurrentItem(index);
		}
	};

	class backOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Me_Main.this.finish();
		}

	}
}
