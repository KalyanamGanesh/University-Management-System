//only service layer declaration is done in this file because we are given interface so we cant implement thsese logic here we can only declare
// if we want to implement then we need to create a new file called implement and extend this service file over there

package com.UMS.University_Management_System.service;

import java.util.List;
//here we are writing the logic of the program that is CRUD operation

import com.UMS.University_Management_System.entity.studentdetails;

//In interface we should not  implement just we need to declare
//this acts as abstract method
public interface StudentDetailsService {
    // creating logics in my service layer
    String createStudentDetails(studentdetails studentdetail);

    // updating all students
    String updateStudentDetails(String studentId, studentdetails updatedStudentDetails);

    // because we are not deleting all , we are deleting particular student details
    // if we want to delete all then no need to give particular student details that
    // is student id or student name anything about particular student

    String deleteStudentDetails(String studentId);

    // this is used to get particular student details
    studentdetails getStudentDetail(String studentId);

    List<studentdetails> getAllStudents();

}
