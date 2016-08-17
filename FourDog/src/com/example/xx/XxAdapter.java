package com.example.xx;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.example.DasiDog.R;
import com.example.DasiDog.Welcome;
import com.example.Sy.SyArticle;

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



public class XxAdapter extends BaseAdapter {

	List<Map<String, String>> list;

	private int resource;

	private LayoutInflater inflater;

	private Context Gz;
	private TextView sy_nr;
	private TextView writer_username;
	private ImageView writer_tx;
	private TextView id;
	private RelativeLayout card;
	private int user_id;
	private TextView comment;
	private TextView sy_date;
	private TextView writer_id_text;
	private TextView pl_num;
	private TextView sy_id;
	private LinearLayout sy_list_layout;
	private TextView pl_num_;
	private RemoteImageHelper lazyImageHelper;
	private TextView now_txt;

	public XxAdapter(List<Map<String, String>> list, int resource,
			Context context, int user_id) {
		this.lazyImageHelper = new RemoteImageHelper();
		this.list = list;

		this.resource = resource;

		this.Gz = context;
		this.user_id = user_id;
		inflater = (LayoutInflater) Gz
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

	public String getText(int arg0, View arg1, ViewGroup arg2) {
		String text = null;
		final Map<String, String> good = list.get(arg0);
		text = good.get("article");
		return text;
	}

	public String getTextId(int arg0, View arg1, ViewGroup arg2) {
		String id = null;
		final Map<String, String> good = list.get(arg0);
		id = good.get("id");
		return id;
	}

	public String getWriterId(int arg0, View arg1, ViewGroup arg2) {
		String writer = null;
		final Map<String, String> good = list.get(arg0);
		writer = good.get("writer_id");
		return writer;
	}

	public void setView(int arg0, View arg1, ViewGroup arg2) {

	}

	@SuppressLint("CutPasteId")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final Map<String, String> good = list.get(arg0);
		String nr = good.get("article");
		String writer = good.get("writer_id");
		String Text_id = good.get("id");
		String pl_num = good.get("pl_num");
		String date = good.get("date");
		String writer_username = good.get("username");
		String now =good.get("now");

		if (arg1 == null) {

			arg1 = inflater.inflate(resource, null);

		}
		this.now_txt=(TextView) arg1.findViewById(R.id.now);
		if(now.equals("1")){
			this.now_txt.setText("大四");
		}
		if(now.equals("2")){
			this.now_txt.setText("毕业");
		}
		this.writer_username = (TextView) arg1
				.findViewById(R.id.sy_writer_username);
		this.writer_username.setText(writer_username);
		this.writer_tx = (ImageView) arg1.findViewById(R.id.sy_tx);
		writer_tx.setImageResource(R.drawable.ic_portrait);
		/*if (path.equals("no")) {
			writer_tx.setImageResource(R.drawable.ic_portrait);
		} else {
			String url_tx = Welcome.URL + "/dasidog/img/" + path;
			lazyImageHelper.loadImage(writer_tx, url_tx, false);
			
			 * try{ byte data[]=getUrlData(url_tx); Bitmap
			 * bm=BitmapFactory.decodeByteArray(data, 0, data.length);
			 * writer_tx.setImageBitmap(toRoundBitmap(bm)); }catch(Exception e){
			 * e.printStackTrace(); }
			 

		}*/
		this.sy_nr = (TextView) arg1.findViewById(R.id.sy_nr);
		this.sy_nr.setText(nr);
		this.id = (TextView) arg1.findViewById(R.id.sy_txt_id);
		this.id.setText(Text_id);
		this.sy_date = (TextView) arg1.findViewById(R.id.sy_date);
		this.sy_date.setText(date);
		this.writer_id_text = (TextView) arg1.findViewById(R.id.sy_writer_id);
		this.writer_id_text.setText(writer);
		this.pl_num = (TextView) arg1.findViewById(R.id.sy_pl_num_);
		this.pl_num.setText(pl_num);
		this.pl_num_ = (TextView) arg1.findViewById(R.id.sy_pl_num_);
		this.pl_num_.setText(pl_num);
		this.card = (RelativeLayout) arg1.findViewById(R.id.sy_card);
		this.sy_list_layout = (LinearLayout) arg1
				.findViewById(R.id.sy_list);
		/*this.card.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it_to_a = new Intent(Gz, XxArticle.class);
				it_to_a.putExtra("user_id", user_id);
				it_to_a.putExtra("writer_id", good.get("writer_id"));
				it_to_a.putExtra("writer_name", good.get("username"));
				it_to_a.putExtra("text_id", good.get("id"));
				it_to_a.putExtra("pl_num", good.get("pl_num"));
				it_to_a.putExtra("nr", good.get("article"));
				it_to_a.putExtra("date", good.get("date"));
				Gz.startActivity(it_to_a);
			}

		});*/
		/*this.sy_list_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(wdwt, Wdwt_article.class);
				it.putExtra("username", username);
				it.putExtra("user_id", user_id);
				it.putExtra("text_id", good.get("id"));
				it.putExtra("wdwt_nr", good.get("article"));
				it.putExtra("writer", good.get("writer_id"));
				it.putExtra("pl_num", good.get("pl_num"));
				it.putExtra("wdwt_title", good.get("title"));
				it.putExtra("wdwt_date", good.get("date"));
				wdwt.startActivity(it);
			}
		});
		*/

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