package com.gu.gurecyclerviewmaster;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.gu.gurecyclerview.GuRecyclerView;
import com.gu.gurecyclerviewmaster.databinding.ActivityMainBinding;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mActivityMainBinding;
    int mHeaderValue = 0, mTailValue = -1;
    static final int ADDNUM = 5;
    DetailAdapter adapter;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mActivityMainBinding.rv.setLayoutManager(new LinearLayoutManager(this));
        mActivityMainBinding.rv.setLoadingListener(new GuRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshRecyclerView();
            }

            @Override
            public void onLoadMore() {
                loadMoreRecyclerView();
            }
        });
        adapter = new DetailAdapter();
        addHeader(adapter.getData());
        mActivityMainBinding.rv.setAdapter(adapter);
    }

    private void addHeader(List<DataBean> list) {
        for (int i = ADDNUM; i > 0; i--) {
            list.add(0, new DataBean(String.valueOf(i + mHeaderValue)));
        }
        mHeaderValue -= ADDNUM;
    }

    private void addTail(List<DataBean> list) {
        if (!list.isEmpty())
            mTailValue = Integer.valueOf(list.get(list.size() - 1).getName());
        for (int i = 0; i < ADDNUM; i++) {
            list.add(new DataBean(String.valueOf(mTailValue + i + 1)));
        }
    }

    private void refreshRecyclerView() {
        disposables.add(observableCreate().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                mActivityMainBinding.rv.refreshComplete();
                addHeader(adapter.getData());
                adapter.notifyItemRangeInserted(1, ADDNUM);
                mActivityMainBinding.rv.scrollToPosition(0);
            }
        }));
    }

    private void loadMoreRecyclerView() {
        disposables.add(observableCreate().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                int pos = adapter.getItemCount() + 1;
                mActivityMainBinding.rv.refreshComplete();
                addTail(adapter.getData());
                adapter.notifyItemRangeInserted(pos, ADDNUM);
                mActivityMainBinding.rv.scrollToPosition(adapter.getItemCount());
            }
        }));
    }

    private Observable<String> observableCreate() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Thread.sleep(2000);
                e.onNext("");
                e.onComplete();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
