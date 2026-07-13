package com.UMS.University_Management_System.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.UMS.University_Management_System.entity.workersdetails;
import com.UMS.University_Management_System.service.workerDetailsService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/workersdetails")
public class ControllerWorkerApi {
    workerDetailsService workerdetailsService;

    public ControllerWorkerApi(workerDetailsService workerdetailsService) {
        this.workerdetailsService = workerdetailsService;
    }

    @GetMapping
    public List<workersdetails> getAllWorkers() {
        return workerdetailsService.getAllWorkers();
    }

    @GetMapping("/{workerId}")
    public workersdetails getWorkerDetail(@PathVariable Long workerId) {
        return workerdetailsService.getWorkerdetail(workerId);

    }

    @PostMapping
    public String createWorkerDetails(@RequestBody workersdetails workerdetail) {
        return workerdetailsService.createWorkerDetail(workerdetail);
    }

    @PutMapping("/{workerId}")
    public String updateWorkerDetail(@PathVariable Long workerId, @RequestBody workersdetails updatedworkerdetail) {
        return workerdetailsService.updateWorkerDetail(workerId, updatedworkerdetail);
    }

    @DeleteMapping("/{workerId}")
    public String deleteWorkerDetail(@PathVariable Long workerId) {
        return workerdetailsService.deleteworkerDetail(workerId);
    }
}
