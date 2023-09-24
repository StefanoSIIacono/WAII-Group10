package com.lab2.server.controllers


import com.lab2.server.dto.*
import com.lab2.server.exceptionsHandler.exceptions.*
import com.lab2.server.services.SecurityService
import io.micrometer.observation.annotation.Observed
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@Slf4j
@Observed
class SecurityController(
    private val securityService: SecurityService
) {

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    fun signup(@RequestBody(required = true) body: CreateProfileDTO) {
        securityService.signup(body)
    }

    @PostMapping("/expert")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("MANAGER")
    fun createExpert(@RequestBody(required = true) body: CreateExpertDTO) {
        securityService.createExpert(body)
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody(required = true) body: LoginDTO, response: HttpServletResponse) {
        val jwt = securityService.login(body)
        val cookie = Cookie("token", jwt.access_token)
        cookie.secure = false
        cookie.isHttpOnly = true
        response.addCookie(cookie)
    }

    @PostMapping("/user/logout")
    @ResponseStatus(HttpStatus.OK)
    fun logout(response: HttpServletResponse) {
        val cookie = Cookie("token", null)
        cookie.maxAge = 0
        cookie.secure = false
        cookie.isHttpOnly = true
        cookie.path = "/"
        response.addCookie(cookie)
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @Secured("MANAGER", "PROFILE", "EXPERT")
    fun getMe(principal: JwtAuthenticationToken): MeDTO {
        return securityService.getLoggedUserInfo(principal)
    }
}