package com.sun_asterisk.myfood.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.ui.main.OnCallBackLogoutListener
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.annotation.Role
import com.sun_asterisk.myfood.utils.extension.isMultiClick
import com.sun_asterisk.myfood.utils.extension.showAlertDialogBasic
import com.sun_asterisk.myfood.utils.extension.toDateWithFormat
import kotlinx.android.synthetic.main.fragment_profile.buttonLogout
import kotlinx.android.synthetic.main.fragment_profile.editTextBirthDay
import kotlinx.android.synthetic.main.fragment_profile.editTextDateCreated
import kotlinx.android.synthetic.main.fragment_profile.editTextPhoneNumber
import kotlinx.android.synthetic.main.fragment_profile.textViewEmail
import kotlinx.android.synthetic.main.fragment_profile.textViewName
import kotlinx.android.synthetic.main.fragment_profile.textViewRole
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment(), OnClickListener {

    private val viewModel: ProfileViewModel by viewModel()
    private var onCallBackLogoutListener: OnCallBackLogoutListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCallBackLogoutListener) onCallBackLogoutListener = context
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun setUpView() {
        buttonLogout.setOnClickListener(this)
    }

    override fun registerLiveData() {
        viewModel.onUserEvent.observe(this, Observer {
            it?.let {
                bindImmutableDataUser(it)
                bindMutableDataUser(it)
            }
        })
    }

    override fun bindView() {
    }

    private fun bindImmutableDataUser(user: User) {
        textViewName.text = user.name
        textViewEmail.text = user.email
        textViewRole.text = when (user.role) {
            Role.BUYER -> getString(R.string.text_display_buyer)
            Role.FARMER -> getString(R.string.text_display_farmer)
            else -> ""
        }
    }

    private fun bindMutableDataUser(user: User) {
        editTextPhoneNumber.setText(user.phone)
        editTextBirthDay.setText(
            user.birthday.toDateWithFormat(
                Constant.DATETIME_FORMAT_YYYY_MM_DD,
                getString(R.string.text_time_format)
            )
        )
        editTextDateCreated.setText(
            user.dateCreated.toDateWithFormat(
                Constant.DATETIME_FORMAT_FULL, getString(R.string.text_time_format_hh_mm_dd_mm_yyyy)
            )
        )
    }

    override fun onClick(v: View?) {
        if (isMultiClick()) return
        when (v?.id) {
            R.id.buttonLogout -> {
                context?.showAlertDialogBasic(getString(R.string.text_confirm_logout)) {
                    viewModel.logout()
                    onCallBackLogoutListener?.onLogoutReceived()
                }
            }
        }
    }
}
