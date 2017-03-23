package com.gu.gurecyclerview;

/**
 * Created by Gu on 2017/3/5.
 */

public interface GuBaseHeader {
    int STATE_NORMAL = 0;
    int STATE_RELEASE_TO_REFRESH = 1;
    int STATE_REFRESHING = 2;
    int STATE_DONE = 3;

    void onMove(float delta);

    boolean releaseAction();

    void refreshComplete();

    int getVisiableHeight();

    void setVisiableHeight(int height);
}
