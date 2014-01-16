package gt.toolbox.listener;

import gt.toolbox.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.provider.Settings;

public class AutoBrightnessListener extends ActivityLaucheListener {

	private Uri uri;

	public AutoBrightnessListener() {
		uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
	}

	@Override
	public void onLaunch(ContextWrapper wrapper, String packageName,
			Context context) {
		// TODO Auto-generated method stub
		BrightnessUtils.setMode(wrapper,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC, uri);
	}

	@Override
	public void onExit(ContextWrapper wrapper, String packageName,
			Context context) {
		// TODO Auto-generated method stub
		BrightnessUtils.setMode(wrapper,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC, uri);
	}

}
