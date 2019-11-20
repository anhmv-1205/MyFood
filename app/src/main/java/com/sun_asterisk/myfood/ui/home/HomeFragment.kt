package com.sun_asterisk.myfood.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.ui.category.CategoryFragment
import com.sun_asterisk.myfood.utils.extension.addChildFragment
import com.sun_asterisk.myfood.utils.extension.comebackHomeFragment
import kotlinx.android.synthetic.main.fragment_home.imageButtonFavorite
import kotlinx.android.synthetic.main.fragment_home.imageButtonHome
import kotlinx.android.synthetic.main.fragment_home.imageButtonPerson
import org.koin.android.ext.android.inject

class HomeFragment : BaseFragment(), View.OnClickListener {

    private val categoryFragment: CategoryFragment by inject()

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun setUpView() {
        imageButtonHome.setOnClickListener(this)
        imageButtonFavorite.setOnClickListener(this)
        imageButtonPerson.setOnClickListener(this)
        addChildFragment(
            R.id.frameLayoutHome,
            categoryFragment,
            false,
            CategoryFragment::class.java.simpleName,
            null
        )
    }

    override fun bindView() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageButtonHome -> {
                comebackHomeFragment()
            }
            R.id.imageButtonFavorite -> {
            }
            R.id.imageButtonPerson -> {
            }
        }
    }
}
