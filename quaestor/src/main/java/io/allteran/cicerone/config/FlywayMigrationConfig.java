package io.allteran.cicerone.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;

@ApplicationScoped
public class FlywayMigrationConfig {
    @ConfigProperty(name = "quarkus.datasource.reactive.url")
    String DB_URL;
    @ConfigProperty(name = "quarkus.datasource.username")
    String DB_USERNAME;
    @ConfigProperty(name = "quarkus.datasource.password")
    String DB_PASSWORD;

    public void runFlywayMigration(@Observes StartupEvent event) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:" + DB_URL, DB_USERNAME, DB_PASSWORD)
                .load();
        flyway.migrate();
    }
}
