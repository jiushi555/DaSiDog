package com.example.xx;

import com.example.DasiDog.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Notion extends Activity {
	private int user_id;
	private String notion;
	private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent it=super.getIntent();
		user_id=it.getIntExtra("user_id", 0);
		notion=it.getStringExtra("notion");
		if(notion.equals("0")){
			Notion.this.setContentView(R.layout.notion_no);
		}else{
			Notion.this.setContentView(R.layout.notion);
		}
		back=(ImageView) findViewById(R.id.btnBack_notice);
		back.setOnClickListener(new backOnClick());
	}
	private class backOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			Notion.this.finish();
		}
		
	}
}
