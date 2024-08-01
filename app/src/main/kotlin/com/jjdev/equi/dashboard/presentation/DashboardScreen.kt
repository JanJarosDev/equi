package com.jjdev.equi.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.jjdev.equi.core.ui.theme.dimens
import com.jjdev.equi.ui.DetailsListComponent
import com.jjdev.equi.ui.PieChartColors
import com.jjdev.equi.ui.PieChartComponent

@Composable
fun DashboardScreen(
    state: DashboardScreenReducer.DashboardState,
    modifier: Modifier = Modifier,
) {
    //Todo replace with state arguments
    val tempData = mapOf(Pair("EGU", 10), Pair("XXX", 10))
    val tempColors = PieChartColors.entries.map { it.color }

    //Todo fix
    val dismissmoreXD = {}

    Dialog(dismissmoreXD) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column {
                Column(Modifier.padding(MaterialTheme.dimens.large)) {
                    Text("Title")
                    Spacer(Modifier.size(MaterialTheme.dimens.medium))
                    Text("Subtitle")
                }
                Spacer(Modifier.size((MaterialTheme.dimens.small)))
                Row(
                    Modifier
                        .padding(MaterialTheme.dimens.small)
                        .fillMaxWidth(),
                    Arrangement.spacedBy(MaterialTheme.dimens.small, Alignment.End),
                ) {
                    Button(onClick = {}) {
                        Text("Dismiss")
                    }
                    Button(onClick = {}) {
                        Text("Confirm")
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .padding(top = MaterialTheme.dimens.tripleExtraLarge)
    ) {
        PieChartComponent(
            data = tempData
        )
        DetailsListComponent(
            data = tempData,
            colors = tempColors
        )
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(
        state = DashboardScreenReducer.DashboardState.initial()
    )
}