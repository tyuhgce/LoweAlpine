package com.tyuhgce.LoweAlpine.interfaces.BaseEntities;

import com.tyuhgce.LoweAlpine.utils.RequestParams;

public interface BookingEntity {
    /**
     * books place in hall of cinema
     *
     * @param requestParams it contains place number, hall name and cinema name
     * @return true if place is booked successfully, false otherwise
     */
    boolean book(RequestParams requestParams) throws Exception;
}
