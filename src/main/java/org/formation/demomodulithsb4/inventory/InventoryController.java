package org.formation.demomodulithsb4.inventory;

import org.formation.demomodulithsb4.inventory.internal.StockEntity;
import org.formation.demomodulithsb4.inventory.internal.StockRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Lecture du stock, exposée par l'API publique inventory. */
@RestController
@RequestMapping("/stocks")
public class InventoryController {
    private final StockRepository repository;

    public InventoryController(StockRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{coffeeId}")
    public int quantity(@PathVariable Long coffeeId) {
        return repository.findByCoffeeId(coffeeId)
                .map(StockEntity::getQuantity)
                .orElse(0);
    }
}
