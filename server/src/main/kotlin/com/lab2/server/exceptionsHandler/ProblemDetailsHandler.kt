package com.lab2.server.exceptionsHandler

import com.lab2.server.exceptionsHandler.exceptions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ProblemDetailsHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler (ProductNotFoundException::class)
    fun handleProductNotFound (e: ProductNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(DuplicateProductException::class)
    fun handleDuplicateProduct (e: DuplicateProductException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)

    @ExceptionHandler (ProfileNotFoundException::class)
        fun handleProductNotFound (e: ProfileNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(DuplicateProfileException::class)
    fun handleDuplicateProfile (e: DuplicateProfileException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)

    @ExceptionHandler(ProfileEmailChangeNotAllowedException::class)
    fun handleProfileEmailChange (e: ProfileEmailChangeNotAllowedException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)

}