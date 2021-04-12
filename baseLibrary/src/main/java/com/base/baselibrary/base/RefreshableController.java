package com.base.baselibrary.base;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.base.baselibrary.utils.Action;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

/**
 * Create by Darren
 * On 2020/10/10 12:23
 * 下拉刷新/上拉加载 控制器
 **/
public class RefreshableController<D, A extends BaseQuickAdapter<D, BaseViewHolder>> implements OnRefreshLoadMoreListener {
    private SmartRefreshLayout mRefreshLayout;
    private A mAdapter;
    //数据获取
    private Consumer<Integer> mRequestData;
    //仅实现刷新的数据获取
    private Action mOnlyRefresh;
    private int page = 1;

    public RefreshableController(SmartRefreshLayout refreshLayout, A adapter) {
        mRefreshLayout = refreshLayout;
        mAdapter = adapter;

        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    public void setRequestData(Consumer<Integer> requestData) {
        mRequestData = requestData;
    }

    public void setOnlyRefresh(Action onlyRefresh) {
        mOnlyRefresh = onlyRefresh;
    }

    public void refresh() {
        onRefresh(mRefreshLayout);
    }

    public void loadMore() {
        onLoadMore(mRefreshLayout);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        if (mRequestData != null) {
            mRequestData.accept(page);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        if (mRequestData != null) {
            mRequestData.accept(page);
        }
        if (mOnlyRefresh != null) {
            mOnlyRefresh.run();
        }
    }

    /**
     * 部署数据
     */
    public void handleData(List<D> data) {
        if (page <= 1) {//refresh
            if (data == null || data.isEmpty()) {
                refreshFailed();
            } else {
                refreshSuccess(data);
            }
        } else {//load more
            if (data == null || data.isEmpty()) {
                loadMoreFailed();
            } else {
                loadMoreSuccess(data);
            }
        }
    }

    private void refreshSuccess(List<D> data) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.resetNoMoreData();
        mAdapter.setNewInstance(data);
    }

    private void refreshFailed() {
        mRefreshLayout.finishRefreshWithNoMoreData();
        mAdapter.setNewInstance(null);
    }

    private void loadMoreSuccess(List<D> data) {
        mRefreshLayout.finishLoadMore();
        mAdapter.addData(data);
    }

    private void loadMoreFailed() {
        mRefreshLayout.finishLoadMoreWithNoMoreData();
    }


}
