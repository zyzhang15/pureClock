package cn.edu.wic.zzy.pureclock.view;

import java.util.Calendar;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import cn.edu.wic.zzy.pureclock.R;

public class ClockView extends View {
	private static final String TAG = "view.clockview";
	private Handler handler;
	private int pointX;// 屏幕中心X
	private int pointY;// 屏幕中心Y
	private int raduisDial;// 表盘半径
	private int raduisNum;// 数字刻度所在圆的半径
	private float mDensityDpi;// 屏幕密度dpi(每英寸包含像素的个数)
	private int screenWidth;// 屏幕宽度
	private int screenHeight;// 屏幕高度

	Paint mpaint;
	public String YOUR_WORD = "YOU ARE THE ONE";// 一句自定义的文字，可以显示到屏幕时钟上
	private static final double ROTATE_HOUR = Math.PI / 6;// 表盘上每小时角度的弧度
	private static final float ROTATE_SECOND_EVERY_SECOND = 6;// 秒针每秒转过的角度
	private Calendar mCalendar;// 日期类

	public ClockView(Context context) {
		this(context, null);
	}

	public ClockView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {

		super(context, attrs);
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		mDensityDpi = displayMetrics.densityDpi / 300;
		// 找出屏幕中心点
		pointX = screenWidth / 2;
		pointY = screenHeight / 2;
		raduisDial = (int) (Math.min(pointX, pointY) * 0.99);// 使用较小边是为了方便在旋转屏幕时时钟大小不变
		raduisNum = (int) (Math.min(pointX, pointY) * 0.85);

		Log.d(TAG, "密度：densityDpi=" + displayMetrics.densityDpi);
		Log.d(TAG, "mDensityDpi=" + mDensityDpi);
		Log.d(TAG, "宽度像素：screenWidth=" + screenWidth);
		Log.d(TAG, "高度像素：screenHeight=" + screenHeight);
		Log.d(TAG, "xdpi=" + displayMetrics.xdpi);
		Log.d(TAG, "ydpi=" + displayMetrics.ydpi);

		mpaint = new Paint();
		mpaint.setAntiAlias(true);
		mpaint.setStyle(Paint.Style.FILL);
		mpaint.setColor(Color.WHITE);
		// 实例化Handler
		handler = new Handler();
		/**
		 * 
		 *使当前视图失效，并（调用OnDraw(Canvas canvas)）
		 *重绘界面（postInvalidate()用于在子线程中刷新UI）
		 *将一个线程加入到队列中，并延时1秒后执行
		 *即每秒界面界面刷新一次，产生秒针每秒动一下的效果
		*/
		handler.post(new Runnable() {
			@Override
			public void run() {
				postInvalidate();
				handler.postDelayed(this, 1000);
			}
		});

	}

	/**
	 * 画整个界面
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// 画表盘
		drawDial(canvas, mpaint);
		// 画刻度数字
		drawNumber(canvas, mpaint);
		// 画文字，日期，及星期
		drawText(canvas, mpaint);
		// 画针
		drawPointer(canvas, mpaint);
	}

	/**
	 * 画表盘
	 * 
	 * @param canvas
	 */
	private void drawDial(Canvas canvas, Paint mpaint) {

		// 画表盘
		for (int i = 0; i < 60; i++) {
			if (i % 5 == 0) {
				// 画刻度时，为了美观，将刻度线画为圆角矩形
				float LineWidth = 3 * mDensityDpi;// 刻度宽度的（1/2）
				float baseX1 = pointX - raduisDial + 5 * mDensityDpi;
				float baseY1 = pointY - LineWidth;// 两个y的差是宽度
				float baseX2 = pointX - raduisDial + 25 * mDensityDpi;
				float baseY2 = pointY + LineWidth;
				float raduisLine = LineWidth;// 线段端点圆弧的半径
				RectF oval = new RectF(baseX1, baseY1, baseX2, baseY2);// 设置个新的长方形

				mpaint.setStyle(Paint.Style.FILL);
				mpaint.setColor(Color.WHITE);

				canvas.drawRoundRect(oval, raduisLine, raduisLine * 2, mpaint);
			} else {
				float LineWidth = 1 * mDensityDpi;// 刻度（1/2）宽度
				float baseX1 = pointX - raduisDial + 5 * mDensityDpi;
				float baseY1 = pointY - LineWidth;// 两个y的差是宽度
				float baseX2 = pointX - raduisDial + 15 * mDensityDpi;
				float baseY2 = pointY + LineWidth;
				float raduisLine = LineWidth;// 线段端点圆弧的半径
				RectF oval = new RectF(baseX1, baseY1, baseX2, baseY2);// 设置个新的长方形
				canvas.drawRoundRect(oval, raduisLine, raduisLine, mpaint);
			}
			canvas.rotate(ROTATE_SECOND_EVERY_SECOND, pointX, pointY);

		}
	}

