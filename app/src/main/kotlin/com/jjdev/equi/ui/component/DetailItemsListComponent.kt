package com.jjdev.equi.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import com.jjdev.equi.ui.PieChartColor
import com.jjdev.equi.ui.preview.DetailItemsListPreviewProvider
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DetailItemsListComponent(
    investments: ImmutableList<InvestmentUIModel>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        itemsIndexed(investments) { index, investment ->
            DetailItemComponent(
                investment = investment,
                pieChartColor = PieChartColor.entries[index],
                isLoading = isLoading,
            )
        }
    }
}

@Preview
@Composable
private fun DetailsListComponentPreview(
    @PreviewParameter(DetailItemsListPreviewProvider::class)
    detailItemsListPreviewModel: DetailItemsListPreviewProvider.DetailItemsListPreviewModel,
) {
    DetailItemsListComponent(
        investments = detailItemsListPreviewModel.investments,
    )
}
