package gt.toolbox;

public abstract class ActivityLaucheListener {
	//when a new task is brought to the front
	public abstract void onLaunch(String packageName, Context context);
	//before an old one is sent to the back
	public abstract void onExit(String packageName, Context context);
}
