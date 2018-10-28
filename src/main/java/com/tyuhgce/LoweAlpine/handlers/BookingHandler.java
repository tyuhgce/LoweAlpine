package com.tyuhgce.LoweAlpine.handlers;

import com.tyuhgce.LoweAlpine.utils.Mapper;
import com.tyuhgce.LoweAlpine.utils.enums.HttpCode;
import com.tyuhgce.LoweAlpine.utils.RequestParams;
import com.tyuhgce.LoweAlpine.handlers.base.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler proceeds requests for booked queries
 *
 * @version 1.0
 * @since 1.0
 */
public class BookingHandler extends Handler implements Route {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingHandler.class);

    @Override
    public Object handle(Request request, Response response) {
        LOGGER.trace("BookingHandler.handle received request: " + Mapper.toString(request));
        RequestParams rq = new RequestParams(request);
        boolean isLocked;

        try {
            try {
                if (cachedStorage.isCacheStorageAvailable() && cachedStorage.check(rq)) {
                    LOGGER.debug("BookingHandler.handle value " + Mapper.toString(rq) + " was founded in cachedStorage");
                    return proceedRequest(response, HttpCode.LOCKED);
                }
            } catch (Exception e) {
                LOGGER.error("BookingHandler.handle the query to cachedStorage was failed", e);
            }

            isLocked = mainStorage.book(rq);
            LOGGER.debug("BookingHandler.handle place " + Mapper.toString(rq) + " was booked with result " + isLocked);
            if (!isLocked) {
                return proceedRequest(response, HttpCode.LOCKED);
            } else {
                try {
                    cachedStorage.update(rq);
                } catch (Exception e) {
                    LOGGER.error("BookingHandler.handle cant update the cachedStorage, storages are inconsistent");
                }
                return proceedRequest(response, HttpCode.SUCCESS);
            }
        } catch (Exception e) {
            LOGGER.error("BookingHandler.handle the query was failed", e);
            return proceedRequest(response, HttpCode.SERVER_ERROR);
        }
    }
}
