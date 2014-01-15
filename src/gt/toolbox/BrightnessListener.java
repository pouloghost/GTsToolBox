package gt.toolbox;

import android.net.Uri;
import android.provider.Settings;

public class BrightnessListener extends ActivityLaucheListener {

	private int brightness;
	private Uri uri;

	public BrightnessListener(int brightness) {
		this.brightness = brightness;
		uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
	}

	@Override
	public void onLaunch(String packageName, Context context) {
		// TODO Auto-generated method stub
		setBrightness();
	}

	private void setBrightness() {
		BrightnessUtils.setMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL,
				uri);
		Settings.System.putInt(TaskWatcherService.getInstance()
				.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,
				brightness);
		BrightnessUtils.notifyChange(uri, null);
	}

	@Override
	public void onExit(String packageName, Context context) {
		// TODO Auto-generated method stub
		BrightnessUtils.setMode(
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC, uri);
	}
}
