package com.sun_asterisk.myfood.utils.widget.dialogmanager

import android.content.Context
import java.lang.ref.WeakReference

class DialogManagerImpl(ctx: Context?) : DialogManager {

    private var context: WeakReference<Context?>? = null
    private var progressDialog: ProgressDialog? = null

    init {
        context = WeakReference(ctx).apply {
            progressDialog = ProgressDialog(this.get()!!)
        }
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun hideLoading() {
        progressDialog?.hide()
    }

    override fun setState(isVisible: Boolean) {
        if (isVisible) progressDialog?.show() else progressDialog?.hide()
    }

    override fun onRelease() {
        progressDialog = null
    }
}
