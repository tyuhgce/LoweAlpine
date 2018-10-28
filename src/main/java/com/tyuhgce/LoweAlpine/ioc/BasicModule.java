package com.tyuhgce.LoweAlpine.ioc;

import com.google.inject.AbstractModule;
import com.tyuhgce.LoweAlpine.utils.TarantoolSocketProvider;
import com.tyuhgce.LoweAlpine.utils.jdbc.JdbcConnector;
import com.tyuhgce.LoweAlpine.utils.jdbc.JdbcConnectorImpl;
import com.tyuhgce.LoweAlpine.interfaces.CachedStorage;
import com.tyuhgce.LoweAlpine.interfaces.MainStorage;
import com.tyuhgce.LoweAlpine.scheduler.ScheduledTask;
import com.tyuhgce.LoweAlpine.storages.PostgresStorage;
import com.tyuhgce.LoweAlpine.storages.TarantoolStorage;
import org.tarantool.TarantoolClientConfig;
import org.tarantool.TarantoolClientImpl;

import java.util.TimerTask;

import static com.tyuhgce.LoweAlpine.utils.ConfigKeeper.TARANTOOL_PASSWORD;
import static com.tyuhgce.LoweAlpine.utils.ConfigKeeper.TARANTOOL_USERNAME;

/**
 * builds ioc dependencies
 *
 * @version 1.0
 * @since 1.0
 */
public class BasicModule extends AbstractModule {
    private TarantoolClientImpl buildClient() {
        TarantoolClientConfig config = new TarantoolClientConfig();
        config.username = TARANTOOL_USERNAME;
        config.password = TARANTOOL_PASSWORD;
        return new TarantoolClientImpl(new TarantoolSocketProvider(), config);
    }

    @Override
    protected void configure() {
        bind(JdbcConnector.class).to(JdbcConnectorImpl.class);
        bind(TarantoolClientImpl.class).toInstance(buildClient());
        bind(CachedStorage.class).to(TarantoolStorage.class);
        bind(MainStorage.class).to(PostgresStorage.class);
        bind(TimerTask.class).to(ScheduledTask.class);
    }
}
