package gt.toolbox;

import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class LockerListener extends ActivityLaucheListener {

	private PowerManager manager = null;
	private WakeLock lock = null;
	private String tag = null;

	public LockerListener(String packageName) {
		tag = packageName;
	}

	@Override
	public void onLaunch(String packageName, Context context) {
		// TODO Auto-generated method stub
		manager = (PowerManager) TaskWatcher.getInstance().getSystemService(
				android.content.Context.POWER_SERVICE);
		lock = manager.newWakeLock(PowerManager.FULL_WAKE_LOCK, tag);
		lock.acquire();
	}

	@Override
	public void onExit(String packageName, Context context) {
		// TODO Auto-generated method stub
		if (lock != null && lock.isHeld()) {
			lock.release();
		}
	}

}
