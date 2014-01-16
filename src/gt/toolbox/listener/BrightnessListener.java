package gt.toolbox.listener;

import gt.toolbox.Context;
import android.content.ContextWrapper;
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
	public void onLaunch(ContextWrapper wrapper, String packageName,
			Context context) {
		// TODO Auto-generated method stub
		BrightnessUtils.setBrightness(wrapper,brightness,uri);
	}

	@Override
	public void onExit(ContextWrapper wrapper, String packageName,
			Context context) {
		// TODO Auto-generated method stub
		BrightnessUtils.setMode(wrapper,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC, uri);
	}
}
