package com.github.willome.events.sourcing.factory;

/**
 *
 * @author Axione
 *
 * @param <U> Source Type
 * @param <V> AggregateEvent
 */
public interface AggregateEventFactory<U, V extends AggregateEvent<U>> {

    /**
     *
     * @param entity entity
     * @return a new {@link AggregateEvent} acting as the event payload to persist
     */
    V newEvent(U entity);

    /**
     * the source type.
     *
     * @return Class the source type
     */
    Class<U> getResolvableType();

}
