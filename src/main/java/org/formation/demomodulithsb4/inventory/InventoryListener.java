package org.formation.demomodulithsb4.inventory;

import org.formation.demomodulithsb4.catalog.CoffeeCreatedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {

    @ApplicationModuleListener
    public void onCoffeeCreated(CoffeeCreatedEvent event) {

        System.out.println(
                "Inventory initialized for "
                        + event.coffeeName()
        );
    }
}