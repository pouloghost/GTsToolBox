package gt.toolbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AppShowActivity extends Activity {
	ListView lv;
	MyAdapter adapter;
	ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		//lv = (ListView) findViewById(R.id.lv);
		// �õ�PackageManager����
		PackageManager pm = getPackageManager();
		// �õ�ϵͳ��װ�����г������PackageInfo����
		// List<ApplicationInfo> packs = pm.getInstalledApplications(0);
		List<PackageInfo> packs = pm.getInstalledPackages(0);
		for (PackageInfo pi : packs) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			// ��ʾ�û���װ��Ӧ�ó��򣬶�����ʾϵͳ����
			// if((pi.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0&&
			// (pi.applicationInfo.flags&ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)==0)
			// {
			// //�⽫����ʾ���а�װ��Ӧ�ó��򣬰���ϵͳӦ�ó���
			// map.put("icon", pi.applicationInfo.loadIcon(pm));//ͼ��
			// map.put("appName", pi.applicationInfo.loadLabel(pm));//Ӧ�ó�������
			// map.put("packageName", pi.applicationInfo.packageName);//Ӧ�ó������
			// //ѭ����ȡ���浽HashMap�У������ӵ�ArrayList�ϣ�һ��HashMap����һ��
			// items.add(map);
			// }
			// �⽫����ʾ���а�װ��Ӧ�ó��򣬰���ϵͳӦ�ó���
			map.put("icon", pi.applicationInfo.loadIcon(pm));// ͼ��
			map.put("appName", pi.applicationInfo.loadLabel(pm));// Ӧ�ó�������
			map.put("packageName", pi.applicationInfo.packageName);// Ӧ�ó������
			// ѭ����ȡ���浽HashMap�У������ӵ�ArrayList�ϣ�һ��HashMap����һ��
			items.add(map);
		}

		/**
		 * ������Context ArrayList(item�ļ���) item��layout ����ArrayList�е�HashMap��key������
		 * key����Ӧ��ֵ����Ӧ�Ŀؼ�id
		 */
		//adapter = new MyAdapter(this, items, R.layout.piitem, new String[] {
		//		"icon", "appName", "packageName" }, new int[] { R.id.icon,
		//		R.id.appName, R.id.packageName });
		lv.setAdapter(adapter);
	}
}

class MyAdapter extends SimpleAdapter {
	private int[] appTo;
	private String[] appFrom;
	private ViewBinder appViewBinder;
	private List<? extends Map<String, ?>> appData;
	private int appResource;
	private LayoutInflater appInflater;

	public MyAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		appData = data;
		appResource = resource;
		appFrom = from;
		appTo = to;
		appInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent,
				appResource);
	}

	private View createViewFromResource(int position, View convertView,
			ViewGroup parent, int resource) {
		View v;
		if (convertView == null) {
			v = appInflater.inflate(resource, parent, false);
			final int[] to = appTo;
			final int count = to.length;
			final View[] holder = new View[count];
			for (int i = 0; i < count; i++) {
				holder[i] = v.findViewById(to[i]);
			}
			v.setTag(holder);
		} else {
			v = convertView;
		}
		bindView(position, v);
		return v;
	}

	private void bindView(int position, View view) {
		final Map dataSet = appData.get(position);
		if (dataSet == null) {
			return;
		}
		final ViewBinder binder = appViewBinder;
		final View[] holder = (View[]) view.getTag();
		final String[] from = appFrom;
		final int[] to = appTo;
		final int count = to.length;
		for (int i = 0; i < count; i++) {
			final View v = holder[i];
			if (v != null) {
				final Object data = dataSet.get(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}
				boolean bound = false;
				if (binder != null) {
					bound = binder.setViewValue(v, data, text);
				}
				if (!bound) {
					/**
					 * �Զ�����������������������ݴ��ݹ����Ŀؼ��Լ�ֵ���������ͣ�
					 * ִ����Ӧ�ķ��������Ը����Լ���Ҫ�������if��䡣���⣬CheckBox��
					 * ������TextView�Ŀؼ�Ҳ�ᱻʶ���TextView�������Ҫ�ж�ֵ������
					 */

					if (v instanceof TextView) {
						// �����TextView�ؼ��������SimpleAdapter�Դ��ķ����������ı�
						setViewText((TextView) v, text);
					} else if (v instanceof ImageView) {
						// �����ImageView�ؼ��������Լ�д�ķ���������ͼƬ
						setViewImage((ImageView) v, (Drawable) data);
					} else {
						throw new IllegalStateException(
								v.getClass().getName()
										+ " is not a "
										+ "view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}
	}

	public void setViewImage(ImageView v, Drawable value) {
		v.setImageDrawable(value);
	}
}
