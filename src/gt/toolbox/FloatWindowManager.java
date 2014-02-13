package gt.toolbox;

import gt.toolbox.listener.BrightnessListener;
import gt.toolbox.listener.BrightnessUtils;
import gt.toolbox.listener.ListenerFactory;
import gt.toolbox.listener.ListenerFactory.ListenerType;
import gt.toolbox.listener.LockerListener;
import gt.toolbox.service.TaskExcutorService;
import gt.toolbox.service.TaskWatcherService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class FloatWindowManager {

	// singleton
	private static FloatWindowManager instance = null;

	// -----------------------float--------------------
	private View floatView;

	private WindowManager wm;
	private WindowManager.LayoutParams layoutParams;
	private static final String PREF_NAME = "gt.toolbox.pref";

	public static FloatWindowManager getManager() {
		if (instance == null) {
			instance = new FloatWindowManager();
		}
		return instance;
	}

	private Position position;
	private Intent changeIntent = new Intent(
			TaskExcutorService.MessageType.DIRECT_MODIFY.toString());

	private Intent saveIntent = new Intent(
			TaskExcutorService.MessageType.SAVE_LISTENER.toString());;

	private FloatWindowManager() {

	}

	private void attatchBig(final Activity activity) {
		floatView = View.inflate(activity.getApplicationContext(),
				R.layout.float_window_big, null);
		// bind listeners
		Button goSmallB = (Button) floatView.findViewById(R.id.goSmallB);
		goSmallB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wm.removeView(floatView);
				attatchSmall(activity);
				showFloatWindow();
			}
		});
		Button saveB = (Button) floatView.findViewById(R.id.saveB);
		saveB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				activity.sendBroadcast(saveIntent);
			}
		});
		SeekBar brightSB = (SeekBar) floatView.findViewById(R.id.brightSB);
		brightSB.setProgress(BrightnessUtils.getBrightness(activity));
		brightSB.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				changeIntent.putExtra(ListenerFactory.LISTENER_TYPE,
						ListenerType.BRIGHTNESS_MANUAL.toString());
				changeIntent.putExtra(BrightnessListener.BRIGHTNESS, progress);
				activity.sendBroadcast(changeIntent);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});

		CheckBox lockCB = (CheckBox) floatView.findViewById(R.id.lockCB);
		lockCB.setChecked(BrightnessUtils.isLocked(activity,
				TaskWatcherService.topPackage()));
		lockCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				changeIntent.putExtra(ListenerFactory.LISTENER_TYPE,
						ListenerType.WAKE_LOCK.toString());
				changeIntent.putExtra(LockerListener.LOCK_ON, isChecked);
				activity.sendBroadcast(changeIntent);
			}
		});
	}

	private void attatchSmall(final Activity activity) {
		floatView = View.inflate(activity.getApplicationContext(),
				R.layout.float_window_small, null);
		Button goBigB = (Button) floatView.findViewById(R.id.goBigB);
		goBigB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				wm.removeView(floatView);
				attatchBig(activity);
				showFloatWindow();
			}
		});
		// goBigButton.setOnTouchListener(nullListener);
	}

	// -----float----
	public void createFloatWindow(Activity activity, boolean small) {
		if (small) {
			attatchSmall(activity);
		} else {
			attatchBig(activity);
		}

		DisplayMetrics screen = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(screen);

		int width = floatView.getWidth();

		position = new Position(activity.getSharedPreferences(PREF_NAME,
				Activity.MODE_WORLD_READABLE), screen.widthPixels / 2, width);

		if (wm == null) {
			wm = (WindowManager) activity.getApplicationContext()
					.getSystemService(Context.WINDOW_SERVICE);
			layoutParams = new WindowManager.LayoutParams();
			layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
			layoutParams.format = PixelFormat.RGBA_8888;
			layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
			layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
			layoutParams.x = position.getX();
			layoutParams.y = position.getY();

			layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
			layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		}

		showFloatWindow();
	}

	public void finalPosition(int dx, int dy) {
		position.setX((int) (layoutParams.x + dx));
		position.setY((int) (layoutParams.y + dy));
		layoutParams.x = position.getX();
		layoutParams.y = position.getY();
		wm.updateViewLayout(floatView, layoutParams);
	}

	private void showFloatWindow() {
		wm.addView(floatView, layoutParams);
		wm.updateViewLayout(floatView, layoutParams);
	}

	public void updatePosition(int dx, int dy) {
		layoutParams.x += dx;
		layoutParams.y += dy;
		wm.updateViewLayout(floatView, layoutParams);
	}
}
