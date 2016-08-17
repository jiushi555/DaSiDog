package com.example.Sy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.DasiDog.JSONParser;
import com.example.DasiDog.R;

import com.example.DasiDog.Welcome;
import com.example.Ky.KyArticle;
import com.example.ProgressDialog.CustomProgressDialog;

@SuppressLint({ "NewApi" })
public class SampleFragment extends Fragment implements OnItemClickListener {
	private boolean isNoRestListView = false;
	private int mPosition = 0;
	private int headHeight = 0;
	private int scrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
	private ListView mListView;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private View placeHolderView = null;
	private String username;
	private int user_id;
	private int type = 0;
	private int lvIndext;
	private String text_id;
	private CustomProgressDialog progressDialog = null;
	private float mLastY = 0;
	public TabBaseFramentListener baseListener = null;
	private String url = null;
	private int success;
	JSONParser jsonParser = new JSONParser();
	private JSONArray data = null;
	private LinearLayout loadLayout = null;
	private TextView loadinfo = null;
	private SimpleAdapter simpleAdapter;
	private LinearLayout fxshlayout;
	private int currentPage = 1;
	private int linesize = 15;
	private int allRecorders;
	private int begin = 0;
	private LinearLayout wdwt_pl;
	private LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT, 100);
	private int lastItem;
	private A_Adapter mgs;
	private String pl_from;
	private String pl_to;
	private String pl_to_name;
	private String pl_id;
	private Button btn;
	private CharSequence pl_nr;
	private EditText pl_edt;
	private String now;
	private String url_pl;
	private Context context;
	private String re_type;
	private Map<String, String> map;

	public SampleFragment(String now, int mPosition, int headH,
			Context context, String re_type, Map<String, String> map) {
		this.mPosition = mPosition;
		this.headHeight = headH;
		this.now = now;
		this.context = context;
		this.re_type = re_type;
		this.map = map;
	}

	private String array_name;
	private String id_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (this.now.equals("1")) {
			this.url = Welcome.URL + "/dasidog/gz_pl_jz.php";
			this.url_pl = Welcome.URL + "/dasidog/gz_pl_fb.php";
			this.array_name = "gz_pl";
			this.id_name = "gz_id";
		}
		if (this.now.equals("2")) {
			this.url = Welcome.URL + "/dasidog/ky_pl_jz.php";
			this.url_pl = Welcome.URL + "/dasidog/ky_pl_fb.php";
			this.array_name = "ky_pl";
			this.id_name = "ky_id";
		}

	}

	public void settype(int type) {
		this.type = type;
	}

	public void setName(String name) {
		this.username = name;
	}

	public void setUserId(int user_id) {
		this.user_id = user_id;
	}

	public void setTextId(String text_id) {
		this.text_id = text_id;
	}

	public String getName() {
		return username;
	}

	public String getTextId() {
		return text_id;
	}

	public int getUserId() {
		return user_id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.gz_pl_framgment_list, null);

		System.out.println("****************onCreateView");
		return v;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		text_id = this.getTextId();
		username = this.getName();
		user_id = this.getUserId();
		this.loadLayout = new LinearLayout(getActivity());
		this.mListView = (ListView) getActivity()
				.findViewById(R.id.gz_listView);
		this.wdwt_pl = (LinearLayout) getActivity().findViewById(
				R.id.LinearLayout);
		this.fxshlayout = (LinearLayout) getActivity().findViewById(
				R.id.wdwt_pl_layout);
		this.wdwt_pl = (LinearLayout) getActivity().findViewById(
				R.id.LinearLayout);
		this.fxshlayout = (LinearLayout) getActivity().findViewById(
				R.id.wdwt_pl_layout);
		this.pl_edt = (EditText) getActivity().findViewById(R.id.a_pl_edt);

		this.loadinfo = new TextView(getActivity());
		this.loadinfo.setText("加载更多中..");
		this.loadinfo.setTextColor(SampleFragment.this.getResources().getColor(
				R.color.loadtext_color));
		this.loadinfo.setGravity(Gravity.CENTER);
		this.loadinfo.setTextSize(20.0f);
		this.loadLayout.addView(loadinfo, this.layoutParams);
		this.loadLayout.setGravity(Gravity.CENTER);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		placeHolderView = LayoutInflater.from(getActivity()).inflate(
				R.layout.view_header_placeholder, mListView, false);
		System.out.println("*************oncreate view:" + headHeight);
		placeHolderView.setPadding(0, headHeight, 0, 0);
		mListView.addHeaderView(placeHolderView, this, false);
		baseListener.restScrollState();
		if (re_type.equals("yes")) {
			new SampleFragment.Gz_Pl_refresh().execute();
		} else {
			new SampleFragment.Gz_Pl().execute();
		}

		int len = 100;

		// if (mPosition == 2) {
		// len = 1;
		// }

		System.out.println("****************user_id" + this.getUserId());
		System.out.println("****************usernamewojiushi" + this.getName());
		System.out.println("****************text_id" + this.getTextId());

		/*
		 * mListView = (ListView) getActivity().findViewById(R.id.listView);
		 * placeHolderView =
		 * LayoutInflater.from(getActivity()).inflate(R.layout.
		 * view_header_placeholder, mListView, false);
		 * System.out.println("*************oncreate view:" + headHeight);
		 * placeHolderView.setPadding(0, headHeight, 0, 0);
		 * mListView.addHeaderView(placeHolderView);
		 * 
		 * // mListView.setOnScrollListener(this); simpleAdapter = new
		 * SimpleAdapter(getActivity(), list, R.layout.fxsh_pl_list_item, new
		 * String[] { "nr" }, new int[] { R.id.pl_nr});
		 * mListView.setAdapter(simpleAdapter);
		 * mListView.setOnItemClickListener(this);
		 */

		baseListener.restScrollState();
		System.out.println("****************onActivityCreated");
		this.mgs = new A_Adapter(list, R.layout.sy_pl_list_item, getActivity(),
				user_id);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("****************onResume");
	}

	public void resetHeaderView(int height) {
		if (placeHolderView != null) {
			placeHolderView.setPadding(0, height, 0, 0);
		}
	}

	public void resetListViewState(int scrollHeight) {
		if (isNoRestListView) {
			return;
		}

		System.out.println("*****************restScrollState start");
		if (mListView == null) {
			return;
		}

		System.out.println("*****************restScrollState start2");

		if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
			return;
		}
		System.out.println("*****************restScrollState scrollHeight:"
				+ scrollHeight);

		mListView.setSelectionFromTop(1, scrollHeight);
		System.out.println("*****************restScrollState end");
	}

	/*
	 * @Override public void onScroll(AbsListView view, int firstVisibleItem,
	 * int visibleItemCount, int totalItemCount) {
	 * System.out.println("**********************firstVisibleItem:" +
	 * firstVisibleItem + ",visi:" + visibleItemCount + ",total:" +
	 * totalItemCount); if (visibleItemCount == totalItemCount && totalItemCount
	 * > 1) { baseListener.onScroll(view, firstVisibleItem, visibleItemCount,
	 * totalItemCount); isNoRestListView = true; } else { isNoRestListView =
	 * false; }
	 * 
	 * if (baseListener != null && scrollState !=
	 * AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
	 * baseListener.onScroll(view, firstVisibleItem, visibleItemCount,
	 * totalItemCount); if (placeHolderView != null) {
	 * System.out.println("**************headtop:" +
	 * placeHolderView.getPaddingTop()); } } }
	 * 
	 * @Override public void onScrollStateChanged(AbsListView view, int
	 * scrollState) { // nothing this.scrollState = scrollState; }
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(getActivity(), "toast lixiaodaoaaaa 010",
				Toast.LENGTH_SHORT).show();
	}

	/*
	 * 评论加载
	 */

	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setTabBaseFramentListener(TabBaseFramentListener listener) {
		this.baseListener = listener;
	}

	/**
	 * 设置ListView Top
	 * 
	 * @param scrollHeight
	 */
	class Gz_Pl_refresh extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			SampleFragment.this.progressDialog = CustomProgressDialog
					.createDialog(getActivity());
			SampleFragment.this.progressDialog.setMessage("刷新中...");

			SampleFragment.this.progressDialog.show();
		}

		@SuppressWarnings("deprecation")
		protected String doInBackground(String... args) {
			SampleFragment.this.begin = (SampleFragment.this.currentPage - 1)
					* SampleFragment.this.linesize;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(SampleFragment.this.id_name,
					String.valueOf(SampleFragment.this.text_id)));
			params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			System.out.println("**************text_f_id"
					+ SampleFragment.this.text_id);
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params,
					context);
			Log.d("Create Response", json.toString());
			try {
				SampleFragment.this.success = json.getInt("success");
				if (success == 1) {
					data = json.getJSONArray(SampleFragment.this.array_name);
					for (int i = 0; i < data.length(); i++) {
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						int from = c.getInt("from");
						int to = c.getInt("to");
						String nr = (String) c.get("nr");
						String pl_date = c.getString("date");
						int where_id = c.getInt("where_id");
						int type = c.getInt("type");
						String from_name = c.getString("from_name");
						String from_now = c.getString("from_now");
						String to_name = c.getString("to_name");
						String from_tx = c.getString("from_tx");
						Map<String, String> map = new HashMap<String, String>();
						// adding each child node to HashMap key => value
						map.put("id", String.valueOf(id));
						map.put("from", String.valueOf(from));
						map.put("nr", nr);
						map.put("to", String.valueOf(to));
						map.put("pl_date", pl_date);
						map.put("where_id", String.valueOf(where_id));
						map.put("type", String.valueOf(type));
						map.put("from_name", from_name);
						map.put("from_now", from_now);
						map.put("to_name", to_name);
						map.put("from_tx", from_tx);
						// adding HashList to ArrayList
						list.add(map);
					}
				} else {
					SampleFragment.this.loadinfo.setText("暂时还没有评论^.^");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			progressDialog.dismiss();
			/*
			 * placeHolderView =
			 * LayoutInflater.from(getActivity()).inflate(R.layout
			 * .view_header_placeholder, mListView, false);
			 * System.out.println("*************oncreate view:" + headHeight);
			 * placeHolderView.setPadding(0, headHeight, 0, 0);
			 */

			SampleFragment.this.mListView
					.addFooterView(SampleFragment.this.loadLayout,null,false);

			mListView.setAdapter(mgs);
			SampleFragment.this.mListView
					.setOnScrollListener(new OnScrollListenerlmpl());
			mListView.setOnItemClickListener(new PlOnItemClick());
			mListView
					.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

						@Override
						public void onCreateContextMenu(ContextMenu menu,
								View v, ContextMenuInfo menuInfo) {
							menu.setHeaderTitle("请选择");
							menu.add(Menu.NONE, 1, Menu.NONE, "回复");
							menu.add(Menu.NONE, 2, Menu.NONE, "复制");
							menu.add(Menu.NONE, 3, Menu.NONE, "举报");

						}
					});
			mListView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					final int action = event.getActionMasked();
					switch (action & MotionEvent.ACTION_MASK) {

					case MotionEvent.ACTION_MOVE:

						final float y = event.getY();

						if (y > mLastY) {
							wdwt_pl.setVisibility(View.VISIBLE);
						} else {
							wdwt_pl.setVisibility(View.GONE);
						}

						mLastY = y;

						break;

					}
					return false;
				}

			});

		}

	}

	// 加载评论
	class Gz_Pl extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			SampleFragment.this.progressDialog = CustomProgressDialog
					.createDialog(getActivity());
			SampleFragment.this.progressDialog.setMessage("载入中...");

			SampleFragment.this.progressDialog.show();
		}

		@SuppressWarnings("deprecation")
		protected String doInBackground(String... args) {
			SampleFragment.this.begin = (SampleFragment.this.currentPage - 1)
					* SampleFragment.this.linesize;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(SampleFragment.this.id_name,
					String.valueOf(SampleFragment.this.text_id)));
			params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			System.out.println("**************text_f_id"
					+ SampleFragment.this.text_id);
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params,
					context);
			Log.d("Create Response", json.toString());
			try {
				SampleFragment.this.success = json.getInt("success");
				if (success == 1) {
					data = json.getJSONArray(SampleFragment.this.array_name);
					for (int i = 0; i < data.length(); i++) {
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						int from = c.getInt("from");
						int to = c.getInt("to");
						String nr = c.getString("nr");
						String pl_date = c.getString("date");
						int where_id = c.getInt("where_id");
						int type = c.getInt("type");
						String from_name = c.getString("from_name");
						String from_now = c.getString("from_now");
						String to_name = c.getString("to_name");
						String from_tx = c.getString("from_tx");
						Map<String, String> map = new HashMap<String, String>();
						// adding each child node to HashMap key => value
						map.put("id", String.valueOf(id));
						map.put("from", String.valueOf(from));
						map.put("nr", nr);
						map.put("to", String.valueOf(to));
						map.put("pl_date", pl_date);
						map.put("where_id", String.valueOf(where_id));
						map.put("type", String.valueOf(type));
						map.put("from_name", from_name);
						map.put("from_now", from_now);
						map.put("to_name", to_name);
						map.put("from_tx", from_tx);
						// adding HashList to ArrayList
						list.add(map);
					}
				} else {
					SampleFragment.this.loadinfo.setText("暂时还没有评论^.^");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			progressDialog.dismiss();
			/*
			 * placeHolderView =
			 * LayoutInflater.from(getActivity()).inflate(R.layout
			 * .view_header_placeholder, mListView, false);
			 * System.out.println("*************oncreate view:" + headHeight);
			 * placeHolderView.setPadding(0, headHeight, 0, 0);
			 */

			SampleFragment.this.mListView
					.addFooterView(SampleFragment.this.loadLayout,null,false);

			mListView.setAdapter(mgs);
			SampleFragment.this.mListView
					.setOnScrollListener(new OnScrollListenerlmpl());
			mListView.setOnItemClickListener(new PlOnItemClick());
			mListView
					.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

						@Override
						public void onCreateContextMenu(ContextMenu menu,
								View v, ContextMenuInfo menuInfo) {
							menu.setHeaderTitle("请选择");
							menu.add(Menu.NONE, 1, Menu.NONE, "回复");
							menu.add(Menu.NONE, 2, Menu.NONE, "复制");
							menu.add(Menu.NONE, 3, Menu.NONE, "举报");

						}
					});
			mListView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					final int action = event.getActionMasked();
					switch (action & MotionEvent.ACTION_MASK) {

					case MotionEvent.ACTION_MOVE:

						final float y = event.getY();

						if (y > mLastY) {
							wdwt_pl.setVisibility(View.VISIBLE);
						} else {
							wdwt_pl.setVisibility(View.GONE);
						}

						mLastY = y;

						break;

					}
					return false;
				}

			});

		}

	}

	private class PlOnItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			SampleFragment.this.pl_from = String.valueOf(user_id);
			SampleFragment.this.pl_id = mgs.getId(arg2 - 1, arg1, mListView);
			SampleFragment.this.pl_to = mgs.getWriterId(arg2 - 1, arg1,
					mListView);
			SampleFragment.this.pl_nr = mgs.getText(arg2 - 1, arg1, mListView);
			SampleFragment.this.pl_to_name = mgs.getWriterName(arg2 - 1, arg1,
					mListView);
			arg1.showContextMenu();
			System.out.println(arg2);
			System.out.println(pl_id);
			System.out.println(pl_to);

		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			this.pl_edt.setHint("回复" + pl_to_name + "：");
			this.btn = (Button) getActivity().findViewById(R.id.a_pl_but);
			this.btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					SampleFragment.this.pl_nr = SampleFragment.this.pl_edt
							.getText().toString();
					if (SampleFragment.this.pl_nr.equals("")) {
						Toast.makeText(getActivity(), "请输入内容",
								Toast.LENGTH_SHORT).show();
					} else {
						new SampleFragment.Gz_Pl_().execute();
					}

				}
			});

		}
		if (item.getItemId() == 2) {
			ClipboardManager clip = (ClipboardManager) getActivity()
					.getSystemService(getActivity().CLIPBOARD_SERVICE);
			clip.setText(pl_nr);
			Toast.makeText(getActivity(), "内容已复制到剪切板", Toast.LENGTH_SHORT)
					.show();
		}
		if (item.getItemId() == 3) {
			Toast.makeText(getActivity(), "举报成功", Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	class Gz_Pl_ extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			SampleFragment.this.progressDialog = CustomProgressDialog
					.createDialog(getActivity());
			SampleFragment.this.progressDialog.setMessage("评论中...");

			SampleFragment.this.progressDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("from", String
					.valueOf(SampleFragment.this.user_id)));
			params.add(new BasicNameValuePair("to", String.valueOf(pl_to)));
			params.add(new BasicNameValuePair("type", "1"));
			params.add(new BasicNameValuePair("txt_id", text_id));
			params.add(new BasicNameValuePair("pl_nr", pl_nr.toString()));
			params.add(new BasicNameValuePair("pl_for", pl_id));
			JSONObject json = jsonParser.makeHttpRequest(url_pl, "POST",
					params, context);
			Log.d("Create Response", json.toString());
			try {
				SampleFragment.this.success = json.getInt("success");

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			progressDialog.dismiss();
			/*
			 * placeHolderView =
			 * LayoutInflater.from(getActivity()).inflate(R.layout
			 * .view_header_placeholder, mListView, false);
			 * System.out.println("*************oncreate view:" + headHeight);
			 * placeHolderView.setPadding(0, headHeight, 0, 0);
			 */
			if (success == 1) {

				Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT)
						.show();
				refresh();
			} else {
				Toast.makeText(getActivity(), "评论失败，刷新试试？", Toast.LENGTH_SHORT)
						.show();
			}

		}

	}

	private void refresh() {

		((Activity) context).finish();
		Intent it_to_a = new Intent(context, SyArticle.class);
		it_to_a.putExtra("user_id", user_id);
		it_to_a.putExtra("writer_id", map.get("writer_id"));
		it_to_a.putExtra("writer_name", map.get("writer_name"));
		it_to_a.putExtra("text_id", map.get("txt_id"));
		it_to_a.putExtra("pl_num", map.get("pl_num"));
		it_to_a.putExtra("nr", map.get("nr"));
		it_to_a.putExtra("date", map.get("date"));
		it_to_a.putExtra("tx_url", map.get("user_tx_url"));
		it_to_a.putExtra("path", map.get("path"));
		it_to_a.putExtra("re_type", "yes");
		it_to_a.putExtra("now", map.get("now"));
		it_to_a.putExtra("pathX", map.get("pathX"));
		startActivity(it_to_a);

	}

	private class OnScrollListenerlmpl implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			if (SampleFragment.this.lastItem == SampleFragment.this.mgs
					.getCount()
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				currentPage++;
				SampleFragment.this.mListView
						.setSelection(SampleFragment.this.lastItem);
				System.out.println("已经到了底部");
				System.out.println(SampleFragment.this.mgs.getCount());
				System.out.println(SampleFragment.this.lastItem);
				SampleFragment.this.appendData();
			}
			// 顶部停靠
			SampleFragment.this.scrollState = scrollState;
			// 判断滑动方向

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			SampleFragment.this.lastItem = firstVisibleItem + visibleItemCount
					- 2;
			// TODO Auto-generated method stub
			System.out.println("**********************firstVisibleItem:"
					+ firstVisibleItem + ",visi:" + visibleItemCount
					+ ",total:" + totalItemCount);
			if (visibleItemCount == totalItemCount && totalItemCount > 1) {
				baseListener.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
				isNoRestListView = true;
			} else {
				isNoRestListView = false;
			}

			if (baseListener != null
					&& scrollState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
				baseListener.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
				if (placeHolderView != null) {
					System.out.println("**************headtop:"
							+ placeHolderView.getPaddingTop());
				}
			}
			// 以上均是顶部停靠

		}

	}

	@SuppressWarnings("deprecation")
	private void appendData() {
		SampleFragment.this.begin = (SampleFragment.this.currentPage - 1)
				* SampleFragment.this.linesize;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(SampleFragment.this.id_name, String
				.valueOf(SampleFragment.this.text_id)));
		params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
		JSONObject json = jsonParser.makeHttpRequest(url, "POST", params,
				context);
		Log.d("Create Response", json.toString());
		try {
			SampleFragment.this.success = json.getInt("success");
			if (success == 1) {
				data = json.getJSONArray(SampleFragment.this.array_name);
				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					int id = c.getInt("id");
					int from = c.getInt("from");
					int to = c.getInt("to");
					String nr = c.getString("nr");
					String pl_date = c.getString("date");
					int where_id = c.getInt("where_id");
					int type = c.getInt("type");
					String from_name = c.getString("from_name");
					String from_now = c.getString("from_now");
					String to_name = c.getString("to_name");
					String from_tx = c.getString("from_tx");
					Map<String, String> map = new HashMap<String, String>();
					// adding each child node to HashMap key => value
					map.put("id", String.valueOf(id));
					map.put("from", String.valueOf(from));
					map.put("nr", nr);
					map.put("to", String.valueOf(to));
					map.put("pl_date", pl_date);
					map.put("where_id", String.valueOf(where_id));
					map.put("type", String.valueOf(type));
					map.put("from_name", from_name);
					map.put("from_now", from_now);
					map.put("to_name", to_name);
					map.put("from_tx", from_tx);
					// adding HashList to ArrayList
					list.add(map);
					mgs.notifyDataSetChanged();

				}
			} else {
				SampleFragment.this.loadinfo.setText("没有更多了~");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class Wdwt_Pl_ extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			SampleFragment.this.begin = (SampleFragment.this.currentPage - 1)
					* SampleFragment.this.linesize;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("wdwt_id", String
					.valueOf(SampleFragment.this.text_id)));
			params.add(new BasicNameValuePair("begin", String.valueOf(begin)));
			System.out.println("**************text_f_id"
					+ SampleFragment.this.text_id);
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params,
					context);
			Log.d("Create Response", json.toString());
			try {
				SampleFragment.this.success = json.getInt("success");
				if (success == 1) {
					data = json.getJSONArray("wdwt_pl");
					for (int i = 0; i < data.length(); i++) {
						JSONObject c = data.getJSONObject(i);
						int id = c.getInt("id");
						int from = c.getInt("from");
						int to = c.getInt("to");
						String nr = c.getString("nr");
						int nm = c.getInt("nm");
						String pl_date = c.getString("date");
						int where_id = c.getInt("where_id");
						Map<String, String> map = new HashMap<String, String>();
						// adding each child node to HashMap key => value
						map.put("id", String.valueOf(id));
						map.put("from", String.valueOf(from));
						map.put("nm", String.valueOf(nm));
						map.put("nr", nr);
						map.put("to", String.valueOf(to));
						map.put("pl_date", pl_date);
						map.put("where_id", String.valueOf(where_id));
						// adding HashList to ArrayList
						list.add(map);
					}
				} else {
					SampleFragment.this.loadinfo.setText("暂时还没有评论^.^");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done

			/*
			 * placeHolderView =
			 * LayoutInflater.from(getActivity()).inflate(R.layout
			 * .view_header_placeholder, mListView, false);
			 * System.out.println("*************oncreate view:" + headHeight);
			 * placeHolderView.setPadding(0, headHeight, 0, 0);
			 */

			simpleAdapter = new SimpleAdapter(getActivity(), list,
					R.layout.sy_pl_list_item, new String[] { "nr", "from",
							"pl_date" }, new int[] { R.id.pl_nr,
							R.id.writer_id, R.id.date_text });
			SampleFragment.this.mListView
					.addFooterView(SampleFragment.this.loadLayout);

			mListView.setAdapter(simpleAdapter);
			SampleFragment.this.mListView
					.setOnScrollListener(new OnScrollListenerlmpl());
			mListView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					final int action = event.getActionMasked();
					switch (action & MotionEvent.ACTION_MASK) {

					case MotionEvent.ACTION_MOVE:

						final float y = event.getY();

						if (y > mLastY) {
							wdwt_pl.setVisibility(View.VISIBLE);
						} else {
							wdwt_pl.setVisibility(View.GONE);
						}

						mLastY = y;

						break;

					}
					return false;
				}

			});

		}

	}

}