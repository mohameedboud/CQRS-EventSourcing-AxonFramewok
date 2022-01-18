package org.sid.comptecqrses.query.service;

import org.axonframework.eventsourcing.eventstore.DomainEventStream;

public interface EventSourcingService {
    DomainEventStream eventsByAccountId(String accountId);
}
