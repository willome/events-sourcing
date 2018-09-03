package com.github.willome.events.sourcing.test;

import com.github.willome.events.sourcing.factory.AggregateEvent;

@SuppressWarnings("serial")
public class PaymentRequestEvent implements AggregateEvent<PaymentRequest> {

    private PaymentRequest paymentRequest;

    public PaymentRequestEvent() {

    }

    public PaymentRequestEvent(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    @Override
    public int version() {
        return 0;
    }

}
