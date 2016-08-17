package com.example.add;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.DasiDog.JSONParser;
import com.example.DasiDog.R;
import com.example.DasiDog.Sy_Tab;
import com.example.DasiDog.Welcome;
import com.example.Gz.Gz;
import com.example.Ky.Ky;
import com.example.ProgressDialog.CustomProgressDialog;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class Add_Ky extends Activity {
	private TextView back;
	private int user_id;
	private TextView add_btn;
	private String add_txt;
	private EditText add_edt;
	private String url = Welcome.URL + "/dasidog/add_ky.php";
	JSONParser jsonParser = new JSONParser();
	private CustomProgressDialog progressDialog = null;
	private int success;
	// 上传图片
	private ImageView add_img_btn;
	private static final String TAG = "upload";
	private Bitmap bitmap;
	private Bitmap bitmapX;
	private String img_fileL = "no";
	private String img_fileX = "no";
	private PopupWindow popwin = null;
	private View popView = null;
	private ImageView pz;
	private ImageView xc;
	private String url_img = Welcome.URL + "/dasidog/savetofile.php";
	private String url_img_ = Welcome.URL + "/dasidog/savetofile_.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_ky);
		Intent it = super.getIntent();
		user_id = it.getIntExtra("user_id", 0);
		back = (TextView) findViewById(R.id.ky_btn_left);
		add_btn = (TextView) findViewById(R.id.ky_publish);
		add_edt = (EditText) findViewById(R.id.ky_publish_text);
		add_btn.setOnClickListener(new addOnClick());
		back.setOnClickListener(new backOnClick());
		add_img_btn = (ImageView) findViewById(R.id.ky_add_img);
		add_img_btn.setOnClickListener(new addImgOnClick());
	}

	private class addImgOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			LayoutInflater inflater = LayoutInflater.from(Add_Ky.this);
			popView = inflater.inflate(R.layout.add_img, null);
			popwin = new PopupWindow(popView, 500, 400, true);
			popwin.setBackgroundDrawable(new BitmapDrawable());
			popwin.setOutsideTouchable(true);
			popwin.setFocusable(true);
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = 0.7f;
			getWindow().setAttributes(lp);
			popwin.setOnDismissListener(new OnDismissListener() {

				public void onDismiss() {
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.alpha = 1f;
					getWindow().setAttributes(lp);

				}
			});
			pz = (ImageView) popView.findViewById(R.id.imageView1);
			pz.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					takePhoto();
					popwin.dismiss();
				}
			});
			xc = (ImageView) popView.findViewById(R.id.imageView2);
			xc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					takeImage();
					popwin.dismiss();
				}
			});
			popwin.showAtLocation(Add_Ky.this.add_img_btn, Gravity.CENTER, 0, 0);

		}

	}

	private void takeImage() {
		dispatchTakePictureIntent(2);
	}

	private void takePhoto() {
		// Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		// startActivityForResult(intent, 0);
		dispatchTakePictureIntent(1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onActivityResult: " + this);
		if (requestCode == REQUEST_TAKE_PHOTO
				&& resultCode == Activity.RESULT_OK) {
			setPic();
			// Bitmap bitmap = (Bitmap) data.getExtras().get("data");
			// if (bitmap != null) {
			// mImageView.setImageBitmap(bitmap);
			// try {
			// sendPhoto(bitmap);
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
		} else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			if (uri != null) {
				String realPath = getRealPathFromURI(uri);
				Log.e("hao", "获取图片成功，path=" + realPath);

				setImageView(realPath);
			} else {
				Log.e("hao", "从相册获取图片失败");
			}
		}
	}

	private void setImageView(String realPath) {
		Bitmap bmp = compressImageFromFile(realPath);
		Bitmap bmpX = compressXImageFromFile(realPath);
		int degree = readPictureDegree(realPath);
		if (degree <= 0) {
			add_img_btn.setImageBitmap(bmp);
			bitmap = bmp;
			bitmapX=bmpX;
		} else {
			Log.e("hao2", "rotate:" + degree);
			// 创建操作图片是用的matrix对象
			Matrix matrix = new Matrix();
			// 旋转图片动作
			matrix.postRotate(degree);
			// 创建新图片
			Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0,
					bmp.getWidth(), bmp.getHeight(), matrix, true);
			add_img_btn.setImageBitmap(resizedBitmap);

		}
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			// Do not call Cursor.close() on a cursor obtained using this
			// method,
			// because the activity will do that for you at the appropriate time
			Cursor cursor = this.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	private void sendPhoto(Map<String, Bitmap> map) throws Exception {
		new UploadTask(map).execute();
	}

	private class UploadTask extends AsyncTask<Bitmap, Void, Void> {
		private Map<String, Bitmap> mMap;

		public UploadTask(Map<String, Bitmap> map) {
			this.mMap = map;
		}

		protected Void doInBackground(Bitmap... bitmaps) {
			Bitmap bitmapL = this.mMap.get("img_fileL");
			Bitmap bitmapX = this.mMap.get("img_fileX");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmapL.compress(Bitmap.CompressFormat.PNG, 100, stream);
			InputStream in = new ByteArrayInputStream(stream.toByteArray());

			ByteArrayOutputStream stream_ = new ByteArrayOutputStream();
			bitmapX.compress(Bitmap.CompressFormat.PNG, 100, stream_);
			InputStream in_ = new ByteArrayInputStream(stream_.toByteArray());

			DefaultHttpClient httpclient = new DefaultHttpClient();
			try {
				HttpPost httppost = new HttpPost(url_img_); // server

				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("myFile_", img_fileX, in_);
				Log.d("s", img_fileX);
				httppost.setEntity(reqEntity);

				Log.i(TAG, "request " + httppost.getRequestLine());
				HttpResponse response = null;
				try {
					response = httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if (response != null)
						Log.i(TAG, "response "
								+ response.getStatusLine().toString());
				} finally {

				}
			} finally {

			}
			try {
				HttpPost httppost = new HttpPost(url_img); // server

				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("myFile", img_fileL, in);
				Log.d("s", img_fileL);
				httppost.setEntity(reqEntity);

				Log.i(TAG, "request " + httppost.getRequestLine());
				HttpResponse response = null;
				try {
					response = httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if (response != null)
						Log.i(TAG, "response "
								+ response.getStatusLine().toString());
				} finally {

				}
			} finally {

			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume: " + this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
	}

	String mCurrentPhotoPath;

	static final int REQUEST_TAKE_PHOTO = 1;
	static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 2;
	File photoFile = null;

	private void dispatchTakePictureIntent(int i) {
		if (i == 1) {
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// Ensure that there's a camera activity to handle the intent
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				// Create the File where the photo should go
				File photoFile = null;
				try {
					photoFile = createImageFile();
				} catch (IOException ex) {
					// Error occurred while creating the File

				}
				// Continue only if the File was successfully created
				if (photoFile != null) {
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(photoFile));
					startActivityForResult(takePictureIntent,
							REQUEST_TAKE_PHOTO);
				}
			}

		} else if (i == 2) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			this.startActivityForResult(intent,
					PICK_IMAGE_ACTIVITY_REQUEST_CODE);
		}

	}

	/**
	 * http://developer.android.com/training/camera/photobasics.html
	 */
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		String storageDir = Environment.getExternalStorageDirectory()
				+ "/picupload";
		File dir = new File(storageDir);
		if (!dir.exists())
			dir.mkdir();

		File image = new File(storageDir + "/" + imageFileName + ".jpg");

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = image.getAbsolutePath();
		Log.i(TAG, "photo path = " + mCurrentPhotoPath);
		return image;
	}

	private void setPic() {
		// Get the dimensions of the View
		int targetW = add_img_btn.getWidth();
		int targetH = add_img_btn.getHeight();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor << 1;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = compressImageFromFile(mCurrentPhotoPath);
		Bitmap bitmapX = compressXImageFromFile(mCurrentPhotoPath);

		Matrix mtx = new Matrix();
		mtx.postRotate(90);
		// Rotating Bitmap
		Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), mtx, true);
		Bitmap rotatedBMPX = Bitmap.createBitmap(bitmapX, 0, 0,
				bitmapX.getWidth(), bitmapX.getHeight(), mtx, true);
		if (rotatedBMP != bitmap)
			bitmap.recycle();
		if (rotatedBMPX != bitmapX)
			bitmapX.recycle();
		add_img_btn.setImageBitmap(rotatedBMP);

		Add_Ky.this.bitmap = rotatedBMP;
		Add_Ky.this.bitmapX = rotatedBMPX;
	}
	/**
	 * 压缩详细图片
	 */
	private Bitmap compressXImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 400f;//
		float ww = 240f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		// 其实是无效的,大家尽管尝试
		return bitmap;
	}
	/**
     * 压缩图片
     */
    private Bitmap compressImageFromFile(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inJustDecodeBounds = true;//只读边,不读内容  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
  
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        float hh = 200f;//  
        float ww = 120f;//  
        int be = 1;  
        if (w > h && w > ww) {  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置采样率  
          
        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
        newOpts.inPurgeable = true;// 同时设置才会有效  
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收  
          
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
//      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩  
                                    //其实是无效的,大家尽管尝试  
        return bitmap;  
    }

	/**
	 * 以上是添加图片内容
	 * 
	 * @author Administrator
	 * 
	 */
	class addOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			add_txt = add_edt.getText().toString();
			if (add_txt.equals("")) {
				Toast.makeText(Add_Ky.this, "请输入内容", Toast.LENGTH_SHORT).show();
			} else {
				if (bitmap != null) {
					String time = String.valueOf(System.currentTimeMillis());
					img_fileL = time + ".jpg";
					img_fileX = time + "X.jpg";
					Map<String, Bitmap> map = new HashMap<String, Bitmap>();
					map.put("img_fileL", bitmap);
					map.put("img_fileX", bitmapX);
					if (bitmapX == null) {
						Log.e("nuull", "null");
					}
					try {
						sendPhoto(map);
						new SignupUser().execute();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					new SignupUser().execute();
				}
			}

		}

	}

	class SignupUser extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = CustomProgressDialog.createDialog(Add_Ky.this);
			progressDialog.setMessage("发布中...");
			Add_Ky.this.progressDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("nr", add_txt));
			params.add(new BasicNameValuePair("writer", String.valueOf(user_id)));
			params.add(new BasicNameValuePair("img",img_fileL));
			params.add(new BasicNameValuePair("imgX",img_fileX));
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params,Add_Ky.this);
			Log.d("Create Response", json.toString());
			try {
				Add_Ky.this.success = json.getInt("success");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			progressDialog.dismiss();
			if (success == 2) {
				Toast.makeText(Add_Ky.this, "发布失败，刷新试试？", Toast.LENGTH_SHORT)
						.show();
			} else {
				refresh();
				Toast.makeText(Add_Ky.this, "发布成功！", Toast.LENGTH_SHORT).show();
			}

		}
	}
	private void refresh(){
		Add_Ky.this.finish();
		Intent it=new Intent(Add_Ky.this,Sy_Tab.class);
		it.putExtra("user_id", user_id);
		it.putExtra("re_type", "yes");
		it.putExtra("page", "2");
		Add_Ky.this.startActivity(it);
	}
	class backOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Add_Ky.this.finish();
		}

	}
}
