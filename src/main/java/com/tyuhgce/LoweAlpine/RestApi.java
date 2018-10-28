package com.tyuhgce.LoweAlpine;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tyuhgce.LoweAlpine.handlers.BookingHandler;
import com.tyuhgce.LoweAlpine.handlers.GetHandler;
import com.tyuhgce.LoweAlpine.handlers.PlaceHandler;
import com.tyuhgce.LoweAlpine.ioc.BasicModule;
import com.tyuhgce.LoweAlpine.scheduler.ScheduledTask;

import java.util.Timer;

import static spark.Spark.get;

/**
 * starting point of rest api
 *
 * @version 1.0
 * @since 1.0
 */
public class RestApi {
    private static final Timer TIMER = new Timer();

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BasicModule());
        TIMER.schedule(injector.getInstance(ScheduledTask.class), 0, 1000); // Создаем задачу с повторением через 1 сек.

        GetHandler getHandler = injector.getInstance(GetHandler.class);
        PlaceHandler placeHandler = injector.getInstance(PlaceHandler.class);
        BookingHandler bookingHandler = injector.getInstance(BookingHandler.class);

        get("get/:cinema/:hall", getHandler);
        get("check/:cinema/:hall/:place", placeHandler);
        get("book/:cinema/:hall/:place", bookingHandler);

        get("/ping", (req, res) -> "pong");
    }
}
