package ticketchain.mobile.user.data

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale

enum class Occupation {
    STUDENT,
    STUDENT_WORKER,
    WORKER,
    RETIRED;

    override fun toString(): String {
        return name.lowercase().replace("_", " ").capitalize(Locale.current)
    }

    companion object {
        fun fromValue(value: Int): Occupation? {
            return values().getOrNull(value)
        }
    }
}