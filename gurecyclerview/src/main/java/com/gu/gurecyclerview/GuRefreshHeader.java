package com.gu.gurecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Gu on 2017/3/5.
 */

public class GuRefreshHeader extends LinearLayout implements GuBaseHeader {
    private LinearLayout mContainer;
    private int mMeasuredHeight;
    private AnimationDrawable animationDrawable;
    private TextView msg;
    private int mState = STATE_NORMAL;

    public GuRefreshHeader(Context context) {
        this(context, null);
    }

    public GuRefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.refresh_header, this);
        ImageView img = (ImageView) findViewById(R.id.img);
        animationDrawable = (AnimationDrawable) img.getDrawable();
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        msg = (TextView) findViewById(R.id.msg);
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
        mContainer = (LinearLayout) findViewById(R.id.container);
        mContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.CENTER_HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onMove(float delta) {
        if (getVisiableHeight() > 0 || delta > 0) {
            setVisiableHeight((int) delta + getVisiableHeight());
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisiableHeight() > mMeasuredHeight) {
                    changeRefreshHeaderState(STATE_RELEASE_TO_REFRESH);
                } else {
                    changeRefreshHeaderState(STATE_NORMAL);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        if (getVisiableHeight() == 0) {
            isOnRefresh = false;
        }
        if (getVisiableHeight() > mMeasuredHeight && mState < STATE_REFRESHING) {
            changeRefreshHeaderState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        int destHeight = mState == STATE_REFRESHING ? mMeasuredHeight : 0;
        smoothScrollTo(destHeight);
        return isOnRefresh;
    }

    @Override
    public void refreshComplete() {
        changeRefreshHeaderState(STATE_DONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                reset();
            }
        }, 500);
    }

    public void reset() {
        smoothScrollTo(0);
        changeRefreshHeaderState(STATE_NORMAL);
    }

    @Override
    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    /**
     * 更改状态参数和header上得文字
     *
     * @param state refreshHeader状态
     */
    private void changeRefreshHeaderState(int state) {
        if (state == mState)
            return;
        switch (state) {
            case STATE_NORMAL:
                if (animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                msg.setText(R.string.listview_header_hint_normal);
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (!animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                msg.setText(R.string.listview_header_hint_release);
                break;
            case STATE_REFRESHING:
                msg.setText(R.string.refreshing);
                break;
            case STATE_DONE:
                msg.setText(R.string.refresh_done);
                break;
            default:
        }
        mState = state;
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisiableHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisiableHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public int getState() {
        return mState;
    }
}
