package com.example.theme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.DasiDog.R;
import com.example.DasiDog.RandomImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ThemeContentAdapter extends BaseAdapter {
	List<Map<String, String>> list;
	private int resource;
	private LayoutInflater inflater;
	private Context context;
	private RemoteImageHelper lazyImageHelper;
	private int user_id;
	private TextView content_txt, content_date;
	private ImageView avatar,content_top;
	private RelativeLayout content_layout;
	private RandomImg mRandomImg;

	public ThemeContentAdapter(List<Map<String, String>> list, int resource,
			Context context, int user_id) {
		this.lazyImageHelper = new RemoteImageHelper();
		this.list = list;
		this.context = context;
		this.resource = resource;
		this.user_id = user_id;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Map<String, String> map=list.get(position);
		String content=map.get("content");
		String writer=map.get("writer");
		String id=map.get("id");
		String date=map.get("date");
		String avatar_id=map.get("avatar_id");
		if(convertView==null){
			convertView=inflater.inflate(resource, null);
		}
		content_txt=(TextView) convertView.findViewById(R.id.content);
		content_layout=(RelativeLayout) convertView.findViewById(R.id.content_middle);
		content_top=(ImageView) convertView.findViewById(R.id.content_top);
		avatar=(ImageView) convertView.findViewById(R.id.content_avatar);
		content_date=(TextView) convertView.findViewById(R.id.text_date);
		content_txt.setText(content);
		content_date.setText(date);
		int avatarId_int=Integer.parseInt(avatar_id);
		mRandomImg=new RandomImg(avatarId_int, content_top, content_layout, avatar);
		
		return convertView;
	}

}
