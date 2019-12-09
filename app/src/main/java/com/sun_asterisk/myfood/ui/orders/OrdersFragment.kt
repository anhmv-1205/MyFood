package com.sun_asterisk.myfood.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.base.recyclerview.EndlessRecyclerOnScrollListener
import com.sun_asterisk.myfood.base.recyclerview.OnItemClickListener
import com.sun_asterisk.myfood.data.model.Order
import com.sun_asterisk.myfood.ui.detail_order.DetailOrderFragment
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.addFragmentToActivity
import com.sun_asterisk.myfood.utils.extension.onScrollListener
import com.sun_asterisk.myfood.utils.extension.showToast
import com.sun_asterisk.myfood.utils.livedata.autoCleared
import kotlinx.android.synthetic.main.fragment_orders.recyclerViewOrders
import kotlinx.android.synthetic.main.fragment_orders.swipeOrders
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class OrdersFragment : BaseFragment(), OnItemClickListener<Order>, OnRefreshListener {

    private var ordersAdapter: OrdersAdapter by autoCleared()
    private val viewModel: OrdersViewModel by viewModel()
    private lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener
    private var isRefresh = true
    private var isLoadMore = false
    private var currentPage = Constant.DEFAULT_PAGE

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun setUpView() {
        ordersAdapter = OrdersAdapter(context!!, mutableListOf())
        ordersAdapter.registerItemClickListener(this)
        swipeOrders.setColorSchemeResources(R.color.colorGrey500)
        swipeOrders.setOnRefreshListener(this)
        recyclerViewOrders.apply {
            adapter = ordersAdapter
            endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    loadMore(currentPage)
                }
            }
            addOnScrollListener(endlessRecyclerOnScrollListener)
            onScrollListener(layoutManager, swipeOrders)
        }
        ordersAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkLoadType()
            }
        })
    }

    override fun bindView() {
        viewModel.getOrdersOfUser()
    }

    override fun registerLiveData() {
        viewModel.onGetOrdersEvent.observe(this, Observer {
            if (isRefresh) {
                endlessRecyclerOnScrollListener.reset()
                ordersAdapter.setOrders(it)
            } else {
                if (it.size != Constant.DEFAULT_VALUE) ordersAdapter.setOrders(getOrdersNotEmpty().union(it).toMutableList())
                else ordersAdapter.removeLoadingItem()
            }
        })
        viewModel.onProgressDialogEvent.observe(this, Observer {
            if (it) dialogManager?.showLoading()
            else dialogManager?.hideLoading()
        })
        viewModel.onMessageErrorEvent.observe(this, Observer {
            context?.showToast(it)
        })
    }

    private fun getOrdersNotEmpty(): MutableList<Order> {
        return ordersAdapter.getData().filter { it.id.isNotEmpty() }.toMutableList()
    }

    override fun onItemViewClick(item: Order, position: Int) {
        val detailOrderFragment: DetailOrderFragment by inject { parametersOf(item) }
        addFragmentToActivity(
            R.id.containerMain,
            detailOrderFragment,
            true,
            DetailOrderFragment::class.java.simpleName
        )
    }

    override fun onRefresh() {
        isRefresh = true
        ordersAdapter.setLastPositionItem()
        viewModel.getOrdersOfUser()
        swipeOrders.isRefreshing = false
    }

    private fun loadMore(currentPage: Int) {
        isLoadMore = true
        this.currentPage = currentPage
        ordersAdapter.setOrders(ordersAdapter.getData().union(mutableListOf(Order())).toMutableList())
    }

    private fun checkLoadType() {
        if (isRefresh) {
            recyclerViewOrders.smoothScrollToPosition(Constant.DEFAULT_VALUE)
            isRefresh = false
        }
        if (isLoadMore) {
            viewModel.getOrdersOfUser(currentPage)
            isLoadMore = false
        }
    }
}
