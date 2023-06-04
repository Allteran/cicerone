package io.allteran.cicerone.service;

import io.allteran.cicerone.entity.BNCalcPeriod;
import io.allteran.cicerone.repo.BNCalcPeriodRepo;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class BNCalcPeriodService {
    private final BNCalcPeriodRepo repo;

    public Uni<List<BNCalcPeriod>> findAll() {
        return repo.findAll(Sort.ascending("name")).list();
    }
}
