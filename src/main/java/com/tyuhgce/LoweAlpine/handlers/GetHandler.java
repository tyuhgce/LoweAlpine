package com.tyuhgce.LoweAlpine.handlers;

import com.tyuhgce.LoweAlpine.handlers.base.Handler;
import com.tyuhgce.LoweAlpine.utils.Mapper;
import com.tyuhgce.LoweAlpine.utils.RequestParams;
import com.tyuhgce.LoweAlpine.utils.enums.HttpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler proceeds requests for get queries
 *
 * @version 1.0
 * @since 1.0
 */
public class GetHandler extends Handler implements Route {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetHandler.class);

    @Override
    public Object handle(Request request, Response response) {
        LOGGER.trace("GetHandler.handle received request: " + Mapper.toString(request));
        RequestParams rq = new RequestParams(request);
        try {
            try {
                if (cachedStorage.isCacheStorageAvailable()) {
                    String result = cachedStorage.get(rq);
                    LOGGER.debug("GetHandler.handle value " + Mapper.toString(rq) + " was founded in cachedStorage with result " + result);
                    return proceedRequest(response, HttpCode.SUCCESS, result);
                }
            } catch (Exception e) {
                LOGGER.error("GetHandler.handle the query to cachedStorage was failed", e);

                String result = mainStorage.get(rq);
                LOGGER.debug("GetHandler.handle value " + Mapper.toString(rq) + " was founded in mainStorage with result " + result);
                return proceedRequest(response, HttpCode.SUCCESS, result);
            }
            String result = mainStorage.get(rq);
            LOGGER.debug("GetHandler.handle value " + Mapper.toString(rq) + " was founded in cachedStorage with result " + result);
            return proceedRequest(response, HttpCode.SUCCESS, result);
        } catch (Exception e) {
            LOGGER.error("GetHandler.handle the query was failed", e);
            return proceedRequest(response, HttpCode.SERVER_ERROR);
        }
    }
}
