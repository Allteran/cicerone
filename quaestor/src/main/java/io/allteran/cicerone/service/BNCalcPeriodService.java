package io.allteran.cicerone.service;

import io.allteran.cicerone.dto.BNCalcPeriodDTO;
import io.allteran.cicerone.exception.EntityException;
import io.allteran.cicerone.repo.BNCalcPeriodRepo;
import io.allteran.cicerone.util.EntityConverter;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
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

    //WARNING: findById as findAll gives you FULL ACCESS directly to the object in database, so pay attention when you manipulate with those objects
    public Uni<BNCalcPeriodDTO> findById(long id) {
        return repo.findById(id)
                .onItem().ifNotNull().transform(EntityConverter::convertToDTO)
                .onItem().ifNull().failWith(() -> new NotFoundException("Can't find CalcPeriod with ID [" + id + "]"));
    }
    @WithTransaction
    public Uni<BNCalcPeriodDTO> create(BNCalcPeriodDTO dto) {
        if(dto.getName().isEmpty() || dto.getName().isBlank()) {
            throw new EntityException("Can't create CalcPeriod, because some of fields are blank:\nBNCalcPeriodDTO: " + dto);
        }
        return repo.persistAndFlush(EntityConverter.convertToEntity(dto)).map(EntityConverter::convertToDTO);
    }

    @WithTransaction
    public Uni<BNCalcPeriodDTO> update(long idFromDb, BNCalcPeriodDTO dto) {
        if(dto.getName().isBlank() || dto.getName().isEmpty()) {
            throw new EntityException("Can't create CalcPeriod, because some of fields are blank:\nBNCalcPeriodDTO: " + dto);
        }
        return repo.findById(idFromDb)
                .onItem().ifNotNull().transform(bnCalcPeriod -> {
                    bnCalcPeriod.setName(dto.getName());
                    return EntityConverter.convertToDTO(bnCalcPeriod);
                })
                .onItem().ifNull().failWith(() -> new NotFoundException("Can't find CalcPeriod with ID [" + idFromDb + "]"));
    }

    @WithTransaction
    public Uni<Boolean> delete(long id) {
        return repo.deleteById(id);
    }


}
