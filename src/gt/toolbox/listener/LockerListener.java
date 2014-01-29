package gt.toolbox.listener;

import java.util.HashMap;

import gt.toolbox.ExcutionContext;
import android.content.ContextWrapper;
import android.os.PowerManager.WakeLock;

public class LockerListener extends ActivityLaucheListener {

	private WakeLock lock = null;
	private String tag = null;

	private static String TAG = "TAG";

	public LockerListener(String packageName) {
		super(packageName);
		tag = packageName;
	}

	public LockerListener(HashMap<String, Object> para) {
		this((String) para.get(TAG));
	}

	@Override
	public void onLaunch(ContextWrapper wrapper, ExcutionContext context) {
		// TODO Auto-generated method stub
		lock = BrightnessUtils.lockWake(wrapper, tag);
	}

	@Override
	public void onExit(ContextWrapper wrapper, ExcutionContext context) {
		// TODO Auto-generated method stub
		BrightnessUtils.releaseWake(lock);
	}

	@Override
	public String getPara() {
		// TODO Auto-generated method stub
		return TAG + ":" + ListenerFactory.ObjectType.STRING.toString() + ":"
				+ tag;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return ListenerFactory.ListenerType.WAKE_LOCK.toString();
	}

}
