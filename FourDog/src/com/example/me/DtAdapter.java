package com.example.me;

import java.util.List;
import java.util.Map;

import com.example.DasiDog.R;
import com.example.DasiDog.Welcome;
import com.example.Gz.GzArticle;
import com.example.Gz.RemoteImageHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DtAdapter extends BaseAdapter {
	List<Map<String, String>> list;

	private int resource;

	private LayoutInflater inflater;

	private Context Dt;
	private TextView nr;
	private TextView writer_username;
	private ImageView writer_tx;
	private TextView id;
	private RelativeLayout card;
	private int user_id;
	private TextView comment;
	private TextView dt_date;
	private TextView pl_num;
	private LinearLayout dt_list_layout;
	private TextView pl_num_;
	private RemoteImageHelper lazyImageHelper;
	private TextView fb_where;
	private TextView now;

	public DtAdapter(List<Map<String, String>> list, int resource,
			Context context, int user_id) {
		this.lazyImageHelper = new RemoteImageHelper();
		this.list = list;

		this.resource = resource;

		this.Dt = context;
		this.user_id = user_id;
		inflater = (LayoutInflater) Dt
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public String getTxtId(int arg0, View arg1, ViewGroup arg2) {
		String id = null;
		final Map<String, String> good = list.get(arg0);
		id = good.get("id");
		return id;
	}
	public String getWhereId(int arg0, View arg1, ViewGroup arg2){
		String id = null;
		final Map<String, String> good = list.get(arg0);
		id = good.get("where_id");
		return id;
	}
	public String getText(int arg0, View arg1, ViewGroup arg2) {
		String text = null;
		final Map<String, String> good = list.get(arg0);
		text = good.get("article");
		return text;
	}
	public String getNow(int arg0,View arg1,ViewGroup arg2){
		String now=null;
		final Map<String, String> good=list.get(arg0);
		now=good.get("now");
		return now;
	}
	public String getPlnum(int arg0, View arg1, ViewGroup arg2){
		String plnum=null;
		final Map<String, String> good = list.get(arg0);
		plnum = good.get("pl_num");
		return plnum;
	}
	public String getDate(int arg0, View arg1, ViewGroup arg2){
		String date=null;
		final Map<String, String> good = list.get(arg0);
		date = good.get("date");
		return date;
	}
	public String getWhere(int arg0, View arg1, ViewGroup arg2){
		String where=null;
		final Map<String, String> good = list.get(arg0);
		where = good.get("fb_where");
		return where;
	}
	public String getName(int arg0, View arg1, ViewGroup arg2){
		String name=null;
		final Map<String, String> good = list.get(arg0);
		name=good.get("username");
		return name;
	}
	public String getTxUrl(int arg0, View arg1, ViewGroup arg2){
		String url=null;
		final Map<String, String> good=list.get(arg0);
		url=good.get("user_tx");
		return url;
	}
	public String getImgUrl(int arg0, View arg1, ViewGroup arg2){
		String url=null;
		final Map<String, String> good=list.get(arg0);
		url=good.get("path");
		return url;
	}
	public String getImgUrlX(int arg0, View arg1, ViewGroup arg2){
		String url=null;
		final Map<String, String> good=list.get(arg0);
		url=good.get("pathX");
		return url;
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final Map<String, String> good = list.get(arg0);
		String nr = good.get("article");
		String Text_id = good.get("id");
		String pl_num = good.get("pl_num");
		String date = good.get("date");
		String writer_username = good.get("username");
		String fb_where = good.get("fb_where");
		String tx_url=good.get("user_tx");
		String now_id=good.get("now");
		if (arg1 == null) {

			arg1 = inflater.inflate(resource, null);

		}
		this.writer_username = (TextView) arg1
				.findViewById(R.id.sy_writer_username);
		this.writer_username.setText(writer_username);
		this.writer_tx = (ImageView) arg1.findViewById(R.id.sy_tx);
		writer_tx.setImageResource(R.drawable.ic_portrait);
		if(tx_url.equals("no")){
			writer_tx.setImageResource(R.drawable.ic_portrait);
		}else{
			String url=Welcome.URL + "/dasidog/tx/"+tx_url;
			writer_tx.setTag(url);
			Welcome.mimageLoader.getImageByAsyncTask(writer_tx, url,1);
		}
		/*
		 * if (path.equals("no")) {
		 * writer_tx.setImageResource(R.drawable.ic_portrait); } else { String
		 * url_tx = Welcome.URL + "/dasidog/img/" + path;
		 * lazyImageHelper.loadImage(writer_tx, url_tx, false);
		 * 
		 * try{ byte data[]=getUrlData(url_tx); Bitmap
		 * bm=BitmapFactory.decodeByteArray(data, 0, data.length);
		 * writer_tx.setImageBitmap(toRoundBitmap(bm)); }catch(Exception e){
		 * e.printStackTrace(); }
		 * 
		 * 
		 * }
		 */
		this.nr = (TextView) arg1.findViewById(R.id.sy_nr);
		this.nr.setText(nr);
		this.id = (TextView) arg1.findViewById(R.id.sy_txt_id);
		this.id.setText(Text_id);
		this.dt_date = (TextView) arg1.findViewById(R.id.sy_date);
		this.dt_date.setText(date);
		this.pl_num = (TextView) arg1.findViewById(R.id.sy_pl_num_);
		this.pl_num.setText(pl_num);
		this.pl_num_ = (TextView) arg1.findViewById(R.id.sy_pl_num_);
		this.pl_num_.setText(pl_num);
		this.fb_where = (TextView) arg1.findViewById(R.id.dt_where);
		if (fb_where.equals("1")) {
			this.fb_where.setText("工作社区");
		} else if (fb_where.equals("2")) {
			this.fb_where.setText("考研社区");
		}
		this.card = (RelativeLayout) arg1.findViewById(R.id.sy_card);
		this.dt_list_layout = (LinearLayout) arg1.findViewById(R.id.sy_list);
		this.now=(TextView) arg1.findViewById(R.id.sy_now);
		if(now_id.equals("1")){
			this.now.setText("大四");
		}else if(now_id.equals("2")){
			this.now.setText("毕业");
		}
		return arg1;
	}

}
