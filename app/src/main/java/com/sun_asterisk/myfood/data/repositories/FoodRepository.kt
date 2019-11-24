package com.sun_asterisk.myfood.data.repositories

import com.sun_asterisk.myfood.data.datasource.FoodDataSource
import com.sun_asterisk.myfood.data.remote.remotedatasource.FoodRemoteDataSource
import org.koin.core.KoinComponent

class FoodRepository(private val remote: FoodRemoteDataSource) : FoodDataSource.Remote, KoinComponent {

    override suspend fun getFoodsWithIdUser(idUser: String, page: Int) =
        remote.getFoodsWithIdUser(idUser, page)
}
