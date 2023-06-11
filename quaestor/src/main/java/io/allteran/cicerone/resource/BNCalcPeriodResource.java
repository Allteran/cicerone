package io.allteran.cicerone.resource;

import io.allteran.cicerone.dto.BNCalcPeriodDTO;
import io.allteran.cicerone.service.BNCalcPeriodService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Path("/api/v1/quaestor/bn/cperiod")
@Slf4j
@RequestScoped
public class BNCalcPeriodResource {
    private final BNCalcPeriodService periodService;

    @GET
    @WithSession
    public Uni<Response> findAll() {
        return periodService.findAll()
                .onItem().transform(data -> Response.ok(data).build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.NOT_FOUND)
                                .type(MediaType.TEXT_PLAIN_TYPE)
                                .entity(throwable.getMessage()).build());
    }

    @GET
    @WithSession
    @Path("{id}")
    public Uni<Response> findById(@PathParam("id") long id) {
        return periodService.findById(id)
                .onItem().transform(data -> {
                    log.info("onItem.transform");
                    return Response.ok(data).build();
                })
                .onFailure()
                //in case when you should throw a server error, but with your own exception text or whatever
                //use recoverWithItem or any other method that return value instead of invoke()
                //because of some gods (idk why) void method (like invoke) can't handle Response building and just map it to default response
                .recoverWithItem(throwable ->
                        Response.status(Response.Status.NOT_FOUND)
                                .type(MediaType.TEXT_PLAIN_TYPE)
                                .entity(throwable.getMessage()).build());
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @WithSession
    public Uni<Response> create(BNCalcPeriodDTO dto) {
        return periodService.create(dto)
                .onItem().transform(data -> Response.ok(data).build())
                .onFailure()
                .recoverWithItem(throwable -> Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .entity(throwable.getMessage()).build());
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @WithSession
    public Uni<Response> update(@PathParam("id") long id, BNCalcPeriodDTO dto) {
        return periodService.update(id, dto)
                .onItem().transform(data -> Response.ok(data).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .entity(throwable.getMessage()).build());
    }

    @DELETE
    @Path("/delete/{id}")
    @WithSession
    public Uni<Response> delete(@PathParam("id") long id) {
        return periodService.delete(id)
                .onItem().transform(isDeleted -> (isDeleted)
                        ? Response.ok("CalcPeriod deleted successfully").build()
                        : Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).entity("Can't find CalcPeriod with ID [" + id + "]").build()
                );
    }

}
