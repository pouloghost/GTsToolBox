package gt.toolbox;

import android.net.Uri;
import android.provider.Settings;

public class AutoBrightnessListener extends ActivityLaucheListener {

	private Uri uri;

	public AutoBrightnessListener() {
		uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
	}

	@Override
	public void onLaunch(String packageName, Context context) {
		// TODO Auto-generated method stub
		BrightnessUtils.setMode(
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC, uri);
	}

	@Override
	public void onExit(String packageName, Context context) {
		// TODO Auto-generated method stub
		BrightnessUtils.setMode(
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC, uri);
	}

}
