package io.allteran.cicerone.repo;

import io.allteran.cicerone.entity.ContractType;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContractTypeRepo implements PanacheRepository<ContractType> {
}
