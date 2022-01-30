package ticketchain.mobile.user.views.screens.dashboard.widgets.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ticketchain.mobile.user.modifiers.Border
import ticketchain.mobile.user.modifiers.border
import ticketchain.mobile.user.ui.theme.RowActiveColor

@Composable
fun TableRow(
    height: Dp,
    timeSlot: String,
    peopleAmount: Int,
    color: Color,
    active: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                color = if (active) RowActiveColor else Color.Unspecified
            )
            .border(
                bottom = Border(
                    strokeWidth = 1.dp,
                    color = MaterialTheme.colors.secondaryVariant
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.5f)
                .fillMaxHeight()
        ) {
            Icon(
                imageVector = Icons.Default.Schedule, contentDescription = "time-slot-$timeSlot",
                tint = color,
                modifier = Modifier
            )
            Text(
                text = timeSlot,
                color = color
            )
        }

        Divider(
            color = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Icon(
                imageVector = Icons.Default.People,
                contentDescription = "people-amount-$peopleAmount",
                tint = color,
                modifier = Modifier
            )
            Text(
                text = "$peopleAmount",
                color = color
            )
        }
    }
}