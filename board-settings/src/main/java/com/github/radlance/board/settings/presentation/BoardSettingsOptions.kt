package com.github.radlance.board.settings.presentation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.radlance.board.R

@Composable
internal fun BoardSettingsOptions(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    dropDownOptions: List<Int>,
    @StringRes selectedOptionId: Int,
    selectedOptionIndex: Int,
    onDropDownItemSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onExpandedChange(!expanded) }
    ) {
        Row {
            Text(
                text = stringResource(selectedOptionId),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.show_dropdown_options)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.widthIn(min = 220.dp)
        ) {
            dropDownOptions.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(option), fontSize = 16.sp) },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = selectedOptionIndex == index,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            if (selectedOptionIndex == index) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Done,
                                        contentDescription = stringResource(
                                            R.string.selected_dropdown_option
                                        ),
                                        tint = MenuDefaults.containerColor,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(3.dp)
                                    )
                                }
                            }
                        }
                    },
                    onClick = { onDropDownItemSelect(index) }
                )
            }
        }
    }
}