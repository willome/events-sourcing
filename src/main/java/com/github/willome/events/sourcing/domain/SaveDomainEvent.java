package com.github.willome.events.sourcing.domain;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * Each time a model entity (having a method with this annotation) will be saved,
 * the result of this method will be used as the payload of the {@link EventEntity}
 *
 * @author willome
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SaveDomainEvent {

}