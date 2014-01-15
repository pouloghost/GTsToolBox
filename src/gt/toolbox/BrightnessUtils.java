package gt.toolbox;

import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;

public class BrightnessUtils {
	public static void setMode(int mode, Uri uri) {
		try {
			Settings.System.putInt(TaskWatcherService.getInstance()
					.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
			BrightnessUtils.notifyChange(uri, null);
			System.out
					.println("mode "
							+ (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL ? "manual"
									: "auto"));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	// TODO needs a better solution
	public static void notifyChange(Uri uri, ContentObserver observer) {
		BrightnessUtils.notifyChange(uri, null);
	}
}
