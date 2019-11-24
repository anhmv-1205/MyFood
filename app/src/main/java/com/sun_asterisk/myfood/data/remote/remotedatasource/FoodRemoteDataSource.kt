package com.sun_asterisk.myfood.data.remote.remotedatasource

import com.sun_asterisk.myfood.data.datasource.FoodDataSource
import com.sun_asterisk.myfood.data.remote.api.service.MyFoodApi
import org.koin.core.KoinComponent

class FoodRemoteDataSource(private val myFoodApi: MyFoodApi) : FoodDataSource.Remote, KoinComponent {

    override suspend fun getFoodsWithIdUser(idUser: String, page: Int) = myFoodApi.getFoodsWithIdUser(idUser, page)
}
