package com.sun_asterisk.myfood.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.data.remote.request.SignInRequest
import com.sun_asterisk.myfood.ui.home.HomeFragment
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.isEmailValid
import com.sun_asterisk.myfood.utils.extension.replaceFragment
import com.sun_asterisk.myfood.utils.extension.showToast
import kotlinx.android.synthetic.main.fragment_login.buttonLogin
import kotlinx.android.synthetic.main.fragment_login.editTextEmail
import kotlinx.android.synthetic.main.fragment_login.editTextPassword
import kotlinx.android.synthetic.main.fragment_login.textViewRegister
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment(), OnClickListener {

    private val viewModel: LoginViewModel by viewModel()
    private val homeFragment: HomeFragment by inject()

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun setUpView() {
        buttonLogin.setOnClickListener(this)
        textViewRegister.setOnClickListener(this)
    }

    override fun registerLiveData() {
        viewModel.onLoginEvent.observe(this, Observer {
            if (it)
                replaceFragment(R.id.containerMain, homeFragment, false, HomeFragment::class.java.simpleName)
        })
        viewModel.onProgressDialogEvent.observe(this, Observer {
            if (it) dialogManager?.showLoading()
            else dialogManager?.hideLoading()
        })
        viewModel.onMessageError.observe(this, Observer {
            context?.showToast(it.message ?: getString(R.string.text_have_error))
        })
    }

    override fun bindView() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonLogin -> {
                val email = editTextEmail.text.toString().trim()
                val password = editTextPassword.text.toString().trim()
                if (validateLoginForm(email, password)) viewModel.login(SignInRequest(email, password))
            }
            R.id.textViewRegister -> {

            }
        }
    }

    private fun validateLoginForm(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            editTextEmail.error = getString(R.string.text_email_not_empty)
            editTextEmail.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            editTextPassword.error = getString(R.string.text_password_not_empty)
            editTextPassword.requestFocus()
            return false
        }
        if (!email.isEmailValid()) {
            editTextEmail.error = getString(R.string.text_email_wrong_format)
            editTextEmail.requestFocus()
            return false
        }
        if (password.length < Constant.PASSWORD_NUMBER_OF_CHARACTER) {
            editTextPassword.error = getString(R.string.text_password_rule)
            editTextPassword.requestFocus()
            return false
        }
        return true
    }
}
