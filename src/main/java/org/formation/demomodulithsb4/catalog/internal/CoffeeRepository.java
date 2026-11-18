package org.formation.demomodulithsb4.catalog.internal;

import org.springframework.data.jpa.repository.JpaRepository;

/** Dépôt interne : les autres modules ne manipulent pas les entités catalog. */
public interface CoffeeRepository extends JpaRepository<CoffeeEntity, Long> { }
