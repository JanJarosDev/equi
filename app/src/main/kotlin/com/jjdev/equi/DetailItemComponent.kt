package com.jjdev.equi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jjdev.equi.ui.theme.PortfolioDeltaTypography
import com.jjdev.equi.ui.theme.dimens

@Composable
fun DetailItemComponent(
    data: Pair<String, Int>,
    color: Color,
) {
    Surface(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
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
                        color = color,
                        shape = RoundedCornerShape(MaterialTheme.dimens.cornerRadius)
                    )
                    .size(MaterialTheme.dimens.extraLarge)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.dimens.small)
            ) {
                Text(
                    text = data.first,
                    style = PortfolioDeltaTypography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = data.second.toString(),
                    style = PortfolioDeltaTypography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview
@Composable
fun DetailItemComponentPreview() {
    DetailItemComponent(
        data = Pair("EGU", 10),
        color = PieChartColors.RED.color
    )
}
