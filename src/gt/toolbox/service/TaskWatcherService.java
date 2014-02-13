package gt.toolbox.service;

import gt.toolbox.ExcutionContext;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

//watching the changes in tasks and broadcast the change
public class TaskWatcherService extends Service {

	public static final String CONTEXT = "gt.task.context";

	// singleton like access
	private static TaskWatcherService self = null;

	// observe task change
	private static String lastPackage = "";
	private ActivityManager manager;
	private Intent intent = new Intent(
			TaskExcutorService.MessageType.TASK_CHANGE.toString());

	// threading
	private static int INTERVAL = 1000;
	private boolean run = true;

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
							// the context needed by the listeners
							intent.putExtra(CONTEXT, new ExcutionContext(
									currentPackage, lastPackage));
							// differ from the direct modify from float window
							sendBroadcast(intent);
							lastPackage = currentPackage;
						}
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
		super.onDestroy();
	}

	public static String topPackage() {
		return lastPackage;
	}
}
