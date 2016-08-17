package com.example.Sy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.example.DasiDog.R;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
 * A helper class facilitates loading a remote image into a given ListView.
 * http://www.cnblogs.com/bjzhanghao/archive/2012/11/11/2764970.html
 * 
 * @author zhanghao
 *
 */
public class RemoteImageHelper {

	private final Map<String, Bitmap> cache = new HashMap<String, Bitmap>();

	public void loadImage(final ImageView imageView, final String urlString) {
		loadImage(imageView, urlString, true);
	}

	public void loadImage(final ImageView imageView, final String urlString, boolean useCache) {
		if (useCache && cache.containsKey(urlString)) {
			imageView.setImageBitmap(cache.get(urlString));
		}

		//You may want to show a "Loading" image here
		imageView.setImageResource(R.drawable.image_indicator);

		Log.d(this.getClass().getSimpleName(), "Image url:" + urlString);

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageView.setImageBitmap((Bitmap) message.obj);
			}
		};

		Runnable runnable = new Runnable() {
			public void run() {
				Bitmap bmzz=null;
				try {
					byte data[]=download(urlString);
					Bitmap bm=BitmapFactory.decodeByteArray(data, 0, data.length);
					bmzz=toRoundBitmap(bm);
					if (bm != null) {
						cache.put(urlString, bm);
					}
				} catch (Exception e) {
					Log.e(this.getClass().getSimpleName(), "Image download failed", e);
					//Show "download fail" image 
					imageView.setImageResource(R.drawable.image_indicator);
				}
				
				//Notify UI thread to show this image using Handler
				Message msg = handler.obtainMessage(1, bmzz);
				handler.sendMessage(msg);
			}
		};
		new Thread(runnable).start();

	}

	/**
	 * Download image from given url.
	 * Make sure you have "android.permission.INTERNET" permission set in AndroidManifest.xml.
	 * 
	 * @param urlString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	
	private byte[] download(String urlString) throws MalformedURLException, IOException {
		ByteArrayOutputStream bos=null;
		InputStream input = (InputStream) new URL(urlString).getContent();
		bos =new ByteArrayOutputStream();
		byte data[]=new byte[50];
		int len=0;
		while((len=input.read(data))!=-1){
			bos.write(data,0,len);
		}
		return bos.toByteArray();
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
