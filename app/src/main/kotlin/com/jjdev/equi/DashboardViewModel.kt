package com.jjdev.equi

import com.jjdev.equi.core.base.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

class DashboardViewModel :
    BaseViewModel<DashboardScreenReducer.DashboardState, DashboardScreenReducer.DashboardEvent, DashboardScreenReducer.DashboardEffect>(
        initialState = DashboardScreenReducer.DashboardState.initial(),
        reducer = DashboardScreenReducer(),
    ) {

    private val _data = MutableStateFlow<DashboardScreenViewState?>(null)
    val data: StateFlow<DashboardScreenViewState?> = _data.asStateFlow()

    init {
        Timber.i("Initializing DashboardViewModel")
        fetchData()
    }

    private fun fetchData() {
        Timber.i("Fetching data")
        _data.update {
            null
        }.also { Timber.i("Data updated") }
    }
}