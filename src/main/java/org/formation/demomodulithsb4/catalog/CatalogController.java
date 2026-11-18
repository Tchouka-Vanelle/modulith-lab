package org.formation.demomodulithsb4.catalog;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Contrôleur HTTP du module catalog. */
@RestController
@RequestMapping("/coffees")
public class CatalogController {
    private final CatalogService service;

    public CatalogController(CatalogService service) {
        this.service = service;
    }

    @PostMapping
    public Long createCoffee(@RequestParam String name, @RequestParam int price) {
        return service.createCoffee(name, price);
    }

    @GetMapping
    public List<CatalogService.CoffeeView> all() {
        return service.findAll();
    }
}
