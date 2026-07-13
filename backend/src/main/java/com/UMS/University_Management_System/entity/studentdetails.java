package com.UMS.University_Management_System.entity;

import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_details_info") // if we dont give this table name, class name will be taken automatically
public class studentdetails {
    @Id // telling jpa to automatically you should create thsese tables
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String studentId;
    private String studentName;
    private String studentAddress;
    private String studentPhonenumber;

    public studentdetails() {

    }

    public studentdetails(String studentId, String studentName, String studentAddress, String studentPhoneNumber) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentAddress = studentAddress;
        this.studentPhonenumber = studentPhoneNumber;
    }

    public String getstudentId() {
        return studentId;
    }

    public void setstudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getstudentName() {
        return studentName;
    }

    public void setstudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public String getStudentPhonenumber() {
        return studentPhonenumber;
    }

    public void setStudentPhonenumber(String studentPhonenumber) {
        this.studentPhonenumber = studentPhonenumber;
    }

}