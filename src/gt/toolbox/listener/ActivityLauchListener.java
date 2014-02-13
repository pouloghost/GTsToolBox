package gt.toolbox.listener;

import android.content.ContextWrapper;
import gt.toolbox.ExcutionContext;

public abstract class ActivityLauchListener implements
		Comparable<ActivityLauchListener> {
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
	public ActivityLauchListener(String packageName) {
		this.packageName = packageName;
	}

	public abstract void onLaunch(ContextWrapper wrapper,
			ExcutionContext context);

	// before an old one is sent to the back
	public abstract void onExit(ContextWrapper wrapper, ExcutionContext context);

	public abstract String getPara();

	@Override
	public int compareTo(ActivityLauchListener another) {
		// TODO Auto-generated method stub
		if (this.getPackageName().equals(another.getPackageName())
				&& this.getType().equals(another.getType())) {
			return 0;
		}
		return 1;
	}

}
