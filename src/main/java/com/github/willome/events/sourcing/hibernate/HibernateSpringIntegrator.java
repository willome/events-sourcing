package com.github.willome.events.sourcing.hibernate;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Register Hibernate listeners.
 *
 * @author willome
 *
 */
@Component
@ConditionalOnProperty(value = "events.sourcing.enable", havingValue = "true", matchIfMissing = true)
public class HibernateSpringIntegrator {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateSpringIntegrator.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private Optional<List<HibernateEventListener>> hibernateEventListeners;

    /**
     * Registering Hibernate Listeners
     */
    @PostConstruct
    public void registerListeners() {
        LOG.debug("Registering Spring managed HibernateEventListeners");

        EventListenerRegistry listenerRegistry = ((SessionFactoryImpl) entityManagerFactory.unwrap(SessionFactory.class))
                .getServiceRegistry()
                .getService(EventListenerRegistry.class);

        if (hibernateEventListeners.isPresent()) {

            hibernateEventListeners.get().forEach(hel -> {

                LOG.debug("Registering: {}", hel.getClass());
                if (PreInsertEventListener.class.isAssignableFrom(hel.getClass())) {
                    listenerRegistry.appendListeners(EventType.PRE_INSERT, (PreInsertEventListener) hel);
                }
                if (PreUpdateEventListener.class.isAssignableFrom(hel.getClass())) {
                    listenerRegistry.appendListeners(EventType.PRE_UPDATE, (PreUpdateEventListener) hel);
                }
                if (PreDeleteEventListener.class.isAssignableFrom(hel.getClass())) {
                    listenerRegistry.appendListeners(EventType.PRE_DELETE, (PreDeleteEventListener) hel);
                }
                if (PostInsertEventListener.class.isAssignableFrom(hel.getClass())) {
                    listenerRegistry.appendListeners(EventType.POST_INSERT, (PostInsertEventListener) hel);
                }
                if (PostUpdateEventListener.class.isAssignableFrom(hel.getClass())) {
                    listenerRegistry.appendListeners(EventType.POST_UPDATE, (PostUpdateEventListener) hel);
                }
                if (PostDeleteEventListener.class.isAssignableFrom(hel.getClass())) {
                    listenerRegistry.appendListeners(EventType.POST_DELETE, (PostDeleteEventListener) hel);
                }
                // Currently we do not need other types of eventListeners. Else this method needs to be extended.

            });

        }

    }

}
