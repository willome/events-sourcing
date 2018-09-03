package com.github.willome.events.sourcing.test;

import org.springframework.stereotype.Service;

import com.github.willome.events.sourcing.factory.AggregateEventFactory;

@Service
public class PaymentRequestFactory implements AggregateEventFactory<PaymentRequest, PaymentRequestEvent> {

    @Override
    public PaymentRequestEvent newEvent(PaymentRequest entity) {
        return new PaymentRequestEvent();
    }

    @Override
    public Class<PaymentRequest> getResolvableType() {
        return PaymentRequest.class;
    }

}
