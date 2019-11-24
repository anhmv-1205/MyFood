package com.sun_asterisk.myfood.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun_asterisk.myfood.utils.extension.notNull
import com.sun_asterisk.myfood.utils.extension.showToast
import com.sun_asterisk.myfood.utils.widget.dialogmanager.DialogManager
import com.sun_asterisk.myfood.utils.widget.dialogmanager.DialogManagerImpl

abstract class BaseActivity : AppCompatActivity() {

    var dialogManager: DialogManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogManager = DialogManagerImpl(this)
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

    override fun onDestroy() {
        super.onDestroy()
        dialogManager?.onRelease()
        dialogManager = null
    }
}
