package com.tyuhgce.LoweAlpine.storages;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tyuhgce.LoweAlpine.utils.Mapper;
import com.tyuhgce.LoweAlpine.utils.RequestParams;
import com.tyuhgce.LoweAlpine.utils.jdbc.JdbcConnectorImpl;
import com.tyuhgce.LoweAlpine.handlers.PlaceHandler;
import com.tyuhgce.LoweAlpine.interfaces.MainStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * main storage example
 *
 * @version 1.0
 * @since 1.0
 */
@Singleton
public class PostgresStorage implements MainStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceHandler.class);

    private static final String BOOK_PLACE_QUERY =
            "with t as ( " +
                    "select p.id " +
                    "from public.places p " +
                    "join public.halls h on h.id = p.hall_id " +
                    "join public.cinemas ci on ci.id = h.cinema_id " +
                    "where p.state is false and p.order_id = ? and h.hall_name = ? and ci.cinema_name = ? " +
                    ") " +
                    "update public.places " +
                    "set state = true " +
                    "from t " +
                    "where public.places.id = t.id ";

    private static final String CLEAR_PLACE_QUERY =
            "with t as ( " +
                    "select p.id " +
                    "from public.places p " +
                    "join public.halls h on h.id = p.hall_id " +
                    "join public.cinemas ci on ci.id = h.cinema_id " +
                    "where p.order_id = ? and h.hall_name = ? and ci.cinema_name = ? " +
                    ") " +
                    "update public.places " +
                    "set state = false " +
                    "from t " +
                    "where public.places.id = t.id ";

    private static final String MAIN_STORAGE_INFORMATION_SELECT =
            "select concat(c.cinema_name, '|', h.hall_name), STRING_AGG(concat(p.state::int), ',' order by p.order_id) " +
                    "from public.places p " +
                    "join public.halls h on h.id = p.hall_id " +
                    "join public.cinemas c on c.id = h.cinema_id ";

    private static final String MAIN_STORAGE_INFORMATION_QUERY =
            MAIN_STORAGE_INFORMATION_SELECT +
                    "group by concat(c.cinema_name, '|', h.hall_name) ";

    private static final String CHECK_PLACE_QUERY =
            "select count(*) from public.places p " +
                    "join public.halls h on h.id = p.hall_id " +
                    "join public.cinemas c on c.id = h.cinema_id " +
                    "where p.state is false and p.order_id = ? and h.hall_name = ? and c.cinema_name = ? ";

    private static final String GET_HALL_QUERY =
            MAIN_STORAGE_INFORMATION_SELECT +
                    "where h.hall_name = ? and c.cinema_name = ? " +
                    "group by concat(c.cinema_name, '|', h.hall_name) ";


    @Inject
    private JdbcConnectorImpl jdbcConnector;


    @Override
    public Map<String, String> getMainStorageData() throws Exception {
        LOGGER.trace("PostgresStorage.getMainStorageData was called");
        Map<String, String> data = new HashMap<>();

        try (ResultSet resultSet = jdbcConnector.executeQuery(MAIN_STORAGE_INFORMATION_QUERY, (Object[]) null)) {
            while (resultSet.next()) {
                try {
                    String index = resultSet.getString(1);
                    String values = resultSet.getString(2);

                    data.put(index, values);
                } catch (Exception e) {
                    LOGGER.error("PostgresStorage.getMainStorageData cant get the values from resultSet", e);
                }
            }
        }
        return data;
    }

    @Override
    public boolean book(RequestParams requestParams) throws Exception {
        LOGGER.trace("PostgresStorage.book method was called with values: " + Mapper.toString(requestParams));

        int result = jdbcConnector.executeUpdate(
                BOOK_PLACE_QUERY,
                requestParams.getPlace(),
                requestParams.getHall(),
                requestParams.getCinema()
        );

        LOGGER.debug("PostgresStorage.book method  was return: " + result);
        return result != 0;
    }

    @Override
    public void clear(RequestParams requestParams) throws Exception {
        LOGGER.trace("PostgresStorage.clear method was called with values: " + Mapper.toString(requestParams));

        int result = jdbcConnector.executeUpdate(
                CLEAR_PLACE_QUERY,
                requestParams.getPlace(),
                requestParams.getHall(),
                requestParams.getCinema()
        );

        LOGGER.debug("PostgresStorage.book method returned: " + result);
        if (result == 0) {
            throw new IllegalArgumentException("can't update the place = " + Mapper.toString(requestParams));
        }
    }

    @Override
    public boolean check(RequestParams requestParams) throws Exception {
        LOGGER.trace("PostgresStorage.check was called with values: " + Mapper.toString(requestParams));

        ResultSet resultSet = jdbcConnector.executeQuery(CHECK_PLACE_QUERY,
                requestParams.getPlace(),
                requestParams.getHall(),
                requestParams.getCinema()
        );

        resultSet.next();
        int result = resultSet.getInt(1);

        resultSet.close();
        LOGGER.debug("PostgresStorage.check was return: " + result);
        return result == 1;
    }

    @Override
    public String get(RequestParams requestParams) throws Exception {
        LOGGER.trace("PostgresStorage.check was called with values: " + Mapper.toString(requestParams));

        ResultSet resultSet = jdbcConnector.executeQuery(GET_HALL_QUERY,
                requestParams.getHall(),
                requestParams.getCinema()
        );
        resultSet.next();
        String result = resultSet.getString(2);

        resultSet.close();
        LOGGER.debug("PostgresStorage.get was return: " + result);
        return result;
    }

    @Override
    public void close() {
        LOGGER.trace("PostgresStorage.close was called");
        if (jdbcConnector != null) {
            jdbcConnector.close();
        }
    }
}
