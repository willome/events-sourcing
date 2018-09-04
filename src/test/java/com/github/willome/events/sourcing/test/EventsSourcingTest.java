package com.github.willome.events.sourcing.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.willome.events.sourcing.publisher.EventPublisher;
import com.github.willome.events.sourcing.repository.EventEntityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestsApplication.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class EventsSourcingTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EventEntityRepository eventEntityRepository;

    @Autowired
    private PaymentRequestFactory paymentRequestFactory;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private EventPublisher eventPublisher;

    @After
    public void clean() {
        customerRepository.deleteAll(customerRepository.findAll());
        eventEntityRepository.deleteAll(eventEntityRepository.findAll());
    }

    @Test
    public void test_event_should_be_emitted_on_custom_event() {
        applicationEventPublisher.publishEvent(paymentRequestFactory.newEvent(new PaymentRequest()));
        assertThat(eventEntityRepository.findAll()).hasSize(1);
        Mockito.verify(eventPublisher, Mockito.times(1)).publish(Mockito.any());
        Mockito.reset(eventPublisher);
    }

    @Test
    public void test_event_should_be_emitted_on_new_entity() {
        customerRepository.save(new Customer("aaa", "bbb"));
        assertThat(eventEntityRepository.findAll()).hasSize(1);
        Mockito.verify(eventPublisher, Mockito.times(1)).publish(Mockito.any());
        Mockito.reset(eventPublisher);
    }

    @Test
    public void test_event_should_be_emitted_on_update_entity() {
        Customer cust = new Customer("aaa", "bbb");
        customerRepository.save(cust);
        assertThat(eventEntityRepository.findAll()).hasSize(1);
        Mockito.verify(eventPublisher, Mockito.times(1)).publish(Mockito.any());
        Mockito.reset(eventPublisher);
        cust.setLastName("upd");
        customerRepository.save(cust);
        assertThat(eventEntityRepository.findAll()).hasSize(2);
        Mockito.verify(eventPublisher, Mockito.times(1)).publish(Mockito.any());
        Mockito.reset(eventPublisher);
    }

    @Test
    public void test_event_should_not_be_emitted_on_update_twice() throws InterruptedException {
        Customer cust = new Customer("aaa", "bbb");
        customerRepository.save(cust);
        assertThat(eventEntityRepository.findAll()).hasSize(1);
        Thread.sleep(1000);
        Mockito.verify(eventPublisher, Mockito.times(1)).publish(Mockito.any());
        Mockito.reset(eventPublisher);
        cust.setLastName("bbb");
        customerRepository.save(cust);
        assertThat(eventEntityRepository.findAll()).hasSize(1);
        Mockito.verify(eventPublisher, Mockito.times(0)).publish(Mockito.any());
        Mockito.reset(eventPublisher);
    }

}
