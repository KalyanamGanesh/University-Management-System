package com.UMS.University_Management_System.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.UMS.University_Management_System.entity.studentdetails;
import com.UMS.University_Management_System.repository.studentRepo;

// here we have to implement those methods which we have declared in service file

@Service
public class studentDetailsImplementation implements StudentDetailsService {

    studentRepo studentrepo;

    public studentDetailsImplementation(studentRepo studentrepo) {
        this.studentrepo = studentrepo;
    }

    // below code will be automatically generated if i click source action and click
    // implement/override then below will be automatically displays

    @Override
    public String createStudentDetails(studentdetails studentdetail) {
        studentrepo.save(studentdetail);
        return "Created Successfully";
    }
    // when we give request as create then it must be stored in databases using
    // repository file so we are givin repository file because it has jpa which
    // automatically stores or saves the data over there
    // so therefore due to jpa repository it will be stored in database below 1 line
    // code does that and after storing it gives message as created successfully.

    @Override
    public String updateStudentDetails(String studentId, studentdetails updatedStudentDetails) {
        if (studentrepo.existsById(studentId)) {
            updatedStudentDetails.setstudentId(studentId);
            studentrepo.save(updatedStudentDetails);
            return "Updated Successfully";
        } else {
            return "Student not found";
        }
    }

    @Override
    public String deleteStudentDetails(String studentId) {
        if (studentrepo.existsById(studentId)) {
            studentrepo.deleteById(studentId);
            return "Deleted Successfully";
        } else {
            return "Student not found";
        }
    }

    @Override
    public List<studentdetails> getAllStudents() {
        return studentrepo.findAll();
    }

    @Override
    public studentdetails getStudentDetail(String studentId) {
        return studentrepo.findById(studentId).orElse(null);
    }
}
