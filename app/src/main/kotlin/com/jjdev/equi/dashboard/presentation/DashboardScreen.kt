package com.jjdev.equi.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.jjdev.equi.core.ui.theme.dimens
import com.jjdev.equi.dashboard.presentation.DashboardScreenReducer.DashboardEvent
import com.jjdev.equi.dashboard.presentation.DashboardScreenReducer.DashboardState
import com.jjdev.equi.dashboard.presentation.model.Investment
import com.jjdev.equi.dashboard.presentation.model.NewInvestment
import com.jjdev.equi.ui.DetailsListComponent
import com.jjdev.equi.ui.PieChartColors
import com.jjdev.equi.ui.PieChartComponent
import kotlinx.collections.immutable.toPersistentList

@Composable
fun DashboardScreen(
    state: DashboardState,
    sendEvent: (event: DashboardEvent) -> Unit,
    onRebalanceClick: (amountToInvest: Double, investments: List<NewInvestment>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tempData = if (state.rebalancedInvestments.isNotEmpty()) {
        state.rebalancedInvestments.map {
            Investment(
                ticker = it.ticker,
                weight = it.percentage,
                value = it.newAmount.toInt(),
            )
        }.toPersistentList()
    } else {
        state.investments.map {
            Investment(
                ticker = it.ticker,
                weight = it.percentage,
                value = 0
            )
        }.toPersistentList()
    }

    val tempColors = PieChartColors.entries.map { it.color }.toPersistentList()


    if (state.dialogShown) {
        AddDialog(
            sendEvent = sendEvent,
        )
    }

    Column(
        modifier = modifier
            .padding(top = MaterialTheme.dimens.tripleExtraLarge)
            .fillMaxSize()
    ) {
        PieChartComponent(investments = tempData, isLoading = state.isLoading)
        DetailsListComponent(investments = tempData, colors = tempColors, isLoading = state.isLoading)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { sendEvent(DashboardEvent.UpdateDialog(show = true)) },
            enabled = !state.isLoading,
            modifier = Modifier
                .padding(bottom = MaterialTheme.dimens.large)
                .align(
                    Alignment.CenterHorizontally
                )
        ) {
            Text("Add")
        }
        Text("Amount to invest")
        var amount by remember { mutableStateOf("") }
        TextField(
            value = amount,
            onValueChange = {
                amount = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onRebalanceClick(amount.toDouble(), state.investments) },
            enabled = !state.isLoading,
            modifier = Modifier
                .padding(bottom = MaterialTheme.dimens.large)
                .align(
                    Alignment.CenterHorizontally
                )
        ) {
            Text("Rebalance")
        }
    }
}

//TODO refactor to state
@Suppress("LongMethod")
@Composable
fun AddDialog(
    sendEvent: (event: DashboardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = { sendEvent(DashboardEvent.UpdateDialog(show = false)) }
    ) {
        var ticker by remember { mutableStateOf("") }
        var value by remember { mutableStateOf("") }
        var percentage by remember { mutableStateOf("") }
        Surface(shape = MaterialTheme.shapes.medium, modifier = modifier) {
            Column {
                Column(Modifier.padding(MaterialTheme.dimens.large)) {
                    Text(
                        text = "Add investment",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.size((MaterialTheme.dimens.small)))
                    Text("Ticker")
                    TextField(
                        value = ticker,
                        onValueChange = {
                            ticker = it
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text
                        ),
                        visualTransformation = CapitalizeTransformation()

                    )
                    Text("Value")
                    TextField(
                        value = value,
                        onValueChange = {
                            value = it
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                    Text("Percentage")
                    TextField(
                        value = percentage,
                        onValueChange = {
                            percentage = it
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                }
                Row(
                    Modifier
                        .padding(MaterialTheme.dimens.small)
                        .fillMaxWidth(),
                    Arrangement.spacedBy(MaterialTheme.dimens.small, Alignment.End),
                ) {
                    Button(onClick = { sendEvent(DashboardEvent.UpdateDialog(show = false)) }) {
                        Text("Close")
                    }
                    Button(onClick = {
                        sendEvent(
                            DashboardEvent.AddInvestment(
                                NewInvestment(
                                    ticker,
                                    percentage.toInt(),
                                    value.toDouble()
                                )
                            )
                        )
                        ticker = ""
                        value = ""
                        percentage = ""
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

class CapitalizeTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val capitalizedText = text.text.uppercase()

        return TransformedText(
            AnnotatedString(capitalizedText),
            OffsetMapping.Identity
        )
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(
        state = DashboardState.initial(),
        sendEvent = {},
        onRebalanceClick = { _, _ -> }
    )
}
