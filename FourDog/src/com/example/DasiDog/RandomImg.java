package com.example.DasiDog;

import android.widget.ImageView;
import android.widget.RelativeLayout;

public class RandomImg {
	private int mItem;
	private ImageView mHeader,mVatar;
	private RelativeLayout mBg;
	public  RandomImg(int item,ImageView header,RelativeLayout bg,ImageView avatar){
		mItem=item;
		mHeader=header;
		mBg=bg;
		mVatar=avatar;
		switch (mItem) {
		case 0:
			mBg.setBackgroundResource(R.drawable.comment_bg_01);
			mHeader.setImageResource(R.drawable.bg_feed_top_01);
			mVatar.setImageResource(R.drawable.avatar01);
			break;
		case 1:
			mBg.setBackgroundResource(R.drawable.comment_bg_02);
			mHeader.setImageResource(R.drawable.bg_feed_top_02);
			mVatar.setImageResource(R.drawable.avatar02);
			break;
		case 2:
			mBg.setBackgroundResource(R.drawable.comment_bg_03);
			mHeader.setImageResource(R.drawable.bg_feed_top_03);
			mVatar.setImageResource(R.drawable.avatar03);
			break;
		case 3:
			mBg.setBackgroundResource(R.drawable.comment_bg_04);
			mHeader.setImageResource(R.drawable.bg_feed_top_04);
			mVatar.setImageResource(R.drawable.avatar04);

			break;
		case 4:
			mBg.setBackgroundResource(R.drawable.comment_bg_05);
			mHeader.setImageResource(R.drawable.bg_feed_top_05);
			mVatar.setImageResource(R.drawable.avatar05);

			break;
		case 5:
			mBg.setBackgroundResource(R.drawable.comment_bg_06);
			mHeader.setImageResource(R.drawable.bg_feed_top_06);
			mVatar.setImageResource(R.drawable.avatar06);

			break;
		case 6:
			mBg.setBackgroundResource(R.drawable.comment_bg_07);
			mHeader.setImageResource(R.drawable.bg_feed_top_07);
			mVatar.setImageResource(R.drawable.avatar07);

			break;
		case 7:
			mBg.setBackgroundResource(R.drawable.comment_bg_08);
			mHeader.setImageResource(R.drawable.bg_feed_top_08);
			mVatar.setImageResource(R.drawable.avatar08);

			break;
		case 8:
			mBg.setBackgroundResource(R.drawable.comment_bg_09);
			mHeader.setImageResource(R.drawable.bg_feed_top_09);
			mVatar.setImageResource(R.drawable.avatar10);

			break;
		case 9:
			mBg.setBackgroundResource(R.drawable.comment_bg_10);
			mHeader.setImageResource(R.drawable.bg_feed_top_10);
			mVatar.setImageResource(R.drawable.avatar03);

			break;
		default:
			break;
		}
	}
	
		
}
