package org.formation.demomodulithsb4.inventory;

import org.formation.demomodulithsb4.catalog.CoffeeCreatedEvent;
import org.formation.demomodulithsb4.inventory.internal.StockEntity;
import org.formation.demomodulithsb4.inventory.internal.StockRepository;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/** Le module inventory réagit à l'événement sans appel direct depuis catalog. */
@Component
public class InventoryListener {
    private final StockRepository repository;

    public InventoryListener(StockRepository repository) {
        this.repository = repository;
    }

    @ApplicationModuleListener
    public void onCoffeeCreated(CoffeeCreatedEvent event) {
        repository.save(new StockEntity(event.coffeeId(), 0));
        System.out.println("Inventory initialized for " + event.coffeeName());
    }
}
