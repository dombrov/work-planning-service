package org.planning.server.api.v1;

import org.planning.model.Worker;
import org.planning.server.api.v1.dto.WorkerDto;
import org.planning.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/workers")
@RestController
public class WorkerResource {

    private final WorkerService workerService;

    @Autowired
    public WorkerResource(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("{id}")
    public ResponseEntity<WorkerDto> getWorker(@PathVariable("id") String id) {
        return workerService.get(id)
                .map(worker -> ResponseEntity.ok(WorkerDto.of(worker)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WorkerDto> addWorker(@RequestBody Worker worker) {
        Worker persistedWorker = workerService.add(worker);
        return ResponseEntity.ok(WorkerDto.of(persistedWorker));
    }

    @PutMapping("{id}")
    public ResponseEntity<WorkerDto> updateWorker(@PathVariable("id") String id, @RequestBody Worker worker) {
        Worker persistedWorker = workerService.update(new Worker(id, worker.getFirstName(), worker.getLastName(), worker.isActive()));
        return ResponseEntity.ok(WorkerDto.of(persistedWorker));
    }
}
