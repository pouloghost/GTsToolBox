package gt.toolbox.db;

import java.util.ArrayList;
import java.util.List;

import gt.toolbox.listener.ActivityLauchListener;
import gt.toolbox.listener.ListenerFactory;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	public void add(ActivityLauchListener listener) {
		db.execSQL("INSERT INTO " + DBHelper.DBNAME + " VALUES(null,?,?,?)",
				new Object[] { listener.getPackageName(), listener.getType(),
						listener.getPara() });
	}

	public void delete(ActivityLauchListener listener) {
		db.execSQL("DELETE FROM " + DBHelper.DBNAME + " WHERE "
				+ DBHelper.DBColumn.pack.toString() + "=? AND "
				+ DBHelper.DBColumn.type.toString() + "=?", new Object[] {
				listener.getPackageName(), listener.getType() });
	}

	public void updateParameter(ActivityLauchListener listener) {
		String where = " WHERE " + DBHelper.DBColumn.pack.toString()
				+ "=? AND " + DBHelper.DBColumn.type.toString() + "=?";
		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.DBNAME + where,
				new String[] { listener.getPara(), listener.getPackageName(),
						listener.getType() });
		if (c.getCount() != 0) {
			db.execSQL(
					"UPDATE " + DBHelper.DBNAME + " SET "
							+ DBHelper.DBColumn.para.toString() + "=?" + where,
					new Object[] { listener.getPara(),
							listener.getPackageName(), listener.getType() });
		} else {
			add(listener);
		}
	}

	public List<ActivityLauchListener> query() {
		ArrayList<ActivityLauchListener> list = new ArrayList<ActivityLauchListener>();
		Cursor c = queryCursor();
		while (c.moveToNext()) {
			list.add(ListenerFactory.getListener(c.getString(c
					.getColumnIndex(DBHelper.DBColumn.type.toString())), c
					.getString(c.getColumnIndex(DBHelper.DBColumn.pack
							.toString())), c.getString(c
					.getColumnIndex(DBHelper.DBColumn.para.toString()))));
		}
		c.close();
		return list;
	}

	public Cursor queryCursor() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.DBNAME, null);
		return c;
	}
}
