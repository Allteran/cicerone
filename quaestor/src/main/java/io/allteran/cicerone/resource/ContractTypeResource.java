package io.allteran.cicerone.resource;

import io.allteran.cicerone.dto.ContractTypeDTO;
import io.allteran.cicerone.service.ContractTypeService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Path("/api/v1/quaestor/contract-type")
@Slf4j
@RequestScoped
public class ContractTypeResource {
    private final ContractTypeService contractTypeService;

    @GET
    @WithSession
    public Uni<Response> findAll() {
        return contractTypeService.findAll()
                .onItem().transform(list -> Response.ok(list).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .entity(throwable.getMessage()).build());
    }

    @GET
    @Path("{id}")
    @WithSession
    public Uni<Response> findById(@PathParam("id") long id) {
        return contractTypeService.findById(id)
                .onItem().transform(data -> Response.ok(data).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .entity(throwable.getMessage()).build());
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @WithSession
    public Uni<Response> create(ContractTypeDTO body) {
        return contractTypeService.create(body)
                .onItem().transform(data -> Response.ok(data).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .entity(throwable.getMessage()).build());
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @WithSession
    public Uni<Response> update(@PathParam("id") long idFromDb, ContractTypeDTO body) {
        return contractTypeService.update(idFromDb, body)
                .onItem().transform(data -> Response.ok(data).build())
                .onFailure().recoverWithItem(throwable -> {
                    Response.Status status = (throwable instanceof NotFoundException) ? Response.Status.NOT_FOUND : Response.Status.BAD_REQUEST;
                    return Response.status(status)
                            .type(MediaType.TEXT_PLAIN_TYPE)
                            .entity(throwable.getMessage())
                            .build();
                });
    }

    @DELETE
    @Path("/delete/{id}")
    @WithSession
    public Uni<Response> delete(@PathParam("id") long id) {
        return contractTypeService.delete(id)
                .onItem().transform(isDeleted -> (isDeleted)
                        ? Response.ok("ContractType deleted successfully").build()
                        : Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN_TYPE).entity("Can't find ContractType with ID [" + id + "]").build());

    }


}
