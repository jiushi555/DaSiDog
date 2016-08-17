package com.example.xx;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.example.DasiDog.R;
import com.example.DasiDog.Welcome;
import com.example.emoji.ParseEmojiMsgUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View.OnLongClickListener;

import android.view.View;

import android.view.View.OnClickListener;

import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;

import android.widget.Toast;

public class MgsAdapter extends BaseAdapter {

	List<Map<String, String>> list;

	private int resource;

	private LayoutInflater inflater;

	private Context Sy;
	private TextView sy_nr;
	private TextView writer_username;
	private ImageView writer_tx;
	private TextView id;
	private RelativeLayout card;
	private int user_id;
	private TextView comment;
	private TextView pl_date;
	private TextView writer_id_text;
	private TextView now;
	private TextView pl_num_;
	private RemoteImageHelper lazyImageHelper;
	private TextView fb_where;
	private TextView for_nr;
	private TextView from_name;
	private TextView message_nr;
	private TextView pl_for;
	private ImageView from_tx;
	public MgsAdapter(List<Map<String, String>> list, int resource,
			Context context, int user_id) {
		this.lazyImageHelper = new RemoteImageHelper();
		this.list = list;

		this.resource = resource;

		this.Sy = context;
		this.user_id = user_id;
		inflater = (LayoutInflater) Sy
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {

		return list.size();

	}

	@Override
	public Object getItem(int arg0) {

		return list.get(arg0);

	}

	@Override
	public long getItemId(int arg0) {

		return arg0;

	}

//	回复返回数值
	public String getTo(int arg0, View arg1, ViewGroup arg2){
		String to=null;
		final Map<String, String> good=list.get(arg0);
		to=good.get("pl_from");
		return to;
	}
	public String getWhere(int arg0, View arg1, ViewGroup arg2){
		String where=null;
		final Map<String, String> good=list.get(arg0);
		where=good.get("pl_where");
		return where;
	}
	public String getWhere_id(int arg0, View arg1, ViewGroup arg2){
		String where_id=null;
		final Map<String, String> good=list.get(arg0);
		where_id=good.get("where_id");
		return where_id;
	}
	public String getFor(int arg0, View arg1, ViewGroup arg2){
		String pl_for=null;
		final Map<String, String> good=list.get(arg0);
		pl_for=good.get("fpid");
		return pl_for;
	}
	public String getFormName(int arg0, View arg1, ViewGroup arg2){
		String name=null;
		final Map<String, String> good=list.get(arg0);
		name=good.get("from_name");
		return name;
	}
	
//	查看返回数值
	public String getForName(int arg0, View arg1, ViewGroup arg2){
		String name=null;
		final Map<String, String> good=list.get(arg0);
		name=good.get("for_name");
		return name;
	}
	public String getWriter(int arg0, View arg1, ViewGroup arg2){
		String name=null;
		final Map<String, String> good=list.get(arg0);
		name=good.get("for_writer");
		return name;
	}
	public String getNum(int arg0, View arg1, ViewGroup arg2){
		String name=null;
		final Map<String, String> good=list.get(arg0);
		name=good.get("for_num");
		return name;
	}
	public String getDate(int arg0, View arg1, ViewGroup arg2){
		String name=null;
		final Map<String, String> good=list.get(arg0);
		name=good.get("for_date");
		return name;
	}
	public String getForNr(int arg0, View arg1, ViewGroup arg2){
		String name=null;
		final Map<String, String> good=list.get(arg0);
		name=good.get("for_nr");
		return name;
	}
	public String getTxUrl(int arg0, View arg1, ViewGroup arg2){
		String url=null;
		final Map<String, String> good=list.get(arg0);
		url=good.get("for_tx");
		return url;
	}
	public String getImgUrl(int arg0, View arg1, ViewGroup arg2){
		String url=null;
		final Map<String, String> good=list.get(arg0);
		url=good.get("for_path");
		return url;
	}
	public String getImgXUrl(int arg0, View arg1, ViewGroup arg2){
		String url=null;
		final Map<String, String> good=list.get(arg0);
		url=good.get("for_pathX");
		return url;
	}
	public String getNow(int arg0, View arg1, ViewGroup arg2){
		String url=null;
		final Map<String, String> good=list.get(arg0);
		url=good.get("for_now");
		return url;
	}
	public void setView(int arg0, View arg1, ViewGroup arg2) {

	}

	@SuppressLint("CutPasteId")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final Map<String, String> good = list.get(arg0);
		String nr = good.get("pl_nr");
		String date = good.get("pl_date");
		String writer_username = good.get("from_name");
		String pl_where = good.get("pl_where");
		String for_nr=good.get("for_nr");
		String for_pl_nr=null;
		String from_now=good.get("from_now");
		if(good.get("for_pl_nr")!=null){
			for_pl_nr=good.get("for_pl_nr");
		}
		
		String type=good.get("pl_type");
		String from_tx_url=good.get("from_tx");

		if (arg1 == null) {
			arg1 = inflater.inflate(resource, null);

		}
		this.from_name=(TextView) arg1.findViewById(R.id.mg_writer_username);
		this.from_name.setText(writer_username+": ");
		this.from_tx=(ImageView) arg1.findViewById(R.id.mg_tx);
		if(from_tx_url.equals("no")){
			this.from_tx.setImageResource(R.drawable.ic_portrait);
		}else{
			String url=Welcome.URL + "/dasidog/tx/"+from_tx_url;
			this.from_tx.setTag(url);
			Welcome.mimageLoader.getImageByAsyncTask(from_tx, url,1);
		}
		this.pl_date=(TextView) arg1.findViewById(R.id.mg_date);
		this.pl_date.setText(date);
		this.message_nr=(TextView) arg1.findViewById(R.id.mg_nr);
		SpannableString spannableString_ = ParseEmojiMsgUtil.getExpressionString(this.Sy,nr);
		this.message_nr.setText(spannableString_);
		this.fb_where=(TextView) arg1.findViewById(R.id.mg_where);
		if(pl_where.equals("1")){
			this.fb_where.setText("工作社区");
		}
		if(pl_where.equals("2")){
			this.fb_where.setText("考研社区");
		}
		this.for_nr=(TextView) arg1.findViewById(R.id.for_nr);
		this.for_nr.setText(for_nr);
		if(type.equals("1")){
			this.pl_for=(TextView) arg1.findViewById(R.id.pl_for);
			SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(this.Sy,for_pl_nr);
			this.pl_for.setText(for_pl_nr);
		}
		this.now=(TextView) arg1.findViewById(R.id.mg_now);
		if(from_now.equals("1")){
			this.now.setText("大四");
		}else if(from_now.equals("2")){
			this.now.setText("毕业");
		}
		this.card = (RelativeLayout) arg1.findViewById(R.id.sy_card);
		/*this.card.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it_to_a = new Intent(Sy, SyArticle.class);
				it_to_a.putExtra("user_id", user_id);
				it_to_a.putExtra("writer_id", good.get("writer_id"));
				it_to_a.putExtra("writer_name", good.get("username"));
				it_to_a.putExtra("text_id", good.get("where_id"));
				it_to_a.putExtra("pl_num", good.get("pl_num"));
				it_to_a.putExtra("nr", good.get("article"));
				it_to_a.putExtra("date", good.get("date"));
				it_to_a.putExtra("now", good.get("fb_where"));
				Sy.startActivity(it_to_a);

			}
		});*/

		return arg1;

	}

	private byte[] getUrlData(String tx) throws Exception {
		ByteArrayOutputStream bos = null;
		try {
			System.out.println(tx);
			URL url = new URL(tx);
			bos = new ByteArrayOutputStream();
			byte data[] = new byte[50];
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream input = conn.getInputStream();
			int len = 0;
			while ((len = input.read(data)) != -1) {
				bos.write(data, 0, len);
			}
			return bos.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			if (bos != null) {
				bos.close();
			}
		}

	}

	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Rect rect;
		if (width >= height) {
			rect = new Rect((width - height) / 2, 0, (width - height) / 2
					+ height, height);
		} else {
			rect = new Rect(0, (height - width) / 2, width, width
					+ (height - width) / 2);
		}
		RectF rectF = new RectF(rect);
		canvas.drawOval(rectF, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rectF, paint);
		return output;
	}

}