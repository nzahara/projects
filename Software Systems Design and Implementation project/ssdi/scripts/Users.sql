DROP DATABASE IF EXISTS SSDI_Project;
create database SSDI_Project;
USE SSDI_Project;

 CREATE TABLE `users` (
   `student_id` int(15) NOT NULL,
   `first_name` varchar(255) NOT NULL,
   `middle_name` varchar(255) DEFAULT NULL,
   `last_name` varchar(255) NOT NULL,
   `personal_email_id` varchar(255) NOT NULL,
   `contact_number` varchar(10) NOT NULL,
   `password` varchar(50) DEFAULT NULL,
   `linked_in_id` varchar(100) NOT NULL,
   `graduation_year` int(50) NOT NULL,
   `major` varchar(100) NOT NULL,
   `student_type` varchar(255) NOT NULL,
   `profile_pic` varchar(100) DEFAULT NULL,
   PRIMARY KEY (`student_id`),
   UNIQUE KEY `student_id` (`student_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 

CREATE TABLE `post` (
   `post_id` int(11) NOT NULL AUTO_INCREMENT,
   `student_id` int(11) NOT NULL,
   `category` varchar(20) DEFAULT NULL,
   `attachment` varchar(200) DEFAULT NULL,
   `post_text` varchar(50) NOT NULL,
   `updated_date` datetime NOT NULL,
   PRIMARY KEY (`post_id`),
   KEY `student_id` (`student_id`),
   CONSTRAINT `post_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`student_id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 
 create table ssdi_project.message(
message_id int not null auto_increment,
from_student_id INT(15) NOT NULL ,
to_student_id INT (1) NOT NULL,
message varchar(100) NOT NULL,
attachment varchar(200) NULL,
is_read TINYINT(1) default 0,
send_time datetime not null,
primary key (message_id)
);
 
 alter table post add column is_deleted tinyint(1) default 0;
