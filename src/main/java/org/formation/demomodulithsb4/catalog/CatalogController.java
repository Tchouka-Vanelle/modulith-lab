package org.formation.demomodulithsb4.catalog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

    private final CatalogService service;

    public CatalogController(CatalogService service) {
        this.service = service;
    }

    @GetMapping("/coffee")
    public String createCoffee() {

        service.createCoffee("Espresso");

        return "OK";
    }
}