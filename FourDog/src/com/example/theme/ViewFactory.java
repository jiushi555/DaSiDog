package com.example.theme;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.example.DasiDog.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * ImageView创建工厂
 */
public class ViewFactory {

	/**
	 * 获取ImageView视图的同时加载显示url
	 * 
	 * @param text
	 * @return
	 */
	public static ImageView getImageView(Context context, int url) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		imageView.setImageResource(url);
		return imageView;
	}
}
