package com.UMS.University_Management_System.service;

import java.util.List;

import com.UMS.University_Management_System.entity.facultydetails;

public interface facultyDetailsService {
    String createFacultyDetails(facultydetails facultydetail);

    String updateFacultyDetails(Long facultyId, facultydetails updatedFacultydetails);

    String deleteFacultyDetails(Long facultyId);

    facultydetails getFacultyDetail(Long facultyId);

    List<facultydetails> getAllFaculty();
}
