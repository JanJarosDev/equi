package com.jjdev.equi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel : ViewModel() {

    private val _data = MutableStateFlow<List<Investment>?>(null)
    val data: StateFlow<List<Investment>?> = _data.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        _data.update {
            listOf(
                Investment("EGU", 0.5, 100.0),
                Investment("XXX", 0.5, 100.0),
            )
        }
    }
}