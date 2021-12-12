package org.planning.server.api;

import org.planning.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/shifts")
@RestController
public class ShiftsResource {

    private final ShiftService shiftService;

    @Autowired
    public ShiftsResource(ShiftService shiftService) {
        this.shiftService = shiftService;
    }


}
