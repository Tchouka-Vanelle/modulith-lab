package org.formation.demomodulithsb4.catalog;

/** Événement public du module catalog, consommé par le module inventory. */
public record CoffeeCreatedEvent(Long coffeeId, String coffeeName) { }
