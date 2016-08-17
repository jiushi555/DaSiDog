package com.example.tx;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.DasiDog.JSONParser;
import com.example.DasiDog.R;
import com.example.DasiDog.Sy_Tab;
import com.example.DasiDog.Welcome;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class Tx_zc extends Activity {
	private TextView pass;
	private ImageView tx;
	private String username;
	private int user_id;
	private Context mCon;
	// 头像参数
	private String TAG = "InformationActivity";
	public static final String IMAGE_PATH = "My_weixin";
	private static String localTempImageFileName = "";
	private static final int FLAG_CHOOSE_IMG = 5;
	private static final int FLAG_CHOOSE_PHONE = 6;
	private static final int FLAG_MODIFY_FINISH = 7;
	public static final File FILE_SDCARD = Environment
			.getExternalStorageDirectory();
	public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
	public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
			"images/screenshots");
	// 上传头像参数
	private String filename;
	private String path;
	JSONParser jsonParser = new JSONParser();
	private String postUrl = Welcome.URL + "/dasidog/upload.php"; // 处理POST请求的页

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zc_tx);
		Intent it = super.getIntent();
		mCon = Tx_zc.this;
		username = it.getStringExtra("username");
		user_id = it.getIntExtra("user_id", 0);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		.format(new Date());
		filename = timeStamp + "." + "jpg";
		pass = (TextView) this.findViewById(R.id.pass);
		tx = (ImageView) this.findViewById(R.id.tx_img);
		pass.setOnClickListener(new passOnClick());
		tx.setOnClickListener(new txOnClick());
	}

	private class passOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent pass_it = new Intent(Tx_zc.this, Sy_Tab.class);
			pass_it.putExtra("username", username);
			pass_it.putExtra("user_id", user_id);
			startActivity(pass_it);
			Tx_zc.this.finish();
		}

	}

	private class txOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 调用选择那种方式的dialog
			ModifyAvatarDialog modifyAvatarDialog = new ModifyAvatarDialog(mCon) {
				// 选择本地相册
				@Override
				public void doGoToImg() {
					this.dismiss();
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_PICK);
					intent.setType("image/*");
					startActivityForResult(intent, FLAG_CHOOSE_IMG);
				}

				// 选择相机拍照
				@Override
				public void doGoToPhone() {
					this.dismiss();
					String status = Environment.getExternalStorageState();
					if (status.equals(Environment.MEDIA_MOUNTED)) {
						try {
							localTempImageFileName = "";
							localTempImageFileName = String
									.valueOf((new Date()).getTime()) + ".png";
							File filePath = FILE_PIC_SCREENSHOT;
							if (!filePath.exists()) {
								filePath.mkdirs();
							}
							Intent intent = new Intent(
									android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
							File f = new File(filePath, localTempImageFileName);
							// localTempImgDir和localTempImageFileName是自己定义的名字
							Uri u = Uri.fromFile(f);
							intent.putExtra(
									MediaStore.Images.Media.ORIENTATION, 0);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
							startActivityForResult(intent, FLAG_CHOOSE_PHONE);
						} catch (ActivityNotFoundException e) {
							//
						}
					}
				}
			};
			AlignmentSpan span = new AlignmentSpan.Standard(
					Layout.Alignment.ALIGN_CENTER);
			AbsoluteSizeSpan span_size = new AbsoluteSizeSpan(25, true);
			SpannableStringBuilder spannable = new SpannableStringBuilder();
			String dTitle = "请选择";
			spannable.append(dTitle);
			spannable.setSpan(span, 0, dTitle.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannable.setSpan(span_size, 0, dTitle.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			modifyAvatarDialog.setTitle(spannable);
			modifyAvatarDialog.show();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						Toast.makeText(mCon, "图片没找到", 0).show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();
					Log.i(TAG, "path=" + path);
					Intent intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("path", path);
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				} else {
					Log.i(TAG, "path=" + uri.getPath());
					Intent intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("path", uri.getPath());
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				}
			}
		} else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {
			File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
			Intent intent = new Intent(this, CropImageActivity.class);
			intent.putExtra("path", f.getAbsolutePath());
			startActivityForResult(intent, FLAG_MODIFY_FINISH);
		} else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
			if (data != null) {
				final String path = data.getStringExtra("path");
				Log.i(TAG, "截取到的图片路径是 = " + path);
				Tx_zc.this.path = path;
				pass.setText("完成");
				pass.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						uploadFile();
					}

				});
				Bitmap b = BitmapFactory.decodeFile(path);
				tx.setImageBitmap(toRoundBitmap(b));

			}
		}
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

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	private void uploadFile() {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(postUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/*
			 * Output to the connection. Default is false, set to true because
			 * post method must write something to the connection
			 */
			con.setDoOutput(true);
			/* Read from the connection. Default is true. */
			con.setDoInput(true);
			/* Post cannot use caches */
			con.setUseCaches(false);
			/* Set the post method. Default is GET */
			con.setRequestMethod("POST");
			/* 设置请求属性 */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置StrictMode 否则HTTPURLConnection连接失败，因为这是在主进程中进行网络连接 */
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			/* 设置DataOutputStream，getOutputStream中默认调用connect() */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream()); // output
																				// to
																				// the
																				// connection
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file\";filename=\"" + filename + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(path);
			/* 设置每次写入8192bytes */
			int bufferSize = 8192;
			byte[] buffer = new byte[bufferSize]; // 8k
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* 关闭流，写入的东西自动生成Http正文 */
			fStream.close();
			/* 关闭DataOutputStream */
			ds.close();
			/* 从返回的输入流读取响应信息 */
			InputStream is = con.getInputStream(); // input from the connection
													// 正式建立HTTP连接
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			/* 显示网页响应内容 */
			// Toast.makeText(Tx_zc.this, b.toString().trim(),
			// Toast.LENGTH_SHORT).show();//Post成功
		} catch (Exception e) {
			/* 显示异常信息 */
			// Toast.makeText(Tx_zc.this, "Fail:" + e,
			// Toast.LENGTH_SHORT).show();//Post失败
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_id", String.valueOf(user_id)));
		params.add(new BasicNameValuePair("filename", filename));
		JSONObject json = jsonParser.makeHttpRequest(postUrl, "POST", params,Tx_zc.this);
		try {
			int success = json.getInt("success");
			path = json.getString("path");
			if (success == 1) {
				Toast.makeText(Tx_zc.this, "上传成功", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Intent success = new Intent(Tx_zc.this, Sy_Tab.class);
		success.putExtra("username", username);
		success.putExtra("user_id", user_id);
		success.putExtra("path", path);
		startActivity(success);
		Tx_zc.this.finish();
	}

}