package org.planning.server.api;

import org.planning.model.Shift;
import org.planning.server.api.dto.ExceptionDto;
import org.planning.server.api.dto.ShiftCreateRequestDto;
import org.planning.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;

@RequestMapping("/api/v1/workers/{workerId}/shifts")
@RestController
public class WorkerShiftsResource {

    private final ShiftService shiftService;

    @Autowired
    public WorkerShiftsResource(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping()
    public ResponseEntity getWorkerShifts(@PathVariable("workerId") String workerId, @RequestParam("from") Instant from, @RequestParam("to") Instant to) {
        Collection<Shift> shifts = shiftService.getShifts(workerId, from, to);
        if (shifts != null && !shifts.isEmpty()) {
            return ResponseEntity.ok(shifts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{shiftId}")
    public ResponseEntity<Shift> getWorkerShift(@PathVariable("workerId") String workerId, @PathVariable("shiftId") Long shiftId) {
        Shift shift = shiftService.get(workerId, shiftId);
        if (shift != null) {
            return ResponseEntity.ok(shift);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Shift> addWorkerShift(@PathVariable("workerId") String workerId, @RequestBody ShiftCreateRequestDto requestDto) {
        Shift shift = shiftService.add(requestDto.date, requestDto.dayShift, workerId);
        return ResponseEntity.ok(shift);
    }

    @DeleteMapping
    public void deleteWorkerShift(@PathVariable("workerId") String workerId, @PathVariable("shiftId") Long shiftId) {
        shiftService.delete(workerId, shiftId);
    }
}