	/**
	 * 画刻度数字
	 * 
	 * @param canvas
	 */
	private void drawNumber(Canvas canvas, Paint mpaint) {
		// 画刻度
		for (int i = 0; i < 12; i++) {
			String NUMBER = String.valueOf(((i + 2) % 12) + 1);
			mpaint.setTextSize(70 * mDensityDpi);
			// 文字偏X移量=pointX-文字的宽的一半
			float pointXOffsets = pointX - mpaint.measureText(NUMBER) / 2;
			// 文字偏Y移量=pointY-文字高度的一半(一点慢慢儿调整出的距离)(mpaint.ascent()返回值为负数，理论上应该取反，为何取反后反而对不齐？？？)
			float pointYOffsets = pointY - (mpaint.ascent() / 2 + mpaint.descent() / 2);

			float baseX = (float) (pointXOffsets + raduisNum * Math.cos(i * ROTATE_HOUR));
			float baseY = (float) (pointYOffsets + raduisNum * Math.sin(i * ROTATE_HOUR));

			canvas.drawText(NUMBER, baseX, baseY, mpaint);

			// 辅助圆,检测数字刻偏移是否正常
			/*
			 * Paint mPaint=new Paint(); mPaint.setStyle(Paint.Style.STROKE);
			 * mPaint.setColor(Color.WHITE); canvas.drawCircle(pointX, pointY,
			 * (float) (raduisNum),mPaint);
			 */
		}
	}

	/**
	 * 画文字，星期，日期
	 */
	private void drawText(Canvas canvas, Paint mpaint) {

		// ******画文字*****************************************************************
		mpaint.setTextSize(30);
		// 文字偏X方向偏移量
		float pointXOffsets = mpaint.measureText(YOUR_WORD) / 2;// 文字的宽度一半
		// 文字偏Y方向偏移量
		float pointYOffsets = mpaint.ascent() / 2 + mpaint.descent() / 2;// 文字的高度一般
		float baseX = pointX - pointXOffsets;// x需要往左缩进
		float baseY = pointY - raduisDial / 3 - pointYOffsets;// Y需要向下移
		canvas.drawText(YOUR_WORD, baseX, baseY, mpaint);

		// *********画星期**************************************************************
		mCalendar = Calendar.getInstance();
		String dayOfWeek = this.getWeek(mCalendar.get(Calendar.DAY_OF_WEEK));// 当前星期
		// 文字偏X移量
		mpaint.setTextSize(40);
		pointXOffsets = mpaint.measureText(dayOfWeek) / 2;// 文字的宽度一半
		// Log.d(TAG, "OneWord的size:"+mpaint.getTextSize());
		// 文字偏X移量
		pointYOffsets = mpaint.ascent() / -2 - mpaint.descent() / 2;// 文字的高度一般

		baseX = pointX - raduisDial / 2 - pointXOffsets;// x需要往左缩进
		baseY = pointY + pointYOffsets;// Y需要向下移
		canvas.drawText(dayOfWeek, baseX, baseY, mpaint);

		// *********画日期***************************************************************
		String monthOfYear = this.getMonth((mCalendar.get(Calendar.MONTH) + 1));// 当前日期

		// 画月**************************************************************************
		// 文字偏X移量
		pointXOffsets = mpaint.measureText(monthOfYear) / 2;// 文字的宽度一半
		// 文字偏X移量
		pointYOffsets = mpaint.ascent() / 2 + mpaint.descent() / 2;// 文字的高度一般
		baseX = pointX + raduisDial / 4 + pointXOffsets;// x需要往左缩进
		baseY = pointY + pointYOffsets;// Y需要向下移
		canvas.drawText(monthOfYear, baseX, baseY, mpaint);
		// 画日***************************************************************************
		String dayOfMonth = String.valueOf(mCalendar.get(Calendar.DAY_OF_MONTH));// 当前日期
		// 文字偏X移量
		mpaint.setTextSize(50);
		pointXOffsets = mpaint.measureText(dayOfMonth) / 2;// 文字的宽度一半
		// 文字偏X移量
		pointYOffsets = mpaint.ascent() / 2 + mpaint.descent() / 2;// 文字的高度一般
		baseX = pointX + raduisDial / 2 + pointXOffsets;// x需要往左缩进
		baseY = pointY + pointYOffsets;// Y需要向下移
		canvas.drawText(dayOfMonth, baseX, baseY, mpaint);

		baseX = pointX + raduisDial / 2 - 28 * mDensityDpi;
		baseY = pointY + 28 * mDensityDpi;

		float rectWidth = pointYOffsets * 2 + 25 * mDensityDpi;

		mpaint.setStyle(Paint.Style.STROKE);
		mpaint.setStrokeWidth(5 * mDensityDpi);
		canvas.drawRect(baseX, baseY - rectWidth, baseX + rectWidth, baseY, mpaint);
	}

