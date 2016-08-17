package com.example.DasiDog;

import com.example.set.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Xx extends Activity {
	private ImageView add;
	private int user_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xx);
		Intent super_it=super.getIntent();
		user_id=super_it.getIntExtra("user_id", 0);
		add=(ImageView) findViewById(R.id.img_set);
		add.setOnClickListener(new setOnClick());
		
	}
	class setOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent it = new Intent(Xx.this, Set.class);
			it.putExtra("user_id", user_id);
			Xx.this.startActivity(it);
		}
		
	}
}
