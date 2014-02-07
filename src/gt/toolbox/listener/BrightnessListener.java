package gt.toolbox.listener;

import java.util.HashMap;

import gt.toolbox.ExcutionContext;
import android.content.ContextWrapper;
import android.net.Uri;
import android.provider.Settings;

public class BrightnessListener extends ActivityLauchListener {

	private int brightness;

	private Uri uri;

	private static String BRIGHTNESS = "BRIGHTNESS";

	public BrightnessListener(int brightness, String packageName) {
		super(packageName);
		this.brightness = brightness;
		uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
	}

	public BrightnessListener(HashMap<String, Object> para, String packageName) {
		this(((Integer) para.get(BRIGHTNESS)).intValue(), packageName);
	}

	@Override
	public void onLaunch(ContextWrapper wrapper, ExcutionContext context) {
		// TODO Auto-generated method stub
		BrightnessUtils.setBrightness(wrapper, brightness, uri);
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
		return BRIGHTNESS + ":" + ListenerFactory.ObjectType.INT.toString()
				+ ":" + String.valueOf(brightness);
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return ListenerFactory.ListenerType.BRIGHTNESS_MANUAL.toString();
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}
}
