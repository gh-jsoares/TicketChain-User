package ticketchain.mobile.user.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ticketchain.mobile.user.data.Notification
import ticketchain.mobile.user.data.Theme
import ticketchain.mobile.user.data.Widget

class AppState {

    var loaded: Boolean by mutableStateOf(false)

    var name: String? by mutableStateOf(null)
    var currentTicket: String? by mutableStateOf(null)
    val widgets: MutableList<Widget> = mutableStateListOf()
    var theme: Theme by mutableStateOf(Theme.DARK) // Default
    val notifications: MutableList<Notification> = mutableStateListOf()
    var notificationsEnabled: Boolean? by mutableStateOf(null)
}