package io.allteran.cicerone.resource;

import io.allteran.cicerone.dto.payload.GeneralResponse;
import io.allteran.cicerone.service.TaxService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

@RequestScoped
@RequiredArgsConstructor
@Path("api/v1/quaestor/tax")
public class TaxResource {
    private final TaxService taxService;

    @GET
    @WithSession
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> findAll() {
        return taxService.findAll()
                .onItem().transform(data ->
                        Response.ok(new GeneralResponse<>("OK", data)).build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.NOT_FOUND)
                                .entity(new GeneralResponse<>(throwable.getMessage(), Collections.emptyList()))
                                .build());
    }

    @GET
    @WithSession
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> findById(@PathParam("id") long id) {
        return taxService.findById(id)
                .onItem().transform(data ->
                        Response.ok(new GeneralResponse<>("OK", Collections.singletonList(data))).build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.NOT_FOUND)
                                .entity(new GeneralResponse<>(throwable.getMessage(), Collections.emptyList()))
                                .build()
                );
    }
}
