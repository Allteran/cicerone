package io.allteran.cicerone.service;

import io.allteran.cicerone.dto.TaxDto;
import io.allteran.cicerone.entity.Tax;
import io.allteran.cicerone.exception.NotFoundException;
import io.allteran.cicerone.repo.TaxRepo;
import io.allteran.cicerone.util.EntityConverter;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class TaxService {
    private final TaxRepo repo;

    public Uni<List<TaxDto>> findAll() {
        return repo.findAll(Sort.ascending("id"))
                .list()
                .onItem().ifNotNull().transform(list ->list.stream()
                        .map(EntityConverter::convertToDto)
                        .toList())
                .onItem().ifNull().failWith(() -> new NotFoundException("No taxes in database"));
    }

    public Uni<TaxDto> findById(long id) {
        return repo.findById(id)
                .onItem().ifNotNull().transform(EntityConverter::convertToDto)
                .onItem().ifNull().failWith(() -> new NotFoundException("Can't find tax [ID = " + id + "]"));
    }


}
