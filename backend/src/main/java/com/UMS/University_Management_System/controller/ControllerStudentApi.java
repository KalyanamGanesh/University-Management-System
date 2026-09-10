package com.UMS.University_Management_System.controller;

import org.springframework.web.bind.annotation.*;

import com.UMS.University_Management_System.entity.studentdetails;
import com.UMS.University_Management_System.service.StudentDetailsService;

import java.util.List;

@RestController
@RequestMapping("/studentdetails")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class ControllerStudentApi {
    // after controlling it goes service layer so we are writing
    StudentDetailsService studentDetailsService;

    public ControllerStudentApi(StudentDetailsService studentDetailsService) {
        this.studentDetailsService = studentDetailsService;
    }

    @GetMapping
    public List<studentdetails> getAllStudents() {
        return studentDetailsService.getAllStudents();
    }

    @GetMapping("/{studentId}")
    public studentdetails getStudentDetails(@PathVariable String studentId) {
        return studentDetailsService.getStudentDetail(studentId);
    }

    @PostMapping
    public String createStudentDetails(@RequestBody studentdetails studentDetails) {
        return studentDetailsService.createStudentDetails(studentDetails);
    }

    @PutMapping("/{studentId}")
    public String updateStudentDetails(@PathVariable String studentId,
            @RequestBody studentdetails updatedStudentDetails) {
        return studentDetailsService.updateStudentDetails(studentId, updatedStudentDetails);
    }

    @DeleteMapping("/{studentId}")
    public String deleteStudentDetails(@PathVariable String studentId) {
        return studentDetailsService.deleteStudentDetails(studentId);
    }
}
