package io.allteran.cicerone.service;

import io.allteran.cicerone.dto.BNCalcPeriodDTO;
import io.allteran.cicerone.repo.BNCalcPeriodRepo;
import io.allteran.cicerone.util.EntityConverter;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class BNCalcPeriodService {
    private final BNCalcPeriodRepo repo;

    public Uni<List<BNCalcPeriodDTO>> findAll() {
        return repo.findAll(Sort.ascending("name"))
                .list()
                .onItem().ifNotNull().transform(list -> list.stream()
                        .map(EntityConverter::convertToDTO)
                        .toList())
                .onItem().ifNull().failWith(() -> new NotFoundException("Can't find any record of CalcPeriod in database"));
    }

    public Uni<BNCalcPeriodDTO> findById(long id) {
        return repo.findById(id)
                .onItem().ifNotNull().transform(EntityConverter::convertToDTO)
                .onItem().ifNull().failWith(() -> new NotFoundException("Can't find CalcPeriod with ID [" + id + "]"));
    }

}
