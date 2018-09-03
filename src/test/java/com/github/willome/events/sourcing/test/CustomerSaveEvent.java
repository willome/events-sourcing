package com.github.willome.events.sourcing.test;

import com.github.willome.events.sourcing.factory.AggregateEvent;

@SuppressWarnings("serial")
public class CustomerSaveEvent implements AggregateEvent<Customer> {

    private final Long id;
    private final String firstName;
    private final String lastName;


    public CustomerSaveEvent(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public Long getId() {
        return id;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    @Override
    public int version() {
        return 0;
    }

    //@Override
    /*
    public Class<Customer> type() {
        return Customer.class;
    }
    */

}
