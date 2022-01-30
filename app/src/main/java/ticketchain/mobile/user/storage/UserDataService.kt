package ticketchain.mobile.user.storage

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import ticketchain.mobile.user.data.Notification
import ticketchain.mobile.user.data.Widget
import ticketchain.mobile.user.storage.proto.UserData
import java.io.IOException
import ticketchain.mobile.user.storage.proto.Notification as ProtoNotification

class UserDataService(
    private val dataStore: DataStore<UserData>
) {
    private val userDataFlow: Flow<UserData> = dataStore.data.catch { e ->
        if (e is IOException) {
            Log.e("UserDataService", "Error reading sort order preferences.", e)
            emit(UserData.getDefaultInstance())
        } else {
            throw e
        }
    }

    suspend fun getUserData(): UserData =
        userDataFlow.firstOrNull() ?: UserData.getDefaultInstance()

    suspend fun storeUserData(
        name: String? = null,
        age: Int? = null,
        occupation: Int? = null,
        widgets: List<Widget>? = null,
        theme: Int? = null,
        currentTicket: String? = null,
        notificationsEnabled: Boolean? = null,
        notifications: List<Notification>? = null,
    ) {
        dataStore.updateData {
            val userDataBuilder = it.toBuilder()

            if (name != null) {
                userDataBuilder.name = name
            }

            if (age != null) {
                userDataBuilder.age = age
            }

            if (occupation != null) {
                userDataBuilder.occupation = occupation
            }

            if (widgets != null) {
                userDataBuilder.clearWidgets()
                userDataBuilder.addAllWidgets(widgets.map { widget -> widget.ordinal })
            }

            if (theme != null) {
                userDataBuilder.theme = theme
            }

            if (currentTicket != null) {
                userDataBuilder.currentTicket = currentTicket
            }

            if (notificationsEnabled != null) {
                userDataBuilder.notificationsEnabled = notificationsEnabled
            }

            if (notifications != null) {
                userDataBuilder.clearNotifications()
                userDataBuilder.addAllNotifications(notifications.map {
                    ProtoNotification.newBuilder().apply {
                        threshold = it.threshold
                        type = it.triggerType.ordinal
                        startHour = it.interval?.startHour ?: -1
                        endHour = it.interval?.endHour ?: -1
                        weekdays = it.frequencyString(full = false)
                        enabled = it.enabled
                    }.build()
                })
            }

            userDataBuilder.build()
        }
    }
}

val Context.userDataStore: DataStore<UserData> by dataStore(
    fileName = "user_data.pb",
    serializer = UserDataSerializer
)