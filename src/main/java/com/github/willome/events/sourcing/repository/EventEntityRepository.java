package com.github.willome.events.sourcing.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import com.github.willome.events.sourcing.domain.EventEntity;
import com.github.willome.events.sourcing.domain.EventEntity.Status;

/**
 *
 * @author Axione
 *
 */
public interface EventEntityRepository extends CrudRepository<EventEntity, Long> {

    /**
     *
     * @param status Status
     * @return the first Event with Status
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<EventEntity> findFirstByStatusOrderByOccuredAtAsc(Status status);

    /**
     * 
     * @param status Status
     * @return all the events having the status
     */
    List<EventEntity> findByStatus(Status status);

}
