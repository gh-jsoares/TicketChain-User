package ticketchain.mobile.user.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ticketchain.mobile.user.ui.theme.TransparentBlack
import ticketchain.mobile.user.views.partials.SplitLayout
import ticketchain.mobile.user.views.screens.dashboard.widgets.TicketAverageTable

enum class Widget {
    AVERAGE_TABLE,
    AMOUNT_AND_TIME,
    RECOMMENDED_HOUR;

    override fun toString(): String {
        return name.lowercase().replace("_", " ").capitalize(Locale.current)
    }

    // 0 -> table
    // 1 -> tickets and wait time
    // 2 -> Recommended hour
    @Composable
    fun DrawWidget(alert: Boolean) {
        when (this) {
            AVERAGE_TABLE -> {
                TicketAverageTable(
                    rowHeight = if (alert) 60.dp else 70.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            AMOUNT_AND_TIME -> {
                SplitLayout(
                    leftTopText = "34",
                    leftBottomText = "Queued Tickets",
                    leftIcon = Icons.Default.ConfirmationNumber,
                    rightTopText = "30m",
                    rightBottomText = "Wait time",
                    rightIcon = Icons.Default.Schedule,
                    paddingVertical = 10.dp,
                    color = MaterialTheme.colors.secondary.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            RECOMMENDED_HOUR -> {
                Column(
                    modifier = Modifier
                        .background(color = TransparentBlack.copy(alpha = 0.3f))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule, contentDescription = null,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            Text(
                                text = "Next recommended hour",
                            )
                        }
                        Text(
                            text = "10h",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                    Divider(
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.People, contentDescription = null,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            Text(
                                text = "Average People",
                            )
                        }
                        Text(
                            text = "19",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    }


    companion object {
        fun fromValue(value: Int): Widget? {
            return values().getOrNull(value)
        }
    }
}