package com.sun_asterisk.myfood.ui.comment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.ui.main.OnActionBarListener
import com.sun_asterisk.myfood.utils.extension.showToast
import com.sun_asterisk.myfood.utils.livedata.autoCleared
import kotlinx.android.synthetic.main.fragment_comment.recyclerViewComments
import kotlinx.android.synthetic.main.fragment_comment.swipeComments
import kotlinx.android.synthetic.main.fragment_comment.toolbarComments
import kotlinx.android.synthetic.main.layout_toolbar.view.textViewToolbarTitle
import kotlinx.android.synthetic.main.layout_toolbar.view.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommentFragment : BaseFragment(), OnRefreshListener {

    private val viewModel: CommentViewModel by viewModel()
    private var commentAdapter: CommentAdapter by autoCleared()
    private var onActionBarListener: OnActionBarListener? = null
    private var farmerId: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnActionBarListener) onActionBarListener = context
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun setUpView() {
        onActionBarListener?.setupActionBar(toolbarComments.toolbar)
        swipeComments.setColorSchemeResources(R.color.colorGrey500)
        swipeComments.setOnRefreshListener(this)
        commentAdapter = CommentAdapter(context!!, arrayListOf())
        recyclerViewComments.apply {
            adapter = commentAdapter
        }
    }

    override fun bindView() {
        arguments?.let {
            it.getString(EXTRA_FARMER_ID)?.let { id ->
                viewModel.getComment(id)
                farmerId = id
            }
            it.getString(EXTRA_FARMER_NAME)?.let { name ->
                toolbarComments.toolbar.textViewToolbarTitle.text =
                    getString(R.string.text_title_toolbar_comment, name)
            }
        }
    }

    override fun registerLiveData() {
        viewModel.onCommentsEvent.observe(this, Observer {
            if (it.size == 0) context?.showToast(getString(R.string.text_no_data))
            commentAdapter.setData(it)
            swipeComments.isRefreshing = false
        })

        viewModel.onMessageErrorEvent.observe(this, Observer {
            context?.showToast(it)
        })
    }

    override fun onRefresh() {
        farmerId?.let { viewModel.getComment(it) }
    }

    companion object {
        private const val EXTRA_FARMER_ID = "EXTRA_FARMER_ID"
        private const val EXTRA_FARMER_NAME = "EXTRA_FARMER_NAME"

        fun newInstance(idFarmer: String, nameFarmer: String) = CommentFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_FARMER_ID, idFarmer)
                putString(EXTRA_FARMER_NAME, nameFarmer)
            }
        }
    }
}
