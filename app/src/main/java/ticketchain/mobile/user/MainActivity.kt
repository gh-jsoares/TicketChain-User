package ticketchain.mobile.user

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ticketchain.mobile.user.controllers.SnackbarController
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.ui.theme.TicketChainUserTheme
import ticketchain.mobile.user.ui.theme.TransparentBlack
import ticketchain.mobile.user.views.partials.AppSnackbar
import ticketchain.mobile.user.views.partials.animatedComposable
import ticketchain.mobile.user.views.screens.*
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalAnimationApi
@ExperimentalCoilApi
class MainActivity : ComponentActivity() {

    // Register all screens here
    // First screen is default
    private val screens: List<ApplicationScreen> = listOf(
        LoadingScreen,
        InitialScreen,
        RegisterScreen,
        DashboardScreen,
        ScanScreen,
        TicketScreen,
        NotificationsScreen,
        NewNotificationScreen,
        SettingsScreen,
        AddWidgetScreen,
    )

    @Inject
    lateinit var state: AppState

    @Inject
    lateinit var accountService: AccountService

    private lateinit var snackbarController: SnackbarController

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // force portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val scaffoldState = rememberScaffoldState()

            accountService.navController = navController

            snackbarController =
                SnackbarController(scaffoldState.snackbarHostState, lifecycleScope)

            val currentScreen = screens.find { it.route == currentRoute }

            val drawer =
                if (currentScreen?.hasDrawer == true)
                    currentScreen.drawer(navController, scaffoldState, accountService)
                else null

            // When testing drawer, open automatically
/*
            drawer?.let {
                LaunchedEffect(scaffoldState.drawerState) {
                    scaffoldState.drawerState.open()
                }
            }
*/

            var job: Job? by remember { mutableStateOf(null) }

            LaunchedEffect(null) {
                if (job != null) {
                    job?.cancelAndJoin()
                }
                job = launch {
                    while (true) {
                        try {
                            accountService.countTickets()
                            accountService.hasTicket()
                            accountService.hasIssues()
                            accountService.getCurrentTicket()
                        } catch(e: Exception) {
                            Log.d("OOPS", e.message ?: "oops error")
                        }
                        delay(1000L)
                    }
                }
            }

            TicketChainUserTheme(theme = state.theme) {
                Scaffold(
                    topBar = { currentScreen?.Header(scaffoldState) },
                    scaffoldState = scaffoldState,
                    drawerContent = drawer,
                    drawerScrimColor = TransparentBlack,
                    drawerBackgroundColor = MaterialTheme.colors.secondary,
                    bottomBar = { currentScreen?.Bottom(accountService, navController, scaffoldState) },
                    snackbarHost = { scaffoldState.snackbarHostState }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = screens.first().route
                        ) {

                            screens.forEach { screen ->
                                animatedComposable(screen.route) {
                                    screen.Body(
                                        navController,
                                        snackbarController,
                                        accountService,
                                        state
                                    )
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .fillMaxSize()
                                .zIndex(1000f),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AppSnackbar(snackbarHostState = scaffoldState.snackbarHostState)
                        }
                    }
                }
            }
        }
    }
}