package ticketchain.mobile.user.views.screens.dashboard.widgets.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ticketchain.mobile.user.ui.theme.TransparentBlack

@Composable
fun TableHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = TransparentBlack.copy(alpha = 0.1f))
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.BarChart, contentDescription = "Ticket Average",
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .size(32.dp)
        )

        Text(
            text = "Ticket Average",
            fontWeight = FontWeight.Bold,
        )
    }
}