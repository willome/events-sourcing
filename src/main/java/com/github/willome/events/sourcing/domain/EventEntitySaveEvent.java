package com.github.willome.events.sourcing.domain;

import com.github.willome.events.sourcing.publisher.PublishPendingEventsScheduler;

/**
 * This event will be emitted into spring bus, as soon as an Event has been persisted
 * into the db. Then a listener will be able to push it to the Bus (ActiveMQ/Kafka).
 * {@link PublishPendingEventsScheduler}
 *
 * @author Axione
 *
 */
public class EventEntitySaveEvent {

    private final Long id;

    /**
     * Constructor.
     *
     * @param id Long
     */
    public EventEntitySaveEvent(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
