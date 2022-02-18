package ticketchain.mobile.user.api.responses.ticket

data class Ticket(
    val Id: Int,
    val Owner: String,
    val Date: Long
)
data class TicketResponse(
    val ticket: Ticket,
)