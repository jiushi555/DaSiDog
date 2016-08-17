package com.example.theme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.DasiDog.JSONParser;
import com.example.DasiDog.R;
import com.example.DasiDog.RandomImg;
import com.example.DasiDog.Welcome;
import com.example.ProgressDialog.CustomProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddTheme extends Activity {
	private String id,content_txt,title,context;
	private String url=Welcome.URL + "/dasidog/theme_pl.php";
	private int user_id, randomId,success;
	private ImageView avatar, bg_header;
	private RelativeLayout bg;
	private RandomImg rdImage;
	private EditText content;
	private TextView back,send;
	private CustomProgressDialog dialog;
	private JSONParser jsonParser=new JSONParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_theme);
		Intent it = super.getIntent();
		id = it.getStringExtra("theme_id");
		user_id = it.getIntExtra("user_id", 0);
		title=it.getStringExtra("title");
		context=it.getStringExtra("nr");
		initView();
		avatar.setVisibility(View.VISIBLE);
		int item = (int) (Math.random() * 10);
		randomId=item;
		rdImage = new RandomImg(item, bg_header, bg, avatar);
	}
	private void initView(){
		bg = (RelativeLayout) findViewById(R.id.rl_middle);
		avatar = (ImageView) findViewById(R.id.item_header_image);
		bg_header = (ImageView) findViewById(R.id.iv_top);
		content=(EditText) findViewById(R.id.et_content);
		back=(TextView) findViewById(R.id.theme_back);
		send=(TextView) findViewById(R.id.theme_publish);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AddTheme.this.finish();
			}
		});
		send.setOnClickListener(new SendOnClick());
	}
	private class SendOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			AddTheme.this.content_txt= content.getText().toString();
			if(AddTheme.this.content_txt.equals("")){
				Toast.makeText(AddTheme.this, "请输入内容！", Toast.LENGTH_SHORT).show();
			}else{
				new Send().execute();
			}
		}
		
	}
	private class Send extends AsyncTask<String, String, String>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog=CustomProgressDialog.createDialog(AddTheme.this);
			dialog.setMessage("发送中...");
			dialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> params1=new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("user_id", String.valueOf(user_id)));
			params1.add(new BasicNameValuePair("content",content_txt));
			params1.add(new BasicNameValuePair("avatar_id", String.valueOf(randomId)));
			params1.add(new BasicNameValuePair("theme_id", id));
			JSONObject json=jsonParser.makeHttpRequest(url, "POST", params1, AddTheme.this);
			try {
				AddTheme.this.success=json.getInt("success");
			} catch (JSONException e) {
				e.printStackTrace();
			}	
				
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if(AddTheme.this.success==1){
				Toast.makeText(AddTheme.this, "发布成功", Toast.LENGTH_SHORT).show();
				Intent it=new Intent(AddTheme.this,ThemeContext.class);
				it.putExtra("user_id", user_id);
				it.putExtra("id", id);
				it.putExtra("title", title);
				it.putExtra("nr", context);
				it.putExtra("load_type", "2");
				AddTheme.this.startActivity(it);
				AddTheme.this.finish();
			}else{
				Toast.makeText(AddTheme.this, "发布失败,刷新试试！", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
