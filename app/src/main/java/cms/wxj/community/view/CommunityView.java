package cms.wxj.community.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;

import java.io.InputStream;
import java.util.List;

import cms.wxj.community.R;
import cms.wxj.community.bean.BuildingListBean;
import cms.wxj.community.utils.ActivityUtil;

/**
 * Created by 54966 on 2018/6/20.
 */

public class CommunityView extends FrameLayout {

	public static final String		TAG			= "组件";

	private List<BuildingListBean>	buildingListBeanList;

	private Matrix					matrix;

	private Matrix					tempMatrix;

	private float[]					matrixValue	= new float[9];

	private Paint					mPaint;

	private Bitmap					mVillageBitmap;				// 市政中心

	private Bitmap					mBuilder4SBitmap;			// 4s

	private Bitmap					mBuilderEmptyBitmap;		// 空地建筑物

	private int						halfWidth, halfHeight;		// w:540 h:1004

	private boolean					isFirstScale;

	private float					scaleX;

	private float					scaleY;

	private float					detectorFocusX;

	private float					detectorFocusY;

	public CommunityView(@NonNull Context context) {
		this(context, null);
	}

	public CommunityView(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CommunityView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		halfWidth = ActivityUtil.getScreenWidth(context) / 2;
		halfHeight = ActivityUtil.getScreenHeight(context) / 2;
		mPaint = new Paint();
		matrix = new Matrix();
		tempMatrix = new Matrix();
		initBitmap();
	}

	private void initBitmap() {
		mVillageBitmap = readBitMap(getContext(), R.mipmap.img_community_pic_village);

		mBuilder4SBitmap = readBitMap(getContext(), R.mipmap.img_community_pic_4s);
		mBuilderEmptyBitmap = readBitMap(getContext(), R.mipmap.img_community_pic_emptyland);
	}

	@Override protected void onDraw(Canvas canvas) {

		float zoom = getMatrixScaleX();
		float scaleX = zoom;
		float scaleY = zoom;
		float left = 0;
		float top = 0;
		int size = buildingListBeanList.size();
		BuildingListBean buildingListBean = null;
		float translateX = getMatrixTranslateX();
		float translateY = getMatrixTranslateY();

		for (int i = 0; i < size; i++) {
			buildingListBean = buildingListBeanList.get(i);
			left = (halfWidth - (mBuilder4SBitmap.getWidth()) / 2) + (buildingListBean.posX) * mBuilder4SBitmap.getWidth() * scaleX + translateX;
			top = (halfHeight - (mBuilder4SBitmap.getHeight()) / 2) + (buildingListBean.posY) * mBuilder4SBitmap.getHeight() * scaleY + translateY;

			buildingListBean.left = (int) left;
			buildingListBean.top = (int) top;

			tempMatrix.setTranslate(left, top);
			tempMatrix.postScale(scaleX, scaleX, left, top);

			if (i == 0) {
				canvas.drawBitmap(mVillageBitmap, tempMatrix, mPaint);
			} else {
				canvas.drawBitmap(mBuilder4SBitmap, tempMatrix, mPaint);
			}
		}

	}

	private int		lastX, lastY;

	private boolean	pointer;		// 是否是多个手指

	private boolean	isScale;		// 是否正在缩放

	private int		downX, downY;

	@Override public boolean onTouchEvent(MotionEvent event) {
		scaleGestureDetector.onTouchEvent(event);
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (event.getPointerCount() > 1) {
			pointer = true;
		}

		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				pointer = false;
				downX = x;
				downY = y;

				break;
			case MotionEvent.ACTION_MOVE:
				if (!isScale) {
					int dx = Math.abs(x - downX);
					int dy = Math.abs(y - downY);
					if ((dx > 10 || dy > 10) && !pointer) {
						dx = x - lastX;
						dy = y - lastY;
						Log.e("dx和dy", dx + "-" + dy);
						matrix.postTranslate(dx, dy);
						Log.e("postTranslate方法中dx和dy", "dx" + getMatrixTranslateX() + "-" + getMatrixTranslateY());

						invalidate();
					}
				}

				break;
			case MotionEvent.ACTION_UP:

				break;
		}
		lastX = x;
		lastY = y;

		return true;
	}

	ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {

		@Override public boolean onScale(ScaleGestureDetector detector) {
			float scaleFactor = detector.getScaleFactor();

			if (getMatrixScaleX() * scaleFactor <= 1) {
				scaleFactor = 1 / getMatrixScaleX();
			} else if (getMatrixScaleX() * scaleFactor >= 2) {
				scaleFactor = 2 / getMatrixScaleX();
			}

			detectorFocusX = detector.getFocusX();
			detectorFocusY = detector.getFocusY();
			matrix.postScale(scaleFactor, scaleFactor, detectorFocusX, detectorFocusY);

			invalidate();

			return true;
		}

		@Override public boolean onScaleBegin(ScaleGestureDetector detector) {
			return true;
		}

		@Override public void onScaleEnd(ScaleGestureDetector detector) {

		}
	});

	public void setBuildingListBeanList(List<BuildingListBean> buildingListBeanList) {
		this.buildingListBeanList = buildingListBeanList;
		invalidate();
	}

	private float getMatrixScaleX() {
		matrix.getValues(matrixValue);
		return matrixValue[Matrix.MSCALE_X];
	}

	private float getMatrixScaleY() {
		matrix.getValues(matrixValue);
		return matrixValue[Matrix.MSCALE_Y];
	}

	private float getMatrixTranslateX() {
		matrix.getValues(matrixValue);
		return matrixValue[Matrix.MTRANS_X];
	}

	private float getMatrixTranslateY() {
		matrix.getValues(matrixValue);
		return matrixValue[Matrix.MTRANS_Y];
	}

	public void fuwei() {
		Log.e("aa", getMatrixTranslateX() + "/" + getMatrixTranslateY());
		matrix.postTranslate(-getMatrixTranslateX(), -getMatrixTranslateY());
		invalidate();
	}

	/**
	 * 读取图片资源
	 *
	 * @param context
	 * @param resId
	 * @return
	 */
	public Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

}
