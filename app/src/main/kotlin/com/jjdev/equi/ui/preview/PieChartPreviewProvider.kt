package com.jjdev.equi.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import com.jjdev.equi.ui.preview.helper.InvestmentPreviewParameterHelper
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

class PieChartPreviewProvider :
    PreviewParameterProvider<PieChartPreviewProvider.PieChartPreviewModel> {

    data class PieChartPreviewModel(
        val investments: ImmutableList<InvestmentUIModel>,
    )

    override val values: Sequence<PieChartPreviewModel>
        get() = sequenceOf(
            PieChartPreviewModel(
                investments = InvestmentPreviewParameterHelper.values.toPersistentList(),
            )
        )
}
