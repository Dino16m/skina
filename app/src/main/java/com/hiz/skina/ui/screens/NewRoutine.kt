package com.hiz.skina.ui.screens

import android.widget.Space
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant





data class Weekday(val display: String, val value: String) {}
data class Occurrence(val interval: Int, val unit: String) {}

sealed interface RepeatOption {
    data class Custom(val occurrence: Occurrence) : RepeatOption
    data class Weekdays(val weekdays: List<Weekday>)
}
object InstantIso8601Serializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("java.time.Instant", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())
}

@Serializable()
data class SkincareRoutine(
    val title: String,
    val repeatOption: RepeatOption,
    val createdAt: @Serializable(InstantIso8601Serializer::class) Instant,
    val updatedAt: @Serializable(InstantIso8601Serializer::class) Instant
) {}

sealed interface  RoutineAction {
    data object Create: RoutineAction
    data class Update(val routine: SkincareRoutine): RoutineAction
}

@Serializable
data class NewRoutineRoute(val action: RoutineAction)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRoutinePage(navigateBack: () -> Unit, action: RoutineAction) {
    Scaffold(
        Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = {
                    val titleText = when(action) {
                        RoutineAction.Create -> "Add Skincare Routine"
                        is RoutineAction.Update -> "Update Skincare Routine"
                    }
                    Text(titleText, fontSize = 25.sp, fontWeight = FontWeight.W500)
                },
                modifier = Modifier.shadow(1.dp),

                )
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .absolutePadding(left = 8.dp, right = 8.dp)
                    .fillMaxWidth()
            ) {
                val inputModifier = Modifier.fillMaxWidth()
                var title by rememberSaveable {
                    mutableStateOf("")
                }
                var fixedWeekdays by rememberSaveable {
                    mutableStateOf(true)
                }
                val selectedWeekdays = remember {
                    mutableStateListOf<Weekday>()
                }
                var customOccurrence: Occurrence by remember {
                    mutableStateOf(Occurrence(interval = 1, unit = "Day"))
                }

                TextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = inputModifier,
                    label = {
                        Text(
                            text = "Title",
                            fontSize = 20.sp
                        )
                    },
                    shape = RoundedCornerShape(10),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                RepeatOption(
                    inputModifier,
                    fixedWeekdays = fixedWeekdays,
                    onChange = { fixedWeekdays = it })
                Spacer(modifier = Modifier.height(20.dp))
                if (fixedWeekdays) WeekDaySelection(
                    inputModifier,
                    selected = selectedWeekdays,
                    onChange = {
                        selectedWeekdays.clear()
                        selectedWeekdays.addAll(it)
                    }) else CustomRecurrence(
                    inputModifier,
                    occurrence = customOccurrence,
                    onChange = { customOccurrence = it }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Button(
                    onClick = {}, modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Submit")
                }
            }

        }

    }
}

@Composable
fun RepeatOption(modifier: Modifier, fixedWeekdays: Boolean, onChange: (Boolean) -> Unit) {
    Column(modifier) {
        Text(text = "Repeat option", fontSize = 22.sp)
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .selectableGroup()
                .absolutePadding(top = 15.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.selectable(
                    selected = (fixedWeekdays),
                    onClick = { onChange(true) },
                    role = Role.RadioButton
                )
            ) {
                RadioButton(selected = fixedWeekdays, onClick = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Weekdays", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.selectable(
                    selected = (!fixedWeekdays),
                    onClick = { onChange(false) },
                    role = Role.RadioButton
                )
            ) {
                RadioButton(selected = !fixedWeekdays, onClick = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Custom", fontSize = 18.sp)
            }
        }

    }
}

