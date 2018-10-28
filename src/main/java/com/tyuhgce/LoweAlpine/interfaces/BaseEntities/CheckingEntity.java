package com.tyuhgce.LoweAlpine.interfaces.BaseEntities;

import com.tyuhgce.LoweAlpine.utils.RequestParams;

public interface CheckingEntity {
    /**
     * checks the specific place in hall of cinema
     *
     * @param requestParams it contains place number, hall name and cinema name
     * @return true if place is booked, false otherwise
     */
    boolean check(RequestParams requestParams) throws Exception;

    /**
     * returns all places in specific hall of cinema
     *
     * @param requestParams it contains place number, hall name and cinema name
     * @return all places in hall in format "0,1,0,1,1,0"
     */
    String get(RequestParams requestParams) throws Exception;
}
