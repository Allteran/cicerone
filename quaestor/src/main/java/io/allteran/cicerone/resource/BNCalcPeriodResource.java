package io.allteran.cicerone.resource;

import io.allteran.cicerone.service.BNCalcPeriodService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.annotations.Param;

@RequiredArgsConstructor
@Path("/api/v1/quaestor/bn/cperiod")
@RequestScoped
public class BNCalcPeriodResource {
    private final BNCalcPeriodService periodService;

    @GET
    @WithSession
    public Uni<Response> findAll() {
        return periodService.findAll()
                .map(data -> Response.ok(data).build());
    }
}
