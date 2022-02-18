package ticketchain.mobile.user.api

import ticketchain.mobile.user.api.requests.TicketOwnerRequest
import ticketchain.mobile.user.api.responses.ticket.CurrentTicketResponse
import ticketchain.mobile.user.api.responses.issues.HasIssuesResponse
import ticketchain.mobile.user.api.responses.ticket.CountTicketResponse
import ticketchain.mobile.user.api.responses.ticket.HasTicketResponse
import ticketchain.mobile.user.api.responses.ticket.TicketResponse
import ticketchain.mobile.user.state.AppState

class TicketChainApi(
    store: AppState
) : ApiHandler(store) {

    suspend fun getCurrentTicket(): CurrentTicketResponse {
        return get(Routes.TICKET_CURRENT)
    }

    suspend fun countTickets(): CountTicketResponse {
        return get(Routes.TICKET_COUNT)
    }

    suspend fun hasTicket(owner: String): HasTicketResponse {
        val request = TicketOwnerRequest(owner)
        return post(Routes.TICKET_HAS, request)
    }

    suspend fun requestTicket(owner: String): TicketResponse {
        val request = TicketOwnerRequest(owner)
        return post(Routes.TICKET_REQUEST, request)
    }

    suspend fun hasIssues(): HasIssuesResponse {
        return get(Routes.ISSUES_HAS)
    }
}

