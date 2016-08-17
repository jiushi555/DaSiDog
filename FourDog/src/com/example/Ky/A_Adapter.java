package com.example.Ky;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.example.DasiDog.R;
import com.example.DasiDog.Welcome;
import com.example.Sy.SyArticle;
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



public class A_Adapter extends BaseAdapter {

	List<Map<String, String>> list;

	private int resource;

	private LayoutInflater inflater;

	private Context Gz_pl;
	private TextView writer_id_txt;
	private TextView writer_name_txt;
	private TextView txt_id_txt;
	private TextView pl_id_txt;
	private TextView pl_nr_txt;
	private TextView pl_date_txt;
	private int user_id;
	private TextView now_txt;
	private RemoteImageHelper lazyImageHelper;
	private ImageView user_tx;

	public A_Adapter(List<Map<String, String>> list, int resource,
			Context context, int user_id) {
		this.lazyImageHelper = new RemoteImageHelper();
		this.list = list;

		this.resource = resource;

		this.Gz_pl = context;
		this.user_id = user_id;
		inflater = (LayoutInflater) Gz_pl
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
		text = good.get("nr");
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
		writer = good.get("from");
		return writer;
	}
	public String getWriterName(int arg0, View arg1, ViewGroup arg2) {
		String writer = null;
		final Map<String, String> good = list.get(arg0);
		writer = good.get("from_name");
		return writer;
	}
	public void setView(int arg0, View arg1, ViewGroup arg2) {

	}

	@SuppressLint("CutPasteId")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final Map<String, String> good = list.get(arg0);
		String nr = good.get("nr");
		String writer = good.get("from");
		String Text_id = good.get("id");
		String where_id = good.get("where_id");
		String date = good.get("pl_date");
		String to=good.get("to");
		String type=good.get("type");

		String from_name = good.get("from_name");
		String to_name = good.get("to_name");
		String from_now = good.get("from_now");
		String user_tx_url=good.get("user_tx");
		if (arg1 == null) {

			arg1 = inflater.inflate(resource, null);

		}
		this.writer_id_txt = (TextView) arg1.findViewById(R.id.writer_name);
		if (type.equals("0")) {
			this.writer_id_txt.setText(from_name);
		} else if (type.equals("1")) {
			this.writer_id_txt.setText(from_name + " //回复@" + to_name + "：");
		}
		this.now_txt = (TextView) arg1.findViewById(R.id.now);
		if (from_now.equals("1")) {
			this.now_txt.setText("大四");
		}
		if (from_now.equals("2")) {
			this.now_txt.setText("毕业");
		}
		this.user_tx=(ImageView) arg1.findViewById(R.id.pl_portrati);
		if(user_tx_url.equals("no")){
			this.user_tx.setImageResource(R.drawable.ic_portrait);
		}else{
			String url=Welcome.URL + "/dasidog/tx/"+user_tx_url;
			this.user_tx.setTag(url);
			Welcome.mimageLoader.getImageByAsyncTask(user_tx, url,1);
		}
		this.pl_nr_txt=(TextView) arg1.findViewById(R.id.pl_nr);
		SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(this.Gz_pl, nr);
		this.pl_nr_txt.setText(spannableString);
		this.pl_date_txt=(TextView) arg1.findViewById(R.id.date_text);
		this.pl_date_txt.setText(date);
		this.txt_id_txt=(TextView) arg1.findViewById(R.id.txt_id);
		this.txt_id_txt.setText(Text_id);

		return arg1;

	}
	public String getId(int arg0, View arg1, ViewGroup arg2){
		String id = null;
		final Map<String, String> good = list.get(arg0);
		id = good.get("id");
		return id;
	}
	public String getFromId(int arg0,View arg1,ViewGroup arg2){
		String from=null;
		final Map<String, String> good=list.get(arg0);
		from=good.get("from");
		return from;
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