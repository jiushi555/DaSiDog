package com.example.me;

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

import com.example.DasiDog.DataOperate;
import com.example.DasiDog.DatabaseHelper;
import com.example.DasiDog.JSONParser;
import com.example.DasiDog.R;
import com.example.DasiDog.Sy_Tab;
import com.example.DasiDog.Welcome;
import com.example.DasiDog.Zc_1;
import com.example.DasiDog.Zc_2;
import com.example.ProgressDialog.CustomProgressDialog;
import com.example.tx.CropImageActivity;
import com.example.tx.ModifyAvatarDialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class Reset_Me extends Activity {
	private String user_id;
	private String username;
	private String now;
	private EditText name_txt;
	private RadioGroup reset;
	private RadioButton reset_ds;
	private RadioButton reset_by;
	private TextView reset_btn;
	private String reset_name;
	private int reset_now = 0;
	private ImageView user_tx;
	private CustomProgressDialog progressDialog;
	JSONParser jsonParser = new JSONParser();
	private String url = Welcome.URL + "/dasidog/reset_me.php";
	private int success;
	private String user_tx_url;
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
	private Context mCon;
	// 上传头像参数
	private String filename;
	private String path;
	private String postUrl = Welcome.URL + "/dasidog/upload.php"; // 处理POST请求的页

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_me);
		Intent it = super.getIntent();
		user_id = it.getStringExtra("user_id");
		username = it.getStringExtra("username");
		now = it.getStringExtra("now");
		user_tx_url = it.getStringExtra("user_tx");
		name_txt = (EditText) findViewById(R.id.retUserName);
		user_tx = (ImageView) findViewById(R.id.reset_tx);
		if (user_tx_url.equals("no")) {
			user_tx.setImageResource(R.drawable.ic_portrait);
		} else {
			String url = Welcome.URL + "/dasidog/tx/" + user_tx_url;
			user_tx.setTag(url);
			Welcome.mimageLoader.getImageByAsyncTask(user_tx, url,1);
		}
		mCon = Reset_Me.this;
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		.format(new Date());
		filename = timeStamp + "." + "jpg";
		user_tx.setOnClickListener(new txOnClick());
		reset = (RadioGroup) findViewById(R.id.reset_now);
		reset_ds = (RadioButton) findViewById(R.id.reset_dsg);
		reset_by = (RadioButton) findViewById(R.id.reset_yby);
		reset_btn = (TextView) findViewById(R.id.reset_btn);
		reset.setOnCheckedChangeListener(new nowListener());
		reset_btn.setOnClickListener(new resetOnClick());
		name_txt.setText(username);
		Editable etext = name_txt.getText();
		Selection.setSelection(etext, etext.length());
		if (now.equals("1")) {
			reset_ds.setChecked(true);
		} else if (now.equals("2")) {
			reset_by.setChecked(true);
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

	}

	private class nowListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (reset_ds.getId() == checkedId) {
				reset_now = 1;
			}
			if (reset_by.getId() == checkedId) {
				reset_now = 2;
			}
		}

	}

	private class resetOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			reset_name = name_txt.getText().toString();
			if (reset_now == 0) {
				Toast.makeText(Reset_Me.this, "请选择现在的状态", Toast.LENGTH_SHORT)
						.show();
			}
			if (reset_name.equals("")) {
				Toast.makeText(Reset_Me.this, "请输入昵称", Toast.LENGTH_SHORT)
						.show();
			}
			if (reset_now != 0 & !(reset_name.equals(""))) {
				uploadFile();
				new ResetUser().execute();
			}
		}

	}

	class ResetUser extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = CustomProgressDialog.createDialog(Reset_Me.this);
			progressDialog.setMessage("修改中...");
			Reset_Me.this.progressDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", reset_name));
			params.add(new BasicNameValuePair("user_id", user_id));
			params.add(new BasicNameValuePair("now", String.valueOf(reset_now)));
			JSONObject json = jsonParser.makeHttpRequest(url, "POST", params,Reset_Me.this);
			Log.d("Create Response", json.toString());
			try {
				Reset_Me.this.success = json.getInt("success");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			if(success==1 | success==0){
				Toast.makeText(Reset_Me.this, "修改成功!", Toast.LENGTH_SHORT)
				.show();
			}
			
			progressDialog.dismiss();

		}
	}

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
				Reset_Me.this.path = path;
				Bitmap b = BitmapFactory.decodeFile(path);
				user_tx.setImageBitmap(toRoundBitmap(b));

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
			// Toast.makeText(Reset_Me.this, b.toString().trim(),
			// Toast.LENGTH_SHORT).show();//Post成功
		} catch (Exception e) {
			/* 显示异常信息 */
			// Toast.makeText(Reset_Me.this, "Fail:" + e,
			// Toast.LENGTH_SHORT).show();//Post失败
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_id", String.valueOf(user_id)));
		params.add(new BasicNameValuePair("filename", filename));
		JSONObject json = jsonParser.makeHttpRequest(postUrl, "POST", params,Reset_Me.this);
		try {
			int success = json.getInt("success");
			path = json.getString("path");
			if (success == 1) {
				Toast.makeText(Reset_Me.this, "上传成功，重新打开应用可查看", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		
	}
}
