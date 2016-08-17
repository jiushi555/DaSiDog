package com.example.DasiDog;

import com.example.DasiDog.Login.gozcClickListener;
import com.example.DasiDog.Login.xsPwdOnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Zc_1 extends Activity {
	private TextView zc_next;
	private CheckBox cdpwd;
	private EditText pwd;
	private EditText username;
	private String username_txt;
	private String pwd_txt;
	private TextView zc_dl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zc_1);
		zc_next=(TextView) findViewById(R.id.zc_next);
		zc_next.setOnClickListener(new zcnextOnClickListener());
		cdpwd = (CheckBox) findViewById(R.id.cbPassword);
		pwd = (EditText) findViewById(R.id.etPassword);
		zc_dl=(TextView) findViewById(R.id.zc_dl_1);
		zc_dl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it=new Intent(Zc_1.this,Login.class);
				Zc_1.this.startActivity(it);
				Zc_1.this.finish();
				
			}
		});
		cdpwd.setOnClickListener(new xsPwdOnClick());
		username=(EditText) findViewById(R.id.etUserName);
	}
	class xsPwdOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (cdpwd.isChecked()) {
				pwd.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
			} else {
				pwd.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
			}

		}

	}
	class zcnextOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			username_txt=username.getText().toString();
			pwd_txt=pwd.getText().toString();
			if(username_txt.equals("")){
				Toast.makeText(Zc_1.this,"请输入用户名", Toast.LENGTH_SHORT).show();
			}else if(pwd_txt.equals("")) {
				Toast.makeText(Zc_1.this, "请输入密码", Toast.LENGTH_LONG).show();
			}else{
				Intent it=new Intent(Zc_1.this,Zc_2.class);
				it.putExtra("zc_username", username_txt);
				it.putExtra("zc_pwd", pwd_txt);
				startActivity(it);
			}
			
			
		}
		
	}
}
