package io.allteran.cicerone.repo;

import io.allteran.cicerone.entity.BNCalcPeriod;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BNCalcPeriodRepo implements PanacheRepository<BNCalcPeriod> {

}
