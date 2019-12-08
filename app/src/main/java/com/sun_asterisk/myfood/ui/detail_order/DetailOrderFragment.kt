package com.sun_asterisk.myfood.ui.detail_order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.data.model.Order
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.ui.main.OnActionBarListener
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.annotation.OrderStatus
import com.sun_asterisk.myfood.utils.annotation.Role
import com.sun_asterisk.myfood.utils.extension.gone
import com.sun_asterisk.myfood.utils.extension.hide
import com.sun_asterisk.myfood.utils.extension.loadImageUrl
import com.sun_asterisk.myfood.utils.extension.notNull
import com.sun_asterisk.myfood.utils.extension.replaceIpAddress
import com.sun_asterisk.myfood.utils.extension.setIconByStatusStatus
import com.sun_asterisk.myfood.utils.extension.setStatusOfOrder
import com.sun_asterisk.myfood.utils.extension.show
import com.sun_asterisk.myfood.utils.extension.showToast
import com.sun_asterisk.myfood.utils.extension.toDateWithFormat
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
import kotlinx.android.synthetic.main.item_order.view.imageViewFood
import kotlinx.android.synthetic.main.item_order.view.textViewFoodName
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

class DetailOrderFragment : BaseFragment(), OnClickListener, OnRefreshListener {

    private var onActionBarListener: OnActionBarListener? = null
    private val viewModel: DetailOrderViewModel by viewModel()
    private var order: Order? = null
    private var owner: User? = null

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
                if (owner.role == Role.BUYER) setupViewForBuyer(it)
                else setupViewForFarmer(it)
            }
        })

        viewModel.onRefreshOrderEvent.observe(this, Observer {
            order = it
            bindMutableDataOfOrder(it)
            swipeDetailOrder.isRefreshing = false
        })

        viewModel.onProgressDialogEvent.observe(this, Observer {
            if (it) dialogManager?.showLoading()
            else dialogManager?.hideLoading()
        })

        viewModel.onMessageErrorEvent.observe(this, Observer {
            swipeDetailOrder.isRefreshing = false
            context?.showToast(it)
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
        cardViewInformationOrder.textViewFoodName.text = order.food.name
        cardViewInformationOrder.textViewTimeBuy.text = getString(
            R.string.text_time_buy_and_shift, order.date_buy.toDateWithFormat(
                Constant.DATETIME_FORMAT_YYYY_MM_DD,
                getString(R.string.text_time_format)
            ), order.shift.toUpperCase(Locale.US)
        )
        cardViewInformationOrder.imageViewFood.loadImageUrl(order.food.imgUrl.replaceIpAddress())
        textViewContentNote.text = order.note
        textViewTimeCreatedOrder.text = getString(
            R.string.text_date_created_order,
            order.dateCreated.toDateWithFormat(
                Constant.DATETIME_FORMAT_FULL,
                getString(R.string.text_time_format_hh_mm_dd_mm_yyyy)
            )
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
        when (v?.id) {
            R.id.textViewDirection -> {
            }
            R.id.textViewRate -> {
            }
            R.id.imageViewPhone -> {
            }
            R.id.buttonCancel -> viewModel.updateOrderStatus(order!!.id, OrderStatus.CANCELED)

            R.id.buttonApprove -> viewModel.updateOrderStatus(order!!.id, OrderStatus.APPROVED)

            R.id.buttonReject -> viewModel.updateOrderStatus(order!!.id, OrderStatus.REJECTED)

            R.id.buttonDone -> viewModel.updateOrderStatus(order!!.id, OrderStatus.DONE)

            R.id.buttonReorder -> {
            }
        }
    }

    override fun onRefresh() {
        order?.let { viewModel.refreshOrder(it.id) }
    }

    companion object {
        private const val EXTRA_ORDER = "EXTRA_ORDER"

        fun newInstance(order: Order) = DetailOrderFragment().apply {
            arguments = Bundle().apply { putParcelable(EXTRA_ORDER, order) }
        }
    }
}
