package com.example.theme;

import java.util.List;
import java.util.Map;

import com.example.DasiDog.R;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ThemeAdapter extends BaseAdapter {
	List<Map<String, String>> list;

	private int resource;

	private LayoutInflater inflater;

	private Context context;
	private ImageView theme_title;
	private TextView theme_text;
	private TextView theme_num;
	private RemoteImageHelper lazyImageHelper;
	private int user_id;
	private RelativeLayout theme_layout;

	public ThemeAdapter(List<Map<String, String>> list, int resource,
			Context context, int user_id) {
		this.lazyImageHelper = new RemoteImageHelper();
		this.list = list;
		this.context = context;
		this.resource = resource;

		this.user_id = user_id;
		inflater = (LayoutInflater) context
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
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final Map<String, String> good = list.get(arg0);
		String title = good.get("title");
		String nr = good.get("nr");
		String num = good.get("num");
		String theme_id=good.get("id");
		if (arg1 == null) {
			arg1 = inflater.inflate(resource, null);
		}
		this.theme_layout=(RelativeLayout) arg1.findViewById(R.id.theme_lay);
		this.theme_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it=new Intent(context,ThemeContext.class);
				it.putExtra("id", good.get("id"));
				it.putExtra("title", good.get("title"));
				it.putExtra("nr", good.get("nr"));
				it.putExtra("user_id", user_id);
				context.startActivity(it);
			}
		});
		int id = arg0 + 1;
		this.theme_title = (ImageView) arg1.findViewById(R.id.theme_img);
		this.theme_text = (TextView) arg1.findViewById(R.id.theme_title);
		this.theme_num = (TextView) arg1.findViewById(R.id.theme_num);
		this.theme_text.setText("#"+title+"#");
		this.theme_num.setText(num);
		switch (id) {
		case 1:
			this.theme_title.setImageResource(R.drawable.a1);
			break;
		case 2:
			this.theme_title.setImageResource(R.drawable.a2);
			break;
		case 3:
			this.theme_title.setImageResource(R.drawable.a3);
			break;
		case 4:
			this.theme_title.setImageResource(R.drawable.a4);
			break;
		case 5:
			this.theme_title.setImageResource(R.drawable.a5);
			break;
		case 6:
			this.theme_title.setImageResource(R.drawable.a6);
			break;
		case 7:
			this.theme_title.setImageResource(R.drawable.a7);
			break;
		default:
			break;
		}

		return arg1;
	}

}
