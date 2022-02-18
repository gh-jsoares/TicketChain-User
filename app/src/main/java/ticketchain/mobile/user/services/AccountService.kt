package ticketchain.mobile.user.services

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ticketchain.mobile.user.api.TicketChainApi
import ticketchain.mobile.user.data.*
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.storage.UserDataService
import ticketchain.mobile.user.views.screens.DashboardScreen
import ticketchain.mobile.user.views.screens.NotificationsScreen
import ticketchain.mobile.user.views.screens.SettingsScreen
import ticketchain.mobile.user.views.screens.TicketScreen

class AccountService(
    val api: TicketChainApi,
    private val userDataService: UserDataService,
    val state: AppState
) {

    lateinit var navController: NavController

    /*
     * API STUFF
     */

    @ExperimentalAnimationApi
    suspend fun requestTicket() {
        val response = api.requestTicket(state.name ?: "")
        state.myTicket = response.ticket.Id

        navController.backQueue.clear()
        navController.navigate(TicketScreen.route)
    }

    suspend fun countTickets() {
        val response = api.countTickets()
        state.countTickets = response.ticketCount
        state.waitTime = response.waitTime
    }

    suspend fun getCurrentTicket() {
        val response = api.getCurrentTicket()
        state.currentTicket = response.currentTicket
    }

    suspend fun hasIssues() {
        val response = api.hasIssues()
        state.alert = response.hasIssues
    }

    suspend fun hasTicket() {
        val response = api.hasTicket(state.name ?: "")
        if (!response.hasTicket) {
            state.myTicket = null
        }
    }

    /*
     * ACCOUNT STUFF
     */

    suspend fun loadData() {
        val userData = userDataService.getUserData()
        state.name = userData.name
        state.theme = Theme.fromValue(userData.theme)
        state.widgets.clear()
        state.widgets.addAll(userData.widgetsList.map { Widget.fromValue(it)!! })
        state.myTicket = userData.currentTicket.toIntOrNull()
        state.notificationsEnabled = userData.notificationsEnabled
        state.notifications.clear()
        state.notifications.addAll(userData.notificationsList.map {
            Notification(
                triggerType = NotificationTrigger.fromValue(it.type),
                threshold = it.threshold,
                enabled = it.enabled,
                frequency = Notification.frequencyFromString(it.weekdays),
                interval = if (it.startHour > -1 && it.endHour > -1) NotificationInterval(
                    startHour = it.startHour,
                    endHour = it.endHour
                ) else null
            )
        })

        state.loaded = true
    }

    fun isFirstSetup(): Boolean {
        return state.loaded && state.name.isNullOrBlank()
    }

    suspend fun changeTheme(theme: Theme) {
        state.theme = theme

        userDataService.storeUserData(
            theme = state.theme.ordinal,
        )
    }

    suspend fun toggleNotifications(value: Boolean): Boolean {
        userDataService.storeUserData(
            notificationsEnabled = value
        )

        state.notificationsEnabled = value

        return value
    }

    suspend fun deleteNotification(index: Int) {
        state.notifications.removeAt(index)

        userDataService.storeUserData(
            notifications = state.notifications
        )
    }

    suspend fun toggleNotification(index: Int): Boolean {
        val enabled = state.notifications[index].toggle()

        userDataService.storeUserData(
            notifications = state.notifications
        )

        return enabled
    }

    @ExperimentalAnimationApi
    suspend fun createNotification(
        triggerType: NotificationTrigger,
        threshold: Int,
        frequency: List<Weekday>,
        interval: NotificationInterval?
    ) {
        val notification = Notification(
            triggerType = triggerType,
            threshold = threshold,
            enabled = true,
            frequency = frequency,
            interval = interval
        )

        state.notifications.add(notification)
        userDataService.storeUserData(
            notifications = state.notifications
        )

        navController.popBackStack(route = NotificationsScreen.route, inclusive = false)
    }

    @ExperimentalAnimationApi
    suspend fun addWidget(widget: Widget) {
        state.widgets.add(widget)

        userDataService.storeUserData(
            widgets = state.widgets
        )

        navController.popBackStack(route = SettingsScreen.route, inclusive = false)
    }

    @ExperimentalAnimationApi
    suspend fun setup(name: String, age: Int, occupation: Int) {
        state.name = name
        state.widgets.clear()

        calculateWidgets(age, occupation)
        state.notificationsEnabled = true
        state.notifications.clear()

        userDataService.storeUserData(
            name = name,
            age = age,
            occupation = occupation,
            widgets = state.widgets,
            theme = state.theme.ordinal,
            notifications = state.notifications,
            notificationsEnabled = state.notificationsEnabled
        )

        navController.navigate(DashboardScreen.route)
    }

    private fun calculateWidgets(age: Int, occupation: Int) {
        when (Occupation.fromValue(occupation)) {
            Occupation.WORKER, Occupation.STUDENT_WORKER -> state.widgets.add(Widget.AMOUNT_AND_TIME)
            Occupation.STUDENT -> state.widgets.add(Widget.AVERAGE_TABLE)
            Occupation.RETIRED -> state.widgets.add(Widget.RECOMMENDED_HOUR)
            else -> Unit
        }
    }
}