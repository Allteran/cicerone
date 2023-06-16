package io.allteran.cicerone.repo;

import io.allteran.cicerone.entity.Tax;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaxRepo implements PanacheRepository<Tax> {
}
