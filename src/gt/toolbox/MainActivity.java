package gt.toolbox;

import gt.toolbox.db.DBManager;
import gt.toolbox.listener.BrightnessListener;
import gt.toolbox.listener.BrightnessUtils;
import gt.toolbox.listener.ListenerFactory;
import gt.toolbox.listener.ListenerFactory.ListenerType;
import gt.toolbox.listener.LockerListener;
import gt.toolbox.service.TaskExcutorService;
import gt.toolbox.service.TaskWatcherService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

	private Intent watcherIntent;
	private Intent excutorIntent;
	private Button stopButton;
	private Button manualButton;

	private DBManager db;
	// -----------------------float--------------------
	private View floatView;

	private WindowManager wm;
	private float touchStartX;
	private float touchStartY;
	private float x;
	private float y;
	private float statusBar = 30;
	private WindowManager.LayoutParams layoutParams;

	private Intent changeIntent = new Intent(
			TaskExcutorService.MessageType.DIRECT_MODIFY.toString());
	private Intent saveIntent = new Intent(
			TaskExcutorService.MessageType.SAVE_LISTENER.toString());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new DBManager(this);

		stopButton = (Button) findViewById(R.id.stopButton);
		stopButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(watcherIntent);
			}
		});
		manualButton = (Button) findViewById(R.id.manualButton);
		manualButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Settings.System.putInt(TaskWatcherService.getInstance()
						.getContentResolver(),
						Settings.System.SCREEN_BRIGHTNESS_MODE,
						Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
				Uri uri = Settings.System
						.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
				TaskWatcherService.getInstance().getContentResolver()
						.notifyChange(uri, null);
			}
		});

		watcherIntent = new Intent(this, TaskWatcherService.class);
		excutorIntent = new Intent(this, TaskExcutorService.class);
		startService(watcherIntent);
		startService(excutorIntent);
		System.out.println("start in activity");
		createFloatWindow(true);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// -----float----
	private void createFloatWindow(boolean small) {
		if (wm == null) {
			wm = (WindowManager) this.getApplicationContext().getSystemService(
					Context.WINDOW_SERVICE);
			layoutParams = new WindowManager.LayoutParams();
			layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
			layoutParams.format = PixelFormat.RGBA_8888;
			layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
			layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
			layoutParams.x = 0;
			layoutParams.y = 0;

			layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
			layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		}
		if (small) {
			attatchSmall();
		} else {
			attatchBig();
		}
	}

	private void attatchSmall() {
		floatView = View.inflate(getApplicationContext(),
				R.layout.float_window_small, null);
		wm.addView(floatView, layoutParams);
		wm.updateViewLayout(floatView, layoutParams);
		Button goBigButton = (Button) floatView.findViewById(R.id.goBigButton);
		goBigButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				wm.removeView(floatView);
				attatchBig();
			}
		});
	}

	@SuppressLint("NewApi")
	private void attatchBig() {
		floatView = View.inflate(getApplicationContext(),
				R.layout.float_window_big, null);
		wm.addView(floatView, layoutParams);
		wm.updateViewLayout(floatView, layoutParams);
		// bind listeners
		Button goSmallButton = (Button) floatView
				.findViewById(R.id.goSmallButton);
		goSmallButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wm.removeView(floatView);
				attatchSmall();
			}
		});
		Button saveButton = (Button) floatView.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendBroadcast(saveIntent);
			}
		});
		SeekBar brightSeekbar = (SeekBar) floatView
				.findViewById(R.id.brightSeekBar);
		brightSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				changeIntent.putExtra(ListenerFactory.LISTENER_TYPE,
						ListenerType.BRIGHTNESS_MANUAL.toString());
				changeIntent.putExtra(BrightnessListener.BRIGHTNESS, progress);
				sendBroadcast(changeIntent);
			}
		});
		brightSeekbar.setProgress(BrightnessUtils.getBrightness(this));
		CheckBox lockCheckBox = (CheckBox) floatView
				.findViewById(R.id.lockCheckBox);
		lockCheckBox.setChecked(BrightnessUtils.isLocked(this));
		lockCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				changeIntent.putExtra(ListenerFactory.LISTENER_TYPE,
						ListenerType.WAKE_LOCK.toString());
				changeIntent.putExtra(LockerListener.LOCK_ON, isChecked);
				sendBroadcast(changeIntent);
			}
		});
	}
}
