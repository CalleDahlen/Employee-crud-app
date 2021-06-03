package net.employees.model;

public class Employee {
	private int id;
	private String firstName;
	private String lastName;
	private double salary;
	private boolean isCEO;
	private boolean isManager;
	private Integer managerId;

	public Employee(int id, String firstName, String lastName, double salary, boolean isCEO, boolean isManager,
			Integer managerId) {

		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.salary = salary;
		this.isCEO = isCEO;
		this.isManager = isManager;
		this.managerId = managerId;
	}

	public Employee(String firstName, String lastName, double salary, boolean isCEO, boolean isManager,
			Integer managerId) {

		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.salary = salary;
		this.isCEO = isCEO;
		this.isManager = isManager;
		this.managerId = managerId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	// used to calculate the salary when creating an employee
	public double calculateSalary(double salaryRank) {
		if (isCEO) {
			salary = 2.725 * salaryRank;
		} else if (isManager) {
			salary = 1.725 * salaryRank;
		} else {
			salary = 1.125 * salaryRank;
		}
		return salary;
	}

	public boolean isCEO() {
		return isCEO;
	}

	public void setCEO(boolean isCEO) {
		this.isCEO = isCEO;
	}

	public boolean isManager() {
		return isManager;
	}

	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

}
