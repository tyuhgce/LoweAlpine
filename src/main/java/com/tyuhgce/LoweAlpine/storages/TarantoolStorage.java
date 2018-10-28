package com.tyuhgce.LoweAlpine.storages;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tyuhgce.LoweAlpine.utils.Mapper;
import com.tyuhgce.LoweAlpine.utils.RequestParams;
import com.tyuhgce.LoweAlpine.interfaces.CachedStorage;
import com.tyuhgce.LoweAlpine.interfaces.MainStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tarantool.TarantoolClientImpl;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.tyuhgce.LoweAlpine.utils.ConfigKeeper.TARANTOOL_INDEX;
import static com.tyuhgce.LoweAlpine.utils.ConfigKeeper.TARANTOOL_SPACE;

/**
 * cached storage example
 *
 * @version 1.0
 * @since 1.0
 */
@Singleton
public class TarantoolStorage implements CachedStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(TarantoolStorage.class);

    @Inject
    private TarantoolClientImpl TARANTOOL_CLIENT;
    @Inject
    private MainStorage MAIN_STORAGE;

    @Override
    public void close() {
        LOGGER.trace("TarantoolStorage.close was called");
        if (TARANTOOL_CLIENT != null) {
            TARANTOOL_CLIENT.close();
        }
    }

    @Override
    public void init() {
        LOGGER.trace("TarantoolStorage.init was called");
        setCacheStorageAvailable(false);
        StorageHandler.isFillingInProgress = true;
        Map<String, String> data;
        try {
            data = MAIN_STORAGE.getMainStorageData();
        } catch (Exception e) {
            LOGGER.error("TarantoolStorage.initcant getMainStorageData from MainStorage", e);
            return;
        }
        LOGGER.debug("TarantoolStorage.init returned " + Mapper.toString(data));

        ArrayList<Future<List<?>>> futures = new ArrayList<>();
        for (Map.Entry<String, String> entry :
                data.entrySet()) {
            Future<List<?>> future = TARANTOOL_CLIENT.replace(
                    TARANTOOL_SPACE,
                    Arrays.asList(entry.getKey(), entry.getValue())
            );
            futures.add(future);
        }
        for (Future<List<?>> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                LOGGER.error("TarantoolStorage.init cant insert in cachedStorage ", e);
            }
        }
        StorageHandler.isFillingInProgress = false;
        setCacheStorageAvailable(true);
    }

    @Override
    public void update(RequestParams requestParams) throws Exception {
        LOGGER.trace("TarantoolStorage.update was called");

        String hall = get(requestParams);
        int place = getPlace(requestParams.getPlace());
        hall = replaceChar(hall, place, '1');

        Future<List<?>> future = TARANTOOL_CLIENT.replace(
                TARANTOOL_SPACE,
                Arrays.asList(requestParams.getKey(), hall)
        );
        future.get();
    }

    private String getList(String key) throws ExecutionException, InterruptedException {
        LOGGER.trace("TarantoolStorage.getList was called with key = " + key);
        List<?> data = TARANTOOL_CLIENT
                .select(
                        TARANTOOL_SPACE,
                        TARANTOOL_INDEX,
                        Collections.singletonList(key),
                        0,
                        1,
                        0)
                .get();
        LOGGER.debug("TarantoolStorage.getList received " + Mapper.toString(data));
        ArrayList list = (ArrayList) data.get(0);
        return (String) list.get(1);
    }


    @Override
    public boolean check(RequestParams requestParams) throws Exception {
        LOGGER.trace("TarantoolStorage.check method was called");
        return isBooked(getList(requestParams.getKey()), requestParams.getPlace());
    }

    @Override
    public String get(RequestParams requestParams) throws Exception {
        LOGGER.trace("TarantoolStorage.get method was called");
        return getList(requestParams.getKey());
    }

    @Override
    public void clear(RequestParams requestParams) throws Exception {
        LOGGER.trace("TarantoolStorage.clear was called");

        String hall = get(requestParams);
        int place = getPlace(requestParams.getPlace());
        hall = replaceChar(hall, place, '0');

        Future<List<?>> future = TARANTOOL_CLIENT.replace(
                TARANTOOL_SPACE,
                Arrays.asList(requestParams.getKey(), hall)
        );
        future.get();
    }

    private String replaceChar(String str, int position, char symbol) {
        return str.substring(0, position) + symbol + str.substring(position + 1);
    }

    private int getPlace(int place) {
        return 2 * place - 2;
    }

    private boolean isBooked(String places, int place) {
        return places.charAt(getPlace(place)) == '1';
    }

    // StorageHandler
    private static class StorageHandler {
        private static final Logger LOGGER = LoggerFactory.getLogger(StorageHandler.class);
        private static volatile boolean cacheStorageAvailable = false;
        private static volatile boolean isFillingInProgress = false;

        private static boolean isCacheStorageAvailable() {
            LOGGER.trace("TarantoolStorage.StorageHandler.isCacheStorageAvailable: " + cacheStorageAvailable);
            return cacheStorageAvailable;
        }

        static boolean isIsFillingInProgress() {
            LOGGER.trace("TarantoolStorage.StorageHandler.isFillingInProgress: " + isFillingInProgress);
            return isFillingInProgress;
        }

        private static void setCacheStorageAvailable(boolean cacheStorageAvailable) {
            LOGGER.trace("TarantoolStorage.StorageHandler.isCacheStorageAvailable changes from " + StorageHandler.cacheStorageAvailable + " to " + cacheStorageAvailable);
            StorageHandler.cacheStorageAvailable = cacheStorageAvailable;
        }
    }

    @Override
    public boolean isCacheStorageAvailable() {
        return StorageHandler.isCacheStorageAvailable();
    }

    @Override
    public boolean isFillingInProgress() {
        return StorageHandler.isIsFillingInProgress();
    }

    @Override
    public void setCacheStorageAvailable(boolean cacheStorageAvailable) {
        StorageHandler.setCacheStorageAvailable(cacheStorageAvailable);
    }
}
