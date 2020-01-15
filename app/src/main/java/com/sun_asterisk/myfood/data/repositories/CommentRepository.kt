package com.sun_asterisk.myfood.data.repositories

import com.sun_asterisk.myfood.data.datasource.CommentDataSource
import com.sun_asterisk.myfood.data.remote.remotedatasource.CommentRemoteDataSource
import com.sun_asterisk.myfood.data.remote.request.RatingRequest
import org.koin.core.KoinComponent

class CommentRepository(private val remote: CommentRemoteDataSource) : CommentDataSource.Remote, KoinComponent {
    override suspend fun createComment(ratingRequest: RatingRequest) = remote.createComment(ratingRequest)

    override suspend fun getCommentsByFarmerId(farmerId: String) = remote.getCommentsByFarmerId(farmerId)
}
