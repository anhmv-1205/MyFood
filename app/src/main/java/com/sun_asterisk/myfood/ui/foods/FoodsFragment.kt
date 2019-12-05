package com.sun_asterisk.myfood.ui.foods

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.base.recyclerview.EndlessRecyclerOnScrollListener
import com.sun_asterisk.myfood.base.recyclerview.OnItemClickListener
import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.remote.request.CreateOrderRequest
import com.sun_asterisk.myfood.ui.main.OnActionBarListener
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.compareTimeWithCurrent
import com.sun_asterisk.myfood.utils.extension.createCalendarWithFormat
import com.sun_asterisk.myfood.utils.extension.delayTask
import com.sun_asterisk.myfood.utils.extension.fadeOutWithAnimation
import com.sun_asterisk.myfood.utils.extension.loadImageUrl
import com.sun_asterisk.myfood.utils.extension.notNull
import com.sun_asterisk.myfood.utils.extension.onScrollListener
import com.sun_asterisk.myfood.utils.extension.replaceIpAddress
import com.sun_asterisk.myfood.utils.extension.setState
import com.sun_asterisk.myfood.utils.extension.show
import com.sun_asterisk.myfood.utils.extension.showDatePickerDialog
import com.sun_asterisk.myfood.utils.extension.showToast
import com.sun_asterisk.myfood.utils.extension.toDateWithFormat
import com.sun_asterisk.myfood.utils.extension.validateItemDuration
import com.sun_asterisk.myfood.utils.listener.OnDataCalendarListener
import com.sun_asterisk.myfood.utils.livedata.autoCleared
import kotlinx.android.synthetic.main.bottom_sheet_create_order.bottomSheetCreateOrder
import kotlinx.android.synthetic.main.bottom_sheet_create_order.buttonOrder
import kotlinx.android.synthetic.main.bottom_sheet_create_order.editTextNote
import kotlinx.android.synthetic.main.bottom_sheet_create_order.editTextTimeBuy
import kotlinx.android.synthetic.main.bottom_sheet_create_order.imageViewClose
import kotlinx.android.synthetic.main.bottom_sheet_create_order.radioButtonAM
import kotlinx.android.synthetic.main.bottom_sheet_create_order.radioGroupTime
import kotlinx.android.synthetic.main.bottom_sheet_create_order.textViewTime
import kotlinx.android.synthetic.main.bottom_sheet_create_order.view.itemFood
import kotlinx.android.synthetic.main.fragment_foods.recyclerViewFoods
import kotlinx.android.synthetic.main.fragment_foods.swipeFoods
import kotlinx.android.synthetic.main.fragment_foods.toolbarFoods
import kotlinx.android.synthetic.main.fragment_foods.viewTransparent
import kotlinx.android.synthetic.main.item_food_vertical.view.imageViewFood
import kotlinx.android.synthetic.main.item_food_vertical.view.imageViewNew
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewAmountBuy
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewCost
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewFoodName
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewOutOfFood
import kotlinx.android.synthetic.main.layout_toolbar.view.textViewToolbarTitle
import kotlinx.android.synthetic.main.layout_toolbar.view.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoodsFragment : BaseFragment(), OnRefreshListener, OnItemClickListener<Food>, OnClickListener,
    OnDataCalendarListener {

    private var onActionBarListener: OnActionBarListener? = null
    private var foodAdapter by autoCleared<FoodAdapter>()
    private lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener
    private val viewModel: FoodsViewModel by viewModel()
    private var isRefresh = true
    private var isLoadMore = false
    private var currentPage = Constant.DEFAULT_VALUE
    private lateinit var farmer: User
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CardView>
    private var foodSelected: Food? = null
    private lateinit var dateBuy: String
    private var shift = Constant.SHIFT_AM

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
        foodAdapter.registerItemClickListener(this)
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
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetCreateOrder)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, state: Float) {
            }

            override fun onStateChanged(p0: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        viewTransparent.fadeOutWithAnimation()
                        swipeFoods.isEnabled = true
                        clearFormBottomSheet()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        swipeFoods.isEnabled = false
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                }
            }
        })
        imageViewClose.setOnClickListener(this)
        editTextTimeBuy.setOnClickListener(this)
        buttonOrder.setOnClickListener(this)
        radioGroupTime.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonAM -> shift = Constant.SHIFT_AM
                R.id.radioButtonPM -> shift = Constant.SHIFT_PM
            }
        }
    }

    override fun registerLiveData() {
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
        viewModel.onCreateOrderEvent.observe(this, Observer {
            if (it) context?.showToast(getString(R.string.text_order_success))
            delayTask({ bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED })

        })
        viewModel.onProgressEvent.observe(this, Observer {
            if (it && !swipeFoods.isRefreshing) dialogManager?.showLoading()
            else dialogManager?.hideLoading()
        })
        viewModel.onMessageError.observe(this, Observer {
            when (it) {
                "ERROR_DUPLICATE" -> {
                    context?.showToast(getString(R.string.text_order_duplicate))
                }
                else -> context?.showToast(it)
            }
        })
    }

    override fun bindView() {
        viewModel.getFoodsWithUserId(farmer.id)
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageViewClose -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            R.id.editTextTimeBuy -> {
                if (this::dateBuy.isInitialized && dateBuy.isNotEmpty())
                    context?.showDatePickerDialog(
                        dateBuy.createCalendarWithFormat(Constant.DATETIME_FORMAT_YYYY_MM_DD),
                        this
                    )
                else context?.showDatePickerDialog(listener = this)
            }
            R.id.buttonOrder -> {
                if (validateForm()) {
                    foodSelected?.let {
                        val createOrderRequest =
                            CreateOrderRequest(it.farmerId, it.id, dateBuy, shift, editTextNote.text.toString())
                        viewModel.createOrder(createOrderRequest)
                    }
                } else {
                    textViewTime.error = getString(R.string.text_not_empty)
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        return this::dateBuy.isInitialized && dateBuy.isNotEmpty()
    }

    override fun onItemViewClick(item: Food, position: Int) {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            viewTransparent.show()
            foodSelected = item
            setDataForBottomSheet(item)
        }
    }

    private fun clearFormBottomSheet() {
        dateBuy = ""
        radioButtonAM.isChecked = true
        editTextTimeBuy.text?.clear()
        editTextNote.text?.clear()
    }

    private fun setDataForBottomSheet(item: Food) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetCreateOrder.itemFood.textViewFoodName.text = item.name
        bottomSheetCreateOrder.itemFood.textViewCost.text = context!!.getString(
            R.string.text_display_food_cost,
            item.cost,
            item.unitCost ?: "",
            item.unitFood
        )
        bottomSheetCreateOrder.itemFood.textViewAmountBuy.text =
            context!!.getString(R.string.text_display_amount_buy, item.amountBuy)
        bottomSheetCreateOrder.itemFood.textViewOutOfFood.setState(!item.state)
        bottomSheetCreateOrder.itemFood.imageViewNew.setState(item.dateCreated.validateItemDuration(Constant.RULE_NEW_FOOD_FOLLOW_DAY))
        bottomSheetCreateOrder.itemFood.imageViewFood.loadImageUrl(item.imgUrl.replaceIpAddress())
    }

    override fun onDataSet(dateStr: String) {
        textViewTime.error = null
        if (dateStr.compareTimeWithCurrent(shift)) {
            editTextTimeBuy.setText(
                dateStr.toDateWithFormat(
                    Constant.DATETIME_FORMAT_YYYY_MM_DD,
                    getString(R.string.text_date_format)
                )
            )
            dateBuy = dateStr
        } else {
            textViewTime.error = getString(R.string.text_time_buy_error)
            delayTask({ textViewTime.error = null }, Constant.MAX_TIME_DOUBLE_CLICK_EXIT)
        }
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
