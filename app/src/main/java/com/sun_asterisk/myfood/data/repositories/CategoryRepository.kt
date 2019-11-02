package com.sun_asterisk.myfood.data.repositories

import com.sun_asterisk.myfood.data.datasource.CategoryDataSource
import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.data.remote.remotedatasource.CategoryRemoteDataSource
import org.koin.core.KoinComponent

class CategoryRepository(private val remote: CategoryRemoteDataSource) : CategoryDataSource.Remote, KoinComponent {
    override suspend fun getCategory(): ArrayList<Category> = remote.getCategory()
}
