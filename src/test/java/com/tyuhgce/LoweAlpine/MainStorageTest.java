package com.tyuhgce.LoweAlpine;

import com.tyuhgce.LoweAlpine.base.BaseTest;
import com.tyuhgce.LoweAlpine.utils.RequestParams;
import org.junit.Test;

import java.util.Map;

public class MainStorageTest extends BaseTest {
    @Test
    public void testBookMethodSuccess() throws Exception {
        RequestParams requestParams = new RequestParams("CINEMA", "earth", 1);
        mainStorage.clear(requestParams);
        assert mainStorage.book(requestParams);
    }

    @Test
    public void testBookMethodFailed() throws Exception {
        RequestParams requestParams = new RequestParams("CINEMA", "earth", 3);
        assert !mainStorage.book(requestParams);
    }

    @Test
    public void testGetMainStorageDataMethod() throws Exception {
        Map<String, String> map = mainStorage.getMainStorageData();
        assert map != null && !map.isEmpty();
        for (Map.Entry<String, String> entry :
                map.entrySet()) {
            assert entry.getKey().contains("|");
            assert entry.getValue().matches("[0-9]+(?:-[0-9]+)?(,[0-9]+(?:-[0-9]+)?)*");
        }
    }

    @Test
    public void testGetMethod() throws Exception {
        RequestParams requestParams = new RequestParams("CINEMA", "earth", 2);
        String result = mainStorage.get(requestParams);
        assert result.matches("[0-9]+(?:-[0-9]+)?(,[0-9]+(?:-[0-9]+)?)*");
    }

    @Test
    public void testCheckMethodSuccess() throws Exception {
        RequestParams requestParams = new RequestParams("CINEMA", "earth", 2);
        assert mainStorage.check(requestParams);
    }

    @Test
    public void testCheckMethodFailed() throws Exception {
        RequestParams requestParams = new RequestParams("CINEMA", "earth", 4);
        assert !mainStorage.check(requestParams);
    }
}
