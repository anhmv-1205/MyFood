package com.sun_asterisk.myfood.data.remote.remotedatasource

import com.sun_asterisk.myfood.data.datasource.CommentDataSource
import com.sun_asterisk.myfood.data.remote.api.service.MyFoodApi
import com.sun_asterisk.myfood.data.remote.request.RatingRequest

class CommentRemoteDataSource(private val myFoodApi: MyFoodApi) : CommentDataSource.Remote {
    override suspend fun createComment(ratingRequest: RatingRequest) = myFoodApi.createComment(ratingRequest)

    override suspend fun getCommentsByFarmerId(farmerId: String) = myFoodApi.getCommentsByFarmerId(farmerId)
}
