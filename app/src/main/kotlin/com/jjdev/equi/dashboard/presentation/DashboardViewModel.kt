package com.jjdev.equi.dashboard.presentation

import com.jjdev.equi.core.base.presentation.BaseViewModel
import com.jjdev.equi.dashboard.presentation.DashboardScreenReducer.DashboardEffect
import com.jjdev.equi.dashboard.presentation.DashboardScreenReducer.DashboardEvent
import com.jjdev.equi.dashboard.presentation.DashboardScreenReducer.DashboardState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class DashboardViewModel : BaseViewModel<DashboardState, DashboardEvent, DashboardEffect>(
    initialState = DashboardState.initial(),
    reducer = DashboardScreenReducer(),
) {

    private val _data = MutableStateFlow(DashboardState.initial())
    val data: StateFlow<DashboardState> = _data.asStateFlow()

    init {
        Timber.i("Initializing DashboardViewModel")
        fetchData()
    }

    private fun fetchData() {
        Timber.i("Fetching data")
        _data.also { Timber.i("Data updated") }
    }
}