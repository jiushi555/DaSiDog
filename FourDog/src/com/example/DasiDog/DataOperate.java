package com.example.DasiDog;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataOperate {
	private static final String TABLENAME = "user_session";
	private SQLiteDatabase db = null;

	public DataOperate(SQLiteDatabase paramSQLiteDatabase) {
		this.db = paramSQLiteDatabase;
	}

	public void delete(int paramInt) {
		this.db.execSQL("DELETE FROM user_session WHERE id=1");
	}

	public void insert(int paramInt, String paramString, int now) {
		String str = "INSERT INTO user_session (user_id,username,now) VALUES('"
				+ paramInt + "','" + paramString + "','" + now + "')";
		this.db.execSQL(str);
	}

	public int select_id() {
		int i = 0;
		Cursor result = this.db.rawQuery(
				"SELECT user_id FROM user_session WHERE id=1", null);
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			i = result.getInt(0);
		}
		return i;
	}

	public String select_name() {
		String str = null;
		Cursor result = this.db.rawQuery(
				"SELECT username FROM user_session WHERE id=1", null);
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			str = result.getString(0);
		}
		return str;
	}
	public int select_now(){
		int now=0;
		Cursor result = this.db.rawQuery(
				"SELECT now FROM user_session WHERE id=1", null);
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			now = result.getInt(0);
		}
		return now;
	}
	public void update(int paramInt, String paramString, int now) {
		String str = "UPDATE user_session SET username='" + paramString
				+ "',user_id='" + paramInt + "',now='" + now + "'WHERE id=" + 1;

		this.db.execSQL(str);
	}

	public void close() {
		this.db.close();
	}
}