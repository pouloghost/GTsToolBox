package gt.toolbox.listener;

import android.content.ContextWrapper;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;

public class BrightnessUtils {
	public static void setMode(ContextWrapper wrapper, int mode, Uri uri) {
		try {
			Settings.System.putInt(wrapper.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
			BrightnessUtils.notifyChange(wrapper, uri, null);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	// TODO needs a better solution
	public static void notifyChange(ContextWrapper wrapper, Uri uri,
			ContentObserver observer) {

		wrapper.getContentResolver().notifyChange(uri, observer);
	}

	public static WakeLock lockWake(ContextWrapper wrapper, String tag) {
		PowerManager manager = (PowerManager) wrapper
				.getSystemService(android.content.Context.POWER_SERVICE);
		WakeLock lock = manager.newWakeLock(PowerManager.FULL_WAKE_LOCK, tag);
		lock.acquire();
		return lock;
	}

	public static void releaseWake(WakeLock lock) {
		if (lock != null && lock.isHeld()) {
			lock.release();
		}
	}

	public static void setBrightness(ContextWrapper wrapper, int brightness,
			Uri uri) {
		BrightnessUtils.setMode(wrapper,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL, uri);
		Settings.System.putInt(wrapper.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, brightness);
		BrightnessUtils.notifyChange(wrapper, uri, null);
	}
}
