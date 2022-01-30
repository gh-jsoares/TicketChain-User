package ticketchain.mobile.user.ui.theme

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import ticketchain.mobile.user.data.Theme

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = WashedBlue,
    primaryVariant = HotBlue,
    secondary = DarkBlue,
    secondaryVariant = DarkGray,
    background = NavyBlue,
    surface = NavyBlue,
    error = TransparentLightRed,
    onPrimary = White,
    onSecondary = White,
    onBackground = LightBlue,
    onSurface = LightGray,
    onError = HotRed,
)

@Composable
fun TicketChainUserTheme(
    theme: Theme,
    content: @Composable () -> Unit
) {
    val colors = when (theme) {
        Theme.DARK -> DarkColorPalette
        Theme.LIGHT -> lightColors()
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}