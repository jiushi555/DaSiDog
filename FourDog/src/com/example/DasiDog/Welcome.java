package com.example.DasiDog;

import com.example.CheckNetWork.CheckNetWork;
import com.example.guide.Guide;
import com.example.loader.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class Welcome extends Activity implements AnimationListener {
	private ImageView logo;
	private ImageView logo_name;
//	public static String URL = "http://192.168.1.102:82";
	public static String URL = "http://yjx555.lin8.siteonlinetest.com";
	public static String new_version=null; 
	public static String new_url=null;
	public static int version_id=0;
	public static String version=null;
	public static Boolean version_boolean=false;
	private DatabaseHelper helper;
	private DataOperate operate;
	private int user_id;
	private String username;
	private int now;
	private boolean isFirstUse;
	private SharedPreferences preferences = null;
	public static ImageLoader mimageLoader;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		mimageLoader=new ImageLoader();	
		beginAnimation();
	}

	

	private void beginAnimation() {
		preferences = getSharedPreferences("isFirstUse", MODE_WORLD_READABLE);
		isFirstUse = preferences.getBoolean("isFirstUse", true);
		try {
			version_id=Welcome.this.getPackageManager().getPackageInfo("com.example.DasiDog", 0).versionCode;
			version=Welcome.this.getPackageManager().getPackageInfo("com.example.DasiDog", 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Welcome.this.helper = new DatabaseHelper(Welcome.this);
		Welcome.this.operate = new DataOperate(
				Welcome.this.helper.getWritableDatabase());
		Welcome.this.logo = (ImageView) this.findViewById(R.id.welcome_id);
		Welcome.this.logo_name = (ImageView) this.findViewById(R.id.logo_name);
		Animation anim_logo = AnimationUtils.loadAnimation(Welcome.this,
				R.anim.welcome_anim);
		Welcome.this.logo.setAnimation(anim_logo);
		Animation anim_logo_name = AnimationUtils.loadAnimation(Welcome.this,
				R.anim.welcome_anim);
		Welcome.this.logo_name.setAnimation(anim_logo_name);
		anim_logo.setAnimationListener(this);
		// anim_logo_name.setAnimationListener(this);
	}

	@Override
	public void onAnimationEnd(Animation animation) {

		if (isFirstUse) {
			Intent it = new Intent(Welcome.this, Guide.class);
			startActivity(it);
			this.finish();
		} else {
			Welcome.this.user_id = operate.select_id();
			if (user_id != 0) {
				Welcome.this.username = operate.select_name();
				Welcome.this.now = operate.select_now();
				Intent it = new Intent(Welcome.this, Sy_Tab.class);
				it.putExtra("user_id", user_id);
				it.putExtra("username", username);
				it.putExtra("now", now);
				startActivity(it);
				this.finish();
			} else {

				Intent it = new Intent(Welcome.this, Login.class);
				startActivity(it);
				this.finish();
			}
		}
		finish();

		Editor editor = preferences.edit();
		editor.putBoolean("isFirstUse", false);
		editor.commit();

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

}
