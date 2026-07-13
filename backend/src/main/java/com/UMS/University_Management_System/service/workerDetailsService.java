package com.UMS.University_Management_System.service;

import java.util.List;

import com.UMS.University_Management_System.entity.workersdetails;

public interface workerDetailsService {

    String createWorkerDetail(workersdetails workerdetail);

    String updateWorkerDetail(Long workerId, workersdetails updatedworkerdetails);

    String deleteworkerDetail(Long workerId);

    workersdetails getWorkerdetail(Long workerId);

    List<workersdetails> getAllWorkers();

}
