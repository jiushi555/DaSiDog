package com.example.set;

import com.example.DasiDog.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class About_Us extends Activity {
	private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		back=(ImageView) findViewById(R.id.btnBack);
		back.setOnClickListener(new backListener());
	}
	class backListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			About_Us.this.finish();
			
		}
		
	}
}
