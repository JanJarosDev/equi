package com.jjdev.equi.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import com.jjdev.equi.ui.PieChartColor
import com.jjdev.equi.ui.preview.helper.InvestmentPreviewParameterHelper
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

class DetailItemsListPreviewProvider :
    PreviewParameterProvider<DetailItemsListPreviewProvider.DetailItemsListPreviewModel> {

    data class DetailItemsListPreviewModel(
        val investments: ImmutableList<InvestmentUIModel>,
        val pieChartColors: ImmutableList<PieChartColor>,
    )

    override val values: Sequence<DetailItemsListPreviewModel>
        get() = sequenceOf(
            DetailItemsListPreviewModel(
                investments = InvestmentPreviewParameterHelper.values.toPersistentList(),
                pieChartColors = PieChartColor.entries.toPersistentList()
            )
        )
}
