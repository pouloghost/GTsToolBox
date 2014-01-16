package gt.toolbox.listener;

import gt.toolbox.Context;
import android.content.ContextWrapper;
import android.os.PowerManager.WakeLock;

public class LockerListener extends ActivityLaucheListener {

	private WakeLock lock = null;
	private String tag = null;

	public LockerListener(String packageName) {
		tag = packageName;
	}

	@Override
	public void onLaunch(ContextWrapper wrapper, String packageName,
			Context context) {
		// TODO Auto-generated method stub
		lock = BrightnessUtils.lockWake(wrapper, tag);
	}

	@Override
	public void onExit(ContextWrapper wrapper, String packageName,
			Context context) {
		// TODO Auto-generated method stub
		BrightnessUtils.releaseWake(lock);
	}

}
