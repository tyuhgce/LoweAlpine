package com.tyuhgce.LoweAlpine.utils;

import org.tarantool.SocketChannelProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static com.tyuhgce.LoweAlpine.utils.ConfigKeeper.TARANTOOL_PORT;
import static com.tyuhgce.LoweAlpine.utils.ConfigKeeper.TARANTOOL_URL;

/**
 * Socket provider for tarantool app
 *
 * @version 1.0
 * @since 1.0
 */
public class TarantoolSocketProvider implements SocketChannelProvider {
    @Override
    public SocketChannel get(int retryNumber, Throwable lastError) {
        if (lastError != null) {
            lastError.printStackTrace(System.out);
        }
        try {
            return SocketChannel.open(new InetSocketAddress(TARANTOOL_URL, TARANTOOL_PORT));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
