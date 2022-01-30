package ticketchain.mobile.user.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ticketchain.mobile.user.controllers.SnackbarController
import ticketchain.mobile.user.data.*
import ticketchain.mobile.user.services.AccountService
import ticketchain.mobile.user.state.AppState
import ticketchain.mobile.user.ui.theme.TransparentBlack
import ticketchain.mobile.user.views.partials.RadioButtonRow
import ticketchain.mobile.user.views.partials.TicketButton
import ticketchain.mobile.user.views.partials.TicketMultiDropdownMenu
import ticketchain.mobile.user.views.screens.drawers.DashboardDrawer
import ticketchain.mobile.user.views.screens.headers.DashboardHeader

@ExperimentalAnimationApi
object NewNotificationScreen : ApplicationScreen {
    override val route = "notifications.new"
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
    private fun NumberInputField(
        label: String,
        value: Int?,
        onValueChange: (String) -> Unit,
        fullWidth: Boolean = true,
        modifier: Modifier = Modifier
    ) {
        val widthModifier = if (fullWidth) Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
        else Modifier.padding(horizontal = 10.dp)
        OutlinedTextField(
            label = {
                Text(text = label)
            },
            value = if (value != null) "$value" else "",
            onValueChange = onValueChange,
            modifier = widthModifier.then(modifier),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType =
                KeyboardType.Number
            )
        )
    }

    @Composable
    private fun IntervalInputField(
        label: String,
        value: Int?,
        updateInterval: (Int?) -> Unit,
        modifier: Modifier = Modifier
    ) {
        NumberInputField(label = label, value = value, onValueChange = {
            val tempInt = it.toIntOrNull() ?: -1
            if (tempInt >= 0) {
                updateInterval(tempInt)
            } else {
                updateInterval(null)
            }
        }, fullWidth = false, modifier = modifier)
    }

    @Composable
    private fun RadioButtonInputField(
        value: Int?,
        setValue: (Int?) -> Unit,
    ) {
        NumberInputField(label = "Threshold", value = value, onValueChange = {
            val tempInt = it.toIntOrNull() ?: -1
            if (tempInt >= 0) {
                setValue(tempInt)
            } else {
                setValue(null)
            }
        })
    }

    @Composable
    private fun SectionTitle(title: String, required: Boolean) {
        Text(
            text = (if (required) "*$title" else title).uppercase(),
            color = if (required) MaterialTheme.colors.onPrimary else Color.Unspecified,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
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

        var triggerType by remember { mutableStateOf(NotificationTrigger.TICKET) }

        val (threshold, setThreshold) = remember { mutableStateOf(null as Int?) }
        var frequencyType by remember { mutableStateOf(0) }
        val frequency = remember { mutableStateListOf<Weekday>() }
        var intervalStart by remember { mutableStateOf<Int?>(null) }
        var intervalEnd by remember { mutableStateOf<Int?>(null) }
        var enabled: Boolean by remember { mutableStateOf(false) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState!!)
        ) {

            ScreenTitle(
                title = "New Notification",
                icon = Icons.Default.NotificationAdd,
                verticalPadding = 15.dp
            )

            Column(
                modifier = Modifier
                    .background(color = TransparentBlack.copy(alpha = 0.1f))
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 10.dp)
            ) {
                SectionTitle(title = "Trigger (Less than)", required = true)

                RadioButtonRow(
                    selected = triggerType == NotificationTrigger.TICKET,
                    onClick = {
                        triggerType = NotificationTrigger.TICKET
                    }, label = "Tickets"
                )
                AnimatedVisibility(visible = triggerType == NotificationTrigger.TICKET) {
                    RadioButtonInputField(threshold, setThreshold)
                }

                RadioButtonRow(
                    selected = triggerType == NotificationTrigger.TIME,
                    onClick = {
                        triggerType = NotificationTrigger.TIME
                    }, label = "Wait time (in minutes)"
                )
                AnimatedVisibility(visible = triggerType == NotificationTrigger.TIME) {
                    RadioButtonInputField(threshold, setThreshold)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 10.dp)
            ) {
                SectionTitle(title = "Frequency", required = true)

                RadioButtonRow(
                    selected = frequencyType == 0,
                    onClick = {
                        frequencyType = 0
                        frequency.clear()
                    }, label = "Everyday"
                )

                RadioButtonRow(
                    selected = frequencyType == 1,
                    onClick = {
                        frequencyType = 1
                        frequency.clear()
                    }, label = "Advanced"
                )
                AnimatedVisibility(visible = frequencyType == 1) {
                    TicketMultiDropdownMenu(
                        label = "Days",
                        onValueChange = { _, lbl ->
                            val weekday = Weekday.valueOf(lbl.uppercase())
                            if (frequency.contains(weekday))
                                frequency.remove(weekday)
                            else
                                frequency.add(weekday)
                        },
                        items = Weekday.all().map { it.toString() },
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .background(color = TransparentBlack.copy(alpha = 0.1f))
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 10.dp)
            ) {
                SectionTitle(title = "Interval (in hours)", required = false)

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IntervalInputField(
                        label = "From",
                        value = intervalStart,
                        updateInterval = {
                            intervalStart = it
                        },
                        modifier = Modifier.weight(1f)
                    )
                    IntervalInputField(
                        label = "To",
                        value = intervalEnd,
                        updateInterval = {
                            intervalEnd = it
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            enabled = threshold != null && threshold > 0
                    && (frequencyType == 1 && frequency.isNotEmpty() || frequencyType == 0)
                    && (intervalStart != null && intervalEnd != null || intervalStart == null && intervalEnd == null)


            Column(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .width(260.dp)
            ) {
                TicketButton(
                    enabled = enabled,
                    text = "Create".uppercase(),
                    onClick = {
                        coroutineScope.launch {
                            accountService.createNotification(
                                triggerType = triggerType,
                                threshold = threshold!!,
                                frequency = if (frequencyType == 0) Weekday.all() else frequency,
                                interval = if (intervalStart != null) NotificationInterval(
                                    startHour = intervalStart!!,
                                    endHour = intervalEnd!!
                                ) else null
                            )
                        }
                    },
                    color = MaterialTheme.colors.primary,
                    icon = Icons.Default.NotificationAdd
                )
            }
        }
    }
}