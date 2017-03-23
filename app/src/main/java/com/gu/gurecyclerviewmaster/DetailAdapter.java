package com.gu.gurecyclerviewmaster;

import android.view.ViewGroup;

import com.gu.gurecyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.gu.gurecyclerview.baseadapter.BaseRecyclerViewHolder;
import com.gu.gurecyclerviewmaster.databinding.ItemViewBinding;

/**
 * Created by Gu on 2017/3/19.
 */

public class DetailAdapter extends BaseRecyclerViewAdapter<DataBean> {
    @Override
    public BaseRecyclerViewHolder<DataBean,?> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_view);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<DataBean, ItemViewBinding> {

        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final DataBean bean, int position) {
            binding.setDetailBean(bean);
        }
    }
}
