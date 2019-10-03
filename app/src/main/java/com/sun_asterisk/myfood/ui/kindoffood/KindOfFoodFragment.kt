package com.sun_asterisk.myfood.ui.kindoffood

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
import com.sun_asterisk.myfood.data.model.KindOfFood
import kotlinx.android.synthetic.main.fragment_kind_of_food.recyclerViewKindOfFood

class KindOfFoodFragment : BaseFragment(), OnItemClickListener<KindOfFood> {

    private lateinit var kindOfFoodAdapter: KindOfFoodAdapter
    private lateinit var snapHelper: SnapHelper

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_kind_of_food, container, false)
    }

    override fun onResume() {
        super.onResume()
        kindOfFoodAdapter.setOnItemKindOfFoodListener(this)
    }

    override fun onStop() {
        super.onStop()
        kindOfFoodAdapter.unRegisterItemClickListener()
    }

    override fun setUpView() {
        val kindOfFoods = mutableListOf<KindOfFood>()
        kindOfFoods.add(KindOfFood("Rau", "Rau sach co nguon goc ro rang, duoc tuoi boi nguon nuoc sach que huowng"))
        kindOfFoods.add(KindOfFood("Rau", "Rau sach co nguon goc ro rang, duoc tuoi boi nguon nuoc sach que huowng"))
        kindOfFoods.add(KindOfFood("Rau", "Rau sach co nguon goc ro rang, duoc tuoi boi nguon nuoc sach que huowng"))
        kindOfFoods.add(KindOfFood("Rau", "Rau sach co nguon goc ro rang, duoc tuoi boi nguon nuoc sach que huowng"))
        kindOfFoods.add(KindOfFood("Rau", "Rau sach co nguon goc ro rang, duoc tuoi boi nguon nuoc sach que huowng"))

        kindOfFoodAdapter = KindOfFoodAdapter(context!!, mutableListOf())
        kindOfFoodAdapter.setOnItemKindOfFoodListener(this)
        recyclerViewKindOfFood.apply {
            adapter = kindOfFoodAdapter
            setHasFixedSize(true)
        }
        kindOfFoodAdapter.setKindOfFoods(kindOfFoods)

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

    override fun onItemViewClick(item: KindOfFood, position: Int) {
    }

    companion object {
        private const val TIME_DURATION = 350L
        private const val TIME_DELAY = 100L

        fun newInstance() = KindOfFoodFragment()
    }
}
