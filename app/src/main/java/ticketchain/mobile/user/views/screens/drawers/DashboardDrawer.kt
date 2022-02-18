package ticketchain.mobile.user.views.screens.drawers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import ticketchain.mobile.user.R
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.ui.theme.TransparentBlack
import ticketchain.mobile.user.views.partials.TicketButton
import ticketchain.mobile.user.views.partials.TicketChainLogo
import ticketchain.mobile.user.views.screens.*

data class MenuItem(val displayName: String, val destination: String)

@ExperimentalAnimationApi
@Composable
fun DashboardDrawer(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    accountService: AccountService
) {
    val coroutineScope = rememberCoroutineScope()
    var enabled by remember { mutableStateOf(true) }

    IconButton(
        onClick = {
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        },
        modifier = Modifier
            .padding(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Close, contentDescription = "close menu",
            modifier = Modifier
                .size(30.dp)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, bottom = 50.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TicketChainLogo(
                modifier = Modifier.padding(vertical = 50.dp)
            )

            TicketMenu(navController, scaffoldState, accountService)
        }
        AnimatedVisibility(visible = accountService.state.myTicket == null) {
            TicketButton(
                onClick = {
                    enabled = false
                    coroutineScope.launch {
                        try {
                            scaffoldState.drawerState.close()
                            accountService.requestTicket()
                        } catch (e: Exception) {
                        }
                        enabled = true
                    }
                },
                text = stringResource(R.string.drawer_button_request_ticket).uppercase(),
                icon = Icons.Default.BookmarkAdd,
                enabled = enabled,
                modifier = Modifier
                    .widthIn(max = 200.dp)
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun TicketMenu(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    accountService: AccountService
) {
    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val menuItems = listOf(
        MenuItem("Dashboard", DashboardScreen.route),
        MenuItem("Notifications", NotificationsScreen.route),
        MenuItem("Settings", SettingsScreen.route),
    )

    //var active by remember { mutableStateOf(currentRoute) }

    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        menuItems.forEach { item ->
            //val isActive = currentRoute == item.destination
            val isActive = currentRoute?.startsWith(item.destination) ?: false
            val color =
                if (isActive)
                    MaterialTheme.colors.primary
                else
                    Color.Unspecified

            Text(
                text = item.displayName,
                textAlign = TextAlign.Right,
                color = color,
                fontSize = 18.sp,
                modifier = Modifier
                    .clickable {
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                            if (item.destination == DashboardScreen.route && accountService.state.myTicket != null) {
                                navController.navigate(TicketScreen.route)
                            } else {
                                navController.navigate(item.destination)
                            }
                        }
                    }
                    .then(
                        if (isActive)
                            Modifier.background(color = TransparentBlack.copy(alpha = 0.2f))
                        else Modifier
                    )
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .then(
                        if (isActive)
                            Modifier
                                .drawBehind {
                                    val width = 50f
                                    val radius = 30f
                                    drawRoundRect(
                                        color = color,
                                        cornerRadius = CornerRadius(radius),
                                        size = size.copy(width = width),
                                        topLeft = Offset(size.width - width / 2, 0f)
                                    )
                                }
                        else Modifier
                    )
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 20.dp)
            )
        }
    }
}
