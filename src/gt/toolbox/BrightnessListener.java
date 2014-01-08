package gt.toolbox;

import android.net.Uri;
import android.provider.Settings;

public class BrightnessListener extends ActivityLaucheListener {

	private int brightness = 255;
	private Uri uri;

	public BrightnessListener(int brightness) {
		this.brightness = brightness;
		uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
	}

	@Override
	public void onLaunch(String packageName, Context context) {
		// TODO Auto-generated method stub
		setMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		setBrightness();
	}

	private void setMode(int mode) {
		try {
			Settings.System.putInt(TaskWatcher.getInstance()
					.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
			TaskWatcher.getInstance().getContentResolver()
					.notifyChange(uri, null);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	private void setBrightness() {
		Settings.System.putInt(TaskWatcher.getInstance().getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, brightness);
		TaskWatcher.getInstance().getContentResolver().notifyChange(uri, null);
	}

	@Override
	public void onExit(String packageName, Context context) {
		// TODO Auto-generated method stub
		setMode(Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}
}
