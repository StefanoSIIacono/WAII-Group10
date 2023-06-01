package com.lab2.server.observer

import io.micrometer.observation.annotation.Observed
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@Slf4j
@Observed
class ObserverController(private val observedService: ObservedServiceImpl){

    companion object{
        private val log = LoggerFactory.getLogger(ObserverController::class.java)
    }

    @GetMapping("/user/{userId}")
    fun userName(@PathVariable("userId") userId: String?): String? {
        log.info("Got a request")
        return observedService.userName(userId)
    }
}