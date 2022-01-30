package ticketchain.mobile.user.views.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ticketchain.mobile.user.R
import ticketchain.mobile.user.controllers.SnackbarController
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.views.partials.TicketButton
import ticketchain.mobile.user.views.screens.headers.LogoLargeHeader


@ExperimentalAnimationApi
object InitialScreen : ApplicationScreen {

    override val route = "initial"

    @Composable
    override fun Header(scaffoldState: ScaffoldState) {
        LogoLargeHeader()
    }

    @Composable
    override fun Body(
        navController: NavController,
        snackbarController: SnackbarController,
        accountService: AccountService,
        state: AppState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
        ) {
            // Text column
            Column(
                modifier = Modifier.padding(bottom = 50.dp)
            ) {
                Text(
                    text = stringResource(R.string.initial_description_line1),
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp
                )
                Text(
                    text = stringResource(R.string.initial_description_line2),
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp
                )
                Text(
                    text = stringResource(R.string.initial_description_line3),
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp
                )
            }

            TicketButton(
                text = stringResource(R.string.initial_button_setup),
                color = MaterialTheme.colors.secondary,
                onClick = {
                    navController.navigate(RegisterScreen.route)
                },
                icon = Icons.Default.ManageAccounts
            )
        }
    }
}
