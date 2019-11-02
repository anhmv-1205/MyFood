package com.sun_asterisk.myfood.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val viewModelJob: Job by lazy { Job() }
    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            coroutineScope.launch(Dispatchers.Main) {
                printException(throwable)
            }
            GlobalScope.launch { println("Caught $throwable") }
        }
    val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob + coroutineExceptionHandler)

    abstract fun printException(throwable: Throwable)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
