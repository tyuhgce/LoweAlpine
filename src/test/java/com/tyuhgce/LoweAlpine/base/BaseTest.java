package com.tyuhgce.LoweAlpine.base;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import com.tyuhgce.LoweAlpine.RestApi;
import com.tyuhgce.LoweAlpine.utils.jdbc.JdbcConnector;
import com.tyuhgce.LoweAlpine.interfaces.CachedStorage;
import com.tyuhgce.LoweAlpine.interfaces.MainStorage;
import com.tyuhgce.LoweAlpine.ioc.BasicModule;
import org.joda.time.Duration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import spark.Spark;

import java.io.IOException;

import static com.tyuhgce.LoweAlpine.utils.ConfigKeeper.POSTGRES_INIT_SCRIPT;

public abstract class BaseTest {
    private static final boolean areContainersRunManually = false;

    @ClassRule
    public static DockerComposeRule docker = areContainersRunManually ?
            null :
            DockerComposeRule.builder()
                    .file("src/main/resources/docker-compose.yaml")
                    .waitingForService("db",
                            HealthChecks.toHaveAllPortsOpen(),
                            Duration.standardSeconds(30)
                    )
                    .waitingForService("tarantool",
                            HealthChecks.toHaveAllPortsOpen(),
                            Duration.standardSeconds(30))
                    .shutdownStrategy(ShutdownStrategy.GRACEFUL)
                    .build();

    protected static MainStorage mainStorage;
    protected static CachedStorage cachedStorage;

    @BeforeClass
    public static void initMainStorage() throws Exception {
        Injector injector = Guice.createInjector(new BasicModule());
        mainStorage = injector.getInstance(MainStorage.class);
        cachedStorage = injector.getInstance(CachedStorage.class);
        if (!areContainersRunManually) {
            JdbcConnector jdbcConnector = injector.getInstance(JdbcConnector.class);
            jdbcConnector.executeQuery(POSTGRES_INIT_SCRIPT);
            cachedStorage.init();
        }
        RestApi.main(null);
        Spark.awaitInitialization();
    }

    @AfterClass
    public static void close() throws IOException {
        if (!areContainersRunManually) {
            mainStorage.close();
            cachedStorage.close();
        }
        Spark.stop();
    }
}
