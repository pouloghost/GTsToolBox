package gt.toolbox;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {

	private Intent i;
	private Button stopButton;
	private static Window window;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		i = new Intent(this, TaskWatcher.class);

		stopButton = (Button) findViewById(R.id.stopButton);
		stopButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(i);
			}
		});

		MainActivity.window = this.getWindow();

		startService(i);
		TaskWatcher.getInstance().registerListener("gt.toolbox",
				new LockerListener("gt.toolbox"));
		TaskWatcher.getInstance().registerListener("com.bbk.launcher2",
				new BrightnessListener(1));
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

	public static Window getAppWindow() {
		return window;
	}
}
