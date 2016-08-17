package com.example.DasiDog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "session.db";
	private static final int DATABASEVERSION = 1;
	private static final String TABLENAME = "user_session";

	public DatabaseHelper(Context paramContext) {
		super(paramContext, DATABASENAME, null, DATABASEVERSION);
	}

	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		String sql="CREATE TABLE "+TABLENAME+" ("+
					"id INTEGER PRIMARY KEY NOT NULL,"+
					"user_id INTEGER(10) NOT NULL,"+
					"username VARCHAR(50) NOT NULL,"+
					"now INTEGER(2) NOT NULL)";
		paramSQLiteDatabase.execSQL(sql);
		
	}

	public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1,
			int paramInt2) {
		paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTSuser_session");
		onCreate(paramSQLiteDatabase);
	}
	
}