package com.example.parallax;

import android.animation.IntEvaluator;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * Created by zhaojun.gao on 2019/10/12.
 */
public class CustomAnimation extends Animation {
    private ImageView mImg_header;
    private int mStartValue;
    private int mEndValue;

    public CustomAnimation(ImageView img_header, int startValue, int endValue) {
        mImg_header = img_header;
        mStartValue = startValue;
        mEndValue = endValue;
    }

    //重写该方法 interpolatedTime 0.0-1.0
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Log.e("applyTransformation","=========================="+interpolatedTime);
        IntEvaluator intEvaluator = new IntEvaluator();
        mImg_header.getLayoutParams().height =  intEvaluator.evaluate( interpolatedTime,mStartValue,mEndValue );
        mImg_header.requestLayout();
        setDuration( 500 );
        setInterpolator( new OvershootInterpolator(  ) );
        super.applyTransformation( interpolatedTime, t );
    }
}
