package com.sun_asterisk.myfood.utils.annotation

import androidx.annotation.StringDef

@StringDef(OrderStatus.REQUESTING, OrderStatus.APPROVED, OrderStatus.DONE, OrderStatus.CANCELED, OrderStatus.REJECTED)
annotation class OrderStatus {

    companion object {
        const val REQUESTING = "Requesting"
        const val APPROVED = "Approved"
        const val DONE = "Done"
        const val CANCELED = "Canceled"
        const val REJECTED = "Rejected"
    }
}
