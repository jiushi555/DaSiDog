package com.example.me;

import java.util.List;
import java.util.Map;

import com.example.DasiDog.R;
import com.example.DasiDog.Welcome;
import com.example.Gz.GzArticle;
import com.example.Gz.RemoteImageHelper;
import com.example.emoji.ParseEmojiMsgUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CyAdapter extends BaseAdapter {
	List<Map<String, String>> list;

	private int resource;

	private LayoutInflater inflater;

	private Context Dt;
	private TextView for_nr;
	private TextView writer_username;
	private ImageView writer_tx;
	private TextView id;
	private RelativeLayout card;
	private int user_id;
	private TextView comment;
	private TextView dt_date;
	private LinearLayout dt_list_layout;
	private RemoteImageHelper lazyImageHelper;
	private TextView pl_where;

	public CyAdapter(List<Map<String, String>> list, int resource,
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
	public String getWhere(int arg0, View arg1, ViewGroup arg2){
		String pl_where=null;
		final Map<String, String> map=list.get(arg0);
		pl_where=map.get("pl_where");
		return pl_where;
	}
	public String getWhereId(int arg0, View arg1, ViewGroup arg2){
		String where_id=null;
		final Map<String, String> map=list.get(arg0);
		where_id=map.get("where_id");
		return where_id;
	}
	public String getTextId(int arg0, View arg1, ViewGroup arg2){
		String id=null;
		final Map<String, String> map=list.get(arg0);
		id=map.get("id");
		return id;
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final Map<String, String> good = list.get(arg0);

		String nr = good.get("article");
		String Text_id = good.get("id");
		String date = good.get("date");
		String pl_where = good.get("pl_where");
		String for_nr = good.get("for_nr");
		String from_name = good.get("from_name");
		String to_name = good.get("to_name");
		String from_tx=good.get("from_tx");

		if (arg1 == null) {

			arg1 = inflater.inflate(resource, null);

		}

		this.writer_tx = (ImageView) arg1.findViewById(R.id.sy_tx);
		writer_tx.setImageResource(R.drawable.ic_portrait);
		if(from_tx.equals("no")){
			writer_tx.setImageResource(R.drawable.ic_portrait);
		}else{
			String url=Welcome.URL + "/dasidog/tx/"+from_tx;
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
		this.writer_username = (TextView) arg1
				.findViewById(R.id.cy_writer_username);
		SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(this.Dt, nr);
		this.writer_username.setText(from_name+" 回复@"+to_name+":"+spannableString);
		this.for_nr = (TextView) arg1.findViewById(R.id.for_nr);
		this.for_nr.setText(for_nr);
		this.id = (TextView) arg1.findViewById(R.id.sy_txt_id);
		this.id.setText(Text_id);
		this.dt_date = (TextView) arg1.findViewById(R.id.cy_date);
		this.dt_date.setText(date);
		this.pl_where = (TextView) arg1.findViewById(R.id.cy_where);
		if (pl_where.equals("1")) {
			this.pl_where.setText("工作社区");
		} else if (pl_where.equals("2")) {
			this.pl_where.setText("考研社区");
		}
		this.card = (RelativeLayout) arg1.findViewById(R.id.sy_card);
		this.dt_list_layout = (LinearLayout) arg1.findViewById(R.id.sy_list);

		return arg1;
	}

}