	/**
	 * 画指针
	 */
	private void drawPointer(Canvas canvas, Paint mpaint) {
		// 在画指针时是以9点钟为基点，所以需要将其先转至12点位置，再进行相应的旋转操作
		canvas.save();// 状态A
		canvas.rotate(ROTATE_SECOND_EVERY_SECOND * 15, pointX, pointY);

		/*if (hourAndMinuteNeedChange) {
		    drawPinterHour(canvas, mpaint);
			drawPinterMinute(canvas, mpaint);
		 }*/

	    drawPinterHour(canvas, mpaint);
		drawPinterMinute(canvas, mpaint);
		drawPinterSecond(canvas, mpaint);

		// Log.d(TAG, "drawPointer:" + canvas.getSaveCount());// 查看当前状态栈的状态个数
		canvas.restore();// 三个针画完后再恢复画板到状态A
	}

	/**
	 * 画时针
	 * 
	 * @param canvas
	 * @param mpaint
	 */
	private void drawPinterHour(Canvas canvas, Paint mpaint) {
		float timeNowHour = mCalendar.get(Calendar.HOUR);// 当前时
		float timeNowMinute = mCalendar.get(Calendar.MINUTE);// 当前分
		float timeNowSecond = mCalendar.get(Calendar.SECOND);// 当前秒
		float hour = timeNowHour + timeNowMinute / 60.0f + timeNowSecond / 3600.0f;// 此时准确的小时值

		canvas.save();
		// 旋转画板，第一个参数为旋转角度，第二、三个参数为旋转坐标点
		canvas.rotate(hour / 12.0f * 360.0f, pointX, pointY);//

		float pointwidth = 10 * mDensityDpi;// 针的（1/2）宽度
		float baseX1 = pointX - raduisDial * 0.6f;
		float baseY1 = pointY - pointwidth;// 两y之差为线的宽度
		float baseX2 = pointX;
		float baseY2 = pointY + pointwidth;
		float raduisLine = pointwidth;
		RectF oval3 = new RectF(baseX1, baseY1, baseX2, baseY2);// 设置个新的长方形
		mpaint.setStyle(Paint.Style.FILL);
		mpaint.setColor(Color.WHITE);
		canvas.drawRoundRect(oval3, raduisLine, raduisLine, mpaint);
		canvas.drawCircle(pointX, pointY, pointwidth * 2, mpaint);
		canvas.restore();// 恢复画板到最初状态
	}

