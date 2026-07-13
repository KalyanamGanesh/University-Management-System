package com.UMS.University_Management_System.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.UMS.University_Management_System.entity.workersdetails;

public interface workerRepo extends JpaRepository<workersdetails, Long> {

}
