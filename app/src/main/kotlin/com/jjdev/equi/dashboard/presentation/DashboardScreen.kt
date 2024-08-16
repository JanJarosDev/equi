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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.jjdev.equi.R
import com.jjdev.equi.core.ui.theme.dimens
import com.jjdev.equi.dashboard.presentation.DashboardReducer.DashboardEvent
import com.jjdev.equi.dashboard.presentation.DashboardReducer.DashboardState
import com.jjdev.equi.dashboard.presentation.model.AddInvestmentDialogModel
import com.jjdev.equi.dashboard.presentation.model.InvestmentUIModel
import com.jjdev.equi.ui.component.DetailItemsListComponent
import com.jjdev.equi.ui.component.PieChartComponent
import java.math.BigDecimal

@Composable
fun DashboardScreen(
    state: DashboardState,
    sendEvent: (event: DashboardEvent) -> Unit,
    onRebalanceClick: (amountToInvest: BigDecimal, investments: List<InvestmentUIModel>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rebalanceButtonEnabled = state.amountToInvest != null && !state.isLoading

    if (state.dialogShown) {
        AddInvestmentDialog(
            addInvestmentDialogModel = state.addInvestmentDialogModel,
            sendEvent = sendEvent,
        )
    }

    Column(
        modifier = modifier
            .padding(top = MaterialTheme.dimens.tripleExtraLarge)
            .fillMaxSize()
    ) {
        PieChartComponent(
            investments = state.investments,
            isLoading = state.isLoading,
        )
        DetailItemsListComponent(
            investments = state.investments,
            isLoading = state.isLoading,
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { sendEvent(DashboardEvent.UpdateDialog(show = true)) },
            enabled = !state.isLoading,
            modifier = Modifier
                .padding(bottom = MaterialTheme.dimens.large)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.dashboard_button_add_investment))
        }
        Text(stringResource(R.string.dashboard_label_amount_to_invest))
        TextField(
            value = state.amountToInvest?.toString() ?: "",
            onValueChange = { sendEvent(DashboardEvent.UpdateAmountToInvest(amount = it)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = {
                state.amountToInvest?.let {
                    onRebalanceClick(BigDecimal.valueOf(it), state.investments)
                }
            },
            enabled = rebalanceButtonEnabled,
            modifier = Modifier
                .padding(bottom = MaterialTheme.dimens.large)
                .align(Alignment.CenterHorizontally),
        ) {
            Text(stringResource(R.string.dashboard_button_rebalance))
        }
    }
}

@Composable
fun AddInvestmentDialog(
    addInvestmentDialogModel: AddInvestmentDialogModel?,
    sendEvent: (event: DashboardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = { sendEvent(DashboardEvent.UpdateDialog(show = false)) }) {
        Surface(shape = MaterialTheme.shapes.medium, modifier = modifier) {
            Column {
                Column(Modifier.padding(MaterialTheme.dimens.large)) {
                    Text(
                        text = stringResource(R.string.add_investment_dialog_label_add_investment),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.size((MaterialTheme.dimens.small)))
                    Text(stringResource(R.string.add_investment_dialog_label_ticker))
                    TextField(
                        value = addInvestmentDialogModel?.ticker ?: "",
                        onValueChange = { sendEvent(DashboardEvent.UpdateDialogTicker(ticker = it)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text
                        ),
                        visualTransformation = CapitalizeTransformation()
                    )
                    Text(stringResource(R.string.add_investment_dialog_label_value))
                    TextField(
                        value = addInvestmentDialogModel?.value ?: "",
                        onValueChange = { sendEvent(DashboardEvent.UpdateDialogValue(value = it)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                    Text(stringResource(R.string.add_investment_dialog_label_percentage))
                    TextField(
                        value = addInvestmentDialogModel?.weight ?: "",
                        onValueChange = { sendEvent(DashboardEvent.UpdateDialogWeight(weight = it)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                }
                AddInvestmentDialogButtons(
                    addInvestmentDialogModel = addInvestmentDialogModel,
                    sendEvent = sendEvent,
                )
            }
        }
    }
}

@Composable
fun AddInvestmentDialogButtons(
    addInvestmentDialogModel: AddInvestmentDialogModel?,
    sendEvent: (event: DashboardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val addButtonEnabled = !addInvestmentDialogModel?.ticker.isNullOrBlank() &&
            !addInvestmentDialogModel?.value.isNullOrBlank() &&
            !addInvestmentDialogModel?.weight.isNullOrBlank()
    Row(
        modifier
            .padding(MaterialTheme.dimens.small)
            .fillMaxWidth(),
        Arrangement.spacedBy(MaterialTheme.dimens.small, Alignment.End),
    ) {
        Button(onClick = { sendEvent(DashboardEvent.UpdateDialog(show = false)) }) {
            Text(stringResource(R.string.add_investment_dialog_button_close))
        }
        Button(
            enabled = addButtonEnabled,
            onClick = {
                sendEvent(
                    DashboardEvent.AddInvestment(
                        ticker = addInvestmentDialogModel?.ticker ?: "",
                        value = addInvestmentDialogModel?.value ?: "",
                        weight = addInvestmentDialogModel?.weight ?: ""
                    )
                ).also {
                    sendEvent(DashboardEvent.UpdateDialogTicker(ticker = ""))
                    sendEvent(DashboardEvent.UpdateDialogValue(value = ""))
                    sendEvent(DashboardEvent.UpdateDialogWeight(weight = ""))
                }
            }) {
            Text(stringResource(R.string.add_investment_dialog_button_add))
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
