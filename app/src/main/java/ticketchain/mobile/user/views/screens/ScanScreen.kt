package ticketchain.mobile.user.views.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import ticketchain.mobile.user.controllers.SnackbarController
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.ui.theme.transparentBlue
import ticketchain.mobile.user.utils.QRCodeUtils
import ticketchain.mobile.user.views.partials.SplitLayout
import ticketchain.mobile.user.views.partials.TicketButton
import ticketchain.mobile.user.views.partials.TicketImage
import ticketchain.mobile.user.views.screens.drawers.DashboardDrawer
import ticketchain.mobile.user.views.screens.headers.DashboardHeader

@ExperimentalCoilApi
@ExperimentalAnimationApi
object ScanScreen : ApplicationScreen {
    override val route = "dashboard.scan"
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

        val ticketNumber = state.myTicket
        val ticket = if (ticketNumber != null) "$ticketNumber" else "-"
        LaunchedEffect(ticketNumber) {
            if (ticketNumber == null) {
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
            ScreenTitle(title = "Scan", icon = Icons.Default.QrCode)

            SplitLayout(
                leftTopText = ticket,
                leftBottomText = "My Ticket",
                leftIcon = Icons.Default.ConfirmationNumber,
                rightIcon = Icons.Default.Storefront,
                rightTopText = "3",
                rightBottomText = "Counter",
                paddingVertical = 10.dp,
                color = transparentBlue
            )

            TicketImage(
                data = QRCodeUtils.generateQRCodeUrl("${state.name}:$ticket"),
                contentDescription = ticket,
                modifier = Modifier
                    .padding(vertical = 60.dp)
                    .height(260.dp)
                    .width(260.dp)
                    .clip(MaterialTheme.shapes.large)
                    .border(
                        width = 6.dp,
                        color = MaterialTheme.colors.primary,
                        shape = MaterialTheme.shapes.large
                    )
            )

            Column(
                modifier = Modifier
                    .width(260.dp)
            ) {
                TicketButton(
                    text = "DISCARD TICKET".uppercase(),
                    onClick = {
                        accountService.state.myTicket = null
                    },
                    color = MaterialTheme.colors.secondaryVariant,
                    icon = Icons.Default.BookmarkRemove
                )
            }
        }
    }
}