package gt.toolbox;

import gt.toolbox.service.TaskExcutorService;
import gt.toolbox.service.TaskWatcherService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private Intent watcherIntent;
	private Intent excutorIntent;
	private Button stopButton;

	private FloatWindowManager manager = FloatWindowManager.getManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		stopButton = (Button) findViewById(R.id.stopButton);
		stopButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(watcherIntent);
				stopService(excutorIntent);
			}
		});

		watcherIntent = new Intent(this, TaskWatcherService.class);
		excutorIntent = new Intent(this, TaskExcutorService.class);
		startService(watcherIntent);
		startService(excutorIntent);
		System.out.println("start in activity");
		manager.createFloatWindow(this, true);
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

}