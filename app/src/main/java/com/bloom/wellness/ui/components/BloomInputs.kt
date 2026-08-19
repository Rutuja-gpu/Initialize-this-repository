package com.bloom.wellness.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon
import com.bloom.wellness.ui.theme.BloomRoseTint
import com.bloom.wellness.ui.theme.BloomSurface
import com.bloom.wellness.ui.theme.BloomTextSecondary

@Composable
fun BloomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = BloomTextSecondary) },
        singleLine = true,
        shape = RoundedCornerShape(BloomDimens.CardCornerSmall),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = BloomSurface,
            unfocusedContainerColor = BloomSurface,
            focusedIndicatorColor = BloomMaroon,
            unfocusedIndicatorColor = BloomRoseTint
        ),
        modifier = modifier.fillMaxWidth()
    )
}

/** Icon + title + description + trailing switch, used for the onboarding "what to track" list and Settings toggles. */
@Composable
fun ToggleRow(
    icon: ImageVector,
    title: String,
    description: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = BloomDimens.SpaceSm)
    ) {
        IconChip(icon = icon, size = 36.dp)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = BloomDimens.SpaceMd)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            if (description != null) {
                Text(description, style = MaterialTheme.typography.bodySmall, color = BloomTextSecondary)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = BloomMaroon)
        )
    }
}

/** A single radio choice with title + subtitle, used for journey stage / reminder period pickers. */
@Composable
fun RadioOptionRow(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick)
            .padding(vertical = BloomDimens.SpaceSm)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = BloomMaroon)
        )
        Column(modifier = Modifier.padding(start = BloomDimens.SpaceSm)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = BloomTextSecondary)
        }
    }
}

/** A row of mutually-exclusive pill choices (meal rating, symptom severity). */
@Composable
fun PillSelector(
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(BloomDimens.SpaceSm)) {
        options.forEach { option ->
            val isSelected = option == selected
            Card(
                onClick = { onSelect(option) },
                shape = RoundedCornerShape(50),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) BloomMaroon else BloomRoseTint
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    option,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) BloomSurface else BloomMaroon,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = BloomDimens.SpaceSm)
                )
            }
        }
    }
}

/** Label + numeric value flanked by -/+ buttons, used for goal targets and daily log quantities. */
@Composable
fun StepperRow(
    label: String,
    value: Int,
    unit: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().padding(vertical = BloomDimens.SpaceSm)
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        StepperControl(value = value, unit = unit, onDecrement = onDecrement, onIncrement = onIncrement)
    }
}

@Composable
fun StepperControl(
    value: Int,
    unit: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        RoundIconButton(icon = Icons.Outlined.Remove, onClick = onDecrement)
        Text(
            "$value $unit",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(64.dp)
                .padding(horizontal = BloomDimens.SpaceXs)
        )
        RoundIconButton(icon = Icons.Outlined.Add, onClick = onIncrement)
    }
}

@Composable
private fun RoundIconButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(BloomRoseTint)
    ) {
        Icon(icon, contentDescription = null, tint = BloomMaroon, modifier = Modifier.size(18.dp))
    }
}
