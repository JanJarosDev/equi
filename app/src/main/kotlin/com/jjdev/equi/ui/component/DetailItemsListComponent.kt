package com.jjdev.equi.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.jjdev.equi.dashboard.presentation.model.Investment
import com.jjdev.equi.ui.PieChartColor
import com.jjdev.equi.ui.preview.DetailItemsListPreviewProvider
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DetailItemsListComponent(
    investments: ImmutableList<Investment>,
    pieChartColors: ImmutableList<PieChartColor>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    //Todo lazycolumn
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        investments.forEachIndexed { index, value ->
            DetailItemComponent(
                investment = value,
                pieChartColor = pieChartColors[index],
                isLoading = isLoading,
            )
        }
    }
}

@Preview
@Composable
fun DetailsListComponentPreview(
    @PreviewParameter(DetailItemsListPreviewProvider::class)
    detailItemsListPreviewModel: DetailItemsListPreviewProvider.DetailItemsListPreviewModel,
) {
    DetailItemsListComponent(
        investments = detailItemsListPreviewModel.investments,
        pieChartColors = detailItemsListPreviewModel.pieChartColors,
    )
}
