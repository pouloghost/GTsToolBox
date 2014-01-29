package gt.toolbox.listener;

import android.content.ContextWrapper;
import gt.toolbox.ExcutionContext;

public abstract class ActivityLaucheListener {
	// must have a constructor using a hashmap as parameter
	// when a new task is brought to the front

	// java bean part
	private String packageName;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public abstract String getType();

	// function part
	public ActivityLaucheListener(String packageName) {
		this.packageName = packageName;
	}

	public abstract void onLaunch(ContextWrapper wrapper,
			ExcutionContext context);

	// before an old one is sent to the back
	public abstract void onExit(ContextWrapper wrapper, ExcutionContext context);

	public abstract String getPara();
}
