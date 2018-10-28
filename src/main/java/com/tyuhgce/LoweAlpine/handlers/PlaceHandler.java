package com.tyuhgce.LoweAlpine.handlers;

import com.tyuhgce.LoweAlpine.utils.Mapper;
import com.tyuhgce.LoweAlpine.utils.RequestParams;
import com.tyuhgce.LoweAlpine.utils.enums.HttpCode;
import com.tyuhgce.LoweAlpine.handlers.base.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler proceeds requests for place queries
 *
 * @version 1.0
 * @since 1.0
 */
public class PlaceHandler extends Handler implements Route {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceHandler.class);

    @Override
    public Object handle(Request request, Response response) {
        LOGGER.trace("PlaceHandler.handle receive request: " + Mapper.toString(request));
        RequestParams rq = new RequestParams(request);

        try {
            try {
                if (cachedStorage.isCacheStorageAvailable() && cachedStorage.check(rq)) {
                    LOGGER.debug("PlaceHandler.handle value " + Mapper.toString(rq) + " was founded in cachedStorage");
                    return proceedRequest(response, HttpCode.LOCKED);
                }
            } catch (Exception e) {
                LOGGER.error("PlaceHandler.handle query to cachedStorage was failed, trying to receive from mainStorage", e);
                if (mainStorage.check(rq)) {
                    LOGGER.debug("PlaceHandler.handle value " + Mapper.toString(rq) + " was founded in mainStorage, it is locked");
                    return proceedRequest(response, HttpCode.LOCKED);
                }
            }
            return proceedRequest(response, HttpCode.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("PlaceHandler.handle the query was failed", e);
            return proceedRequest(response, HttpCode.SERVER_ERROR);
        }
    }
}
