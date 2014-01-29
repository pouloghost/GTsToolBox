package gt.toolbox.listener;

import java.util.HashMap;

public class ListenerFactory {

	public static enum ListenerType {
		BRIGHTNESS_AUTO, BRIGHTNESS_MANUAL, WAKE_LOCK
	}

	public static enum ObjectType {
		INT, FLOAT, DOUBLE, BOOLEAN, STRING
	}

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
