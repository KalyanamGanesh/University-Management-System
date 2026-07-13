package com.UMS.University_Management_System.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.UMS.University_Management_System.entity.facultydetails;

public interface facultyRepo extends JpaRepository<facultydetails, Long> {

}
