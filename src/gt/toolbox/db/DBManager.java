package gt.toolbox.db;

import java.util.ArrayList;
import java.util.List;

import gt.toolbox.listener.ActivityLaucheListener;
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

	public void add(ActivityLaucheListener listener) {
		db.execSQL("INSERT INTO " + DBHelper.DBNAME + " VALUES(null,?,?,?)",
				new Object[] { listener.getPackageName(), listener.getType(),
						listener.getPara() });
	}

	public void delete(ActivityLaucheListener listener) {
		db.execSQL("DELETE FROM " + DBHelper.DBNAME
				+ " WHERE package=? AND type=?",
				new Object[] { listener.getPackageName(), listener.getType() });
	}

	public void updateParameter(ActivityLaucheListener listener) {
		db.execSQL("UPDATE " + DBHelper.DBNAME
				+ " SET para=? WHERE package=? AND type=?",
				new Object[] { listener.getPara(), listener.getPackageName(),
						listener.getType() });
	}

	public List<ActivityLaucheListener> query() {
		ArrayList<ActivityLaucheListener> list = new ArrayList<ActivityLaucheListener>();
		Cursor c = queryCursor();
		while (c.moveToNext()) {
			list.add(ListenerFactory.getListener(
					c.getString(c.getColumnIndex("type")),
					c.getString(c.getColumnIndex("packageName")),
					c.getString(c.getColumnIndex("para"))));
		}
		c.close();
		return list;
	}

	public Cursor queryCursor() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.DBNAME, null);
		return c;
	}
}
