package com.example.Gz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.DasiDog.JSONParser;
import com.example.DasiDog.R;
import com.example.ProgressDialog.CustomProgressDialog;
import com.example.Sy.SyArticle;
import com.example.emoji.SelectFaceHelper;
import com.example.emoji.SelectFaceHelper.OnFaceOprateListener;
import com.example.image.ImageX;
import com.example.DasiDog.Welcome;
import com.nineoldandroids.view.ViewHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GzArticle extends FragmentActivity implements
		TabBaseFramentListener {
	private String txt_id;
	private String writer_id;
	private String pl_num;
	private int user_id;
	private String writer_name;
	private String nr;
	private String date;
	private TextView id_txt;
	private TextView writer_id_txt;
	private TextView pl_num_txt;
	private TextView writer_name_txt;
	private TextView nr_txt;
	private TextView date_txt;
	private ImageView back;
	private EditText pl_edt;
	private String user_tx_url;
	private ImageView user_tx;
	private String path;
	private ImageView nr_img;
	private String re_type = "no";
	private Map<String, String> map_to;
	private String now;
	private TextView now_txt;
	private String pathX;
	private String urlX;
	private Button mFaceBtn;
	private SelectFaceHelper mFaceHelper;
	private View addFaceToolView;
	private boolean isVisbilityFace;
	// 发布评论
	private String pl_txt;
	private Button pl_btn;
	private String url_pl = Welcome.URL + "/dasidog/gz_pl_fb.php";
	private int success;
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog pDialog = null;
	// 顶部停靠
	private boolean isReset = false;
	private View card;
	private View fxsh_header;
	private int headH = 0;
	private SampleFragment frament = null;
	private int type = 0;
	private int line = 0; // 标记是第一次载入还是评论

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.sy_article);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.article_title);
		Intent it = super.getIntent();
		txt_id = it.getStringExtra("text_id");
		writer_id = it.getStringExtra("writer_id");
		pl_num = it.getStringExtra("pl_num");
		user_id = it.getIntExtra("user_id", 0);
		writer_name = it.getStringExtra("writer_name");
		nr = it.getStringExtra("nr");
		date = it.getStringExtra("date");
		user_tx_url = it.getStringExtra("user_tx");
		path = it.getStringExtra("path");
		now = it.getStringExtra("now");
		pathX = it.getStringExtra("pathX");
		now_txt = (TextView) findViewById(R.id.a_now);
		if (now.equals("1")) {
			now_txt.setText("大四");

		} else {
			now_txt.setText("毕业");
		}
		if (it.getStringExtra("re_type") != null) {
			re_type = it.getStringExtra("re_type");
		}
		map_to = new HashMap<String, String>();
		map_to.put("txt_id", txt_id);
		map_to.put("writer_id", writer_id);
		map_to.put("pl_num", pl_num);
		map_to.put("user_id", String.valueOf(user_id));
		map_to.put("writer_name", writer_name);
		map_to.put("nr", nr);
		map_to.put("date", date);
		map_to.put("user_tx_url", user_tx_url);
		map_to.put("path", path);
		map_to.put("pathX", pathX);
		map_to.put("now", now);
		Log.d("pathX", pathX);
		user_tx = (ImageView) findViewById(R.id.portrati);
		nr_img = (ImageView) findViewById(R.id.sy_a_img);
		if (user_tx_url.equals("no")) {
			user_tx.setImageResource(R.drawable.ic_portrait);
		} else {
			String url = Welcome.URL + "/dasidog/tx/" + user_tx_url;
			user_tx.setTag(url);
			Welcome.mimageLoader.getImageByAsyncTask(user_tx, url, 1);
		}
		if (path.equals("no")) {
			nr_img.setVisibility(View.GONE);
		} else {
			nr_img.setVisibility(View.VISIBLE);
			String url = Welcome.URL + "/dasidog/img/" + path;
			nr_img.setTag(url);
			Welcome.mimageLoader.getImageByAsyncTask(nr_img, url, 2);
		}
		nr_img.setOnClickListener(new ImgOnClick());
		id_txt = (TextView) findViewById(R.id.a_id);
		id_txt.setText(String.valueOf(txt_id));
		writer_id_txt = (TextView) findViewById(R.id.a_writer_id);
		writer_id_txt.setText(String.valueOf(writer_id));
		pl_num_txt = (TextView) findViewById(R.id.ans_num);
		pl_num_txt.setText(pl_num + "人参与回答");
		writer_name_txt = (TextView) findViewById(R.id.a_writer_name);
		writer_name_txt.setText(writer_name);
		nr_txt = (TextView) findViewById(R.id.a_nr);
		nr_txt.setText(nr);
		date_txt = (TextView) findViewById(R.id.a_date);
		date_txt.setText(date);

		back = (ImageView) findViewById(R.id.a_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GzArticle.this.finish();
			}
		});

		pl_edt = (EditText) findViewById(R.id.a_pl_edt);
		pl_btn = (Button) findViewById(R.id.a_pl_but);
		pl_btn.setOnClickListener(new plOnClick());
		addFaceToolView = (View) findViewById(R.id.add_tool);

		// 顶部停靠
		initAllViews();
		init(type, user_id, writer_name, txt_id, line);
		mFaceBtn = (Button) findViewById(R.id.btn_to_face);
		mFaceBtn.setOnClickListener(new FaceOnClick());
		pl_edt.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isVisbilityFace = false;
				addFaceToolView.setVisibility(View.GONE);
				return false;
			}
		});

	}

	private class FaceOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (null == mFaceHelper) {

				mFaceHelper = new SelectFaceHelper(GzArticle.this,
						addFaceToolView);
				mFaceHelper.setFaceOpreateListener(mOnFaceOprateListener);
			}
			if (isVisbilityFace) {
				isVisbilityFace = false;
				addFaceToolView.setVisibility(View.GONE);
			} else {
				isVisbilityFace = true;
				addFaceToolView.setVisibility(View.VISIBLE);
				hideInputManager(GzArticle.this);
			}
		}

	}

	OnFaceOprateListener mOnFaceOprateListener = new OnFaceOprateListener() {
		@Override
		public void onFaceSelected(SpannableString spanEmojiStr) {
			if (null != spanEmojiStr) {
				pl_edt.append(spanEmojiStr);
			}
		}

		@Override
		public void onFaceDeleted() {
			int selection = pl_edt.getSelectionStart();
			String text = pl_edt.getText().toString();
			if (selection > 0) {
				String text2 = text.substring(selection - 1);
				if ("]".equals(text2)) {
					int start = text.lastIndexOf("[");
					int end = selection;
					pl_edt.getText().delete(start, end);
					return;
				}
				pl_edt.getText().delete(selection - 1, selection);
			}
		}

	};

	public void onBackPressed() {
		if (isVisbilityFace) {// 好吧,隐藏表情菜单再退出
			isVisbilityFace = false;
			addFaceToolView.setVisibility(View.GONE);
			return;
		}
		finish();

	};

	// 隐藏软键盘
	public void hideInputManager(Context ct) {
		try {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(((Activity) ct).getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {

		}
	}

	private class ImgOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent it = new Intent(GzArticle.this, ImageX.class);
			it.putExtra("pathX", pathX);
			GzArticle.this.startActivity(it);
		}

	}

	private void init(int type, int id, String name, String nr_id, int line) {
		frament = new SampleFragment(type, headH, GzArticle.this, re_type,
				map_to);
		frament.setName(name);
		frament.setUserId(id);
		frament.setTextId(nr_id);
		frament.setTabBaseFramentListener(this);
		frament.settype(line);
		String user_name = name;
		int userid = id;
		getSupportFragmentManager().popBackStack();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.contentLayout, frament);
		ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		// ft.addToBackStack(null);// 这行代码可以返回之前的操作（横屏的情况下，即两边都显示的情况下）
		ft.commit();
		System.out.println("****************username1" + user_name);
		System.out.println("****************username1" + userid);
	}

	private void initAllViews() {

		card = findViewById(R.id.card);
		card.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						System.out.println("*************h11:"
								+ card.getHeight());
						headH = card.getHeight();
						if (frament != null) {
							frament.resetHeaderView(headH);
						}
					}
				});
		/*
		 * header_logo = findViewById(R.id.header_logo); iv_tabBack =
		 * (ImageView) findViewById(R.id.iv_tabBack); iv_tabMore = (ImageView)
		 * findViewById(R.id.iv_tabMore); ll_top = (LinearLayout)
		 * findViewById(R.id.ll_top); headerContent =
		 * findViewById(R.id.headerContent);
		 */
		fxsh_header = findViewById(R.id.wdwt_header);
	}

	class plOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			pl_txt = pl_edt.getText().toString();
			if (pl_txt.equals("")) {
				Toast.makeText(GzArticle.this, "请输入评论！", Toast.LENGTH_SHORT)
						.show();
			} else {
				new GzArticle.Sy_pl().execute();
			}

		}

	}

	class Sy_pl extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = CustomProgressDialog.createDialog(GzArticle.this);
			pDialog.setMessage("评论中...");
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("from", String.valueOf(user_id)));
			params.add(new BasicNameValuePair("to", String.valueOf(writer_id)));
			params.add(new BasicNameValuePair("txt_id", String.valueOf(txt_id)));
			params.add(new BasicNameValuePair("pl_nr", pl_txt));
			JSONObject json = jsonParser.makeHttpRequest(url_pl, "POST",
					params, GzArticle.this);
			Log.d("Create Response", json.toString());
			try {
				GzArticle.this.success = json.getInt("success");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done

			if (GzArticle.this.success == 1) {
				refresh();
				Toast.makeText(GzArticle.this, "评论成功", Toast.LENGTH_SHORT)
						.show();

			}
			if (GzArticle.this.success == 2) {
				Toast.makeText(GzArticle.this, "评论失败，刷新试试？", Toast.LENGTH_SHORT)
						.show();
			}

			pDialog.dismiss();
		}

	}

	private void refresh() {

		finish();
		Intent it_to_a = new Intent(GzArticle.this, GzArticle.class);
		it_to_a.putExtra("user_id", user_id);
		it_to_a.putExtra("writer_id", writer_id);
		it_to_a.putExtra("writer_name", writer_name);
		it_to_a.putExtra("text_id", txt_id);
		it_to_a.putExtra("pl_num", pl_num);
		it_to_a.putExtra("nr", nr);
		it_to_a.putExtra("date", date);
		it_to_a.putExtra("user_tx", user_tx_url);
		it_to_a.putExtra("path", path);
		it_to_a.putExtra("re_type", "yes");
		it_to_a.putExtra("now", now);
		it_to_a.putExtra("pathX", pathX);
		startActivity(it_to_a);

	}

	// 顶部停靠

	@Override
	public void restScrollState() {
		if (isReset) {
			isReset = false;
			System.out.println("*************restScrollState scroll:"
					+ (card.getHeight() + ViewHelper.getTranslationY(card))
					+ ",headH:" + card.getHeight() + ",ty:"
					+ ViewHelper.getTranslationY(card));
			frament.resetListViewState((int) (card.getHeight() + ViewHelper
					.getTranslationY(card)));
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int scrollY = getScrollY(view);

		ViewHelper.setTranslationY(card,
				Math.max(-scrollY, -fxsh_header.getHeight()));

	}

	private int getScrollY(AbsListView view) {
		View c = view.getChildAt(0);
		System.out.println("************start c:" + c);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = view.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = card.getHeight();
		}

		/*
		 * System.out .println("************header Height:" + c.getHeight() +
		 * ",h:" + headerHeight + ",top:" + top);
		 */
		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

}
