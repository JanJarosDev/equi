package com.jjdev.equi.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.jjdev.equi.core.ui.shimmerLoadingAnimation
import com.jjdev.equi.core.ui.theme.PortfolioDeltaTypography
import com.jjdev.equi.core.ui.theme.dimens
import com.jjdev.equi.dashboard.presentation.model.Investment
import com.jjdev.equi.ui.PieChartColor
import com.jjdev.equi.ui.preview.DetailItemPreviewProvider

@Composable
fun DetailItemComponent(
    investment: Investment,
    pieChartColor: PieChartColor,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Surface(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .height(50.dp)
            .padding(
                vertical = MaterialTheme.dimens.small,
                horizontal = MaterialTheme.dimens.large,
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = pieChartColor.color,
                        shape = RoundedCornerShape(MaterialTheme.dimens.cornerRadius)
                    )
                    .size(MaterialTheme.dimens.extraLarge)
            )
            Column(
                modifier = Modifier
                    .padding(start = MaterialTheme.dimens.small)
            ) {
                Text(
                    text = investment.ticker,
                    style = PortfolioDeltaTypography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = investment.weight.toString(),
                    style = PortfolioDeltaTypography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "----> " + investment.value.toString(),
                style = PortfolioDeltaTypography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier
                    .clip(shape = RoundedCornerShape(MaterialTheme.dimens.small))
                    .background(
                        color = if (isLoading) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .align(Alignment.CenterVertically)
                    .size(height = 50.dp, width = 100.dp)
                    .shimmerLoadingAnimation(
                        isLoading = isLoading,
                        isDarkModeActive = isSystemInDarkTheme()
                    )
            )
        }
    }
}

@Preview
@Composable
fun DetailItemComponentPreview(
    @PreviewParameter(DetailItemPreviewProvider::class)
    detailItemPreviewModel: DetailItemPreviewProvider.DetailItemPreviewModel,
) {
    DetailItemComponent(
        investment = detailItemPreviewModel.investment,
        pieChartColor = detailItemPreviewModel.pieChartColor,
    )
}
