package com.base.baselibrary.basekt

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * Create by Darren
 * On 2020/10/10 12:23
 * 下拉刷新/上拉加载 控制器
 */
class RefreshableController<D, A : BaseQuickAdapter<D, BaseViewHolder>>(
    private val refreshLayout: SmartRefreshLayout, private val adapter: A
) : OnRefreshLoadMoreListener {
    //数据获取
    var requestData: (page : Int) -> Unit = {}

    //仅实现刷新的数据获取
    private var onlyRefresh: () -> Unit = {}
    private var page = 1

    fun refresh() {
        onRefresh(refreshLayout)
    }

    fun loadMore() {
        onLoadMore(refreshLayout)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        requestData(page)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        requestData(page)
        onlyRefresh()
    }

    /**
     * 部署数据
     */
    fun handleData(data: List<D>?) {
        if (page <= 1) { //refresh
            if (data == null || data.isEmpty()) {
                refreshFailed()
            } else {
                refreshSuccess(data)
            }
        } else { //load more
            if (data == null || data.isEmpty()) {
                loadMoreFailed()
            } else {
                loadMoreSuccess(data)
            }
        }
    }

    private fun refreshSuccess(data: List<D>) {
        refreshLayout.finishRefresh()
        refreshLayout.resetNoMoreData()
        adapter.setNewInstance(data.toMutableList())
    }

    private fun refreshFailed() {
        refreshLayout.finishRefreshWithNoMoreData()
        adapter.setNewInstance(null)
    }

    private fun loadMoreSuccess(data: List<D>) {
        refreshLayout.finishLoadMore()
        adapter.addData(data)
    }

    private fun loadMoreFailed() {
        refreshLayout.finishLoadMoreWithNoMoreData()
    }

    init {
        refreshLayout.setOnRefreshLoadMoreListener(this)
    }
}