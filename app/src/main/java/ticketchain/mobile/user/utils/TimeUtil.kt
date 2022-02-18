package ticketchain.mobile.user.utils

import kotlin.math.roundToInt

fun Float.toTimeString(): String {
    val time = roundToInt()

    val hour = time / 60
    val mod = time % 60
    return if (hour > 0) {
        "${hour}h${mod}m"
    } else {
        "${mod}m"
    }
}
