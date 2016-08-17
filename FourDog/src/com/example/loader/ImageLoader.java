package com.example.loader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpConnection;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class ImageLoader {
	private LruCache<String, Bitmap> mcache;

	public ImageLoader() {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mcache = new LruCache<String, Bitmap>(cacheSize) {
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
	}

	public void addBitmapToCache(String url, Bitmap bitmap) {
		if (getBitmapFromCache(url) == null) {
			mcache.put(url, bitmap);
		}
	}

	public Bitmap getBitmapFromCache(String url) {
		return mcache.get(url);

	}

	public void getImageByAsyncTask(ImageView imageview, String url,int type) {
		Bitmap bitmap = getBitmapFromCache(url);
		if (bitmap == null) {
			new NewSAsyncTask(imageview, url,type).execute(url);
		} else {
			imageview.setImageBitmap(bitmap);
		}

	}

	private class NewSAsyncTask extends AsyncTask<String, Void, Bitmap> {
		private ImageView mimageView;
		private String mUrl;
		private int mType;

		public NewSAsyncTask(ImageView imageview, String url,int type) {
			mimageView = imageview;
			mUrl = url;
			mType=type;

		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String url=params[0];
			Bitmap bitmap=getBitmapFromUrl(mUrl,mType);
			if(bitmap!=null){
				addBitmapToCache(url, bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			if (mimageView.getTag().equals(mUrl)) {
				mimageView.setImageBitmap(bitmap);
			}

		}

	}

	public Bitmap getBitmapFromUrl(String urlString,int type) {
		Bitmap bitmap;
		Bitmap outbitmap;
		InputStream is = null;
		try {
			URL url = new URL(urlString);
			try {
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				is = new BufferedInputStream(connection.getInputStream());
				bitmap = BitmapFactory.decodeStream(is);
				if(type==1){
					outbitmap=toRoundBitmap(bitmap);
				}else{
					outbitmap=bitmap;
				}
				
				connection.disconnect();
				return outbitmap;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                        bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Rect rect;
        if (width >= height) {
                rect = new Rect((width - height) / 2, 0, (width - height) / 2
                                + height, height);
        } else {
                rect = new Rect(0, (height - width) / 2, width, width
                                + (height - width) / 2);
        }
        RectF rectF = new RectF(rect);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return output;
}
}
