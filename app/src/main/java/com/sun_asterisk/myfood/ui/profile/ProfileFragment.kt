package com.sun_asterisk.myfood.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment

class ProfileFragment : BaseFragment() {

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun setUpView() {}

    override fun registerLiveData() {
    }

    override fun bindView() {
    }
}
