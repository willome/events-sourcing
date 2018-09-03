package com.github.willome.events.sourcing.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The event instanciated by the publisher. Payload can be any serializable {@code Object}.
 *
 * @author Axione
 *
 */
@SuppressWarnings("serial")
public class Payload implements Serializable {

    @JsonProperty("payload")
    private Object body;

    /**
     * Constructor
     */
    public Payload() {
        // empty constructor needed for deserialization
    }

    /**
     * Constructor
     *
     * @param body Object
     */
    public Payload(Object body) {
        this.body = body;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

}
