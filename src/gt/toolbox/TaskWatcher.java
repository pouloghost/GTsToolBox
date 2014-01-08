package gt.toolbox;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class TaskWatcher extends Service {
	public static enum ListenerType {
		ENTER, EXIT
	};

	private static TaskWatcher self = null;

	private String lastPackage = "";
	private Hashtable<String, HashSet<ActivityLaucheListener>> listeners = new Hashtable<String, HashSet<ActivityLaucheListener>>();
	private ActivityManager manager;
	private Context context = new Context();

	private boolean run = true;
	private Handler handler = new Handler();
	private Task task = new Task();

	public static TaskWatcher getInstance() {
		if (self == null) {
			self = new TaskWatcher();
		}
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
		TaskWatcher.self = this;
		manager = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				while (run) {
					try {
						Thread.sleep(1000);
						if (manager == null) {
							System.out.println("manager");
							continue;
						}
						List<RunningTaskInfo> infos = manager
								.getRunningTasks(1);
						if (infos == null) {
							System.out.println("info");
							continue;
						}
						RunningTaskInfo runningInfo = infos.get(0);
						if (runningInfo == null) {
							System.out.println("running Info");
							continue;
						}
						ComponentName activity = runningInfo.baseActivity;
						if (activity == null) {
							System.out.println("activity");
							continue;
						}
						String currentPackage = activity.getPackageName();

						if (!currentPackage.equals(lastPackage)) {
							task.setPackage(currentPackage, lastPackage);
							handler.post(task);
							lastPackage = currentPackage;
						}
						System.out.println(lastPackage);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
			}

		}.start();
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

	private void informListener(ListenerType type, String packageName) {
		HashSet<ActivityLaucheListener> set = listeners.get(packageName);
		if (set == null) {
			return;
		}
		Iterator<ActivityLaucheListener> iterator = set.iterator();
		while (iterator.hasNext()) {
			switch (type) {
			case ENTER:
				iterator.next().onLaunch(packageName, context);
				break;

			case EXIT:
				iterator.next().onExit(packageName, context);
				break;

			default:
				break;
			}

		}
	}

	private void exitNow() {
		informListener(ListenerType.EXIT, lastPackage);
	}

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
			informListener(ListenerType.EXIT, oldPackage);
			informListener(ListenerType.ENTER, newPackage);
		}
	}
}
