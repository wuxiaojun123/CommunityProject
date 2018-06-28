package cms.wxj.community.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import cms.wxj.community.R;
import cms.wxj.community.bean.BuildingListBean;
import cms.wxj.community.utils.ActivityUtil;

/**
 * Created by 54966 on 2018/6/27.
 */

public class CommunityView2 extends FrameLayout {

	private int						halfWidth, halfHeight;		// w:540 h:1004

	private List<BuildingListBean>	buildingListBeanList;

	private Matrix					matrix;

	private float[]					matrixValue	= new float[9];

	private Bitmap					mBuilder4SBitmap;			// 4s

	private int						lastX;

	private int						lastY;

	private int						downX;

	private int						downY;

	private boolean					isScale;

	private boolean					pointer;

	public CommunityView2(@NonNull Context context) {
		this(context, null);
	}

	public CommunityView2(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CommunityView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		halfWidth = ActivityUtil.getScreenWidth(context) / 2;
		halfHeight = ActivityUtil.getScreenHeight(context) / 2;
		matrix = new Matrix();

		mBuilder4SBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_community_pic_4s);
		Log.e("CommunityView2", mBuilder4SBitmap.getWidth() + "宽度");
	}

	@Override protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float scaleX = getMatrixScaleX();
		float scaleY = getMatrixScaleY();
		float translateX = getMatrixTranslateX();
		float translateY = getMatrixTranslateY();
		BuildingListBean buildingListBean = null;
		int size = buildingListBeanList.size();

		for (int i = 0; i < size; i++) {
			buildingListBean = buildingListBeanList.get(i);
			buildingListBean.left = getMarginLeft(buildingListBean.posX, buildingListBean.width, scaleX, translateX);
			buildingListBean.top = getMarginTop(buildingListBean.posY, buildingListBean.height, scaleY, translateY);
			buildingListBean.right = getMarginRight(buildingListBean.left, buildingListBean.width, scaleX);
			buildingListBean.bottom = getMarginBottom(buildingListBean.top, buildingListBean.height, scaleY);

			if (i == 0) Log.e("onDraw点击的", "left=" + buildingListBean.left + "right=" + buildingListBean.right + "top=" + buildingListBean.top + "bottom=" + buildingListBean.bottom);
		}
		// this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		canvas.concat(matrix);
		// this.setLayerType(View.LAYER_TYPE_NONE, null);

	}

	@Override public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		gestureDetector.onTouchEvent(event);
		scaleGestureDetector.onTouchEvent(event);

		if (event.getPointerCount() > 1) {
			pointer = true;
		}

		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				downX = x;
				downY = y;
				pointer = false;

				break;
			case MotionEvent.ACTION_MOVE:
				if (!isScale) {
					int dx = Math.abs(x - downX);
					int dy = Math.abs(y - downY);
					if (dx > 10 && dy > 10 && !pointer) {
						dx = x - lastX;
						dy = y - lastY;
						matrix.postTranslate(dx, dy);
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

	ScaleGestureDetector	scaleGestureDetector	= new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {

														@Override public boolean onScale(ScaleGestureDetector detector) {

															float scaleFactor = detector.getScaleFactor();																																																																																																																																													// scaleFactor:2
																																																																																																																																																																							// getMatrixScale:2
															if (getMatrixScaleX() * scaleFactor <= 1.0f) {
																scaleFactor = 1.0f / getMatrixScaleX();
															} else if (getMatrixScaleX() * scaleFactor >= 2.0f) {
																scaleFactor = 2.0f / getMatrixScaleX();
															}

															matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
															invalidate();

															return true;
														}

														@Override public boolean onScaleBegin(ScaleGestureDetector detector) {
															isScale = true;
															return true;
														}

														@Override public void onScaleEnd(ScaleGestureDetector detector) {
															isScale = false;
														}
													});

	GestureDetector			gestureDetector			= new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

														@Override public boolean onSingleTapConfirmed(MotionEvent e) {
															int x = (int) e.getX();
															int y = (int) e.getY();
															int rawX = (int) e.getRawX();
															int rawY = (int) e.getRawY();
															Log.e("onSingleTapConfirmed", "x=" + x + "y=" + y + "rawX=" + rawX + "rawY=" + rawY);

															if (buildingListBeanList != null) {
																int size = buildingListBeanList.size();
																BuildingListBean bean = null;
																for (int i = 0; i < size; i++) {
																	bean = buildingListBeanList.get(i);

																	if (x > Math.abs(bean.left) && x < bean.right && y > Math.abs(bean.top) && y < bean.bottom) {
																		show("i=" + i);
																		break;
																	}
																}
															}

															return super.onSingleTapConfirmed(e);
														}

														@Override public void onLongPress(MotionEvent e) {
															int x = (int) e.getX();
															int y = (int) e.getY();

															if (buildingListBeanList != null) {
																int size = buildingListBeanList.size();
																BuildingListBean bean = null;
																for (int i = 0; i < size; i++) {
																	bean = buildingListBeanList.get(i);

																	if (x > Math.abs(bean.left) && x < bean.right && y > Math.abs(bean.top) && y < bean.bottom) {
																		show("长按i=" + i);
																		break;
																	}
																}
															}

														}
													});

	public void setBuildingListBeanList(List<BuildingListBean> list) {
		this.buildingListBeanList = list;

		float zoom = getMatrixScaleX();
		float translateX = getMatrixTranslateX();
		float translateY = getMatrixTranslateY();

		BuildingListBean buildingListBean = null;
		LayoutInflater inflater = LayoutInflater.from(getContext());
		int size = buildingListBeanList.size();

		for (int i = 0; i < size; i++) {
			buildingListBean = buildingListBeanList.get(i);
			View view = inflater.inflate(R.layout.community_item2, null);
			ClickImageView imageView = view.findViewById(R.id.id_iv_building);
			if (i == 0) {
				imageView.setBackgroundResource(R.mipmap.img_community_pic_village);
			} else {
				imageView.setBackgroundResource(R.mipmap.img_community_pic_4s);
			}

			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			buildingListBean.left = getMarginLeft(buildingListBean.posX, buildingListBean.width, zoom, translateX);
			buildingListBean.top = getMarginTop(buildingListBean.posY, buildingListBean.height, zoom, translateY);
			buildingListBean.right = getMarginRight(buildingListBean.left, buildingListBean.width, zoom);
			buildingListBean.bottom = getMarginBottom(buildingListBean.top, buildingListBean.height, zoom);

			params.setMargins(buildingListBean.left, buildingListBean.top, 0, 0);
			view.setLayoutParams(params);

			// view.setOnLongClickListener(new OnLongClickListener() {
			// @Override
			// public boolean onLongClick(View v) {
			// show("长按");
			// return false;
			// }
			// });

			addView(view);
		}

	}

	public int getMarginLeft(int posX, int width, float zoom, float translateX) {
		return (int) ((halfWidth - (mBuilder4SBitmap.getWidth() * width) / 2) + (posX) * mBuilder4SBitmap.getWidth() * zoom + translateX);
	}

	public int getMarginTop(int posY, int height, float zoom, float translateY) {
		return (int) ((halfHeight - (mBuilder4SBitmap.getHeight() * height) / 2) + (posY) * mBuilder4SBitmap.getHeight() * zoom + translateY);
	}

	public int getMarginRight(int left, int width, float scaleX) {
		return Math.abs(left) + (int) ((width) * mBuilder4SBitmap.getWidth() * scaleX);
	}

	public int getMarginBottom(int top, int height, float scaleY) {
		return Math.abs(top) + (int) ((height) * mBuilder4SBitmap.getHeight() * scaleY);
	}

	private void initClick(ClickImageView imageView, final BuildingListBean buildingListBean) {
		imageView.setOnClickListener(new OnClickListener() {

			@Override public void onClick(View v) {
				show(buildingListBean.toString());
			}
		});
	}

	public void show(String str) {
		Toast.makeText(getContext(), "点击事件" + str, Toast.LENGTH_SHORT).show();
	}

	public float getMatrixTranslateX() {
		matrix.getValues(matrixValue);
		return matrixValue[Matrix.MTRANS_X];
	}

	public float getMatrixTranslateY() {
		matrix.getValues(matrixValue);
		return matrixValue[Matrix.MTRANS_Y];
	}

	public float getMatrixScaleX() {
		matrix.getValues(matrixValue);
		return matrixValue[Matrix.MSCALE_X];
	}

	public float getMatrixScaleY() {
		matrix.getValues(matrixValue);
		return matrixValue[Matrix.MSCALE_Y];
	}

}
