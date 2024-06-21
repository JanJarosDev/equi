package com.jjdev.porfoliodelta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PieChartComponent(data = mapOf(Pair("EGU", 10), Pair("XXX", 10)))
        }
    }
}