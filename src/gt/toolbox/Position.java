package gt.toolbox;

import android.content.SharedPreferences;

public class Position {
	private static final String X_KEY = "gt.floatwindow.x";
	private static final String Y_KEY = "gt.floatwindow.y";
	private static final int defValue = 0;

	private int x, y;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor = null;
	private int middle, width;

	public Position(SharedPreferences pref, int middle, int width) {
		this.pref = pref;
		x = pref.getInt(X_KEY, defValue);
		y = pref.getInt(Y_KEY, defValue);

		this.middle = middle;
		this.width = width;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		if (editor == null) {
			editor = pref.edit();
		}
		this.x = x > middle ? 2 * middle - width : 0;
		editor.putInt(X_KEY, this.x);
		editor.commit();
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		if (editor == null) {
			editor = pref.edit();
		}
		this.y = y;
		editor.putInt(Y_KEY, y);
		editor.commit();
	}

}
