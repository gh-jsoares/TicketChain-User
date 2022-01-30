package ticketchain.mobile.user.views.screens.dashboard.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ticketchain.mobile.user.ui.theme.CrowdedColor
import ticketchain.mobile.user.ui.theme.NotCrowdedColor
import ticketchain.mobile.user.ui.theme.SemiCrowdedColor
import ticketchain.mobile.user.views.screens.dashboard.widgets.table.TableHeader
import ticketchain.mobile.user.views.screens.dashboard.widgets.table.TableRow
import ticketchain.mobile.user.views.screens.dashboard.widgets.table.TableSubHeader
import java.time.LocalTime
import kotlin.math.roundToInt

data class TableDataRow(val hourStart: Int, val hourEnd: Int, val peopleAmount: Int)

@Composable
fun TicketAverageTable(rowHeight: Dp, modifier: Modifier) {
    val rows = listOf(
        TableDataRow(hourStart = 8, hourEnd = 10, peopleAmount = 56),
        TableDataRow(hourStart = 10, hourEnd = 12, peopleAmount = 19),
        TableDataRow(hourStart = 12, hourEnd = 14, peopleAmount = 67),
        TableDataRow(hourStart = 14, hourEnd = 16, peopleAmount = 38),
        TableDataRow(hourStart = 16, hourEnd = 18, peopleAmount = 24),
    )

    val currentHour = LocalTime.now().hour

    Column(
        modifier = modifier
    ) {
        TableHeader()
        TableSubHeader()

        val maxPeopleAmount = rows.maxOf { it.peopleAmount } / 100f
        rows.forEach {
            val chosenColor = when ((it.peopleAmount / maxPeopleAmount).roundToInt()) {
                in 80..100 -> CrowdedColor
                in 50..80 -> SemiCrowdedColor
                else -> NotCrowdedColor
            }

            TableRow(
                height = rowHeight,
                timeSlot = "${it.hourStart}h - ${it.hourEnd}h",
                peopleAmount = it.peopleAmount,
                color = chosenColor,
                active = currentHour in it.hourStart until it.hourEnd
            )
        }

        TableLegend()
    }
}

@Composable
fun ColorLegend(color: Color, legend: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(end = 10.dp)
                .clip(shape = CircleShape)
                .background(color = color)
                .size(10.dp)
        )
        Text(
            text = legend,
            color = color,
            fontSize = 14.sp
        )
    }
}

@Composable
fun TableLegend() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp)
            .alpha(0.7f)
    ) {
        ColorLegend(color = NotCrowdedColor, legend = "Not crowded")
        ColorLegend(color = SemiCrowdedColor, legend = "Semi-crowded")
        ColorLegend(color = CrowdedColor, legend = "Crowded")
    }
}
