package io.allteran.cicerone.service;

import io.allteran.cicerone.repo.TaxRepo;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class TaxService {
    private final TaxRepo repo;


}