@Composable
fun CustomRecurrence(
    modifier: Modifier = Modifier,
    occurrence: Occurrence,
    onChange: (Occurrence) -> Unit
) {
    val singularDropdownChoices = listOf("Day", "Week")
    val pluralDropdownChoices = singularDropdownChoices.map { "${it}s" }

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Repeat every", fontSize = 22.sp)
        Spacer(modifier = Modifier.width(16.dp))
        TextField(
            value = occurrence.interval.toString(),
            onValueChange = {
                onChange(occurrence.copy(interval = it.toInt()))
            },
            singleLine = true,
            modifier = Modifier.width(60.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(10),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.width(10.dp))
        Rocker(rockUp = {
            onChange(occurrence.copy(interval = occurrence.interval + 1))
        }, rockDown = {
            onChange(
                occurrence.copy(interval = if (occurrence.interval > 1) occurrence.interval - 1 else occurrence.interval)
            )
        })
        Spacer(modifier = Modifier.width(10.dp))
        SelectField(
            selected = occurrence.unit,
            modifier = Modifier.width(115.dp),
            dropdownChoices = if (occurrence.interval > 1) pluralDropdownChoices else singularDropdownChoices,
            contentDescription = "Select weekday",
            onChange = { onChange(occurrence.copy(unit = it)) }
        )
    }
}

@Composable
fun WeekDaySelection(
    modifier: Modifier = Modifier,
    selected: List<Weekday>,
    onChange: (List<Weekday>) -> Unit
) {
    val weekdays = listOf<Weekday>(
        Weekday(display = "S", "Sunday"),
        Weekday("M", "Monday"),
        Weekday("T", "Tuesday"),
        Weekday("W", "Wednesday"),
        Weekday("T", "Thursday"),
        Weekday("F", "Friday"),
        Weekday("S", "Saturday")
    )
    val weekdayChoices = remember {
        mutableStateMapOf<Weekday, Boolean>(
            *weekdays.map { Pair<Weekday, Boolean>(it, false) }.toTypedArray()
        )
    }
    selected.forEach { weekdayChoices[it] = true }
    Column(modifier) {
        Text(text = "Repeat on", fontSize = 22.sp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
        ) {
            for (weekday in weekdays) {
                RoundedCheckbox(
                    value = weekday.display,
                    isChecked = weekdayChoices[weekday] ?: false
                ) {
                    weekdayChoices[weekday] = it
                    onChange(
                        weekdayChoices.filter { choice -> choice.value }
                            .map { choice -> choice.key }
                    )

                }
            }
        }
    }
}

@Composable
fun Rocker(
    modifier: Modifier = Modifier.heightIn(max = 75.dp),
    rockUp: () -> Unit,
    rockDown: () -> Unit
) {
    Box(modifier) {
        Column {
            IconButton(onClick = rockUp) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Up rocker",
                )

            }
            IconButton(onClick = rockDown) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Down rocker",
                )
            }

        }
    }

}

@Composable
fun SelectField(
    selected: String,
    modifier: Modifier = Modifier,
    dropdownChoices: List<String>,
    contentDescription: String = "Select option",
    onChange: (String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box(modifier) {
        TextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = contentDescription,
                    )
                }
            },
            shape = RoundedCornerShape(10),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            dropdownChoices.forEach {
                DropdownMenuItem(text = { Text(it, fontSize = 18.sp) }, onClick = { onChange(it) })
            }
        }
    }
}

@Composable
fun RoundedCheckbox(
    isChecked: Boolean,
    value: String,
    modifier: Modifier = Modifier,
    size: Float = 30f,
    checkedColor: Color = MaterialTheme.colorScheme.primary,
    uncheckedColor: Color = Color.White,
    onValueChange: (Boolean) -> Unit
) {


    val checkboxColor: Color by animateColorAsState(
        if (isChecked) checkedColor else uncheckedColor,
        label = "RoundedCheckboxAnimation"
    )
    val textColor: Color by animateColorAsState(
        if (isChecked) uncheckedColor else checkedColor,
        label = "RoundedCheckboxTextAnimation"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(48.dp)
            .toggleable(
                value = isChecked,
                role = Role.Checkbox,
                onValueChange = onValueChange
            )
    ) {
        Box(
            modifier = Modifier
                .size(size.dp)
                .aspectRatio(1f)
                .background(color = checkboxColor, shape = CircleShape)
                .border(width = 1.5.dp, color = checkedColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(value, color = textColor)
        }

    }
}