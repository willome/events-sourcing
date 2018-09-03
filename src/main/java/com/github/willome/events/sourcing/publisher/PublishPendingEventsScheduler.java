package com.github.willome.events.sourcing.publisher;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.willome.events.sourcing.domain.EventEntity;
import com.github.willome.events.sourcing.domain.EventEntitySaveEvent;
import com.github.willome.events.sourcing.domain.EventEntity.Status;
import com.github.willome.events.sourcing.repository.EventEntityRepository;

/**
 *
 * @author Axione
 *
 */
@Service
@ConditionalOnProperty(value = "events.sourcing.enable", havingValue = "true", matchIfMissing = true)
public class PublishPendingEventsScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(PublishPendingEventsScheduler.class);

    @Autowired
    private EventEntityRepository eventEntityRepository;

    @Autowired
    private EventPublisher eventPublisher;

    /**
     * Publish events scheduler.
     */
    @Scheduled(initialDelayString = "${events.publisher.frequence:3000}", fixedDelayString = "${events.publisher.frequence:3000}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendEvents() {
        LOG.trace("publishing events...");
        publishEvent();
    }

    /**
     * Remove all SENT events
     */
    @Scheduled(initialDelayString = "${events.publisher.clean-frequence:5000}", fixedDelayString = "${events.publisher.clean-frequence:5000}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cleanAckEvents() {
        LOG.trace("cleaning events...");
        eventEntityRepository.findByStatus(Status.SENT).forEach(eventEntityRepository::delete);
    }

    /**
     * Event listener. Events are emitted when a {@link EventEntity} is saved into db.
     * see {@link EventEntity}
     *
     * @param event {@link EventEntitySaveEvent}
     */
    @EventListener
    public void handleEventEntitySavedEvent(EventEntitySaveEvent event) {
        publishEvent();
    }

    /**
     * publish Event asynchronously.
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishEvent() {

        eventEntityRepository.findFirstByStatusOrderByOccuredAtAsc(Status.PENDING)
        .ifPresent(evt -> {

            LOG.info("publishing event {}", evt);
            eventPublisher.publish(evt);
            // warn : if db failure right now, then message will be re-published
            // consumers must take care of unicity (or not)
            evt.sent(LocalDateTime.now());
            eventEntityRepository.save(evt);

        });

    }

}
