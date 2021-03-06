package com.example.michaelcs.customview.CompositView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Create by MichaelCS on 2018/8/16 12:02
 * Email: junhong@turingpic.com
 */
public class ProblemHiddenAnimView {

    private int mHeight;//伸展高度

    private View hideView,down;//需要展开隐藏的布局，开关控件

    private RotateAnimation animation;//旋转动画

    /**
     * 构造器(可根据自己需要修改传参)
     * @param context 上下文
     * @param hideView 需要隐藏或显示的布局view
     * @param down 按钮开关的view
     * @param height 布局展开的高度(根据实际需要传)
     */
    public static ProblemHiddenAnimView newInstance(Context context, View hideView, View down, int height){
        return new ProblemHiddenAnimView(context,hideView,down,height);
    }

    private ProblemHiddenAnimView(Context context, View hideView, View down, int height){
        this.hideView = hideView;
        this.down = down;
        float mDensity = context.getResources().getDisplayMetrics().density;
        mHeight = (int) (mDensity * height + 0.5);//伸展高度
    }

    /**
     * 开关
     */
    public void toggle(){
        startAnimation();
        if (View.VISIBLE == hideView.getVisibility()) {
            closeAnimate(hideView);//布局隐藏
        } else {
            openAnim(hideView);//布局铺开
        }
    }

    /**
     * 开关旋转动画
     */
    private void startAnimation() {
        if (View.VISIBLE == hideView.getVisibility()) {
            animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        animation.setDuration(150);//设置动画持续时间
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        down.startAnimation(animation);
    }

    /**
     * 开启动画
     * @param v detail布局
     */
    private void openAnim(final View v) {
        v.setVisibility(View.VISIBLE);
         ValueAnimator animator = createDropAnimator(v, 0, mHeight);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    /**
     * 关闭动画
     * @param view detail布局
     */
    private void closeAnimate(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    /**
     * 动画
     * @param v 要显示的view
     * @param start 动画起始位置
     * @param end 动画结束位置
     * @return 返回动画
     */

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(900);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg) {
                int value = (int) arg.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}

