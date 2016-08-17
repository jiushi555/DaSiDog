package com.example.guide;

import java.util.ArrayList;
import java.util.List;

import com.example.DasiDog.Login;
import com.example.DasiDog.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class Guide extends Activity implements  OnPageChangeListener{
	private ViewPager viewpager;
	private int[] mImageIds = new int[] { R.drawable.guide1,
			R.drawable.guide2, R.drawable.guide3,R.drawable.guide4 };
	private List<LinearLayout> list = new ArrayList<LinearLayout>();
	private ImageView[] dot=null;
	private int currentIndex;
	private LinearLayout dotLayout=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		dotLayout=(LinearLayout) findViewById(R.id.ll);
		initDot();
		viewpager = (ViewPager) findViewById(R.id.guide_viewpage);
		viewpager.setOnPageChangeListener(this);
		// 为viewpager添加动画
		viewpager.setPageTransformer(true, new DepthPageTransformer());
		
		viewpager.setAdapter(new PagerAdapter() {

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				LayoutInflater viewInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View convertView0 = viewInflater.inflate(R.layout.guide_item, null);  
		        LinearLayout linearLayout = (LinearLayout) convertView0  
		                .findViewById(R.id.guide_item);  
				linearLayout.setBackgroundResource(mImageIds[position]);
				if(position==3){
					Button btn=(Button) convertView0.findViewById(R.id.start);
					btn.setVisibility(View.VISIBLE);
					btn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent it=new Intent(Guide.this,Login.class);
							Guide.this.startActivity(it);
							Guide.this.finish();
							
						}
					});
				}
				/*ImageView imageview = new ImageView(Guide.this);
				imageview.setImageResource(mImageIds[position]);
				imageview.setScaleType(ScaleType.CENTER_CROP);*/
				container.addView(linearLayout);
				list.add(linearLayout);
				return linearLayout;
			}
			public void onPageSelected(int arg0) {  
				System.out.println(arg0);
		    }  
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(list.get(position));
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return mImageIds.length;
			}
		});
	}
	public void initDot(){
		currentIndex=0;
        dot = new ImageView[mImageIds.length];  
  
        //循环取得小点图片  
        for (int i = 0; i < mImageIds.length; i++) {  
            dot[i] = (ImageView) dotLayout.getChildAt(i);  
            dot[i].setEnabled(true);//都设为灰色    
            dot[i].setTag(i);//设置位置tag，方便取出与当前位置对应  
        }  
        dot[currentIndex].setEnabled(false);
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		dot[arg0].setEnabled(false);
		dot[currentIndex].setEnabled(true);
		currentIndex=arg0;
	}
	
}
