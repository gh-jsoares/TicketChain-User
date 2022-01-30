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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ticketchain.mobile.user.controllers.SnackbarController
import ticketchain.mobile.user.data.Widget
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.ui.theme.TransparentBlack
import ticketchain.mobile.user.views.partials.AlertNotice
import ticketchain.mobile.user.views.partials.SplitLayout
import ticketchain.mobile.user.views.partials.TicketButton
import ticketchain.mobile.user.views.screens.dashboard.Greeter
import ticketchain.mobile.user.views.screens.dashboard.widgets.TicketAverageTable
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
    override fun Bottom(navController: NavHostController, scaffoldState: ScaffoldState) {
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
                    /*TODO*/
                    navController.backQueue.clear()
                    navController.navigate(TicketScreen.route)
                },
                icon = Icons.Default.BookmarkAdd,
                small = true,
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
            val alert = true
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
                        widget.DrawWidget(alert = alert)
                        if (index < widgetsCount - 1) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}