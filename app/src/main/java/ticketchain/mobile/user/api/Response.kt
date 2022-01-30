package ticketchain.mobile.user.api

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

sealed class Response<out T> {

    @JsonDeserialize
    data class SuccessResponse<T>(val data: T) : Response<T>()

    @JsonDeserialize
    data class ErrorResponse(val message: String) : Response<Nothing>()
}