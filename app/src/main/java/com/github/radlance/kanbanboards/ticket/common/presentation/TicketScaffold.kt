package com.github.radlance.kanbanboards.ticket.common.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import com.github.radlance.kanbanboards.common.presentation.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScaffold(
    navigateUp: () -> Unit,
    @StringRes titleResId: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButton(navigateUp)
                },
                title = {
                    Text(text = stringResource(titleResId))
                }
            )
        },
        modifier = modifier
    ) { contentPadding ->
        content(
            modifier.padding(
                top = contentPadding.calculateTopPadding(),
                start = contentPadding.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
            )
        )
    }
}