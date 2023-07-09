package io.allteran.cicerone.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@QuarkusTest
class BNCalculationServiceTest {
    @Inject
    BNCalculationService calculationService;

}