package com.tyuhgce.LoweAlpine.utils;

/**
 * Help class to give something params
 * it should be replaced by zookeeper or anything like that.
 *
 * @version 1.0
 * @since 1.0
 */
public class ConfigKeeper {
    public static final String TARANTOOL_URL = "127.0.0.1";
    public static final String TARANTOOL_USERNAME = "LoweAlpine";
    public static final String TARANTOOL_PASSWORD = "secret";
    public static final int TARANTOOL_SPACE = 35;
    public static final int TARANTOOL_INDEX = 0;
    public static final int TARANTOOL_PORT = 3301;

    public static final String POSTGRES_USERNAME = "postgres";
    public static final String POSTGRES_PASSWORD = "example";
    public static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/";
    public static final String POSTGRES_INIT_SCRIPT = "init.sql";
}
