package com.sun_asterisk.myfood.ui.comment

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.model.Comment
import com.sun_asterisk.myfood.data.repositories.CommentRepository
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentViewModel(private val commentRepository: CommentRepository) : BaseViewModel() {

    val onCommentsEvent: SingleLiveEvent<MutableList<Comment>> by lazy { SingleLiveEvent<MutableList<Comment>>() }

    val onMessageErrorEvent: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }

    fun getComment(farmerId: String) {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) {
                    commentRepository.getCommentsByFarmerId(farmerId)
                }
                if (result.data == null) {
                    onMessageErrorEvent.value = result.message
                    return@launch
                }
                onCommentsEvent.value = result.data
            } catch (exception: Exception) {
                onMessageErrorEvent.value = exception.message
            }
        }
    }
}