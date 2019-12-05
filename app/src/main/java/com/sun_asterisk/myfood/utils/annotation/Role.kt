package com.sun_asterisk.myfood.utils.annotation

import androidx.annotation.IntDef

@IntDef(Role.ADMIN, Role.FARMER, Role.BUYER)
annotation class Role {

    companion object {
        const val ADMIN = 1
        const val FARMER = 2
        const val BUYER = 3
    }
}
