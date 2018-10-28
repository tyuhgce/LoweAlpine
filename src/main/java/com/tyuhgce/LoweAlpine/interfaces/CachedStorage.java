package com.tyuhgce.LoweAlpine.interfaces;

import com.tyuhgce.LoweAlpine.interfaces.BaseEntities.ClearingEntity;
import com.tyuhgce.LoweAlpine.utils.RequestParams;
import com.tyuhgce.LoweAlpine.interfaces.BaseEntities.CheckingEntity;

import java.io.Closeable;

public interface CachedStorage extends CheckingEntity, ClearingEntity, Closeable {
    /**
     * initializes Cached Storage,
     * cached storage pulls data from original storage
     */
    void init();

    /**
     * updates specific place in hall of cinema
     *
     * @param requestParams it contains place number, hall name and cinema name
     */
    void update(RequestParams requestParams) throws Exception;

    /**
     * checks available of cached storage
     *
     * @return true if storage available, false otherwise
     */
    boolean isCacheStorageAvailable();

    /**
     * check if cached storage is filling
     *
     * @return true if storage is filling, false otherwise
     */
    boolean isFillingInProgress();

    /**
     * allows control the cached storage available
     *
     * @param cacheStorageAvailable true if storage available, false otherwise
     */
    void setCacheStorageAvailable(boolean cacheStorageAvailable);
}
