package com.sun_asterisk.myfood

import android.os.Bundle
import com.google.android.gms.maps.MapFragment
import com.sun_asterisk.myfood.base.BaseActivity
import com.sun_asterisk.myfood.ui.map.MapsFragment
import com.sun_asterisk.myfood.utils.extension.addFragmentToActivity

class MainActivity : BaseActivity() {

    override fun onCreateView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        init()
    }

    override fun setUpView() {
    }

    override fun bindView() {
    }

    private fun init() {
        addFragmentToActivity(
            R.id.containerMain,
            MapsFragment.newInstance(),
            false,
            MapFragment::class.java.simpleName
        )
    }
}
