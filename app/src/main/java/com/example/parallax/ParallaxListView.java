package com.example.parallax;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * 1.首先，需要获取header的原始高度，根据手指滑动，动态设置header高度
 * Created by zhaojun.gao on 2019/10/9.
 */
public class ParallaxListView extends ListView {

    private int mHeight, mOriginalHeight;
    private int mPicHeight;
    private ImageView img_header;

    public ParallaxListView(Context context) {
        super( context );
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super( context, attrs );
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
    }

    /**
     * @param deltaX
     * @param deltaY         偏移量，也就是当前要滚动的y值。
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX
     * @param maxOverScrollY
     * @param isTouchEvent
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //deltaY:瞬时偏移量 顶部在头，往下拉为-  回到顶头变为0  底部到头往上拉为+，回到底头为0
        //isTouchEvent 是否是手指触摸滑动，true为手指，false为惯性
        Log.e( "overScrollBy", "deltaY: " + deltaY + " scrollY: " + scrollY + " scrollRangeY: " + scrollRangeY
                + " maxOverScrollY: " + maxOverScrollY + " isTouchEvent: " + isTouchEvent );
        if (deltaY < 0 && isTouchEvent) {//顶头往下拉，把拉动的瞬时变化量绝对值加等header高度，实现放大效果
            //改变控件高度并使之生效
//            mHeight += Math.abs( deltaY );  //注意：再次往下拉无法拉动了，原因
            mHeight = img_header.getHeight()+Math.abs( deltaY );
            if (mHeight < mPicHeight) {
                img_header.getLayoutParams().height = mHeight;
                img_header.requestLayout();
            }
        }
        return super.overScrollBy( deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent );
    }

    /**
     * 传入header图片的引用，获取header图片原始高度
     *
     * @param img_header
     */
    public void setParallaxImg(ImageView img_header) {
        this.img_header = img_header;
        mOriginalHeight = img_header.getHeight();//初始高度~
        int originalMesasuredHeight = img_header.getMeasuredHeight();
        mPicHeight = img_header.getDrawable().getIntrinsicHeight();
        Log.e( "高度", "originalHeight=" + mOriginalHeight + "*****" + " originalMesasuredHeight=" + originalMesasuredHeight
                + "  picHeight=" + mPicHeight );

    }

    /**
     * 回弹处理：让图片回到原始高度，并伴随动画:
     * 从当前高度执行动画到原始高度
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //1.动画执行的开始和结束位置
                final int endValue =  mOriginalHeight;
                final int startValue = img_header.getHeight();
                //方式1
                valueAnimation(startValue,endValue);
                //方式2
//                CustomAnimation customAnimation = new CustomAnimation(img_header,startValue,endValue);
//                startAnimation( customAnimation );
                break;
        }
        return super.onTouchEvent( ev );
    }

    private void valueAnimation(final int startHeight, final int endHeight) {
        //执行回弹动画，从当前高度img_header.getHeight()到原始高度mOriginalHeight
        ValueAnimator valueAnimator = ValueAnimator.ofInt( 1 );
        //AnimatorUpdateListener 监听整个动画过程，每播放一帧，onAnimationUpdate就会被播放一次
        valueAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            //持有一个IntEvaluator，方便下面估值的时候使用
            private IntEvaluator mIntEvaluator = new IntEvaluator();
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();//范围从0-1渐变
                Log.e( "onAnimationUpdate", "onAnimationUpdate=" + animatedFraction );
                //2.使高度实时变化回弹，生效
                img_header.getLayoutParams().height =  mIntEvaluator.evaluate( animatedFraction,startHeight,endHeight );
                img_header.requestLayout();
            }
        } );
        valueAnimator.setDuration( 500 );
        valueAnimator.setInterpolator( new OvershootInterpolator(  ) );//?
        valueAnimator.start();
    }

}
