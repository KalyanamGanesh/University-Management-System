
package com.UMS.University_Management_System.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.UMS.University_Management_System.entity.workersdetails;
import com.UMS.University_Management_System.repository.workerRepo;

@Service
public class workerDetailsImplementation implements workerDetailsService {
    workerRepo workerrepo;

    public workerDetailsImplementation(workerRepo workerrepo) {
        this.workerrepo = workerrepo;
    }

    public String createWorkerDetail(workersdetails workerdetail) {
        workerrepo.save(workerdetail);
        return "Created worker detail successfully";
    }

    public String deleteworkerDetail(Long workerId) {
        if (workerrepo.existsById(workerId)) {
            workerrepo.deleteById(workerId);
            return "Worker detail deleted successfully";
        }
        return "Worker not found";
    }

    public List<workersdetails> getAllWorkers() {
        return workerrepo.findAll();
    }

    public workersdetails getWorkerdetail(Long workerId) {
        return workerrepo.findById(workerId).orElse(null);
    }

    public String updateWorkerDetail(Long workerId, workersdetails updatedworkerdetails) {
        if (workerrepo.existsById(workerId)) {
            updatedworkerdetails.setWorkerId(workerId);
            workerrepo.save(updatedworkerdetails);
            return "Worker detail updated successfully";
        }
        return "Worker not found";
    }
}
