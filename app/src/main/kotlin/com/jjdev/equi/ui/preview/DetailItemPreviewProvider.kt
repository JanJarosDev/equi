package com.jjdev.equi.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jjdev.equi.dashboard.presentation.model.Investment
import com.jjdev.equi.ui.PieChartColor
import com.jjdev.equi.ui.preview.helper.InvestmentPreviewParameterHelper

class DetailItemPreviewProvider :
    PreviewParameterProvider<DetailItemPreviewProvider.DetailItemPreviewModel> {

    data class DetailItemPreviewModel(
        val investment: Investment,
        val pieChartColor: PieChartColor,
    )

    private fun createDetailItemPreviewModel(): Sequence<DetailItemPreviewModel> =
        InvestmentPreviewParameterHelper.values.mapIndexed { index, investment ->
            DetailItemPreviewModel(
                investment = investment,
                pieChartColor = PieChartColor.entries[index],
            )
        }

    override val values: Sequence<DetailItemPreviewModel>
        get() = createDetailItemPreviewModel()
}
