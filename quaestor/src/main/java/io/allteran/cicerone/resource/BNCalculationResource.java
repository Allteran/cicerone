package io.allteran.cicerone.resource;

import io.allteran.cicerone.dto.BNRequest;
import io.allteran.cicerone.service.BNCalculationService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
@Path("/api/v1/quaestor/calc/bn")
public class BNCalculationResource {
    private final BNCalculationService calcService;

    @POST
    @Path("test/uop")
    @Consumes(MediaType.APPLICATION_JSON)
    @WithSession
    public Uni<Response> rawUoPTest(BNRequest request) {
        log.info("Testing raw calculation:\nBNRequest = " + request);
        return calcService.calculateUopTest(request).onItem().transform(data -> Response.ok(data).build());
    }
}
