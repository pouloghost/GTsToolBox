package gt.toolbox;

import gt.toolbox.listener.BrightnessListener;
import gt.toolbox.listener.LockerListener;
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

public class MainActivity extends Activity {

	private Intent i;
	private Button stopButton;
	private Button manualButton;
	// -----------------------float--------------------
	private Button goBigButton;
	private Button goSmallButton;
	private View floatView;

	private WindowManager wm;
	private float touchStartX;
	private float touchStartY;
	private float x;
	private float y;
	private float statusBar = 30;
	private WindowManager.LayoutParams layoutParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		i = new Intent(this, TaskWatcherService.class);

		stopButton = (Button) findViewById(R.id.stopButton);
		stopButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(i);
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

		startService(i);

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
		goSmallButton = null;

		floatView = View.inflate(getApplicationContext(),
				R.layout.float_window_small, null);
		wm.addView(floatView, layoutParams);
		wm.updateViewLayout(floatView, layoutParams);
		goBigButton = (Button) floatView.findViewById(R.id.goBigButton);
		goBigButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				wm.removeView(floatView);
				attatchBig();
			}
		});
	}

	private void attatchBig() {
		goBigButton = null;

		floatView = View.inflate(getApplicationContext(),
				R.layout.float_window_big, null);
		wm.addView(floatView, layoutParams);
		wm.updateViewLayout(floatView, layoutParams);
		goSmallButton = (Button) floatView.findViewById(R.id.goSmallButton);
		goSmallButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wm.removeView(floatView);
				attatchSmall();
			}
		});
	}
}
