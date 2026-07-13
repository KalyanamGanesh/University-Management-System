package com.UMS.University_Management_System.controller;

import org.springframework.web.bind.annotation.*;

import com.UMS.University_Management_System.entity.facultydetails;
import com.UMS.University_Management_System.service.facultyDetailsService;

import java.util.List;

@RestController
@RequestMapping("/facultydetails")
public class ControllerFacultyApi {
    facultyDetailsService facultydetailservice;

    public ControllerFacultyApi(facultyDetailsService facultydetailservice) {
        this.facultydetailservice = facultydetailservice;
    }
//
//    @GetMapping
//    public List<facultydetails> getAllFaculty() {
//        return facultydetailservice.getAllFaculty();
//    }
//
//    @GetMapping("facultyId")
//    public facultydetails getFacultyDetail(@RequestParam Long facultyId) {
//        return facultydetailservice.getFacultyDetail(facultyId);
//    }

//    @GetMapping
//    public List<facultydetails> getAllFaculty() {
//        return facultydetailservice.getAllFaculty();
//    }
//
//    @GetMapping("/{facultyId}")
//    public facultydetails getFacultyDetail(@RequestParam Long facultyId) {
//        return facultydetailservice.getFacultyDetail(facultyId);
//    }
@GetMapping
public Object getFacultyDetail(
        @RequestParam(name = "facultyId", required = false) Long facultyId) {

    if (facultyId != null) {
        return facultydetailservice.getFacultyDetail(facultyId);
    }

    return facultydetailservice.getAllFaculty();
}


    @PostMapping
    public String createFacultyDetails(@RequestBody facultydetails facultydetail) {
        return facultydetailservice.createFacultyDetails(facultydetail);
    }

    @PutMapping("/{facultyId}")
    public String updateFacultyDetail(@PathVariable Long facultyId, @RequestBody facultydetails updatedfacultydetails) {
        return facultydetailservice.updateFacultyDetails(facultyId, updatedfacultydetails);
    }

    @DeleteMapping("/{facultyId}")
    public String deleteFacultyDetail(@PathVariable Long facultyId) {
        return facultydetailservice.deleteFacultyDetails(facultyId);
    }

}
