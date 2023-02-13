package com.petitcl.domain4k.spring

import com.petitcl.domain4k.context.*
import com.petitcl.domain4k.stereotype.DomainEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class SpringEventsContext(private val publisher: ApplicationEventPublisher) : EventsContext {
    override fun publishEvent(event: DomainEvent)
        = publisher
            .also { println("SpringEventsContext.publishEvent: $event") }
            .publishEvent(event)
}

@Configuration
class Domain4kSpringAdapter {
    @Bean
    fun eventsContext(publisher: ApplicationEventPublisher): EventsContext
        = SpringEventsContext(publisher)

    @Bean
    fun timeContext(): TimeContext = TimeContext.default()

}
