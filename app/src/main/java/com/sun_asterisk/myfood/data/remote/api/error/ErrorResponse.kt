package com.sun_asterisk.myfood.data.remote.api.error

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.toStringWithFormatPattern

class ErrorResponse {
    @Expose
    @SerializedName("data")
    val data: Error? = null

    @Expose
    @SerializedName("status")
    val status: String? = null

    @Expose
    @SerializedName("message")
    val message: String? = null

    inner class Error {
        @Expose
        @SerializedName("code")
        val code: Int = 0
        @Expose
        @SerializedName("errors")
        val messages: List<String>? = null

        val message: String?
            get() = if (messages == null || messages.isEmpty()) {
                ""
            } else {
                messages.toStringWithFormatPattern(Constant.ENTER_SPACE_FORMAT)
            }
    }
}
