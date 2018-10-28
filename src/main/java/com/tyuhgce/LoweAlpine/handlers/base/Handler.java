package com.tyuhgce.LoweAlpine.handlers.base;

import com.tyuhgce.LoweAlpine.utils.enums.HttpCode;
import com.tyuhgce.LoweAlpine.interfaces.CachedStorage;
import com.tyuhgce.LoweAlpine.interfaces.MainStorage;
import spark.Response;

import javax.inject.Inject;

/**
 * Handler proceeds requests for queries
 *
 * @version 1.0
 * @since 1.0
 */
public abstract class Handler {
    @Inject
    protected CachedStorage cachedStorage;
    @Inject
    protected MainStorage mainStorage;

    protected Object proceedRequest(Response response, HttpCode httpCode) {
        response.status(httpCode.getCode());
        return httpCode.getDescription();
    }

    protected Object proceedRequest(Response response, HttpCode httpCode, String body) {
        response.status(httpCode.getCode());
        return body;
    }
}
