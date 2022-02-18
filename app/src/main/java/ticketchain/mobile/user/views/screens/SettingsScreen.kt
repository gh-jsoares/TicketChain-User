package ticketchain.mobile.user.views.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ticketchain.mobile.user.controllers.SnackbarController
import ticketchain.mobile.user.data.Theme
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.ui.theme.TransparentBlack
import ticketchain.mobile.user.views.partials.TicketButton
import ticketchain.mobile.user.views.screens.drawers.DashboardDrawer
import ticketchain.mobile.user.views.screens.headers.DashboardHeader
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@ExperimentalAnimationApi
object SettingsScreen : ApplicationScreen {
    override val route = "settings"
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

        var checked by remember { mutableStateOf(state.theme == Theme.DARK) }
        val coroutineScope = rememberCoroutineScope()

        var dragOffset by remember { mutableStateOf(0f) }
        var dragIndex by remember { mutableStateOf<Int?>(null) }

        val offset = 130f
        val draggableState = rememberDraggableState(onDelta = {
            dragOffset += it
            val widgetsCount = state.widgets.size
            if (dragIndex != null && dragOffset != 0f && dragOffset.absoluteValue > offset) {
                // switch with something
                val direction = (dragOffset / dragOffset.absoluteValue).roundToInt()
                if (dragIndex == widgetsCount - 1 && direction == 1) {
                    // last item nothing to go below
                    return@rememberDraggableState
                } else if (dragIndex == 0 && direction == -1) {
                    // first item nothing to go above
                    return@rememberDraggableState
                } else {
                    val widget = state.widgets[dragIndex!!]
                    state.widgets.removeAt(dragIndex!!)
                    state.widgets.add(dragIndex!! + direction, widget)
                    dragOffset = 0f
                    dragIndex = dragIndex!! + direction
                }
            }
        })

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState!!)
        ) {
            ScreenTitle(title = "Settings", icon = Icons.Default.Settings, verticalPadding = 5.dp)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            ) {
                Text(text = "Color Theme", modifier = Modifier.padding(bottom = 10.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (checked) Theme.DARK.name else Theme.LIGHT.name,
                        fontWeight = FontWeight.Bold,
                    )
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            coroutineScope.launch {
                                checked = !checked
                                accountService.changeTheme(if (checked) Theme.DARK else Theme.LIGHT)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = MaterialTheme.colors.onPrimary,
                            uncheckedTrackColor = MaterialTheme.colors.onSurface,
                            checkedThumbColor = MaterialTheme.colors.onPrimary, // when theme changes color also changes
                            checkedTrackColor = MaterialTheme.colors.onSurface
                        ),
                    )
                }
            }
            Divider(modifier = Modifier.padding(vertical = 10.dp))

            // Dashboard Widgets (add + reorder)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Dashboard Widgets (drag to sort)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp, vertical = 15.dp)
                )

                state.widgets.forEachIndexed { index, widget ->
                    val modifier =
                        if (dragIndex == index)
                            Modifier
                                .background(color = TransparentBlack.copy(alpha = 0.5f))
                                .offset {
                                    IntOffset(0, dragOffset.roundToInt())
                                }
                        else Modifier

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.then(
                            Modifier
                                .fillMaxWidth()
                                .draggable(
                                    state = draggableState,
                                    orientation = Orientation.Vertical,
                                    onDragStarted = {
                                        dragIndex = index
                                    },
                                    onDragStopped = {
                                        dragOffset = 0f
                                        dragIndex = null
                                    }
                                )
                                .background(color = if (index % 2 == 0) TransparentBlack.copy(alpha = 0.1f) else Color.Unspecified)
                                .padding(vertical = 10.dp)
                                .padding(start = 25.dp, end = 10.dp)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.DragHandle, contentDescription = null,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            Text(text = "Widget '$widget'")
                        }
                        IconButton(onClick = {
                            state.widgets.removeAt(index)
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    }
                }

                TicketButton(
                    text = "Add Widget".uppercase(),
                    onClick = {
                        navController.navigate(AddWidgetScreen.route)
                    },
                    icon = Icons.Default.Add,
                    small = true,
                    width = 260.dp,
                    color = MaterialTheme.colors.secondaryVariant,
                    modifier = Modifier
                        .padding(vertical = 25.dp)
                )
            }
        }
    }
}