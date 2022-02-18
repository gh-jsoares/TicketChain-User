package ticketchain.mobile.user.views.screens

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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ticketchain.mobile.user.controllers.SnackbarController
import ticketchain.mobile.user.data.*
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.views.partials.TicketButton
import ticketchain.mobile.user.views.partials.TicketDropdownMenu
import ticketchain.mobile.user.views.screens.drawers.DashboardDrawer
import ticketchain.mobile.user.views.screens.headers.DashboardHeader

@ExperimentalAnimationApi
object AddWidgetScreen : ApplicationScreen {
    override val route = "settings.widget.add"
    override val hasDrawer = true
    private var scrollState: ScrollState? = null

    @Composable
    override fun drawer(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        accountService: AccountService
    ): @Composable (ColumnScope.() -> Unit) = {
        DashboardDrawer(navController, scaffoldState, accountService)
    }

    @Composable
    override fun Header(scaffoldState: ScaffoldState) {
        if (scrollState == null) {
            scrollState = rememberScrollState()
        }

        DashboardHeader(scaffoldState, scrollState!!.value > 0)
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

        val coroutineScope = rememberCoroutineScope()
        var widget by remember { mutableStateOf(Widget.fromValue(0)) }
        var enabled by remember { mutableStateOf(true) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState!!)
        ) {

            ScreenTitle(title = "Settings", icon = Icons.Default.Settings, verticalPadding = 5.dp)

            TicketDropdownMenu(
                label = "Widget",
                defaultValue = widget?.ordinal ?: -1,
                onValueChange = { index, _ ->
                    widget = Widget.fromValue(index)
                },
                items = Widget.values().map { it.toString() },
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 25.dp)
            )

            Divider()

            Text(text = "Preview", modifier = Modifier.padding(top = 10.dp))
            
            AnimatedVisibility(visible = widget != null,
                modifier = if (widget != Widget.AVERAGE_TABLE) Modifier.scale(0.7f)
                            else Modifier.scale(0.7f).offset(y= (-90).dp)
            ) {
                widget?.DrawWidget(state, alert = false, state.theme == Theme.DARK)
            }

            Column(
                modifier = if (widget != Widget.AVERAGE_TABLE) Modifier.padding(top = 20.dp).width(260.dp)
                    else Modifier.width(260.dp).offset(y=(-90).dp)
            ) {
                TicketButton(
                    enabled = enabled,
                    text = "Add Widget".uppercase(),
                    onClick = {
                        enabled = false
                        coroutineScope.launch {
                            accountService.addWidget(widget!!)
                        }
                    },
                    color = MaterialTheme.colors.primary,
                    icon = Icons.Default.Add
                )
            }
        }
    }
}