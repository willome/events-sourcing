package com.github.willome.events.sourcing.hibernate;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.github.willome.events.sourcing.domain.SaveDomainEvent;

/**
 * Default Hibernate events listener.
 *
 * @author Axione
 *
 */
@SuppressWarnings("serial")
@Component
@ConditionalOnProperty(value = "events.sourcing.enable", havingValue = "true", matchIfMissing = true)
public class DefaultHibernateEventListener implements HibernateEventListener, PostInsertEventListener, PostUpdateEventListener,
ApplicationContextAware, InitializingBean {

    private ApplicationContext ctx;

    // map<Entity, EntityEventFactory bean name>
    private Map<Class<?>, String> cacheSaveDomainEvents = new HashMap<>();

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return true;
    }

    /**
     * Same event for insert or update.
     *
     * @param entity Object
     */
    private void handlePostInsertOrUpdate(Object entity) {
        if (cacheSaveDomainEvents.containsKey(entity.getClass())) {
            Object bean = ctx.getBean(cacheSaveDomainEvents.get(entity.getClass()));
            try {
                Method m = bean.getClass().getDeclaredMethod("newEvent", entity.getClass());
                ReflectionUtils.makeAccessible(m);
                Object event = ReflectionUtils.invokeMethod(m, bean, entity);
                // publish the events for the local Events store
                applicationEventPublisher.publishEvent(event);
            }
            catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        handlePostInsertOrUpdate(event.getEntity());
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        handlePostInsertOrUpdate(event.getEntity());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ctx.getBeansWithAnnotation(SaveDomainEvent.class).forEach((n, o) -> {
            try {
                Method m = o.getClass().getDeclaredMethod("getResolvableType");
                ReflectionUtils.makeAccessible(m);
                Object entityClazz = ReflectionUtils.invokeMethod(m, o);
                cacheSaveDomainEvents.put((Class<?>) entityClazz, n);
            }
            catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

}
