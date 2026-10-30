package org.formation.demomodulithsb4.catalog;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

    private final ApplicationEventPublisher publisher;

    public CatalogService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void createCoffee(String name) {

        System.out.println("Coffee created : " + name);

        publisher.publishEvent(
                new CoffeeCreatedEvent(name)
        );
    }
}