package com.sun_asterisk.myfood.ui.category

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.base.recyclerview.OnItemClickListener
import com.sun_asterisk.myfood.data.model.Category
import kotlinx.android.synthetic.main.fragment_category.recyclerViewKindOfFood

class CategoryFragment : BaseFragment(), OnItemClickListener<Category> {

    private lateinit var mCategoryAdapter: CategoryAdapter
    private lateinit var snapHelper: SnapHelper

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onResume() {
        super.onResume()
        mCategoryAdapter.setOnItemKindOfFoodListener(this)
    }

    override fun onStop() {
        super.onStop()
        mCategoryAdapter.unRegisterItemClickListener()
    }

    override fun setUpView() {
        val kindOfFoods = mutableListOf<Category>()
        kindOfFoods.add(Category("Rau", "Rau sach co nguon goc ro rang, duoc tuoi boi nguon nuoc sach que huowng"))
        kindOfFoods.add(Category("Rau", "Rau sach co nguon goc ro rang, duoc tuoi boi nguon nuoc sach que huowng"))
        kindOfFoods.add(Category("Rau", "Rau sach co nguon goc ro rang, duoc tuoi boi nguon nuoc sach que huowng"))
        kindOfFoods.add(Category("Rau", "Rau sach co nguon goc ro rang, duoc tuoi boi nguon nuoc sach que huowng"))
        kindOfFoods.add(Category("Rau", "Rau sach co nguon goc ro rang, duoc tuoi boi nguon nuoc sach que huowng"))

        mCategoryAdapter = CategoryAdapter(context!!, mutableListOf())
        mCategoryAdapter.setOnItemKindOfFoodListener(this)
        recyclerViewKindOfFood.apply {
            adapter = mCategoryAdapter
            setHasFixedSize(true)
        }
        mCategoryAdapter.setKindOfFoods(kindOfFoods)

        snapHelper = LinearSnapHelper().apply { attachToRecyclerView(recyclerViewKindOfFood) }

        Handler().postDelayed({
            recyclerViewKindOfFood.findViewHolderForAdapterPosition(0)?.let {
                val constraintLayout = it.itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutHome)
                constraintLayout.animate().scaleY(1F).scaleX(1F).setDuration(TIME_DURATION)
                    .setInterpolator(AccelerateInterpolator()).start()
            }
        }, TIME_DELAY)

        recyclerViewKindOfFood.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerView.layoutManager?.let {
                    val view: View = snapHelper.findSnapView(it) as View
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
        })
    }

    override fun bindView() {
    }

    override fun onItemViewClick(item: Category, position: Int) {
    }

    companion object {
        private const val TIME_DURATION = 350L
        private const val TIME_DELAY = 100L
    }
}
