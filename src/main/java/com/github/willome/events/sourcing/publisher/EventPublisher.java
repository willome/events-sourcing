package com.github.willome.events.sourcing.publisher;

import com.github.willome.events.sourcing.domain.EventEntity;

/**
 * EventPublisher main Interface
 *
 * @author Axione
 *
 */
public interface EventPublisher {

    /**
     *
     * @param event the {@link EventEntity} to publish
     */
    void publish(EventEntity event);

}
