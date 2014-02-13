package gt.toolbox;

import gt.toolbox.db.DBManager;
import gt.toolbox.listener.ActivityLauchListener;
import gt.toolbox.service.TaskExcutorService;
import gt.toolbox.service.TaskWatcherService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {

	public static enum KEYS {
		ICON, NAME, LISTNER
	};

	private ListView listenerLV;
	private Intent watcherIntent;
	private Intent excutorIntent;

	private DBManager db;

	private FloatWindowManager manager = FloatWindowManager.getManager();
	private List<ActivityLauchListener> listeners;
	private ArrayList<HashMap<String, Object>> list;
	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new DBManager(this);

		listenerLV = (ListView) findViewById(R.id.listenerLV);
		String[] from = { KEYS.ICON.toString(), KEYS.LISTNER.toString(),
				KEYS.NAME.toString() };
		int[] to = { R.id.iconIV, R.id.listenerTypeTV, R.id.appNameTV };
		LoadDataTask task = new LoadDataTask();
		task.execute();

		watcherIntent = new Intent(this, TaskWatcherService.class);
		excutorIntent = new Intent(this, TaskExcutorService.class);
		startService(watcherIntent);
		startService(excutorIntent);

		manager.createFloatWindow(this, true);

		listenerLV
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if (id != -1) {
							int pos = (int) id;
							db.delete(listeners.get(pos));
							listeners.remove(pos);
							list.remove(pos);
							adapter.notifyDataSetChanged();
							System.out.println("deleted " + listeners.size());
						}
					}
				});

		try {
			list = task.get();
			adapter = new SimpleAdapter(getApplicationContext(), list,
					R.layout.listener_item, from, to);
			listenerLV.setAdapter(adapter);
			adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

				@Override
				public boolean setViewValue(View view, Object data,
						String textRepresentation) {
					// TODO Auto-generated method stub
					if (view instanceof ImageView && data instanceof Drawable) {
						((ImageView) view).setImageDrawable((Drawable) data);
						return true;
					}
					return false;
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.menu_exit) {
			stopService(watcherIntent);
			stopService(excutorIntent);
			MainActivity.this.finish();
		}
		return super.onContextItemSelected(item);
	}

	class LoadDataTask extends
			AsyncTask<Void, Integer, ArrayList<HashMap<String, Object>>> {

		private HashMap<String, Drawable> icons = new HashMap<String, Drawable>();
		private PackageManager manager = MainActivity.this.getPackageManager();

		@Override
		protected ArrayList<HashMap<String, Object>> doInBackground(
				Void... params) {
			// TODO Auto-generated method stub
			ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
			MainActivity.this.listeners = db.query();

			for (ActivityLauchListener listener : listeners) {
				try {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(KEYS.LISTNER.toString(), listener.getType());
					System.out.println("--" + listener.getType());
					String packageName = listener.getPackageName();
					map.put(KEYS.ICON.toString(), getIcon(packageName));
					map.put(KEYS.NAME.toString(), manager
							.getApplicationLabel(manager.getApplicationInfo(
									packageName, 0)));
					result.add(map);
				} catch (Exception e) {
					System.out.println("load error" + e.getMessage());
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			for (HashMap<String, Object> map : result) {
				System.out.println(map.get(KEYS.LISTNER.toString()) + "-"
						+ map.get(KEYS.NAME.toString()) + "-"
						+ (map.get(KEYS.ICON.toString()) == null));
			}
		}

		private Drawable getIcon(String packageName)
				throws NameNotFoundException {
			Drawable result = icons.get(packageName);
			if (result == null) {
				result = manager.getApplicationIcon(packageName);
				icons.put(packageName, result);
			}
			return result;
		}
	}
}