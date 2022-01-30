package ticketchain.mobile.user.data

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale

enum class NotificationTrigger {
    TICKET,
    TIME;

    companion object {
        fun fromValue(value: Int): NotificationTrigger {
            return when (value) {
                0 -> TICKET
                1 -> TIME
                else -> TICKET // DEFAULT
            }
        }
    }
}


enum class Weekday {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    override fun toString(): String {
        return name.lowercase().capitalize(Locale.current)
    }

    companion object {
        fun all(): List<Weekday> {
            return values().asList()
        }

        fun none(): List<Weekday> {
            return emptyList()
        }
    }
}

data class NotificationInterval(val startHour: Int, val endHour: Int) {
    override fun toString(): String {
        return "${startHour}h to ${endHour}h"
    }
}

data class Notification(
    val triggerType: NotificationTrigger,
    val threshold: Int,
    var enabled: Boolean,
    val frequency: List<Weekday> = Weekday.all(),
    val interval: NotificationInterval? = null
) {
    override fun toString(): String {
        return "Less than ${
            when (triggerType) {
                NotificationTrigger.TICKET -> "$threshold tickets"
                NotificationTrigger.TIME -> "${threshold}m wait time"
            }
        }"
    }

    fun frequencyString(full: Boolean = true): String {
        val days =
            if (frequency.size == Weekday.all().size) "Everyday"
            else frequency.joinToString(", ")

        return if (interval == null || !full) days else "$days from $interval"
    }

    fun toggle(): Boolean {
        enabled = !enabled
        return enabled
    }

    companion object {
        fun frequencyFromString(value: String): List<Weekday> {
            return if (value.equals("Everyday", true)) {
                Weekday.all()
            } else {
                value.split(", ").map { Weekday.valueOf(it.uppercase()) }
            }
        }
    }
}
