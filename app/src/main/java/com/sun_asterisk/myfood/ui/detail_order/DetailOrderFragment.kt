package com.sun_asterisk.myfood.ui.detail_order

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.RatingBar
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.data.model.Order
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.remote.request.CreateOrderRequest
import com.sun_asterisk.myfood.data.remote.request.RatingRequest
import com.sun_asterisk.myfood.ui.main.OnActionBarListener
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.annotation.OrderStatus
import com.sun_asterisk.myfood.utils.annotation.Role
import com.sun_asterisk.myfood.utils.extension.compareTimeWithCurrent
import com.sun_asterisk.myfood.utils.extension.createCalendarWithFormat
import com.sun_asterisk.myfood.utils.extension.delayTask
import com.sun_asterisk.myfood.utils.extension.fadeOutWithAnimation
import com.sun_asterisk.myfood.utils.extension.goBackFragment
import com.sun_asterisk.myfood.utils.extension.gone
import com.sun_asterisk.myfood.utils.extension.hide
import com.sun_asterisk.myfood.utils.extension.isMultiClick
import com.sun_asterisk.myfood.utils.extension.loadImageUrl
import com.sun_asterisk.myfood.utils.extension.notNull
import com.sun_asterisk.myfood.utils.extension.replaceIpAddress
import com.sun_asterisk.myfood.utils.extension.setIconByStatusStatus
import com.sun_asterisk.myfood.utils.extension.setState
import com.sun_asterisk.myfood.utils.extension.setStatusOfOrder
import com.sun_asterisk.myfood.utils.extension.show
import com.sun_asterisk.myfood.utils.extension.showDatePickerDialog
import com.sun_asterisk.myfood.utils.extension.showToast
import com.sun_asterisk.myfood.utils.extension.toDateWithFormat
import com.sun_asterisk.myfood.utils.extension.validateItemDuration
import com.sun_asterisk.myfood.utils.listener.OnDataCalendarListener
import kotlinx.android.synthetic.main.bottom_sheet_create_order.bottomSheetCreateOrder
import kotlinx.android.synthetic.main.bottom_sheet_create_order.buttonOrder
import kotlinx.android.synthetic.main.bottom_sheet_create_order.editTextNote
import kotlinx.android.synthetic.main.bottom_sheet_create_order.editTextTimeBuy
import kotlinx.android.synthetic.main.bottom_sheet_create_order.imageViewClose
import kotlinx.android.synthetic.main.bottom_sheet_create_order.radioButtonAM
import kotlinx.android.synthetic.main.bottom_sheet_create_order.radioGroupTime
import kotlinx.android.synthetic.main.bottom_sheet_create_order.textViewTime
import kotlinx.android.synthetic.main.bottom_sheet_create_order.view.itemFood
import kotlinx.android.synthetic.main.fragment_detail_order.buttonApprove
import kotlinx.android.synthetic.main.fragment_detail_order.buttonCancel
import kotlinx.android.synthetic.main.fragment_detail_order.buttonDone
import kotlinx.android.synthetic.main.fragment_detail_order.buttonReject
import kotlinx.android.synthetic.main.fragment_detail_order.buttonReorder
import kotlinx.android.synthetic.main.fragment_detail_order.cardViewInformationOrder
import kotlinx.android.synthetic.main.fragment_detail_order.imageViewIconStatus
import kotlinx.android.synthetic.main.fragment_detail_order.swipeDetailOrder
import kotlinx.android.synthetic.main.fragment_detail_order.textViewContentNote
import kotlinx.android.synthetic.main.fragment_detail_order.textViewInformationStatus
import kotlinx.android.synthetic.main.fragment_detail_order.textViewTimeCreatedOrder
import kotlinx.android.synthetic.main.fragment_detail_order.toolbarDetailOrder
import kotlinx.android.synthetic.main.fragment_detail_order.viewForBuyer
import kotlinx.android.synthetic.main.fragment_detail_order.viewForFarmer
import kotlinx.android.synthetic.main.fragment_detail_order.viewTransparent
import kotlinx.android.synthetic.main.item_food_vertical.view.imageViewFood
import kotlinx.android.synthetic.main.item_food_vertical.view.imageViewNew
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewAmountBuy
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewCost
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewFoodName
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewOutOfFood
import kotlinx.android.synthetic.main.item_order.textViewStatus
import kotlinx.android.synthetic.main.item_order.view.imageViewFoodOfOrder
import kotlinx.android.synthetic.main.item_order.view.textViewFoodNameOfOrder
import kotlinx.android.synthetic.main.item_order.view.textViewTimeBuy
import kotlinx.android.synthetic.main.layout_buyer_information.view.imageViewPhone
import kotlinx.android.synthetic.main.layout_buyer_information.view.textViewBuyerBirthday
import kotlinx.android.synthetic.main.layout_buyer_information.view.textViewBuyerName
import kotlinx.android.synthetic.main.layout_buyer_information.view.textViewDateCreateAccountBuyer
import kotlinx.android.synthetic.main.layout_farmer_information.textViewRate
import kotlinx.android.synthetic.main.layout_farmer_information.view.textViewDateCreateAccountFarmer
import kotlinx.android.synthetic.main.layout_farmer_information.view.textViewDirection
import kotlinx.android.synthetic.main.layout_farmer_information.view.textViewFarmerBirthday
import kotlinx.android.synthetic.main.layout_farmer_information.view.textViewFarmerName
import kotlinx.android.synthetic.main.layout_farmer_information.view.textViewRate
import kotlinx.android.synthetic.main.layout_toolbar.view.textViewToolbarTitle
import kotlinx.android.synthetic.main.layout_toolbar.view.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class DetailOrderFragment : BaseFragment(), OnClickListener, OnRefreshListener, OnDataCalendarListener {

    private var onActionBarListener: OnActionBarListener? = null
    private val viewModel: DetailOrderViewModel by viewModel()
    private var order: Order? = null
    private var owner: User? = null
    private var partner: User? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CardView>
    private lateinit var dateBuy: String
    private var shift = Constant.SHIFT_AM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnActionBarListener) onActionBarListener = context
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_detail_order, container, false)
    }

    override fun setUpView() {
        onActionBarListener.notNull { it.setupActionBar(toolbarDetailOrder.toolbar) }
        toolbarDetailOrder.toolbar.textViewToolbarTitle.text = getString(R.string.text_information_order)
        swipeDetailOrder.setColorSchemeResources(R.color.colorGrey500)
        swipeDetailOrder.setOnRefreshListener(this)
        order = arguments?.getParcelable(EXTRA_ORDER)
        buttonCancel.setOnClickListener(this)
        buttonApprove.setOnClickListener(this)
        buttonReject.setOnClickListener(this)
        buttonDone.setOnClickListener(this)
        buttonReorder.setOnClickListener(this)
        textViewRate.setOnClickListener(this)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetCreateOrder)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, state: Float) {
            }

            override fun onStateChanged(p0: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        viewTransparent.fadeOutWithAnimation()
                        swipeDetailOrder.isEnabled = true
                        clearFormBottomSheet()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        swipeDetailOrder.isEnabled = false
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
        viewModel.onOwnerEvent.observe(this, Observer {
            owner = it
            when (it.role) {
                Role.BUYER -> viewModel.getFarmerOrBuyer(order!!.sellerId)
                Role.FARMER -> viewModel.getFarmerOrBuyer(order!!.buyerId)
            }
            bindMutableDataOfOrder(order!!)
        })

        viewModel.onUserEvent.observe(this, Observer {
            owner.notNull { owner ->
                if (owner.role == Role.BUYER) {
                    setupViewForBuyer(it)
                } else {
                    setupViewForFarmer(it)
                }
                partner = it
            }
        })

        viewModel.onRefreshOrderEvent.observe(this, Observer {
            order = it
            bindMutableDataOfOrder(it)
            swipeDetailOrder.isRefreshing = false
        })

        viewModel.onCreateOrderEvent.observe(this, Observer {
            it.notNull {
                context?.showToast(getString(R.string.text_order_success))
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                delayTask({
                    goBackFragment()
                })
            }
        })

        viewModel.onCreateCommentEvent.observe(this, Observer {
            if (it) context?.showToast(getString(R.string.text_rating_success))
            dialogManager?.hideLoading()
        })

        viewModel.onProgressDialogEvent.observe(this, Observer {
            //            if (it) dialogManager?.showLoading()
//            else dialogManager?.hideLoading()
        })

        viewModel.onMessageErrorEvent.observe(this, Observer {
            swipeDetailOrder.isRefreshing = false
            context?.showToast(it)
            dialogManager?.hideLoading()
        })
    }

    override fun bindView() {
        order?.let { bindImmutableDataOfOrder(it) }
    }

    private fun setupViewForBuyer(farmer: User) {
        viewForBuyer.show()
        viewForBuyer.textViewDirection.setOnClickListener(this)
        viewForBuyer.textViewRate.setOnClickListener(this)
        viewForBuyer.textViewFarmerName.text = farmer.name
        viewForBuyer.textViewFarmerBirthday.text = farmer.birthday.toDateWithFormat(
            Constant.DATETIME_FORMAT_YYYY_MM_DD,
            getString(R.string.text_time_format)
        )
        viewForBuyer.textViewDateCreateAccountFarmer.text = getString(
            R.string.text_date_created_account,
            farmer.dateCreated.toDateWithFormat(
                Constant.DATETIME_FORMAT_FULL,
                getString(R.string.text_time_format_hh_mm_dd_mm_yyyy)
            )
        )
    }

    private fun setupViewForFarmer(buyer: User) {
        viewForFarmer.show()
        viewForFarmer.imageViewPhone.setOnClickListener(this)
        viewForFarmer.textViewBuyerName.text = buyer.name
        viewForFarmer.textViewBuyerBirthday.text = buyer.birthday.toDateWithFormat(
            Constant.DATETIME_FORMAT_YYYY_MM_DD,
            getString(R.string.text_time_format)
        )
        viewForFarmer.textViewDateCreateAccountBuyer.text = getString(
            R.string.text_date_created_account,
            buyer.dateCreated.toDateWithFormat(
                Constant.DATETIME_FORMAT_FULL,
                getString(R.string.text_time_format_hh_mm_dd_mm_yyyy)
            )
        )
    }

    private fun bindImmutableDataOfOrder(order: Order) {
        cardViewInformationOrder.textViewFoodNameOfOrder.text = order.food.name
        cardViewInformationOrder.textViewTimeBuy.text = getString(
            R.string.text_time_buy_and_shift, order.date_buy.toDateWithFormat(
                Constant.DATETIME_FORMAT_YYYY_MM_DD,
                getString(R.string.text_time_format)
            ), order.shift.toUpperCase(Locale.US)
        )
        cardViewInformationOrder.imageViewFoodOfOrder.loadImageUrl(order.food.imgUrl.replaceIpAddress())
        textViewContentNote.text = order.note
        textViewTimeCreatedOrder.text = getString(
            R.string.text_date_created_order,
            order.dateCreated.toDateWithFormat(
                Constant.DATETIME_FORMAT_FULL,
                getString(R.string.text_time_format_hh_mm_dd_mm_yyyy)
            )
        )
        textViewStatus.text = getString(
            R.string.text_display_food_cost,
            order.food.cost,
            if (order.food.unitCost!!.isNotEmpty()) order.food.unitCost else getString(R.string.text_vn_money),
            order.food.unitFood
        )
    }

    private fun bindMutableDataOfOrder(order: Order) {
        imageViewIconStatus.setIconByStatusStatus(order.status)
        textViewInformationStatus.setStatusOfOrder(order.status)
        setStatusDefaultOfOptions()
        owner.notNull {
            when (it.role) {
                Role.FARMER -> bindOptionsForFarmer(order)
                Role.BUYER -> bindOptionsForBuyer(order)
            }
        }
    }

    private fun setStatusDefaultOfOptions() {
        buttonCancel.gone()
        buttonApprove.gone()
        buttonReject.gone()
        buttonDone.gone()
        buttonReorder.gone()
        viewForBuyer.textViewRate.hide()
        viewForBuyer.textViewDirection.hide()
    }

    private fun bindOptionsForBuyer(order: Order) {
        when (order.status) {
            OrderStatus.REQUESTING -> buttonCancel.show()
            OrderStatus.APPROVED -> viewForBuyer.textViewDirection.show()
            OrderStatus.CANCELED, OrderStatus.REJECTED -> buttonReorder.show()
            OrderStatus.DONE -> {
                textViewRate.show()
                buttonReorder.show()
            }
        }
    }

    private fun bindOptionsForFarmer(order: Order) {
        when (order.status) {
            OrderStatus.REQUESTING -> {
                buttonReject.show()
                buttonApprove.show()
            }
            OrderStatus.APPROVED -> buttonDone.show()
            OrderStatus.CANCELED, OrderStatus.REJECTED, OrderStatus.DONE -> viewForFarmer.imageViewPhone.hide()
        }
    }

    override fun onClick(v: View?) {
        if (isMultiClick()) return
        when (v?.id) {
            R.id.textViewDirection -> {
                directionToFarmerLocation()
            }

            R.id.textViewRate -> {
                showAlertDialogRating()
            }
            R.id.imageViewPhone -> {
                partner?.let {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse(Constant.TELL + it.phone))
                    startActivity(intent)
                }
            }
            R.id.buttonCancel -> viewModel.updateOrderStatus(order!!.id, OrderStatus.CANCELED)

            R.id.buttonApprove -> viewModel.updateOrderStatus(order!!.id, OrderStatus.APPROVED)

            R.id.buttonReject -> viewModel.updateOrderStatus(order!!.id, OrderStatus.REJECTED)

            R.id.buttonDone -> viewModel.updateOrderStatus(order!!.id, OrderStatus.DONE)

            R.id.buttonReorder -> {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    viewTransparent.show()
                    order?.let {
                        setDataForBottomSheet(it.food)
                    }
                }
            }
            R.id.imageViewClose -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            R.id.buttonOrder -> {
                if (validateForm()) {
                    order?.let {
                        val createOrderRequest =
                            CreateOrderRequest(
                                it.food.farmerId,
                                it.food.id,
                                dateBuy,
                                shift,
                                editTextNote.text.toString().trim()
                            )
                        viewModel.createOrder(createOrderRequest)
                    }
                } else {
                    textViewTime.error = getString(R.string.text_not_empty)
                }
            }

            R.id.editTextTimeBuy -> {
                if (this::dateBuy.isInitialized && dateBuy.isNotEmpty())
                    context?.showDatePickerDialog(
                        dateBuy.createCalendarWithFormat(Constant.DATETIME_FORMAT_YYYY_MM_DD),
                        this
                    )
                else context?.showDatePickerDialog(listener = this)
            }
        }
    }

    private fun directionToFarmerLocation() {
        partner?.location?.let {
            val gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=${it[0]},${it[1]}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            activity?.let { fragmentActivity ->
                if (mapIntent.resolveActivity(fragmentActivity.packageManager) != null)
                    startActivity(mapIntent)
            }
        }
    }

    override fun onRefresh() {
        order?.let { viewModel.refreshOrder(it.id) }
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

    private fun validateForm(): Boolean {
        return this::dateBuy.isInitialized && dateBuy.isNotEmpty()
    }

    private fun showAlertDialogRating(

    ) {
        val builder = AlertDialog.Builder(context!!)
        val itemView = LayoutInflater.from(context!!).inflate(R.layout.layout_rating_user, null)
        val ratingBar: RatingBar = itemView.findViewById(R.id.rattingBarComment)
        val editText: EditText = itemView.findViewById(R.id.editTextComment)
        builder.setView(itemView)
        builder.setNegativeButton(getString(R.string.text_cancel)) { dialog, which ->
            dialog?.dismiss()
        }
        val shake = AnimationUtils.loadAnimation(context!!, R.anim.shake)
        builder.setPositiveButton(getString(R.string.text_send), null)
        val dialog = builder.setCancelable(true).create()
        dialog.setOnShowListener { dialogInterface ->
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                if (ratingBar.rating.toInt() == 0) {
                    ratingBar.startAnimation(shake)
                    return@setOnClickListener
                }
                if (editText.text.toString().trim().isEmpty()) {
                    editText.startAnimation(shake)
                    return@setOnClickListener
                }
                order?.let {
                    val ratingRequest = RatingRequest(
                        it.sellerId,
                        it.buyerId,
                        it.foodId,
                        ratingBar.rating.toInt(),
                        editText.text.toString().trim(),
                        it.id
                    )
                    viewModel.createRating(ratingRequest)
                    dialogManager?.showLoading()
                }
                dialogInterface.dismiss()
            }
        }

        dialog.show()
    }

    companion object {
        private const val EXTRA_ORDER = "EXTRA_ORDER"

        fun newInstance(order: Order) = DetailOrderFragment().apply {
            arguments = Bundle().apply { putParcelable(EXTRA_ORDER, order) }
        }
    }
}
