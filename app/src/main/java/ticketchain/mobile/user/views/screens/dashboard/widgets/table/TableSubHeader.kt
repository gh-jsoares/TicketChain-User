package ticketchain.mobile.user.views.screens.dashboard.widgets.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ticketchain.mobile.user.ui.theme.TransparentBlack

@Composable
fun TableSubHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = MaterialTheme.colors.secondaryVariant)
            .fillMaxWidth()
            .height(40.dp)
            .fillMaxHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.5f)
                .fillMaxHeight()
        ) {
            Text(text = "Time Slot")
        }

        Divider(
            color = TransparentBlack.copy(alpha = 0.5f),
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(text = "Amount of People")
        }
    }
}