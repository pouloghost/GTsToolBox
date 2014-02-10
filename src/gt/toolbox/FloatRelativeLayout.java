package gt.toolbox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class FloatRelativeLayout extends RelativeLayout {
	public FloatRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private float touchStartX;
	private float touchStartY;
	private float x;
	private float y;

	// get the event before children get
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		System.out.println("touch event");
		// TODO Auto-generated method stub
		x = event.getRawX();
		y = event.getRawY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchStartX = event.getRawX();
			touchStartY = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			FloatWindowManager.getManager().updatePosition(
					(int) (x - touchStartX), (int) (y - touchStartY));
			break;
		case MotionEvent.ACTION_UP:
			FloatWindowManager.getManager().finalPosition(
					(int) (x - touchStartX), (int) (y - touchStartY));
			break;
		}
		touchStartX = x;
		touchStartY = y;
		return super.onInterceptTouchEvent(event);
	}
}
