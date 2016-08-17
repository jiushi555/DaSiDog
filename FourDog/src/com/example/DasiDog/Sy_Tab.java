package com.example.DasiDog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.DasiDog.R;
import com.example.ProgressDialog.CustomProgressDialog;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Sy_Tab extends Activity {

	Context context = null;

	LocalActivityManager manager = null;
	ViewPager pager = null;
	TextView t1, t2, t3, t4, t_theme;
	ImageView img1, img2, img3, img4, img_theme;
	RelativeLayout r1, r2, r3, r4, rtheme;
	private int user_id;
	private static boolean isExit = false;

	JSONParser jsonParser = new JSONParser();
	private int success;
	private String url_show = Welcome.URL + "/dasidog/select_xx.php";
	private JSONArray data = null;
	private String message;
	private String notion;
	private TextView xx_num;
	private String re_type = "no";
	private int page = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent it = super.getIntent();
		user_id = it.getIntExtra("user_id", 0);
		if (it.getStringExtra("re_type") != null) {
			re_type = "yes";
		}
		if (it.getStringExtra("page") != null) {
			if (it.getStringExtra("page").equals("0")) {
				page = 0;
			}
			if (it.getStringExtra("page").equals("1")) {
				page = 1;
			}
			if (it.getStringExtra("page").equals("2")) {
				page = 2;
			}
			if (it.getStringExtra("page").equals("3")) {
				page = 3;
			}
			if (it.getStringExtra("page").equals("4")) {
				page = 4;
			}
		}
		context = Sy_Tab.this;
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		new Xx_().execute();
		initTextView();
		initPagerViewer();

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
					params, Sy_Tab.this);
			Log.d("Create Response", json.toString());
			try {
				Sy_Tab.this.success = json.getInt("success");
				if (success == 1) {
					Sy_Tab.this.message = json.getString("message");
					Sy_Tab.this.notion = json.getString("notion");

				}
			} catch (JSONException e) {
				Toast.makeText(Sy_Tab.this, "网络不好，请检查网络", Toast.LENGTH_SHORT)
						.show();

			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				public void run() {
					if (success == 1) {
						if (Sy_Tab.this.message.equals("0")
								&& Sy_Tab.this.notion.equals("0")) {
							xx_num.setVisibility(View.GONE);
						} else {
							xx_num.setVisibility(View.VISIBLE);
						}
					}
				}
			});

		}
	}

	private void initTextView() {
		t1 = (TextView) findViewById(R.id.t1);
		t2 = (TextView) findViewById(R.id.t2);
		t3 = (TextView) findViewById(R.id.t3);
		t4 = (TextView) findViewById(R.id.t4);
		t_theme = (TextView) findViewById(R.id.t_theme);
		img1 = (ImageView) findViewById(R.id.ivHome);
		img2 = (ImageView) findViewById(R.id.ivgz);
		img3 = (ImageView) findViewById(R.id.ivky);
		img4 = (ImageView) findViewById(R.id.ivxx);
		img_theme = (ImageView) findViewById(R.id.ivtheme);
		r1 = (RelativeLayout) findViewById(R.id.btn_home);
		r2 = (RelativeLayout) findViewById(R.id.btn_gz);
		r3 = (RelativeLayout) findViewById(R.id.btn_ky);
		r4 = (RelativeLayout) findViewById(R.id.btn_xx);
		rtheme = (RelativeLayout) findViewById(R.id.btn_theme);
		xx_num = (TextView) findViewById(R.id.xx_num);

		img1.setOnClickListener(new MyOnClickListener(0));
		img2.setOnClickListener(new MyOnClickListener(1));
		img3.setOnClickListener(new MyOnClickListener(2));
		img_theme.setOnClickListener(new MyOnClickListener(3));
		img4.setOnClickListener(new MyOnClickListener(4));
		
		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
		t_theme.setOnClickListener(new MyOnClickListener(3));
		t4.setOnClickListener(new MyOnClickListener(4));
		
		r1.setOnClickListener(new MyOnClickListener(0));
		r2.setOnClickListener(new MyOnClickListener(1));
		r3.setOnClickListener(new MyOnClickListener(2));
		rtheme.setOnClickListener(new MyOnClickListener(3));
		r4.setOnClickListener(new MyOnClickListener(4));
		

	}

	private void initPagerViewer() {
		pager = (ViewPager) findViewById(R.id.viewpage);
		final ArrayList<View> list = new ArrayList<View>();
		Intent intent = new Intent(context, com.example.Sy.Sy.class);
		intent.putExtra("user_id", user_id);
		list.add(getView("A", intent));
		Intent intent2 = new Intent(context, com.example.Gz.Gz.class);
		intent2.putExtra("user_id", user_id);
		list.add(getView("B", intent2));
		Intent intent3 = new Intent(context, com.example.Ky.Ky.class);
		intent3.putExtra("user_id", user_id);
		list.add(getView("C", intent3));
		Intent intent4 = new Intent(context, com.example.theme.Theme.class);
		intent4.putExtra("user_id", user_id);
		list.add(getView("D", intent4));
		Intent intent5 = new Intent(context, com.example.xx.Xx.class);
		intent5.putExtra("user_id", user_id);
		list.add(getView("E", intent5));
		

		pager.setAdapter(new MyPagerAdapter(list));
		pager.setCurrentItem(page);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
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
				img1.setImageResource(R.drawable.tab_1_on);
				img2.setImageResource(R.drawable.tab_2_off);
				img3.setImageResource(R.drawable.tab_3_off);
				img4.setImageResource(R.drawable.tab_4_off);
				img_theme.setImageResource(R.drawable.theme_off);
				break;
			case 1:
				img1.setImageResource(R.drawable.tab_1_off);
				img2.setImageResource(R.drawable.tab_2_on);
				img3.setImageResource(R.drawable.tab_3_off);
				img4.setImageResource(R.drawable.tab_4_off);
				img_theme.setImageResource(R.drawable.theme_off);

				break;
			case 2:
				img1.setImageResource(R.drawable.tab_1_off);
				img2.setImageResource(R.drawable.tab_2_off);
				img3.setImageResource(R.drawable.tab_3_on);
				img4.setImageResource(R.drawable.tab_4_off);
				img_theme.setImageResource(R.drawable.theme_off);

				break;
			case 3:
				img1.setImageResource(R.drawable.tab_1_off);
				img2.setImageResource(R.drawable.tab_2_off);
				img3.setImageResource(R.drawable.tab_3_off);
				img4.setImageResource(R.drawable.tab_4_off);
				img_theme.setImageResource(R.drawable.theme_on);

				break;
			case 4:
				img1.setImageResource(R.drawable.tab_1_off);
				img2.setImageResource(R.drawable.tab_2_off);
				img3.setImageResource(R.drawable.tab_3_off);
				img4.setImageResource(R.drawable.tab_4_on);
				img_theme.setImageResource(R.drawable.theme_off);

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
			pager.setCurrentItem(index);
		}
	};

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
