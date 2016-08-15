package com.lisen.android.weixin6_0.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.lisen.android.weixin6_0.R;

/**
 * Created by Administrator on 2016/8/15.
 */
public class MyTabView extends View {

    private Bitmap mIconBitmap;
    private int mColor = 0xff45c01a;
    private String mTabName;
    private int mTabNameSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());

    //用于绘制纯色区域
    private Bitmap mBitmap;
    private Paint mPaint;
    private Canvas mCanvas;
    //透明度
    float mAlpha = 1.0f;
    //用于绘制图标和文字
    private Paint mTextPaint;
    private Rect mIconBound;
    private Rect mTextBound;


    public MyTabView(Context context) {
        this(context, null);
    }

    public MyTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyTabView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            switch (a.getIndex(i)) {
                case R.styleable.MyTabView_tab_icon:
                    BitmapDrawable d = (BitmapDrawable) a.getDrawable(R.styleable.MyTabView_tab_icon);
                    if (d != null) {
                        mIconBitmap = d.getBitmap();
                    }
                    break;
                case R.styleable.MyTabView_tab_color:
                    mColor = a.getColor(R.styleable.MyTabView_tab_color, 0xff45c01a);
                    break;
                case R.styleable.MyTabView_tab_text:
                    mTabName = a.getString(R.styleable.MyTabView_tab_text);
                    break;
                case R.styleable.MyTabView_tab_text_size:
                    mTabNameSize = (int) a.getDimension(R.styleable.MyTabView_tab_text_size,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;

            }
        }
        a.recycle();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTabNameSize);
        mTextPaint.setAntiAlias(true);
        //得到文字的范围
        mTextPaint.getTextBounds(mTabName, 0, mTabName.length(), mTextBound);
        mIconBound = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTextBound.height());
        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        int top = getMeasuredHeight() / 2 - (iconWidth + mTextBound.height()) / 2;
        //得到图标的范围
        mIconBound.set(left, top, left + iconWidth, top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制原图标
         canvas.drawBitmap(mIconBitmap, null, mIconBound, null);
        int alpha = (int) Math.ceil(255 * mAlpha);
        //准备图片
        setupTargetBitmap(alpha);
        //绘制原文字
        drawText(canvas, alpha);
        //绘制变色文本
        drawColorText(canvas, alpha);
        //将准备好的图片画在view上，绘制变色图标
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    /**
     * 绘制变色文本
     * @param canvas
     * @param alpha
     */
    private void drawColorText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        //设置透明度
        mTextPaint.setAlpha(alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconBound.bottom + mTextBound.height();
        canvas.drawText(mTabName, x, y, mTextPaint);
    }

    /**
     * 绘制原文本
     * @param canvas
     * @param alpha
     */
    private void drawText(Canvas canvas, int alpha) {
        mTextPaint.setColor(0xff333333);
        //设置透明度
        mTextPaint.setAlpha(255 - alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconBound.bottom + mTextBound.height();
        canvas.drawText(mTabName, x, y, mTextPaint);
    }

    /**
     * 在内存中准备图片
     */
    private void setupTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //将图片设置成一张画布
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAlpha(alpha);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mCanvas.drawRect(mIconBound, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap, null, mIconBound, mPaint);
    }

    /**
     * 设置透明度，实现变色效果
     * @param alpha
     */

    public void setIconAlpha(float alpha) {
        mAlpha = alpha;
        invalidateView();
    }

    /**
     * 重绘
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            //当前是在ui线程
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public final static String INSTANCE_STATU = "instance";
    public final static String STATU_MALPHA = "mAlpha";
    /**
     * 保存状态
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        //保存系统原来的东西
        bundle.putParcelable(INSTANCE_STATU, super.onSaveInstanceState());
        //保存mAlpha
        bundle.putFloat(STATU_MALPHA, mAlpha);
        return bundle;
    }

    /**
     * 恢复状态
     * @param state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATU));
            mAlpha = bundle.getFloat(STATU_MALPHA);
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
