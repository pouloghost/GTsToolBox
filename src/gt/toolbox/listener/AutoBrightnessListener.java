package gt.toolbox.listener;

import gt.toolbox.ExcutionContext;
import android.content.ContextWrapper;
import android.net.Uri;
import android.provider.Settings;

public class AutoBrightnessListener extends ActivityLaucheListener {

	private Uri uri;

	public AutoBrightnessListener(String packageName) {
		super(packageName);
		uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
	}

	@Override
	public void onLaunch(ContextWrapper wrapper, ExcutionContext context) {
		// TODO Auto-generated method stub
		BrightnessUtils.setMode(wrapper,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC, uri);
	}

	@Override
	public void onExit(ContextWrapper wrapper, ExcutionContext context) {
		// TODO Auto-generated method stub
		BrightnessUtils.setMode(wrapper,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC, uri);
	}

	@Override
	public String getPara() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return ListenerFactory.ListenerType.BRIGHTNESS_AUTO.toString();
	}

}
