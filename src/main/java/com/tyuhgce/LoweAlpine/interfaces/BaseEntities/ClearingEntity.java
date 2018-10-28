package com.tyuhgce.LoweAlpine.interfaces.BaseEntities;

import com.tyuhgce.LoweAlpine.utils.RequestParams;

public interface ClearingEntity {
    /**
     * clears specific place in hall of cinema
     *
     * @param requestParams it contains place number, hall name and cinema name
     */
    void clear(RequestParams requestParams) throws Exception;
}
