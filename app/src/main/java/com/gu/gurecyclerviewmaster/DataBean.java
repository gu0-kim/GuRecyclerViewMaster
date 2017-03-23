package com.gu.gurecyclerviewmaster;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Gu on 2017/3/19.
 */

public class DataBean extends BaseObservable implements Serializable {

    private String name;

    public DataBean(String name) {
        this.name = name;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public void setName(String name) {
        this.name = name;
    }


}
