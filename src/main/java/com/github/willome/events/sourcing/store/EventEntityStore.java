package com.github.willome.events.sourcing.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.willome.events.sourcing.domain.EventEntity;
import com.github.willome.events.sourcing.domain.Payload;
import com.github.willome.events.sourcing.factory.AggregateEvent;
import com.github.willome.events.sourcing.repository.EventEntityRepository;

/**
 * EventEntity Store
 *
 * @author Axione
 *
 */
@Service
@ConditionalOnProperty(value = "events.sourcing.enable", havingValue = "true", matchIfMissing = true)
public class EventEntityStore {

    @Autowired
    private EventEntityRepository eventEntityRepository;

    /**
     * Listen for EntityCreatedEvent and persist a new {@code EventEntity}
     *
     * @param event EntityCreatedEvent
     */
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void storeEvent(AggregateEvent<?> event) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setPayload(new Payload(event));
        eventEntity.setType(event.getClass().getCanonicalName());
        eventEntity.setSchema(event.schema());
        eventEntity.setVersion(event.version());
        eventEntityRepository.save(eventEntity);
    }

}
