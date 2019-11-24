package com.sun_asterisk.myfood.ui.foods

import android.content.Context
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
import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.ui.main.OnActionBarListener
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.notNull
import com.sun_asterisk.myfood.utils.extension.onScrollListener
import com.sun_asterisk.myfood.utils.extension.showToast
import com.sun_asterisk.myfood.utils.livedata.autoCleared
import kotlinx.android.synthetic.main.fragment_foods.recyclerViewFoods
import kotlinx.android.synthetic.main.fragment_foods.swipeFoods
import kotlinx.android.synthetic.main.fragment_foods.toolbarFoods
import kotlinx.android.synthetic.main.layout_toolbar.view.textViewToolbarTitle
import kotlinx.android.synthetic.main.layout_toolbar.view.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoodsFragment : BaseFragment(), OnRefreshListener, OnItemClickListener<Food> {

    private var onActionBarListener: OnActionBarListener? = null
    private var foodAdapter by autoCleared<FoodAdapter>()
    private lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener
    private val viewModel: FoodsViewModel by viewModel()
    private var isRefresh = true
    private var isLoadMore = false
    private var currentPage = Constant.DEFAULT_VALUE
    private lateinit var farmer: User

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnActionBarListener) onActionBarListener = context
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_foods, container, false)
    }

    override fun setUpView() {
        onActionBarListener?.notNull { it.setupActionBar(toolbarFoods.toolbar) }
        arguments?.getParcelable<User>(EXTRA_USER)?.let { user ->
            farmer = user
            toolbarFoods.toolbar.textViewToolbarTitle.text = user.name
        }
        foodAdapter = FoodAdapter(context!!, mutableListOf())
        swipeFoods.setColorSchemeResources(R.color.colorGrey500)
        swipeFoods.setOnRefreshListener(this)
        recyclerViewFoods.apply {
            adapter = foodAdapter
            endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    loadMore(currentPage)
                }
            }
            addOnScrollListener(endlessRecyclerOnScrollListener)
            onScrollListener(layoutManager, swipeFoods)
        }
        foodAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkLoadType()
            }
        })
    }

    override fun bindView() {
        registerLiveData()
        viewModel.getFoodsWithUserId(farmer.id)
    }

    private fun registerLiveData() {
        viewModel.onGetFoodEvent.observe(this, Observer {
            if (isRefresh) {
                endlessRecyclerOnScrollListener.reset()
                foodAdapter.setFoods(it.foods)
                // show not item available if it's size = 0
            } else {
                if (it.foods.isNotEmpty()) foodAdapter.setFoods(getFoodsNotEmpty().union(it.foods).toMutableList())
                else foodAdapter.removeLoadingItem()
            }
        })
        viewModel.onProgressEvent.observe(this, Observer {
            if (it && !swipeFoods.isRefreshing) dialogManager?.showLoading()
            else dialogManager?.hideLoading()
        })
        viewModel.onMessageError.observe(this, Observer {
            context?.showToast(it)
        })
    }

    private fun getFoodsNotEmpty(): MutableList<Food> {
        return foodAdapter.getData().filter { it.id.isNotEmpty() }.toMutableList()
    }

    /**
     * This func to check load more or refresh
     */
    private fun checkLoadType() {
        if (isRefresh) {
            recyclerViewFoods.smoothScrollToPosition(Constant.DEFAULT_VALUE)
            isRefresh = false
        }
        if (isLoadMore) {
            viewModel.getFoodsWithUserId(farmer.id, currentPage)
            isLoadMore = false
        }
    }

    private fun loadMore(currentPage: Int) {
        isLoadMore = true
        this.currentPage = currentPage
        foodAdapter.setFoods(foodAdapter.getData().union(mutableListOf(Food())).toMutableList())
    }

    override fun onRefresh() {
        isRefresh = true
        viewModel.getFoodsWithUserId(farmer.id)
        swipeFoods.isRefreshing = false
    }

    override fun onItemViewClick(item: Food, position: Int) {
        context?.showToast(item.name)
    }

    override fun onDetach() {
        super.onDetach()
        onActionBarListener = null
    }

    companion object {
        private const val EXTRA_USER = "EXTRA_USER"

        fun newInstance(user: User) = FoodsFragment().apply {
            arguments = Bundle().apply { putParcelable(EXTRA_USER, user) }
        }
    }
}
