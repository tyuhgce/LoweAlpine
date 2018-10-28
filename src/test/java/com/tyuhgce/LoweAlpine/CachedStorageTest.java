package com.tyuhgce.LoweAlpine;

import com.tyuhgce.LoweAlpine.base.BaseTest;
import com.tyuhgce.LoweAlpine.utils.RequestParams;
import org.junit.Test;

public class CachedStorageTest extends BaseTest {
    @Test
    public void testGetMethod() throws Exception {
        RequestParams requestParams = new RequestParams("CINEMA", "earth", 1);
        String result = cachedStorage.get(requestParams);
        assert result.matches("[0-9]+(?:-[0-9]+)?(,[0-9]+(?:-[0-9]+)?)*");
    }

    @Test
    public void testCheckMethodSuccess() throws Exception {
        RequestParams requestParams = new RequestParams("CINEMA", "earth", 4);
        assert cachedStorage.check(requestParams);
    }

    @Test
    public void testCheckMethodFailed() throws Exception {
        RequestParams requestParams = new RequestParams("CINEMA", "earth", 1);
        cachedStorage.clear(requestParams);
        assert !cachedStorage.check(requestParams);
    }

    @Test
    public void testUpdateMethod() throws Exception {
        RequestParams requestParams = new RequestParams("CINEMA", "earth", 2);
        cachedStorage.clear(requestParams);
        assert !cachedStorage.check(requestParams);

        cachedStorage.update(requestParams);
        assert cachedStorage.check(requestParams);
    }
}
