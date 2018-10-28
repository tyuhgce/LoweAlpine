package com.tyuhgce.LoweAlpine.interfaces;

import com.tyuhgce.LoweAlpine.interfaces.BaseEntities.BookingEntity;
import com.tyuhgce.LoweAlpine.interfaces.BaseEntities.CheckingEntity;
import com.tyuhgce.LoweAlpine.interfaces.BaseEntities.ClearingEntity;

import java.io.Closeable;
import java.util.Map;

public interface MainStorage extends CheckingEntity, BookingEntity, ClearingEntity, Closeable {
    /**
     * returns all rows from MainStorage
     *
     * @return map with following strings: "cinema|hall" -> "1,0,0,0,1,1"
     */
    Map<String, String> getMainStorageData() throws Exception;
}
