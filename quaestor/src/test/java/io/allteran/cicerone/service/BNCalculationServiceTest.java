package io.allteran.cicerone.service;

import io.allteran.cicerone.dto.BNRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@QuarkusTest
class BNCalculationServiceTest {
    @Inject
    BNCalculationService calculationService;

}