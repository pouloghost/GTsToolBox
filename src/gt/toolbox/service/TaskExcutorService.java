package gt.toolbox.service;

import gt.toolbox.ExcutionContext;
import gt.toolbox.listener.ActivityLaucheListener;
import gt.toolbox.listener.BrightnessListener;
import gt.toolbox.listener.LockerListener;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class TaskExcutorService extends Service {
	public static enum ActionType {
		ENTER, EXIT
	};

	// commands for each event
	private Hashtable<String, HashSet<ActivityLaucheListener>> listeners = new Hashtable<String, HashSet<ActivityLaucheListener>>();
	private ExcutionContext excutionContext;
	private BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			excutionContext = intent
					.getParcelableExtra(TaskWatcherService.CONTEXT);
			informListener(ActionType.EXIT, excutionContext);
			informListener(ActionType.ENTER, excutionContext);
			System.out.println(excutionContext.toString());
		}
	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("create");
		IntentFilter filter = new IntentFilter();
		filter.addAction(TaskWatcherService.ACTION_TAG);
		registerReceiver(br, filter);

		bindListeners();
		System.out.println("done");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(br);
		exitNow();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private void bindListeners() {
		// TODO Auto-generated method stub
		registerListener("gt.toolbox", new LockerListener("gt.toolbox"));
		registerListener("com.bbk.launcher2", new BrightnessListener(1,
				"com.bbk.launcher2"));
		registerListener("com.android.launcher", new BrightnessListener(1,
				"com.android.launcher"));
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
	private void informListener(ActionType type, ExcutionContext context) {
		String packageName = "";
		switch (type) {
		case ENTER:
			// enter new task
			packageName = context.getNewPackage();
			break;
		case EXIT:
			// exit old task
			packageName = context.getOldPackage();
			break;
		}
		HashSet<ActivityLaucheListener> set = listeners.get(packageName);
		if (set == null) {
			return;
		}
		Iterator<ActivityLaucheListener> iterator = set.iterator();
		while (iterator.hasNext()) {
			switch (type) {
			case ENTER:
				iterator.next().onLaunch(this, context);
				break;

			case EXIT:
				iterator.next().onExit(this, context);
				break;

			default:
				break;
			}

		}
	}

	private void exitNow() {
		informListener(ActionType.EXIT, excutionContext);
	}
}
