package gt.toolbox.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import android.content.Intent;

public class ListenerFactory {

	public static final String LISTENER_TYPE = "listener type";

	public static enum ListenerType {
		BRIGHTNESS_AUTO, BRIGHTNESS_MANUAL, WAKE_LOCK
	}

	public static enum ObjectType {
		INT, FLOAT, DOUBLE, BOOLEAN, STRING
	}

	private static TreeSet<ActivityLauchListener> listeners = new TreeSet<ActivityLauchListener>();

	public static ActivityLauchListener getListener(ListenerType type,
			String packageName, String para) {
		ActivityLauchListener result = null;
		switch (type) {
		case BRIGHTNESS_AUTO:
			result = new AutoBrightnessListener(packageName);
			break;
		case BRIGHTNESS_MANUAL:
			result = new BrightnessListener(parseParameters(para), packageName);
			break;
		case WAKE_LOCK:
			result = new LockerListener(parseParameters(para));
			break;
		}
		return result;
	}

	public static ActivityLauchListener getListener(String type,
			String packageName, String para) {
		ListenerType listenerType = ListenerType.valueOf(type);
		return getListener(listenerType, packageName, para);
	}

	public static ActivityLauchListener addDirectModify(Intent intent,
			String pkg, ArrayList<Boolean> launch) {
		launch.clear();
		ListenerType listenerType = ListenerType.valueOf(intent
				.getStringExtra(LISTENER_TYPE));
		ActivityLauchListener listener = null;
		System.out.println("add " + listenerType.toString() + " "
				+ listeners.size());
		switch (listenerType) {
		case BRIGHTNESS_MANUAL:
			int value = intent.getIntExtra(BrightnessListener.BRIGHTNESS, 1);
			listener = new BrightnessListener(value, pkg);
			launch.add(true);
			listeners.add(listener);
			break;
		case WAKE_LOCK:
			boolean boolVal = intent.getBooleanExtra(LockerListener.LOCK_ON,
					false);
			listener = new LockerListener(pkg);
			launch.add(boolVal);
			listeners.add(listener);
			break;
		case BRIGHTNESS_AUTO:
			break;
		default:
			break;
		}
		return listener;
	}

	public static TreeSet<ActivityLauchListener> getDirectModifies() {
		return listeners;
	}

	public static void clearDirectModifies() {
		listeners = new TreeSet<ActivityLauchListener>();
	}

	private static HashMap<String, Object> parseParameters(String para) {
		// the para is key:type:val;key:type:val;...form
		// should regular expression instead
		HashMap<String, Object> result = new HashMap<String, Object>();
		String[] kvs = para.split(";");
		for (String val : kvs) {
			String[] maps = val.split(":");
			result.put(maps[0], getObject(maps[1], maps[2]));
		}
		return result;
	}

	private static Object getObject(String type, String val) {
		// type is a enum of ObjectType
		ObjectType objectType = ObjectType.valueOf(type);
		Object result = null;
		switch (objectType) {
		case INT:
			int intVal = Integer.parseInt(val);
			result = Integer.valueOf(intVal);
			break;
		case FLOAT:
			float floatVal = Float.parseFloat(val);
			result = Float.valueOf(floatVal);
			break;
		case DOUBLE:
			double doubleVal = Double.parseDouble(val);
			result = Double.valueOf(doubleVal);
			break;
		case BOOLEAN:
			boolean boolVal = Boolean.parseBoolean(val);
			result = Boolean.valueOf(boolVal);
			break;
		case STRING:
			result = val;
			break;
		}
		return result;
	}
}
