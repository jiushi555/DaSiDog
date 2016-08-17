package com.example.image;

import com.example.DasiDog.R;
import com.example.DasiDog.Welcome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageX extends Activity {
	private String img_url;
	private ImageView imageview;
	private String url;
	private ProgressDialog pg=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		Intent it=super.getIntent();
		img_url=it.getStringExtra("pathX");
		url=Welcome.URL + "/dasidog/img/"+img_url;
		new Update().execute();
		imageview=(ImageView) findViewById(R.id.imgView);

	}
	private class Update extends AsyncTask<String, String, String>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pg=ProgressDialog.show(ImageX.this,null,null);
			pg.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			imageview.setTag(url);
			Welcome.mimageLoader.getImageByAsyncTask(imageview, url,2);
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			pg.dismiss();
			super.onPostExecute(result);
		}
		
	}
}