	/**
	 * 画分针
	 * 
	 * @param canvas
	 * @param mpaint
	 */
	private void drawPinterMinute(Canvas canvas, Paint mpaint) {

		float timeNowMinute = mCalendar.get(Calendar.MINUTE);// 当前时间的分
		float timeNowSecond = mCalendar.get(Calendar.SECOND);// 当前时间的秒

		//A方案：计算出分针准确的值，分针每秒都会缓慢移动
		//B方案：计算出分针的整数值，分针每分钟跳动一次
		float minute_A = timeNowMinute + timeNowSecond / 60.0f;
		float minute_B = timeNowMinute;
		canvas.save();
		// 旋转画板，第一个参数为旋转角度，第二、三个参数为旋转坐标点
		canvas.rotate(minute_B / 60.0f * 360.0f, pointX, pointY);

		float baseX1 = pointX - raduisDial * 0.8f;
		float pointwidth = 8 * mDensityDpi;// 针的（1/2）宽度
		float baseY1 = pointY - pointwidth;
		float baseX2 = pointX;
		float baseY2 = pointY + pointwidth;// 两y之差为线的宽度
		float raduisLine = pointwidth;// 线的端点的圆角半径为线宽的一半
		RectF oval3 = new RectF(baseX1, baseY1, baseX2, baseY2);// 设置个新的长方形
		canvas.drawRoundRect(oval3, raduisLine, raduisLine, mpaint);

		mpaint.setStyle(Paint.Style.FILL);
		mpaint.setColor(Color.WHITE);
		canvas.drawCircle(pointX, pointY, pointwidth * 2, mpaint);
		canvas.restore();// 恢复画板到最初状态
	}

	private void drawPinterSecond(Canvas canvas, Paint mpaint) {

		float timeNowSecond = mCalendar.get(Calendar.SECOND);// 当前时间的秒

		float second = timeNowSecond;// 此时秒值
		canvas.save();
		canvas.rotate(second / 60.0f * 360.0f, pointX, pointY);
		// 旋转画板，第一个参数为旋转角度，第二、三个参数为旋转坐标点
		float pointwidth = 4 * mDensityDpi;// 针的（1/2）宽度

		float baseX1 = pointX - raduisDial * 0.85f;
		float baseY1 = pointY - pointwidth;// 两y之差为线的宽度
		float baseX2 = pointX;
		float baseY2 = pointY + pointwidth;
		float raduisLine = pointwidth;// 线的端点的圆角半径为线宽的一半

		RectF oval = new RectF(baseX1, baseY1, baseX2, baseY2);// 设置个新的长方形
		mpaint.setColor(Color.RED);
		mpaint.setStyle(Paint.Style.STROKE);
		canvas.drawRoundRect(oval, raduisLine, raduisLine, mpaint);

		// 再画秒针的尾巴效果
		pointwidth = 6 * mDensityDpi;// 针的（1/2）宽度
		float baseX3 = pointX;
		float baseY3 = pointY - pointwidth;// 两y之差为线的宽度;
		float baseX4 = pointX + raduisDial * 0.5f;
		float baseY4 = pointY + pointwidth;// 两y之差为线的宽度;

		oval = new RectF(baseX3, baseY3, baseX4, baseY4);// 设置个新的长方形
		raduisLine = pointwidth;// 线的端点的圆角半径为线宽的一半
		canvas.drawRoundRect(oval, raduisLine, raduisLine, mpaint);

		canvas.drawCircle(pointX, pointY, pointwidth * 2, mpaint);

		canvas.restore();// 恢复画板到最初状态

	}

	/**
	 * 获取按一定格式化的月份
	 * 
	 * @param month
	 * @return
	 */
	private String getMonth(int month) {

		switch (month) {
		case 1:
			return this.getContext().getString(R.string.January);
		case 2:
			return this.getContext().getString(R.string.February);
		case 3:
			return this.getContext().getString(R.string.Marth);
		case 4:
			return this.getContext().getString(R.string.April);
		case 5:
			return this.getContext().getString(R.string.May);
		case 6:
			return this.getContext().getString(R.string.June);
		case 7:
			return this.getContext().getString(R.string.July);
		case 8:
			return this.getContext().getString(R.string.August);
		case 9:
			return this.getContext().getString(R.string.September);
		case 10:
			return this.getContext().getString(R.string.October);
		case 11:
			return this.getContext().getString(R.string.December);
		case 12:
			return this.getContext().getString(R.string.November);

		default:
			return "";
		}
	}

	/**
	 * 获取按一定格式化的星期
	 * 
	 * @param week
	 * @return
	 */
	private String getWeek(int week) {
		switch (week) {
		case 1:
			return this.getContext().getString(R.string.Sunday);
		case 2:
			return this.getContext().getString(R.string.Monday);
		case 3:
			return this.getContext().getString(R.string.Tuesday);
		case 4:
			return this.getContext().getString(R.string.Wednesday);
		case 5:
			return this.getContext().getString(R.string.Thursday);
		case 6:
			return this.getContext().getString(R.string.Friday);
		case 7:
			return this.getContext().getString(R.string.Saturday);

		default:
			return "";
		}
	}

}
