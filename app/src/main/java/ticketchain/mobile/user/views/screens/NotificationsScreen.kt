package ticketchain.mobile.user.views.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ticketchain.mobile.user.controllers.SnackbarController
import ticketchain.mobile.user.data.*
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.ui.theme.TransparentBlack
import ticketchain.mobile.user.views.partials.ConfirmDialog
import ticketchain.mobile.user.views.partials.TicketButton
import ticketchain.mobile.user.views.screens.drawers.DashboardDrawer
import ticketchain.mobile.user.views.screens.headers.DashboardHeader

@ExperimentalAnimationApi
object NotificationsScreen : ApplicationScreen {
    override val route = "notifications"
    override val hasDrawer = true
    private var scrollState: ScrollState? = null

    @Composable
    override fun drawer(
        navController: NavHostController,
        scaffoldState: ScaffoldState
    ): @Composable (ColumnScope.() -> Unit) = {
        DashboardDrawer(navController, scaffoldState)
    }

    @Composable
    override fun Header(scaffoldState: ScaffoldState) {
        if (scrollState == null) {
            scrollState = rememberScrollState()
        }

        DashboardHeader(scaffoldState, scrollState!!.value > 0)
    }

    @Composable
    private fun NotificationRow(
        accountService: AccountService,
        coroutineScope: CoroutineScope,
        notification: Notification,
        selectNotification: (Notification?) -> Unit,
        index: Int,
        background: Color
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = background)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxHeight()
                    .weight(weight = 2f)
            ) {
                Text(
                    text = notification.toString(),
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(

                        imageVector = Icons.Default.Event, contentDescription = null,
                        tint = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .padding(end = 5.dp)
                    )
                    Text(
                        text = notification.frequencyString(),
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colors.secondaryVariant,
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(140.dp)
                    .weight(weight = 2f, fill = false)
            ) {
                var enabled by remember { mutableStateOf(notification.enabled) }
                Checkbox(
                    checked = enabled,
                    onCheckedChange = {
                        coroutineScope.launch {
                            enabled = accountService.toggleNotification(index)
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        //uncheckedColor = MaterialTheme.colors.surface
                        checkedColor = MaterialTheme.colors.primaryVariant,
                        checkmarkColor = MaterialTheme.colors.onPrimary
                    ),
                )
                IconButton(
                    onClick = { /*TODO*/ },
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit, contentDescription = null,
                        tint = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .size(28.dp)
                    )
                }
                IconButton(
                    onClick = {
                        selectNotification(notification)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete, contentDescription = null,
                        tint = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .size(28.dp)
                    )
                }
            }
        }
    }

    @Composable
    override fun Body(
        navController: NavController,
        snackbarController: SnackbarController,
        accountService: AccountService,
        state: AppState
    ) {
        if (scrollState == null) {
            scrollState = rememberScrollState()
        }

        var enabled = state.notificationsEnabled ?: true
        val coroutineScope = rememberCoroutineScope()

        val (selectedNotification, selectNotification) = remember { mutableStateOf(null as Notification?) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState!!)
        ) {

            ScreenTitle(
                title = "Notifications", icon = Icons.Default.Notifications,
                verticalPadding = 10.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (enabled) "On".uppercase() else "Off".uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = if (enabled) MaterialTheme.colors.primary else Color.Unspecified,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Switch(
                        checked = enabled,
                        onCheckedChange = {
                            coroutineScope.launch {
                                enabled = accountService.toggleNotifications(!enabled)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colors.onPrimary,
                            checkedTrackColor = MaterialTheme.colors.primaryVariant,
                            checkedTrackAlpha = 1f,
                            uncheckedThumbColor = MaterialTheme.colors.onPrimary,
                        ),
                    )
                }
            }

            state.notifications.forEachIndexed { index, notification ->
                NotificationRow(
                    coroutineScope = coroutineScope,
                    notification = notification,
                    index = index,
                    selectNotification = selectNotification,
                    accountService = accountService,
                    background = if (index % 2 == 0) TransparentBlack.copy(alpha = 0.1f) else Color.Unspecified
                )
            }


            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(260.dp)
            ) {
                TicketButton(
                    text = "Create Notification".uppercase(),
                    onClick = {
                        navController.navigate(NewNotificationScreen.route)
                    },
                    color = MaterialTheme.colors.secondaryVariant,
                    icon = Icons.Default.NotificationAdd
                )
            }
        }

        AnimatedVisibility(visible = selectedNotification != null) {
            ConfirmDialog(
                text = "Are you sure you want to delete this notification?",
                onClose = {
                    selectNotification(null)
                },
                onSave = {
                    val index = state.notifications.indexOf(selectedNotification)
                    selectNotification(null)
                    coroutineScope.launch {
                        accountService.deleteNotification(index)
                    }
                })

        }
    }
}