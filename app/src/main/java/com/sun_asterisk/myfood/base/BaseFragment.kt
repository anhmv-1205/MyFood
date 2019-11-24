package com.sun_asterisk.myfood.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.sun_asterisk.myfood.utils.widget.dialogmanager.DialogManager
import com.sun_asterisk.myfood.utils.widget.dialogmanager.DialogManagerImpl

abstract class BaseFragment : Fragment() {

    var dialogManager: DialogManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogManager = if (context is BaseActivity) {
            (activity as BaseActivity).dialogManager
        } else {
            DialogManagerImpl(context)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        bindView()
    }

    override fun onStop() {
        super.onStop()
        onHideSoftKeyBoard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialogManager?.hideLoading()
    }

    fun onHideSoftKeyBoard() {
        val inputMng: InputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMng.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    protected abstract fun createView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View

    protected abstract fun setUpView()

    protected abstract fun bindView()
}
