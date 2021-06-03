CREATE DATABASE libraryemployees;

USE libraryemployees;

CREATE TABLE employees (
	id  int NOT NULL AUTO_INCREMENT,
	firstName varchar(50) NOT NULL,
	lastName varchar(50) NOT NULL,
	salary decimal(18,3),
	isCEO BIT NOT NULL,
	isManager BIT NOT NULL,
	managerID int null,
	PRIMARY KEY (id)
);
