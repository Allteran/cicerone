package io.allteran.cicerone.service;

import io.allteran.cicerone.dto.ContractTypeDTO;
import io.allteran.cicerone.exception.EntityException;
import io.allteran.cicerone.repo.ContractTypeRepo;
import io.allteran.cicerone.util.EntityConverter;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class ContractTypeService {
    private final ContractTypeRepo repo;

    public Uni<List<ContractTypeDTO>> findAll() {
        return repo.findAll().list()
                .onItem().ifNotNull().transform(list -> list.stream()
                        .map(EntityConverter::convertToDTO)
                        .toList())
                .onItem().ifNull().failWith(() -> new NotFoundException("Can't find any record of ContractType in database"));
    }

    public Uni<ContractTypeDTO> findById(long id) {
        return repo.findById(id)
                .onItem().ifNotNull().transform(EntityConverter::convertToDTO)
                .onItem().ifNull().failWith(() -> new NotFoundException("Can't find ContractType with ID [" + id + "]"));
    }

    @WithTransaction
    public Uni<ContractTypeDTO> create(ContractTypeDTO dto) {
        if(dto.getName().isEmpty() || dto.getName().isBlank()) {
            throw new EntityException("Can't create ContractType: some fields blank or empty:\n" + dto);
        }
        return repo.persistAndFlush(EntityConverter.convertToEntity(dto)).map(EntityConverter::convertToDTO);
    }

    @WithTransaction
    public Uni<ContractTypeDTO> update(long idFromDb, ContractTypeDTO dto) {
        if(dto.getName().isBlank() || dto.getName().isEmpty()) {
            throw new EntityException("Can't create ContractType: some fields blank or empty:\n" + dto);
        }

        return repo.findById(idFromDb)
                .onItem().ifNull().failWith(() -> new NotFoundException("Can't find ContractType with ID [" + idFromDb + "]"))
                .onItem().ifNotNull().transform(contractType -> {
                    contractType.setName(dto.getName());
                    return EntityConverter.convertToDTO(contractType);
                });
    }

    @WithTransaction
    public Uni<Boolean> delete(long id) {
        return repo.deleteById(id);
    }

}
