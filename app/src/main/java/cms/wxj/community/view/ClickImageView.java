package cms.wxj.community.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by 54966 on 2018/6/28.
 */

public class ClickImageView extends ImageView {

	public ClickImageView(Context context) {
		super(context);
	}

	public ClickImageView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public ClickImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private int	downX;

	private int	downY;

	@Override public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				downX = x;
				downY = y;

				break;
			case MotionEvent.ACTION_UP:
				if (downX == x && downY == y) {
					Log.e("onTouchEvent", "imageVIew自己处理");
					return true;
				}

				break;
		}

		return false;
	}

}
