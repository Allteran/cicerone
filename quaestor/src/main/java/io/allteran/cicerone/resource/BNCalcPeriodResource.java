package io.allteran.cicerone.resource;

import io.allteran.cicerone.service.BNCalcPeriodService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
                .map(data -> Response.ok(data).build());
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
                .recoverWithItem(throwable -> {
                    log.info("onFailure.recoverWithItem");
                    return Response.status(Response.Status.NOT_FOUND)
                            .type(MediaType.TEXT_PLAIN_TYPE)
                            .entity(throwable.getMessage()).build();
                });
    }
}
