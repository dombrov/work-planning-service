package org.planning.server.api.v1;

import org.planning.model.Shift;
import org.planning.server.api.v1.dto.ShiftDto;
import org.planning.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/shifts")
@RestController
public class ShiftsResource {

    private final ShiftService shiftService;

    @Autowired
    public ShiftsResource(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping()
    public ResponseEntity getShifts(@RequestParam("from") Instant from, @RequestParam("to") Instant to) {
        Collection<Shift> shifts = shiftService.getShifts(from, to);
        if (shifts != null && !shifts.isEmpty()) {
            Collection<ShiftDto> shiftsDto = shifts.stream()
                    .map(ShiftDto::of)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(shiftsDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
