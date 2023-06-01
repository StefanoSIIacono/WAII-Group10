package com.lab2.server.observer

import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ObserverHandler(): ObservationHandler<Observation.Context> {

    companion object{
        private val logger = LoggerFactory.getLogger(ObserverHandler::class.java)
    }


    private fun getUserTypeFromContext(context: Observation.Context): String{
        return "prova"
    }

    override fun onStart(context: Observation.Context){
        logger.info("Before running the observation for context [{}], userType[{}]",
                context.getName())//, context.getUserTypeFromContext(context))
    }

    override fun onStop(context: Observation.Context){
        logger.info("After running the observation for context [{}], userType[{}]",
                context.getName())//, context.getUserTypeFromContext(context))
    }

    override fun supportsContext(context: Observation.Context): Boolean {
        return true
    }


}