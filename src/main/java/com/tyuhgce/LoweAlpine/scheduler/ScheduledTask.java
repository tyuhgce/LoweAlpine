package com.tyuhgce.LoweAlpine.scheduler;

import com.google.inject.Singleton;
import com.tyuhgce.LoweAlpine.interfaces.CachedStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import java.util.TimerTask;

/**
 * runs scheduled task for init cached storage
 *
 * @version 1.0
 * @since 1.0
 */
@Singleton
public class ScheduledTask extends TimerTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);
    @Inject
    private CachedStorage CACHED_STORAGE;

    @Override
    public void run() {
        try {
            LOGGER.trace("ScheduledTask.run start run method");
            if (!CACHED_STORAGE.isCacheStorageAvailable() && !CACHED_STORAGE.isFillingInProgress()) {
                CACHED_STORAGE.init();
            }
        } catch (Exception e) {
            LOGGER.error("ScheduledTask.run cant initialize cachedStorage", e);
        }
    }
}
