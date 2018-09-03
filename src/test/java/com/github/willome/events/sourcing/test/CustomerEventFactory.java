package com.github.willome.events.sourcing.test;

import com.github.willome.events.sourcing.domain.SaveDomainEvent;
import com.github.willome.events.sourcing.factory.AggregateEventFactory;

@SaveDomainEvent
public class CustomerEventFactory implements AggregateEventFactory<Customer, CustomerSaveEvent> {

    @Override
    public CustomerSaveEvent newEvent(Customer entity) {
        return new CustomerSaveEvent(entity.getId(), entity.getFirstName(), entity.getLastName());
    }

    @Override
    public Class<Customer> getResolvableType() {
        return Customer.class;
    }

}
