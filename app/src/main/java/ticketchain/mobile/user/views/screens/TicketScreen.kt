package ticketchain.mobile.user.views.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import ticketchain.mobile.user.controllers.SnackbarController
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.ui.theme.transparentBlue
import ticketchain.mobile.user.utils.toTimeString
import ticketchain.mobile.user.views.partials.AlertNotice
import ticketchain.mobile.user.views.partials.SplitLayout
import ticketchain.mobile.user.views.partials.TicketButton
import ticketchain.mobile.user.views.screens.dashboard.Greeter
import ticketchain.mobile.user.views.screens.drawers.DashboardDrawer
import ticketchain.mobile.user.views.screens.headers.DashboardHeader
import kotlin.math.roundToInt

@ExperimentalCoilApi
@ExperimentalAnimationApi
object TicketScreen : ApplicationScreen {
    override val route = "dashboard.ticket"
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
        LaunchedEffect(state.myTicket) {
            if (state.myTicket == null) {
                navController.backQueue.clear()
                navController.navigate(DashboardScreen.route)
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState!!)
        ) {
            val alert = state.alert ?: false

            Greeter(state, modifier = Modifier.padding(bottom = if (alert) 0.dp else 30.dp))
            if (alert) {
                AlertNotice()
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(transparentBlue)
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = if (state.myTicket != null) "${state.myTicket}" else "-",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = "My Ticket",
                        tint = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = "My Ticket",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.secondaryVariant
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(260.dp)
                    .padding(vertical = if (alert) 30.dp else 40.dp)
            ) {
                TicketButton(
                    text = "Show Ticket".uppercase(),
                    onClick = {
                        navController.navigate(ScanScreen.route)
                    },
                    modifier = Modifier.padding(10.dp),
                    icon = Icons.Default.QrCode
                )
            }

            SplitLayout(
                leftTopText = state.currentTicket?.toString() ?: "-",
                leftBottomText = "Current Ticket",
                leftIcon = Icons.Default.ConfirmationNumber,
                rightTopText = state.waitTime?.toTimeString() ?: "-",
                rightBottomText = "Wait time",
                rightIcon = Icons.Default.Schedule,
                paddingVertical = 10.dp,
                color = MaterialTheme.colors.secondary.copy(alpha = 0.2f)
            )

            Column(
                modifier = Modifier
                    .padding(top = if (alert) 40.dp else 60.dp)
                    .width(260.dp)
            ) {
                TicketButton(
                    text = "DISCARD TICKET".uppercase(),
                    onClick = {
                        state.myTicket = null
                    },
                    color = MaterialTheme.colors.secondaryVariant,
                    icon = Icons.Default.BookmarkRemove
                )
            }
        }
    }
}