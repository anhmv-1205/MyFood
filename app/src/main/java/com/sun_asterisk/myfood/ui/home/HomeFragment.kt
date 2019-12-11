package com.sun_asterisk.myfood.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.ui.category.CategoryFragment
import com.sun_asterisk.myfood.ui.farmer_food.FarmerFoodFragment
import com.sun_asterisk.myfood.ui.orders.OrdersFragment
import com.sun_asterisk.myfood.ui.profile.ProfileFragment
import com.sun_asterisk.myfood.utils.annotation.Role
import kotlinx.android.synthetic.main.fragment_home.imageButtonFavorite
import kotlinx.android.synthetic.main.fragment_home.imageButtonHome
import kotlinx.android.synthetic.main.fragment_home.imageButtonPerson
import kotlinx.android.synthetic.main.fragment_home.toolbarHome
import kotlinx.android.synthetic.main.fragment_home.viewPagerHome
import kotlinx.android.synthetic.main.layout_toolbar.view.textViewToolbarTitle
import kotlinx.android.synthetic.main.layout_toolbar.view.toolbar
import org.koin.android.ext.android.inject

class HomeFragment : BaseFragment(), View.OnClickListener {

    private val categoryFragment: CategoryFragment by inject()
    private val profileFragment: ProfileFragment by inject()
    private val ordersFragment: OrdersFragment by inject()
    private val farmerFoodFragment: FarmerFoodFragment by inject()
    private var titleHomeFragment: String = ""
    private lateinit var arrayFragment: ArrayList<Fragment>

    private val viewModel: HomeViewModel by inject()

    private lateinit var homePagerAdapter: HomePagerAdapter

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun setUpView() {
        toolbarHome.toolbar.navigationIcon = null
        imageButtonHome.setOnClickListener(this)
        imageButtonFavorite.setOnClickListener(this)
        imageButtonPerson.setOnClickListener(this)
        viewModel.getRoleOfUser()
    }

    override fun registerLiveData() {
        viewModel.onGetRoleOfUserEvent.observe(this, Observer {
            when (it) {
                Role.FARMER -> {
                    arrayFragment = arrayListOf(ordersFragment, farmerFoodFragment, profileFragment)
                    titleHomeFragment = getString(R.string.text_title_my_food)
                    toolbarHome.textViewToolbarTitle.text = titleHomeFragment
                }
                Role.BUYER -> {
                    arrayFragment = arrayListOf(ordersFragment, categoryFragment, profileFragment)
                    titleHomeFragment = getString(R.string.text_title_category)
                    toolbarHome.textViewToolbarTitle.text = titleHomeFragment
                }
            }
            homePagerAdapter =
                HomePagerAdapter(childFragmentManager, arrayFragment)
            viewPagerHome.adapter = homePagerAdapter
            viewPagerHome.currentItem = Type.DEPEND_ROLE.value
            viewPagerHome.offscreenPageLimit = PAGE_LIMIT
            viewPagerHome.setSwipePagingEnabled(false)
        })
    }

    override fun bindView() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageButtonHome -> {
                viewPagerHome.currentItem = Type.DEPEND_ROLE.value
                toolbarHome.toolbar.textViewToolbarTitle.text = titleHomeFragment
            }
            R.id.imageButtonFavorite -> {
                viewPagerHome.currentItem = Type.ORDER.value
                toolbarHome.toolbar.textViewToolbarTitle.text = getString(R.string.text_title_order)
            }
            R.id.imageButtonPerson -> {
                viewPagerHome.currentItem = Type.PROFILE.value
                toolbarHome.toolbar.textViewToolbarTitle.text = getString(R.string.text_title_profile)
            }
        }
    }

    enum class Type(val value: Int) {
        ORDER(0),
        DEPEND_ROLE(1),
        PROFILE(2)
    }

    companion object {
        const val PAGE_LIMIT = 5
    }
}
