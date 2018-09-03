# events-sourcing
- Events-sourcing is a library used to generate and send events for a Spring application. 
- Hibernate Domain events are automatically sent when an entity is persisted (or deleted). 


# Howto
## Maven
```
mvn clean install
```
then in the project
```

<dependency>
  <groupId>com.github.willome</groupId>
  <artifactId>events-sourcing</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Spring (boot) configuration
```
@ComponentScan({"com.axione.events.sourcing"})
```

## EventPublisher
Define a new EventPublisher : 
```
@Service
public MyEventPublisher implements EventPublisher {}
```

## liquibase
```
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog" xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">

   	<include file="liquibase-events-sourcing-changelog-1.0.xml" />

</databaseChangeLog>
```

## Sending a custom Event
- crate a new Event implements AggregateEvent<In> with In, an Object
- create a new factory implements AggregateEventFactory<In, Out> with In, an object & Out, the reponse. Annotate with @Service.
- Inject ApplicationEventPublisher applicationEventPublisher
- call applicationEventPublisher.publish(factory.newEvent(In));

## Domain models events
- crate a new Event implements AggregateEvent<In> with In, an Entity
- create a new factory implements AggregateEventFactory<In, Out> with In, an Entity & Out, the reponse. Annotate with @SaveDomainEvent
- each save/update on db will automatically publish the event.
