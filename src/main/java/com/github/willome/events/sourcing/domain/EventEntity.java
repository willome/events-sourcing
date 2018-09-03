package com.github.willome.events.sourcing.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

/**
 *
 * @author Axione
 *
 */
@Entity
@Table(name = "t_events", uniqueConstraints = { @UniqueConstraint(columnNames = { "event_uuid" }) })
@TypeDef(name = "jsonb", typeClass = JsonbUserType.class)
public class EventEntity {

    /**
     *
     * @author Axione
     *
     */
    public enum Status {
        /**
         * Default value
         */
        PENDING,

        /**
         * Value when event has been published
         */
        SENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "payload")
    @Type(type = "jsonb", parameters = { @Parameter(name = JsonbUserType.CLASS, value = "com.github.willome.events.sourcing.domain.Payload") })
    private Payload payload;

    @Column(nullable = false, name = "event_uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false, name = "occured_at")
    private LocalDateTime occuredAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(nullable = false, name = "event_type")
    private String type;

    @Column(nullable = false, name = "event_version")
    private int version;

    @Column(nullable = false, name = "event_schema")
    private String schema;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * Event source
     *
     * @return Collection<Object>
     */
    @DomainEvents
    Collection<Object> domainEvents() {
        return Arrays.asList(new EventEntitySaveEvent(this.id));
    }

    /**
     * callbackMethod
     */
    @AfterDomainEventPublication
    void callbackMethod() {
        // do nothing right now
    }

    @Override
    public String toString() {
        return String.format("Event[id=%d, status='%s, sentAt='%s']", id, status, sentAt);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getOccuredAt() {
        return occuredAt;
    }

    public Long getId() {
        return id;
    }

    /**
     * update Events infos when it has been sent.
     *
     * @param at LocalDateTime
     */
    public void sent(LocalDateTime at) {
        this.sentAt = at;
        this.status = Status.SENT;
    }

}
