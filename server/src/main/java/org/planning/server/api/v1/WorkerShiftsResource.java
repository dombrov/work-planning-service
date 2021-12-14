package org.planning.server.api.v1;

import org.planning.model.Shift;
import org.planning.server.api.v1.dto.ShiftCreateRequestDto;
import org.planning.server.api.v1.dto.ShiftDto;
import org.planning.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

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
            Collection<ShiftDto> shiftsDto = shifts.stream()
                    .map(ShiftDto::of)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(shiftsDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{shiftId}")
    public ResponseEntity<ShiftDto> getWorkerShift(@PathVariable("workerId") String workerId, @PathVariable("shiftId") Long shiftId) {
        return shiftService.get(workerId, shiftId)
                .map(shift -> ResponseEntity.ok(ShiftDto.of(shift)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ShiftDto> addWorkerShift(@PathVariable("workerId") String workerId, @RequestBody ShiftCreateRequestDto requestDto) {
        Shift shift = shiftService.add(requestDto.date, requestDto.dayShift, workerId);
        return ResponseEntity.ok(ShiftDto.of(shift));
    }

    @DeleteMapping("{shiftId}")
    public void deleteWorkerShift(@PathVariable("workerId") String workerId, @PathVariable("shiftId") Long shiftId) {
        shiftService.delete(workerId, shiftId);
    }
}
