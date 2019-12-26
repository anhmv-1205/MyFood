package com.sun_asterisk.myfood.ui.edit_food

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.Observer
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.ui.main.OnActionBarListener
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.checkCameraAndGalleryPermission
import com.sun_asterisk.myfood.utils.extension.cropImage
import com.sun_asterisk.myfood.utils.extension.getCacheImagePath
import com.sun_asterisk.myfood.utils.extension.getKeyByValue
import com.sun_asterisk.myfood.utils.extension.goBackFragment
import com.sun_asterisk.myfood.utils.extension.loadImagePath
import com.sun_asterisk.myfood.utils.extension.loadImageUrl
import com.sun_asterisk.myfood.utils.extension.notNull
import com.sun_asterisk.myfood.utils.extension.replaceIpAddress
import com.sun_asterisk.myfood.utils.extension.showAlertDialogBasic
import com.sun_asterisk.myfood.utils.extension.showToast
import com.sun_asterisk.myfood.utils.extension.toRequestBodyImageType
import com.sun_asterisk.myfood.utils.extension.toRequestBodyTextType
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_create_food.buttonCreate
import kotlinx.android.synthetic.main.fragment_create_food.editTextCost
import kotlinx.android.synthetic.main.fragment_create_food.editTextFoodName
import kotlinx.android.synthetic.main.fragment_create_food.imageButtonSelectImage
import kotlinx.android.synthetic.main.fragment_create_food.imageViewFood
import kotlinx.android.synthetic.main.fragment_create_food.spinnerCategory
import kotlinx.android.synthetic.main.fragment_create_food.spinnerUnit
import kotlinx.android.synthetic.main.fragment_create_food.textViewCostDetail
import kotlinx.android.synthetic.main.fragment_create_food.toolbarCreateFood
import kotlinx.android.synthetic.main.layout_toolbar.view.textViewToolbarTitle
import kotlinx.android.synthetic.main.layout_toolbar.view.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditFoodFragment : BaseFragment(), OnClickListener, OnItemSelectedListener {

    private var onActionBarListener: OnActionBarListener? = null
    private val viewModel: EditFoodViewModel by viewModel()
    private var nameCategories = mutableListOf<String>()
    private var categoryHashMap: HashMap<String, String> = hashMapOf()
    private var fileName = ""
    private var imagePart = ""
    private var categoryId = ""
    private var unit = ""
    private var foodEdit: Food? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnActionBarListener) onActionBarListener = context
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_create_food, container, false)
    }

    override fun setUpView() {
        onActionBarListener.notNull { it.setupActionBar(toolbarCreateFood.toolbar) }
        toolbarCreateFood.toolbar.textViewToolbarTitle.text = getString(R.string.text_edit_food)
        imageButtonSelectImage.setOnClickListener(this)
        nameCategories.add(getString(R.string.text_title_category))
        buttonCreate.text = getString(R.string.text_title_button_edit)
        val adapterUnit =
            ArrayAdapter.createFromResource(context!!, R.array.units, R.layout.spinner_item_textview)
        adapterUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUnit.adapter = adapterUnit
        spinnerUnit.onItemSelectedListener = this
        spinnerCategory.onItemSelectedListener = this
        buttonCreate.setOnClickListener(this)
        arguments?.let {
            it.getParcelableArrayList<Category>(EXTRA_CATEGORIES)?.let { categories ->
                setupSpinnerCategory(categories)
            }
            it.getParcelable<Food>(EXTRA_FOOD)?.let { food ->
                foodEdit = food
            }
        }

        editTextCost.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (s.isEmpty()) textViewCostDetail.text = ""
                    else textViewCostDetail.text = getString(R.string.text_show_cost, it, unit)
                }
            }
        })
    }

    private fun bindDefaultFood(food: Food) {
        editTextFoodName.setText(food.name)
        editTextCost.setText(food.cost.toString())
        textViewCostDetail.text = getString(R.string.text_show_cost, food.cost.toString(), food.unitCost)
        val nameCategory = categoryHashMap.getKeyByValue(food.categoryId)
        val indexCategory = if (nameCategory != null) nameCategories.indexOf(nameCategory) else Constant.DEFAULT_VALUE
        spinnerCategory.setSelection(indexCategory)
        val indexUnit = resources.getStringArray(R.array.units).indexOf(food.unitFood)
        spinnerUnit.setSelection(if (indexUnit == Constant.INVALID_VALUE) Constant.DEFAULT_VALUE else indexUnit)
        imageViewFood.loadImageUrl(food.imgUrl.replaceIpAddress(), R.drawable.ic_farmer)
    }

    override fun bindView() {
        foodEdit?.let { bindDefaultFood(it) }
    }

    override fun registerLiveData() {
        viewModel.onEditFoodEvent.observe(this, Observer {
            context?.showAlertDialogBasic(getString(R.string.text_edit_food_success), false) {
                goBackFragment()
            }
        })
        viewModel.onMessageErrorEvent.observe(this, Observer {
            context?.showToast(it)
        })
        viewModel.onDialogProgressEvent.observe(this, Observer {
            if (it) dialogManager?.showLoading()
            else dialogManager?.hideLoading()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constant.REQUEST_IMAGE_CAPTURE -> if (resultCode == Activity.RESULT_OK) {
                cropImage(getCacheImagePath(fileName), isBitmapMaxSize = true)
            }
            Constant.REQUEST_GALLERY_IMAGE ->
                if (resultCode == Activity.RESULT_OK) data?.let {
                    it.data?.let { data ->
                        cropImage(data, isBitmapMaxSize = true)
                    }
                }
            UCrop.REQUEST_CROP -> if (resultCode == Activity.RESULT_OK) {
                handleUCropResult(data)
            }
            UCrop.RESULT_ERROR -> context?.showToast(UCrop.getError(data!!).toString())
        }
    }

    private fun handleUCropResult(data: Intent?) {
        if (data == null) return
        UCrop.getOutput(data)?.let {
            it.path?.let { part ->
                imagePart = part
                imageViewFood.loadImagePath(part)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonCreate -> {
                if (validateForm()) {
                    foodEdit?.let {
                        viewModel.editFood(
                            it.id,
                            categoryId.toRequestBodyTextType(),
                            if (imagePart.isEmpty()) null else imagePart.toRequestBodyImageType(),
                            editTextFoodName.text.toString().trim().toRequestBodyTextType(),
                            editTextCost.text.toString().trim().toRequestBodyTextType(),
                            unit.toRequestBodyTextType()
                        )
                    }
                }
            }
            R.id.imageButtonSelectImage -> {
                fileName = System.currentTimeMillis().toString() + Constant.IMAGE_FILE_FORMAT
                checkCameraAndGalleryPermission(fileName)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinnerCategory -> {
                categoryId = ""
                if (position > 0) {
                    val categoryName = parent.getItemAtPosition(position).toString()
                    categoryId = categoryHashMap[categoryName] ?: ""
                }
            }

            R.id.spinnerUnit -> {
                unit = ""
                if (position > 0) unit = parent.getItemAtPosition(position).toString()
                textViewCostDetail.text = if (unit.isEmpty()) "" else
                    getString(R.string.text_show_cost, editTextCost.text.toString().trim(), unit)
            }
        }
    }

    private fun setupSpinnerCategory(categories: MutableList<Category>) {
        if (categories.isNotEmpty()) {
            categories.forEach {
                nameCategories.add(it.name)
                categoryHashMap[it.name] = it.id
            }
        }
        val categoryAdapter = ArrayAdapter(context!!, R.layout.spinner_item_textview, nameCategories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter
    }

    private fun validateForm(): Boolean {
        if (editTextFoodName.text.isEmpty()) {
            editTextFoodName.error = getString(R.string.text_no_name_error)
            editTextFoodName.isFocusable = true
            return false
        }
        if (editTextCost.text.isEmpty()) {
            editTextCost.error = getString(R.string.text_no_cost_error)
            editTextCost.isFocusable = true
            return false
        }
        if (unit.isEmpty()) {
            ((spinnerUnit.selectedView) as TextView).error = getString(R.string.text_unit_error)
            return false
        }
        if (categoryId.isEmpty()) {
            ((spinnerCategory.selectedView) as TextView).error = getString(R.string.text_category_error)
            return false
        }
        return true
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    companion object {
        private const val EXTRA_CATEGORIES = "EXTRA_CATEGORIES"
        private const val EXTRA_FOOD = "EXTRA_FOOD"

        fun newInstance(food: Food, categories: ArrayList<Category>) = EditFoodFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(EXTRA_CATEGORIES, categories)
                putParcelable(EXTRA_FOOD, food)
            }
        }
    }
}
