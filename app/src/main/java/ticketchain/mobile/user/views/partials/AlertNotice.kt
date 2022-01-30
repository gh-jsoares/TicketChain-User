package ticketchain.mobile.user.views.partials

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ticketchain.mobile.user.ui.theme.TransparentDarkRed

@Composable
fun AlertNotice() {
    val alerts = listOf(
        "A worker has reported an issue.",
        "Wait times may be greater than usual!"
    )

    val cornerRadius = 5.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 15.dp)
            .background(
                color = MaterialTheme.colors.error,
                shape = RoundedCornerShape(cornerRadius)
            )
            .border(
                width = 2.dp,
                color = TransparentDarkRed,
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .width(300.dp)
    ) {
        alerts.forEach { alert ->
            Text(
                text = alert,
                color = MaterialTheme.colors.onError,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
