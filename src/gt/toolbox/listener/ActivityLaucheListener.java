package gt.toolbox.listener;

import android.content.ContextWrapper;
import gt.toolbox.Context;

public abstract class ActivityLaucheListener {
	// must have a constructor using a hashmap as parameter
	// when a new task is brought to the front
	public abstract void onLaunch(ContextWrapper wrapper, String packageName,
			Context context);

	// before an old one is sent to the back
	public abstract void onExit(ContextWrapper wrapper, String packageName,
			Context context);

	public abstract String getPara();
}
