package com.fei.parallaxviewpager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

/**
 * @ClassName: SplashView
 * @Description: 启动View
 * @Author: Fei
 * @CreateDate: 2021/1/27 8:56
 * @UpdateUser: Fei
 * @UpdateDate: 2021/1/27 8:56
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SplashView extends View {

    // 旋转动画执行的时间
    private final long ROTATION_ANIMATION_TIME = 1400;
    private Paint mPaint;
    //大圆半径,宽度的1/4
    private float mBigCircleRadius;
    //小圆半径,大圆半径1/8
    private float mSmallCircleRadius;
    //颜色数组
    private int[] mColors;
    //当前状态
    private SplashState mSplashState;
    //中心点
    private int mCenterX;
    private int mCenterY;
    //当前角度
    private float mCurrentAngle;
    //当前半径
    private float mCurrentRadius;
    //对角线一般
    private double mDiagonal;
    //扩展半径
    private float mExpandRadius;

    public SplashView(Context context) {
        this(context, null);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mColors = getResources().getIntArray(R.array.splashViewColor);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mCenterX = getMeasuredWidth() / 2;
        mCenterY = getMeasuredHeight() / 2;
        mBigCircleRadius = getMeasuredWidth() / 4f;
        mSmallCircleRadius = mBigCircleRadius / 8f;
        mDiagonal = Math.sqrt(mCenterX * mCenterY + mCenterY * mCenterY);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mSplashState == null) {
            mSplashState = new RotateSplashState();
        }
        mSplashState.onDraw(canvas);
    }

    public void disappear() {
        if (mSplashState != null && mSplashState instanceof RotateSplashState) {
            ((RotateSplashState) mSplashState).cancel();
        }
    }

    private abstract class SplashState {
        public abstract void onDraw(Canvas canvas);
    }

    /**
     * 旋转
     */
    private class RotateSplashState extends SplashState {

        private ValueAnimator valueAnimator;

        public RotateSplashState() {
            //旋转0-360,这里一定要用PI,直接用360不是弧度制
            valueAnimator = ObjectAnimator.ofFloat(0f, (float) (Math.PI * 2));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //需要将值放到全局，以便下一个动画使用
                    mCurrentAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
//            valueAnimator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    //可以这里停止后开启缩放动画，或者开启无限循环，用户自己控制下一个动画时间
//                }
//            });
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setRepeatCount(-1);
            valueAnimator.setDuration(ROTATION_ANIMATION_TIME * 2);
            valueAnimator.start();
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            //1.画小圆，小圆角度不断变化，使之转动
            //获取平均每个角度
            float degree = (float) (Math.PI * 2f / mColors.length);//原来角度
            for (int i = 0; i < mColors.length; i++) {
                mPaint.setColor(mColors[i]);
                //原来角度+旋转角度，利用三角函数
                float x = (float) (mCenterX + Math.cos(degree * i + mCurrentAngle) * mBigCircleRadius);
                float y = (float) (mCenterY + Math.sin(degree * i + mCurrentAngle) * mBigCircleRadius);
                canvas.drawCircle(x, y, mSmallCircleRadius, mPaint);
            }
        }

        public void cancel() {
            //停止旋转动画
            valueAnimator.cancel();
            //开启缩放动画
            mSplashState = new ScaleSplashState();
        }
    }

    /**
     * 缩放
     */
    private class ScaleSplashState extends SplashState {

        public ScaleSplashState() {
            //从大圆半径到0
            ValueAnimator valueAnimator = ObjectAnimator.ofFloat(mBigCircleRadius, 0);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSplashState = new ExpandSplashState();
                }
            });
            valueAnimator.setInterpolator(new AnticipateInterpolator(5f));
            valueAnimator.setDuration(ROTATION_ANIMATION_TIME);
            valueAnimator.start();
        }

        @Override
        public void onDraw(Canvas canvas) {
            //2.缩放，改变大圆半径
            canvas.drawColor(Color.WHITE);
            //1.画小圆，小圆角度不断变化，使之转动
            //获取平均每个角度
            float degree = (float) (Math.PI * 2f / mColors.length);//原来角度
            for (int i = 0; i < mColors.length; i++) {
                mPaint.setColor(mColors[i]);
                //原来角度+旋转角度，利用三角函数
                float x = (float) (mCenterX + Math.cos(degree * i + mCurrentAngle) * mCurrentRadius);
                float y = (float) (mCenterY + Math.sin(degree * i + mCurrentAngle) * mCurrentRadius);
                canvas.drawCircle(x, y, mSmallCircleRadius, mPaint);
            }
        }
    }

    /**
     * 扩散
     */
    private class ExpandSplashState extends SplashState {

        public ExpandSplashState() {
            mPaint.setColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.STROKE);
            //0-对角线一半，圆的半径
            ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, (float) mDiagonal);
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    setVisibility(GONE);
                }
            });
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mExpandRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.setDuration(ROTATION_ANIMATION_TIME);
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.start();
        }

        @Override
        public void onDraw(Canvas canvas) {
            //3.扩散，画空心圆，改变画笔宽度
            float strokeWidth = (float) (mDiagonal - mExpandRadius);
            mPaint.setStrokeWidth(strokeWidth);
            float radius = mExpandRadius + strokeWidth / 2;
            canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);
        }
    }
}
