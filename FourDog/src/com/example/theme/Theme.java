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
import com.example.Ky.Ky;
import com.example.Ky.KyAdapter;
import com.example.ProgressDialog.CustomProgressDialog;
import com.example.set.Set;
import com.example.theme.CycleViewPager.ImageCycleViewListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Administrator
 * 
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") public class Theme extends Activity {

	private List<ImageView> views = new ArrayList<ImageView>();
	private List<ADInfo> infos = new ArrayList<ADInfo>();
	private CycleViewPager cycleViewPager;
	private int user_id;
	private ImageView set;
	// 加载显示项目
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog progressDialog = null;
	private int success;
	private int type=1;
	private String url_show = Welcome.URL + "/dasidog/theme_nr.php";
	private JSONArray data = null;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ListView datalist;
	private ThemeAdapter mgs;
	private int[] imageUrls = { R.drawable.theme_title1,
			R.drawable.theme_title2, R.drawable.theme_title3 };

	@TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme);
		Intent it=super.getIntent();
		user_id=it.getIntExtra("user_id", 0);
		set=(ImageView) findViewById(R.id.theme_set);
		set.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(Theme.this, Set.class);
				it.putExtra("user_id", user_id);
				Theme.this.startActivity(it);
			}
		});
		initialize();
		datalist = (ListView) this.findViewById(R.id.theme_list);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		new Theme_().execute();
		mgs = new ThemeAdapter(list, R.layout.theme_item, Theme.this, user_id);

	}
	class Theme_ extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type", String.valueOf(type)));
			JSONObject json = jsonParser.makeHttpRequest(url_show, "POST",
					params,Theme.this);
			Log.d("Create Response", json.toString());
			try {
				Theme.this.success = json.getInt("success");
				if (success == 1) {

					data = json.getJSONArray("theme");
					System.out.println(data.length());
					for (int i = 0; i < data.length(); i++) {
						Map<String, String> map = new HashMap<String, String>();
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						String title=c.getString("title");
						String nr=c.getString("nr");
						String num=c.getString("num");

						map.put("id", String.valueOf(id));
						map.put("title", title);
						map.put("nr", nr);
						map.put("num", num);
						// adding HashList to ArrayList
						Theme.this.list.add(map);
					}
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
					

					// updating listview
			

					

					Theme.this.datalist.setAdapter(Theme.this.mgs);
				}
			});

		}
	}
	@SuppressLint("NewApi")
	private void initialize() {

		cycleViewPager = (CycleViewPager) getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);

		for (int i = 0; i < imageUrls.length; i++) {
			ADInfo info = new ADInfo();
			info.setUrl(imageUrls[i]);
			info.setContent("图片-->" + i);
			infos.add(info);
		}

		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1)
				.getUrl()));
		for (int i = 0; i < infos.size(); i++) {
			views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));

		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(true);

		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, infos, mAdCycleViewListener);
		// 设置轮播
		cycleViewPager.setWheel(true);

		// 设置轮播时间，默认5000ms
		cycleViewPager.setTime(4000);
		// 设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				position = position - 1;
				Toast.makeText(Theme.this, "position-->" + info.getContent(),
						Toast.LENGTH_SHORT).show();
			}

		}

	};

	/**
	 * 配置ImageLoder
	 */
	private void configImageLoader() {
		// 初始化ImageLoader
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}
}
