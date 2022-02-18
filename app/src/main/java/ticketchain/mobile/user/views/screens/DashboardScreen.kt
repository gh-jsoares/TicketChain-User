package ticketchain.mobile.user.views.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ticketchain.mobile.user.controllers.SnackbarController
import ticketchain.mobile.user.data.Theme
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.views.partials.AlertNotice
import ticketchain.mobile.user.views.partials.TicketButton
import ticketchain.mobile.user.views.screens.dashboard.Greeter
import ticketchain.mobile.user.views.screens.drawers.DashboardDrawer
import ticketchain.mobile.user.views.screens.headers.DashboardHeader

@ExperimentalAnimationApi
object DashboardScreen : ApplicationScreen {
    override val route = "dashboard"
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
    override fun Bottom(accountService: AccountService, navController: NavHostController, scaffoldState: ScaffoldState) {
        val coroutineScope = rememberCoroutineScope()
        var enabled by remember { mutableStateOf(true) }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(3.dp, object : Shape {
                    override fun createOutline(
                        size: Size,
                        layoutDirection: LayoutDirection,
                        density: Density
                    ): Outline {
                        return Outline.Rectangle(
                            Rect(
                                -10f,
                                0f,
                                size.width + 10f,
                                size.height + 10f
                            )
                        )
                    }
                })
                .background(color = MaterialTheme.colors.secondary)
        ) {
            TicketButton(
                text = "Request Ticket".uppercase(),
                onClick = {
                    enabled = false
                    coroutineScope.launch {
                        try {
                            accountService.requestTicket()
                        } catch(e: Exception) {
                            enabled = true
                        }
                    }
                },
                icon = Icons.Default.BookmarkAdd,
                small = true,
                enabled = enabled,
                width = 260.dp,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
            )
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState!!)
        ) {
            val alert = state.alert ?: false
            Greeter(state)
            if (alert) {
                AlertNotice()
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                //.padding(bottom = 100.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val widgetsCount = state.widgets.size
                    state.widgets.forEachIndexed { index, widget->
                        widget.DrawWidget(state, alert = alert, state.theme == Theme.DARK)
                        if (index < widgetsCount - 1) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}