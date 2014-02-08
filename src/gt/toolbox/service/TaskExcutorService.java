package gt.toolbox.service;

import gt.toolbox.ExcutionContext;
import gt.toolbox.db.DBManager;
import gt.toolbox.listener.ActivityLauchListener;
import gt.toolbox.listener.BrightnessListener;
import gt.toolbox.listener.ListenerFactory.ListenerType;
import gt.toolbox.listener.LockerListener;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

//excute tasks based on the broadcast by watcher
public class TaskExcutorService extends Service {
	public static enum ActionType {
		ENTER, EXIT
	};

	public static enum MessageType {
		TASK_CHANGE, DIRECT_MODIFY, SAVE_LISTENER
	}

	public static final String LISTENER_TYPE = "listener type";

	private DBManager db;

	// commands for each event
	private Hashtable<String, HashSet<ActivityLauchListener>> listeners = new Hashtable<String, HashSet<ActivityLauchListener>>();
	private ExcutionContext excutionContext;
	private BrightnessListener brightness;
	private LockerListener locker;

	private BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			MessageType type = MessageType.valueOf(intent.getAction());
			switch (type) {
			case TASK_CHANGE:
				excutionContext = intent
						.getParcelableExtra(TaskWatcherService.CONTEXT);
				informListener(ActionType.EXIT, excutionContext);
				informListener(ActionType.ENTER, excutionContext);
				System.out.println(excutionContext.toString());
				clearDirectListeners();
				break;
			case DIRECT_MODIFY:
				ListenerType listenerType = ListenerType.valueOf(intent
						.getStringExtra(LISTENER_TYPE));
				String pkg = TaskExcutorService.this.excutionContext
						.getNewPackage();
				TaskExcutorService service = TaskExcutorService.this;
				switch (listenerType) {
				case BRIGHTNESS_MANUAL:
					int value = intent.getIntExtra(
							BrightnessListener.BRIGHTNESS, 1);
					brightness = new BrightnessListener(value, pkg);
					brightness.onLaunch(service, excutionContext);
					break;
				case WAKE_LOCK:
					boolean boolVal = intent.getBooleanExtra(
							LockerListener.LOCK_ON, false);
					locker = new LockerListener(pkg);
					if (boolVal) {
						locker.onLaunch(service, excutionContext);
						System.out.println("keep wake");
					} else {
						locker.onExit(service, excutionContext);
						locker = null;
						System.out.println("release lock");
					}
					break;
				case BRIGHTNESS_AUTO:
					break;
				default:
					break;
				}
				break;
			case SAVE_LISTENER:
				saveListeners();
				clearDirectListeners();
				break;
			}
		}
	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("create");
		IntentFilter filter = new IntentFilter();
		filter.addAction(MessageType.DIRECT_MODIFY.toString());
		filter.addAction(MessageType.TASK_CHANGE.toString());
		filter.addAction(MessageType.SAVE_LISTENER.toString());
		registerReceiver(br, filter);

		db = new DBManager(this);

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
		List<ActivityLauchListener> list = db.query();
		for (ActivityLauchListener listener : list) {
			registerListener(listener);
		}
	}

	// -----listeners
	public void registerListener(ActivityLauchListener listener) {
		String packageName = listener.getPackageName();
		if (!listeners.containsKey(packageName)) {
			listeners.put(packageName, new HashSet<ActivityLauchListener>());
		}

		listeners.get(packageName).add(listener);
	}

	public void unregisterListener(ActivityLauchListener listener) {
		String packageName = listener.getPackageName();
		HashSet<ActivityLauchListener> listenerSet = listeners.get(packageName);
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
		HashSet<ActivityLauchListener> set = listeners.get(packageName);
		if (set == null) {
			return;
		}
		Iterator<ActivityLauchListener> iterator = set.iterator();
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

	private void saveListeners() {
		if (brightness != null) {
			db.updateParameter(brightness);
			remove(brightness);
		}
		if (locker != null) {
			db.updateParameter(locker);
			remove(locker);
		}
	}

	private void clearDirectListeners() {
		// clear direct modify listeners
		brightness = null;
		locker = null;
	}

	private void remove(ActivityLauchListener listener) {
		String pkg = listener.getPackageName();
		String type = listener.getType();
		HashSet<ActivityLauchListener> set = listeners.get(pkg);
		if (set != null) {
			ActivityLauchListener toDel = null;
			Iterator<ActivityLauchListener> iterator = set.iterator();
			while (iterator.hasNext()) {
				ActivityLauchListener now = iterator.next();
				if (now.getType().equals(type)) {
					toDel = now;
					break;
				}
			}
			if (toDel != null) {
				set.remove(toDel);
			}
		}
	}

	private void exitNow() {
		informListener(ActionType.EXIT, excutionContext);
	}
}
