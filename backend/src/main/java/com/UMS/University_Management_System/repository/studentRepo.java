// this file is the creating of jpa that jpa  which acts a bridge to communicate between databases and the controller
//without creating this it will not know which entity to take
package com.UMS.University_Management_System.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//to create repository we need to create one file and should write this                                         //<entity, datatypes of entity>
//this jparepository will make our work easier and faster  

import com.UMS.University_Management_System.entity.studentdetails;

// this JpaRepository we are giving because we are taken jpa dependency in the beginning so it  stores all information required for connection for database in jdbc connectivity we have written a big code here we are no need to use because we are jpa repository it will store everything required connection 
public interface studentRepo extends JpaRepository<studentdetails, String> {

}
