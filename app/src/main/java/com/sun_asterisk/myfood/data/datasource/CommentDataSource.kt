package com.sun_asterisk.myfood.data.datasource

import com.sun_asterisk.myfood.data.model.Comment
import com.sun_asterisk.myfood.data.remote.request.RatingRequest
import com.sun_asterisk.myfood.data.remote.response.ApiResponse

interface CommentDataSource {
    interface Remote {
        suspend fun createComment(ratingRequest: RatingRequest): ApiResponse<Any>

        suspend fun getCommentsByFarmerId(farmerId: String): ApiResponse<ArrayList<Comment>>
    }
}
