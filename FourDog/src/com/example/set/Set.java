package com.example.set;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.DasiDog.Login;
import com.example.DasiDog.R;
import com.example.DasiDog.Welcome;
import com.example.me.Me_Main;
import com.example.update.UpdateManager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Set extends Activity {
	private ImageView back_btn;
	private TextView quit;
	private TextView version_txt;
	private TextView version_have;
	private RelativeLayout rlFeedback;
	private RelativeLayout rlsetpwd;
	private RelativeLayout about_us;
	private RelativeLayout melayout;
	private RelativeLayout update;
	private int user_id;
	private RelativeLayout tjFriends;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		setContentView(R.layout.activity_set);
		Intent super_it=super.getIntent();
		user_id=super_it.getIntExtra("user_id", 0);
		back_btn=(ImageView) findViewById(R.id.btnBack);
		back_btn.setOnClickListener(new backOnClick());
		quit=(TextView) findViewById(R.id.tvQuit);
		rlFeedback=(RelativeLayout) findViewById(R.id.rlFeedback);
		rlFeedback.setOnClickListener(new feedOnClick());
		rlsetpwd=(RelativeLayout) findViewById(R.id.rlSetPassword);
		rlsetpwd.setOnClickListener(new resetPwd());
		quit.setOnClickListener(new quitOnClick());
		about_us=(RelativeLayout) findViewById(R.id.rlAboutUs);
		about_us.setOnClickListener(new aboutOnClickListener());
		melayout=(RelativeLayout) findViewById(R.id.rlSetData);
		melayout.setOnClickListener(new meOnClickListener());
		tjFriends=(RelativeLayout) findViewById(R.id.tj_friends);
		tjFriends.setOnClickListener(new TjFriendsOnClick());
		update=(RelativeLayout) findViewById(R.id.update);
		update.setOnClickListener(new updateOnClick());
		version_txt=(TextView) findViewById(R.id.version);
		version_txt.setText(String.valueOf(Welcome.version));
		version_have=(TextView) findViewById(R.id.update_have);
		if(Welcome.version_boolean){
			version_have.setVisibility(View.VISIBLE);
		}else{
			version_have.setVisibility(View.GONE);
		}
	}
	class updateOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			UpdateManager manager = new UpdateManager(Set.this);
			// 检查软件更新
			manager.checkUpdate();
		}
		
	}
	class TjFriendsOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			showShare() ;
		}
		
	}
//	shareSDK 社会化分享
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle("大四狗");
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("在这里有大四的大四狗，有毕业的工作狗和研狗，我们可以畅所欲言，谈谈工作，谈谈生活，谈谈未来。。。当然我们也欢迎低年级的小鲜肉");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		 oks.setImagePath(String.valueOf(R.drawable.logo));//确保SDcard下面存在此张图片
		 oks.setImageUrl("http://yjx555.lin8.siteonlinetest.com/logo.gif");
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(this);
		 }
	
	
	class meOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent it_me=new Intent(Set.this,Me_Main.class);
			it_me.putExtra("user_id", user_id);
			Set.this.startActivity(it_me);
		}
		
	}
	class aboutOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent it_us=new Intent(Set.this,About_Us.class);
			Set.this.startActivity(it_us);
			
		}
		
	}
	class resetPwd implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent resetpwd=new Intent(Set.this,ResetPwd_1.class);
			resetpwd.putExtra("user_id", user_id);
			Set.this.startActivity(resetpwd);		
		}
		
	}
	class feedOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent feed=new Intent(Set.this,FeedBack.class);
			Set.this.startActivity(feed);
			
		}
		
	}
	class quitOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent it_quit=new Intent(Set.this,Login.class);
			Set.this.startActivity(it_quit);
		}
		
	}
	class backOnClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			Set.this.finish();
		}
		
	}
}
