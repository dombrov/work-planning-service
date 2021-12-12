package org.planning.server.api;

import org.planning.model.Worker;
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
    public ResponseEntity<Worker> getWorker(@PathVariable("id") String id) {
        Worker worker = workerService.get(id);
        if (worker != null) {
            return ResponseEntity.ok(worker);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Worker> addWorker(@RequestBody Worker worker) {
        Worker persistedWorker = workerService.add(worker);
        return ResponseEntity.ok(persistedWorker);
    }

    @PutMapping("{id}")
    public ResponseEntity<Worker> updateWorker(@PathVariable("id") String id, @RequestBody Worker worker) {
        Worker persistedWorker = workerService.update(new Worker(id, worker.getFirstName(), worker.getLastName(), worker.isActive()));
        return ResponseEntity.ok(persistedWorker);
    }
}
