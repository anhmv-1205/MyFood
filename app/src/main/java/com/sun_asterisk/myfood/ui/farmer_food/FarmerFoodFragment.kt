package com.sun_asterisk.myfood.ui.farmer_food

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.base.recyclerview.EndlessRecyclerOnScrollListener
import com.sun_asterisk.myfood.base.recyclerview.OnItemClickListener
import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.data.remote.request.UpdateFoodRequest
import com.sun_asterisk.myfood.ui.create_food.CreateFoodFragment
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.addFragmentToActivity
import com.sun_asterisk.myfood.utils.extension.isMultiClick
import com.sun_asterisk.myfood.utils.extension.notNull
import com.sun_asterisk.myfood.utils.extension.onScrollListener
import com.sun_asterisk.myfood.utils.extension.showToast
import com.sun_asterisk.myfood.utils.livedata.autoCleared
import kotlinx.android.synthetic.main.fragment_farmer_food.floatButtonAddFood
import kotlinx.android.synthetic.main.fragment_farmer_food.recyclerViewFarmerFood
import kotlinx.android.synthetic.main.fragment_farmer_food.swipeFarmerFood
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FarmerFoodFragment : BaseFragment(), OnRefreshListener, OnClickListener, OnItemClickListener<Food> {

    private var farmerFoodAdapter: FarmerFoodAdapter by autoCleared()
    private lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener
    private var isRefresh = true
    private var isLoadMore = false
    private var currentPage = Constant.DEFAULT_PAGE
    private var categories: MutableList<Category>? = null
    private var positionFoodDeleted = Constant.INVALID_VALUE
    private var positionFoodUpdated = Constant.INVALID_VALUE

    private val viewModel: FarmerFoodViewModel by viewModel()

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_farmer_food, container, false)
    }

    override fun setUpView() {
        farmerFoodAdapter = FarmerFoodAdapter(context!!, mutableListOf())
        farmerFoodAdapter.registerItemClickListener(this)
        swipeFarmerFood.setColorSchemeResources(R.color.colorGrey500)
        swipeFarmerFood.setOnRefreshListener(this)
        floatButtonAddFood.setOnClickListener(this)
        recyclerViewFarmerFood.apply {
            adapter = farmerFoodAdapter
            endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    loadMore(currentPage)
                }
            }
            addOnScrollListener(endlessRecyclerOnScrollListener)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 || dy < 0 && floatButtonAddFood.isShown) floatButtonAddFood.hide()
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) floatButtonAddFood.show()
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
            onScrollListener(layoutManager, swipeFarmerFood)
        }
        farmerFoodAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkLoadType()
            }
        })
    }

    override fun bindView() {
        viewModel.getFoodsOfUser()
        viewModel.getCategories()
    }

    private fun getFoodsNotEmpty(): MutableList<Food> {
        return farmerFoodAdapter.getData().filter { it.id.isNotEmpty() }.toMutableList()
    }

    override fun registerLiveData() {
        viewModel.onGetFoodsOfFarmerEvent.observe(this, Observer {
            if (isRefresh) {
                if (it.size == 0) {
                    context?.showToast(getString(R.string.text_no_data))
                } else {
                    endlessRecyclerOnScrollListener.reset()
                    farmerFoodAdapter.setFoods(it)
                }
            } else {
                if (it.size != Constant.DEFAULT_VALUE) {
                    farmerFoodAdapter.setFoods(getFoodsNotEmpty().union(it).toMutableList())
                } else {
                    farmerFoodAdapter.removeLoadingItem()
                }
            }
        })

        viewModel.onCategoryEvent.observe(this, Observer {
            categories = it
        })

        viewModel.onDeleteFoodEvent.observe(this, Observer {
            farmerFoodAdapter.removeItem(positionFoodDeleted)
        })

        viewModel.onUpdateStateOfFoodEvent.observe(this, Observer {
            it.data.notNull { foodUpdated ->
                farmerFoodAdapter.replaceItem(foodUpdated, positionFoodUpdated)
            }

        })

        viewModel.onMessageErrorEvent.observe(this, Observer {
            if (it.isNotEmpty()) context?.showToast(it)
        })
    }

    override fun onRefresh() {
        isRefresh = true
        farmerFoodAdapter.setLastPositionItem()
        viewModel.getFoodsOfUser()
        swipeFarmerFood.isRefreshing = false
    }

    private fun loadMore(page: Int) {
        isLoadMore = true
        currentPage = page
        farmerFoodAdapter.addLoadingItem()
    }

    override fun onClick(v: View?) {
        if (isMultiClick()) return
        when (v?.id) {
            R.id.floatButtonAddFood -> {
                categories?.let {
                    val createFoodFragment: CreateFoodFragment by inject { parametersOf(it) }
                    addFragmentToActivity(
                        R.id.containerMain,
                        createFoodFragment,
                        true,
                        CreateFoodFragment::class.java.simpleName
                    )
                }
            }
        }
    }

    override fun onItemViewClick(item: Food, position: Int) {
        showImagePickerFoodOptions(item, position)
    }

    private fun checkLoadType() {
        if (isRefresh) {
            recyclerViewFarmerFood.smoothScrollToPosition(Constant.DEFAULT_VALUE)
            isRefresh = false
        }
        if (isLoadMore) {
            viewModel.getFoodsOfUser(currentPage)
            isLoadMore = false
        }
    }

    private fun showImagePickerFoodOptions(food: Food, position: Int) {
        AlertDialog.Builder(context).apply {
            setTitle(food.name)
            val options = arrayOf(
                if (food.state) getString(R.string.text_no_food) else getString(R.string.text_have_food),
                getString(R.string.text_edit),
                getString(R.string.text_delete)
            )
            setItems(options) { _, which ->
                when (which) {
                    FoodOption.UPDATE_STATUS.value -> {
                        viewModel.updateStateOfFood(food.id, UpdateFoodRequest(state = !food.state))
                        positionFoodUpdated = position
                    }
                    FoodOption.EDIT.value -> {
                        context?.showToast("edit")
                    }
                    FoodOption.DELETE.value -> {
                        viewModel.deleteUserById(food.id)
                        positionFoodDeleted = position
                    }
                    else -> {
                    }
                }
            }
        }.create().show()
    }

    enum class FoodOption(val value: Int) {
        UPDATE_STATUS(0), EDIT(1), DELETE(2)
    }
}
