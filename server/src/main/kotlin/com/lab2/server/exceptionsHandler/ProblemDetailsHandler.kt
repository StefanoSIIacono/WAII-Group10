package com.lab2.server.exceptionsHandler

import com.lab2.server.exceptionsHandler.exceptions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ProblemDetailsHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFound(e: ProductNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(TicketNotFoundException::class)
    fun handleTicketNotFound(e: TicketNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(DuplicateProductException::class)
    fun handleDuplicateProduct(e: DuplicateProductException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)

    @ExceptionHandler(ProfileNotFoundException::class)
    fun handleProductNotFound(e: ProfileNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(DuplicateProfileException::class)
    fun handleDuplicateProfile(e: DuplicateProfileException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)

    @ExceptionHandler(DuplicateExpertException::class)
    fun handleDuplicateExpert(e: DuplicateExpertException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)

    @ExceptionHandler(ProfileEmailChangeNotAllowedException::class)
    fun handleProfileEmailChange(e: ProfileEmailChangeNotAllowedException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)

    @ExceptionHandler(ExpertNotFoundException::class)
    fun handleExpertNotFound(e: ExpertNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(IllegalStatusChangeException::class)
    fun handleIllegalStatusChange(e: IllegalStatusChangeException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)

    @ExceptionHandler(ManagerNotFoundException::class)
    fun handleManagerNotFound(e: ManagerNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(ExpertiseNotFoundException::class)
    fun handleExpertiseNotFound(e: ExpertiseNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(IllegalPriorityException::class)
    fun handleIllegalPriority(e: IllegalPriorityException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)

    @ExceptionHandler(DuplicatedExpertiseException::class)
    fun handleDuplicatedExpertise(e: DuplicatedExpertiseException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)

    @ExceptionHandler(AddressNotFoundException::class)
    fun handleAddressNotFound(e: AddressNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(WrongCredentialsException::class)
    fun handleWrongCredentials(e: WrongCredentialsException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.message!!)

    @ExceptionHandler(ExpertIsNullException::class)
    fun handleExpertIsNull(e: ExpertIsNullException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)

    @ExceptionHandler(CannotCreateUserException::class)
    fun handleCannotCreateUser(e: CannotCreateUserException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.message!!)

    @ExceptionHandler(AckMessageInTheFutureException::class)
    fun handleAckMessageInTheFuture(e: AckMessageInTheFutureException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)

    @ExceptionHandler(ExpertiseCurrentlyInUseException::class)
    fun handleExpertiseCurrentlyInUse(e: ExpertiseCurrentlyInUseException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)

    @ExceptionHandler(InvalidBase64Exception::class)
    fun handleInvalidBase64(e: InvalidBase64Exception) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
}