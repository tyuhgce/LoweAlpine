package com.tyuhgce.LoweAlpine.utils.jdbc;

import com.google.inject.Singleton;
import com.tyuhgce.LoweAlpine.utils.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.stream.Collectors;

import static com.tyuhgce.LoweAlpine.utils.ConfigKeeper.*;

@Singleton
public class JdbcConnectorImpl implements JdbcConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConnectorImpl.class);
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            LOGGER.trace("Driver loaded");
            buildConnection();
        } catch (Exception ex) {
            LOGGER.error("JdbcConnectorImpl cant init postgres driver", ex);
        }
    }

    private void prepareStatement(PreparedStatement preparedStmt, Object[] args) throws SQLException {
        LOGGER.trace("JdbcConnectorImpl.prepareStatement was called, args = " + Mapper.toString(args));
        for (int i = 1; i <= args.length; i++) {
            Object arg = args[i - 1];
            if (arg instanceof Integer) {
                preparedStmt.setInt(i, (Integer) arg);
            } else if (arg instanceof String) {
                preparedStmt.setString(i, (String) arg);
            } else {
                throw new IllegalArgumentException("argument " + arg + " cant be casted in String or Integer");
            }
        }
    }

    private static void buildConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            if (connection != null) {
                connection.close();
            }
            // if we doesn't connect to main server, the application is paralyzed, we can't move forward
            // we're trying to reconnect here!
            while (true) {
                try {
                    connection =
                            DriverManager.getConnection(POSTGRES_URL, POSTGRES_USERNAME, POSTGRES_PASSWORD);
                    break;
                } catch (Exception e) {
                    LOGGER.error("JdbcConnectorImpl.buildConnection cant connect to database, trying again!");
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException ex) {
                        LOGGER.error("JdbcConnectorImpl.buildConnection application throw exception while thread sleeping: ", ex);
                    }
                }
            }
        }
    }

    @Override
    public int executeUpdate(String query, Object... args) throws SQLException {
        LOGGER.trace("JdbcConnectorImpl.executeUpdate was called, args = " + Mapper.toString(args));
        buildConnection();
        PreparedStatement preparedStmt = connection.prepareStatement(query);

        if (args != null && args.length != 0) {
            prepareStatement(preparedStmt, args);
        }
        return preparedStmt.executeUpdate();
    }


    @Override
    public ResultSet executeQuery(String query, Object... args) throws SQLException {
        LOGGER.trace("JdbcConnectorImpl.executeUpdate was called, args = " + Mapper.toString(args));
        buildConnection();
        PreparedStatement preparedStmt = connection.prepareStatement(query);

        if (args != null && args.length != 0) {
            prepareStatement(preparedStmt, args);
        }
        return preparedStmt.executeQuery();
    }

    @Override
    public void executeQuery(String file) throws SQLException {
        LOGGER.trace("JdbcConnectorImpl.executeQuery was called, args = " + file);
        buildConnection();
        String sb = "";

        try {
            FileReader fr = new FileReader(new File("src/main/resources/postgresApp/" + POSTGRES_INIT_SCRIPT));

            try (BufferedReader br = new BufferedReader(fr)) {
                sb = br.lines()
                        .collect(Collectors.joining());
            }

            String[] inst = sb.split(";");
            Statement st = connection.createStatement();

            for (String anInst : inst) {
                if (!anInst.trim().equals("")) {
                    st.executeUpdate(anInst);
                    LOGGER.debug(">>" + anInst);
                }
            }

        } catch (Exception e) {
            LOGGER.error("*** Error : " + e.toString(), e);
            LOGGER.error(sb);
        }
    }

    @Override
    public void close() {
        LOGGER.trace("JdbcConnectorImpl.close was called");
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error("JdbcConnectorImpl.close cant close connection with MainStorage", e);
        }
    }
}
