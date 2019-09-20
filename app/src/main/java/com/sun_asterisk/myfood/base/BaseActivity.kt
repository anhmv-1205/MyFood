package com.sun_asterisk.myfood.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun_asterisk.myfood.utils.extension.notNull
import com.sun_asterisk.myfood.utils.extension.showToast

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateView(savedInstanceState)
        setUpView()
        bindView()
    }

    fun onDatabaseError(throwable: Throwable?) {
        throwable?.localizedMessage.notNull { showToast(it) }
    }

    protected abstract fun onCreateView(savedInstanceState: Bundle?)

    protected abstract fun setUpView()

    protected abstract fun bindView()
}
