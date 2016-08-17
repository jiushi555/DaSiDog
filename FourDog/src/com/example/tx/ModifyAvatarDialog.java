package com.example.tx;


import com.example.DasiDog.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyAvatarDialog extends Dialog implements OnClickListener {

	private LayoutInflater factory;
	
	private Button mImg;

	private Button mPhone;

	private Button mCancel;

	public ModifyAvatarDialog(Context context) {
		super(context);
		factory = LayoutInflater.from(context);
	}

	public ModifyAvatarDialog(Context context, int theme) {
		super(context, theme);
		factory = LayoutInflater.from(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(factory.inflate(R.layout.gl_modify_avatar_choose_dialog, null));
		mImg = (Button) this.findViewById(R.id.gl_choose_img);
		mPhone = (Button) this.findViewById(R.id.gl_choose_phone);
		mCancel = (Button) this.findViewById(R.id.gl_choose_cancel);
		mImg.setOnClickListener(this);
		mPhone.setOnClickListener(this);
		mCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gl_choose_img:
			doGoToImg();
			break;
		case R.id.gl_choose_phone:
			doGoToPhone();
			break;
		case R.id.gl_choose_cancel:
			dismiss();
			break;
		}
	}
	
	public void doGoToImg(){
	}
	public void doGoToPhone(){
	}
}
