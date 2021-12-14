package org.planning.server.api.v1.dto;

import org.planning.model.DayShift;

import java.time.Instant;

public class ShiftCreateRequestDto {

    /**
     * UTC date of the shift
     */
    public Instant date;

    public DayShift dayShift;

}
