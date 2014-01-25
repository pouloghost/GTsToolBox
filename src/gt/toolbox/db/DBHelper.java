package gt.toolbox.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_FILE_NAME = "listeners.db";
	private static final int DATABASE_VERSION = 1;
	public static final String DBNAME = "listeners";

	public DBHelper(Context context) {
		super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXIST "
				+ DBNAME
				+ " (_id INTERGER PRIMARY KEY AUTOINCREMENT, package VARCHAR, type VARCHAR, para VARCHAR)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
