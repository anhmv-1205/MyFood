package com.sun_asterisk.myfood.ui.category

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.base.recyclerview.OnItemClickListener
import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.ui.main.OnActionBarListener
import com.sun_asterisk.myfood.ui.map.MapsFragment
import com.sun_asterisk.myfood.utils.extension.addChildFragment
import com.sun_asterisk.myfood.utils.extension.notNull
import com.sun_asterisk.myfood.utils.extension.showToast
import kotlinx.android.synthetic.main.fragment_category.recyclerViewCategory
import kotlinx.android.synthetic.main.fragment_category.toolbarCategory
import kotlinx.android.synthetic.main.layout_toolbar.view.toolbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CategoryFragment : BaseFragment(), OnItemClickListener<Category> {

    private var onActionBarListener: OnActionBarListener? = null
    private lateinit var mCategoryAdapter: CategoryAdapter
    private lateinit var snapHelper: SnapHelper
    private val viewModel: CategoryViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onActionBarListener = if (context is OnActionBarListener) context else null
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onResume() {
        super.onResume()
        mCategoryAdapter.setOnItemCategoryListener(this)
    }

    override fun onStop() {
        super.onStop()
        mCategoryAdapter.unRegisterItemClickListener()
    }

    override fun setUpView() {
        onActionBarListener?.notNull {
            it.setupActionBar(toolbarCategory.toolbar, false)
        }
        dialogManager?.showLoading()
        mCategoryAdapter = CategoryAdapter(context!!, mutableListOf())
        mCategoryAdapter.setOnItemCategoryListener(this)
        recyclerViewCategory.apply {
            adapter = mCategoryAdapter
            setHasFixedSize(true)
        }

        snapHelper = LinearSnapHelper().apply { attachToRecyclerView(recyclerViewCategory) }

        recyclerViewCategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerView.layoutManager?.let {
                    snapHelper.findSnapView(it)?.let { view ->
                        val position = it.getPosition(view)
                        recyclerView.findViewHolderForAdapterPosition(position)?.let { viewHolder ->
                            val constraintLayout =
                                viewHolder.itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutHome)

                            if (newState == RecyclerView.SCROLL_STATE_IDLE)
                                constraintLayout.animate().setDuration(TIME_DURATION).scaleX(1F).scaleY(1F)
                                    .setInterpolator(AccelerateInterpolator()).start()
                            else
                                constraintLayout.animate().setDuration(TIME_DURATION).scaleX(0.75F).scaleY(0.75F)
                                    .setInterpolator(AccelerateInterpolator()).start()
                        }
                    }
                }
            }
        })
    }

    override fun bindView() {
        registerLiveData()
        viewModel.getCategories()
    }

    private fun registerLiveData() {
        viewModel.onCategoryEvent.observe(this, Observer {
            setDataForAdapter(it)
            dialogManager?.hideLoading()
        })

        viewModel.onMessageError.observe(this, Observer {
            context?.showToast(it.message.toString())
            dialogManager?.hideLoading()
        })
    }

    private fun setDataForAdapter(categories: MutableList<Category>) {
        mCategoryAdapter.setCategories(categories)
        Handler().postDelayed({
            recyclerViewCategory.findViewHolderForAdapterPosition(0)?.let {
                val constraintLayout = it.itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutHome)
                constraintLayout.animate().scaleY(1F).scaleX(1F).setDuration(TIME_DURATION)
                    .setInterpolator(AccelerateInterpolator()).start()
            }
        }, TIME_DELAY)
    }

    override fun onItemViewClick(item: Category, position: Int) {
        val mapsFragment: MapsFragment by inject { parametersOf(item.id) }
        addChildFragment(R.id.frameLayoutHome, mapsFragment, true, MapsFragment::class.java.simpleName)
    }

    override fun onDetach() {
        super.onDetach()
        onActionBarListener = null
    }

    companion object {
        private const val TIME_DURATION = 350L
        private const val TIME_DELAY = 100L
    }
}
