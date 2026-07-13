package com.UMS.University_Management_System.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.UMS.University_Management_System.entity.facultydetails;
import com.UMS.University_Management_System.repository.facultyRepo;

@Service
public class facultyDetailsImplementation implements facultyDetailsService {

    facultyRepo facultyrepo;

    public facultyDetailsImplementation(facultyRepo facultyrepo) {
        this.facultyrepo = facultyrepo;
    }

    public String createFacultyDetails(facultydetails facultydetail) {
        // TODO Auto-generated method stub
        facultyrepo.save(facultydetail);
        return "Faculty details created successfully";
    }

    public String deleteFacultyDetails(Long facultyId) {
        // TODO Auto-generated method stub
        if (facultyrepo.existsById(facultyId)) {
            facultyrepo.deleteById(facultyId);
            return "deleted successfully";
        }
        return "faculty not found";
    }

    public List<facultydetails> getAllFaculty() {
        // TODO Auto-generated method stub
        return facultyrepo.findAll();
    }

    public facultydetails getFacultyDetail(Long facultyId) {
        // TODO Auto-generated method stub
        return facultyrepo.findById(facultyId).orElse(null);
    }

    @Override
    public String updateFacultyDetails(Long facultyId, facultydetails updatedFacultydetails) {
        // TODO Auto-generated method stub
        if (facultyrepo.existsById(facultyId)) {
            updatedFacultydetails.setFacultyId(facultyId);
            facultyrepo.save(updatedFacultydetails);
            return "updated successfully";
        }
        return "faculty not found";
    }

}
