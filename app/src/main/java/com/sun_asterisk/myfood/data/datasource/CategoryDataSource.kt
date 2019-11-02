package com.sun_asterisk.myfood.data.datasource

import com.sun_asterisk.myfood.data.model.Category

interface CategoryDataSource {
    interface Remote {
        suspend fun getCategory(): ArrayList<Category>
    }
}
