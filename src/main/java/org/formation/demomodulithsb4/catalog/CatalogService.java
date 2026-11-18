package org.formation.demomodulithsb4.catalog;

import java.util.List;

import org.formation.demomodulithsb4.catalog.internal.CoffeeEntity;
import org.formation.demomodulithsb4.catalog.internal.CoffeeRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** API métier publique du module catalog. */
@Service
public class CatalogService {
    private final CoffeeRepository repository;
    private final ApplicationEventPublisher publisher;

    public CatalogService(CoffeeRepository repository, ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    public Long createCoffee(String name, int price) {
        CoffeeEntity coffee = repository.save(new CoffeeEntity(name, price));
        // L'événement transporte l'identifiant, sans exposer l'entité JPA interne.
        publisher.publishEvent(new CoffeeCreatedEvent(coffee.getId(), coffee.getName()));
        return coffee.getId();
    }

    @Transactional(readOnly = true)
    public List<CoffeeView> findAll() {
        return repository.findAll().stream()
                .map(coffee -> new CoffeeView(coffee.getId(), coffee.getName(), coffee.getPrice()))
                .toList();
    }

    /** Vue publique du catalogue, indépendante du modèle de persistance. */
    public record CoffeeView(Long id, String name, int price) { }
}
