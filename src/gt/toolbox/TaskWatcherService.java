package gt.toolbox;

import gt.toolbox.listener.ActivityLaucheListener;
import gt.toolbox.listener.BrightnessListener;
import gt.toolbox.listener.LockerListener;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class TaskWatcherService extends Service {
	public static enum ActionType {
		ENTER, EXIT
	};

	// singleton like access
	private static TaskWatcherService self = null;

	// observe task change
	private String lastPackage = "";
	private ActivityManager manager;
	// commands for each event
	private Hashtable<String, HashSet<ActivityLaucheListener>> listeners = new Hashtable<String, HashSet<ActivityLaucheListener>>();
	private Context context = new Context();

	// threading
	private static int INTERVAL = 1000;
	private boolean run = true;
	private Handler handler = new Handler();
	private Task task = new Task();

	public static TaskWatcherService getInstance() {
		return self;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// for singleton like access
		TaskWatcherService.self = this;
		startListening();
		bindListeners();
	}

	private void startListening() {
		// TODO Auto-generated method stub
		// observing
		manager = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				while (run) {
					try {
						Thread.sleep(INTERVAL);
						RunningTaskInfo runningInfo = manager
								.getRunningTasks(1).get(0);

						// the starter of this task stack
						// control granularity is task stack
						ComponentName activity = runningInfo.baseActivity;
						String currentPackage = activity.getPackageName();

						if (!currentPackage.equals(lastPackage)) {
							if (!listeners.get(currentPackage).isEmpty()) {
								// new task
								task.setPackage(currentPackage, lastPackage);
								// change settings on main thread
								handler.post(task);
							}
							lastPackage = currentPackage;
							System.out.println(lastPackage);
						}
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
			}

		}.start();
	}

	private void bindListeners() {
		// TODO Auto-generated method stub
		registerListener("gt.toolbox", new LockerListener("gt.toolbox"));
		registerListener("com.bbk.launcher2", new BrightnessListener(1));
		registerListener("com.android.launcher", new BrightnessListener(1));
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		run = false;
		System.out.println("killed");
		exitNow();
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	// -----listeners
	public void registerListener(String packageName,
			ActivityLaucheListener listener) {
		if (!listeners.containsKey(packageName)) {
			listeners.put(packageName, new HashSet<ActivityLaucheListener>());
		}

		listeners.get(packageName).add(listener);
	}

	public void unregisterListener(String packageName,
			ActivityLaucheListener listener) {
		HashSet<ActivityLaucheListener> listenerSet = listeners
				.get(packageName);
		listenerSet.remove(listener);

		if (listenerSet.isEmpty()) {
			listeners.remove(packageName);
		}
	}

	// invoke the commands stored in listeners registered under the package
	private void informListener(ActionType type, String packageName) {
		HashSet<ActivityLaucheListener> set = listeners.get(packageName);
		if (set == null) {
			return;
		}
		Iterator<ActivityLaucheListener> iterator = set.iterator();
		while (iterator.hasNext()) {
			switch (type) {
			case ENTER:
				iterator.next().onLaunch(this, packageName, context);
				break;

			case EXIT:
				iterator.next().onExit(this, packageName, context);
				break;

			default:
				break;
			}

		}
	}

	private void exitNow() {
		informListener(ActionType.EXIT, lastPackage);
	}

	// all the changes must be done on the main thread
	class Task implements Runnable {
		private String newPackage;
		private String oldPackage;

		public void setPackage(String newPackage, String oldPackage) {
			this.newPackage = newPackage;
			this.oldPackage = oldPackage;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			informListener(ActionType.EXIT, oldPackage);
			informListener(ActionType.ENTER, newPackage);
		}
	}
}
