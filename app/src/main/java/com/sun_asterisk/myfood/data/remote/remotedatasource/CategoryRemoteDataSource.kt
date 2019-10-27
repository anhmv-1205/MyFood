package com.sun_asterisk.myfood.data.remote.remotedatasource

import com.sun_asterisk.myfood.data.datasource.CategoryDataSource
import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.data.remote.api.service.MyFoodApi
import org.koin.core.KoinComponent

class CategoryRemoteDataSource(private val myFoodApi: MyFoodApi) : CategoryDataSource.Remote, KoinComponent {
    override suspend fun getCategory(): ArrayList<Category> = myFoodApi.getCategories().data!!
}
