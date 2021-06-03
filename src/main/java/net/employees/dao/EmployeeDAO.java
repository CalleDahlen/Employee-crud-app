package net.employees.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.employees.model.Employee;

public class EmployeeDAO {

	// Sql server connection string and credentials
	private String jdbcURL = "jdbc:sqlserver://localhost;Database=libraryemployees;Trusted_Connection=True;";
	private String jdbcUsername = "root";
	private String jdbcPassword = "secure01";

	// sql query strings used for CRUD operations
	private static final String INSERT_EMPOLOYEE = "INSERT INTO employees"
			+ "  (firstName, lastName, salary, isCEO, isManager, managerId ) VALUES " + " (?, ?, ?, ?, ?, ?);";
	private static final String INSERT_EMPOLOYEE_WITH_NO_MANAGER_ID = "INSERT INTO employees"
			+ "  (firstName, lastName, salary, isCEO, isManager ) VALUES " + " (?, ?, ?, ?, ?);";
	private static final String SELECT_EMPLOYEE_BY_ID = "select id, firstName, lastName, salary, isCEO, isManager, managerId from employees where id =?";
	private static final String SELECT_ALL_EMPLOYEES = "select * from employees order by isCEO, isManager";
	private static final String DELETE_EMPLOYEE = "delete from employees where id = ?;";
	private static final String UPDATE_EMPLOYEE = "update employees set firstName = ?, lastName= ?, salary= ?, isCEO= ?, isManager= ?, managerId= ? where id = ?;";
	private static final String UPDATE_EMPLOYEE_WITH_NO_MANAGER_ID = "update employees set firstName = ?, lastName= ?, salary= ?, isCEO= ?, isManager= ? where id = ?;";
	private static final String CHECK_FOR_CEO = "select id from employees where exists (select * from employees WHERE isCEO=1)";
	private static final String CHECK_IF_MANAGER_IS_MANAGING = "select id from employees where exists (select * from employees WHERE managerId = ?)";
	private static final String CHECK_IF_MANAGER_IS_CEO = "select id from employees where exists (select * WHERE id = ? AND isCEO=1)";
	private static final String CHECK_IF_MANAGER_IS_EMPLOYEE = "select id from employees where exists (select * WHERE id = ? AND isCEO=0 AND isManager=0)";

	// get connection to the SQL Server DB with jdbc
	protected Connection getConnection() {
		Connection connection = null;
		try {
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	// Create the DB CRUD operations.

	// Attempting to insert employee into database and checking to make sure
	// conditions are fulfilled.
	public void insertEmployee(Employee employee) throws SQLException {
		Connection connection = getConnection();
		// creating a statement for sending to the database
		try (PreparedStatement pS = connection.prepareStatement(INSERT_EMPOLOYEE)) {

			try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_FOR_CEO)) {
				// result set containing the data from the query
				ResultSet rs = preparedStatement.executeQuery();
				// processing the result set.
				if (rs.next() == true && employee.isCEO()) {
					System.out.println("There already is a CEO");

				} else {

					if (!(employee.getManagerId() == null)) {
						if (employee.isCEO() == false && employee.isManager() == false) {
							try (PreparedStatement preparedStatement1 = connection
									.prepareStatement(CHECK_IF_MANAGER_IS_CEO)) {
								preparedStatement1.setInt(1, employee.getManagerId());
								ResultSet rs1 = preparedStatement1.executeQuery();

								if (rs1.next()) {
									System.out.println("The CEO cannot manage employees");

								} else {
									try (PreparedStatement preparedStatement2 = connection
											.prepareStatement(CHECK_IF_MANAGER_IS_EMPLOYEE)) {
										preparedStatement2.setInt(1, employee.getManagerId());
										ResultSet rs2 = preparedStatement2.executeQuery();

										if (rs2.next()) {
											System.out.println("A regular employee cannot manage another employee");

										} else {
											pS.setString(1, employee.getFirstName());
											pS.setString(2, employee.getLastName());
											pS.setDouble(3, employee.calculateSalary(employee.getSalary()));
											pS.setBoolean(4, employee.isCEO());
											pS.setBoolean(5, employee.isManager());
											pS.setInt(6, employee.getManagerId());
											pS.executeUpdate();
										}
									}
								}
							}
						} else {
							pS.setString(1, employee.getFirstName());
							pS.setString(2, employee.getLastName());
							pS.setDouble(3, employee.calculateSalary(employee.getSalary()));
							pS.setBoolean(4, employee.isCEO());
							pS.setBoolean(5, employee.isManager());
							pS.setInt(6, employee.getManagerId());
							pS.executeUpdate();
						}

					} else {
						if (employee.getManagerId() == null) {
							try (PreparedStatement pS1 = connection
									.prepareStatement(INSERT_EMPOLOYEE_WITH_NO_MANAGER_ID)) {
								pS1.setString(1, employee.getFirstName());
								pS1.setString(2, employee.getLastName());
								pS1.setDouble(3, employee.calculateSalary(employee.getSalary()));
								pS1.setBoolean(4, employee.isCEO());
								pS1.setBoolean(5, employee.isManager());
								pS1.executeUpdate();
							}

						}
					}
				}
			}
		}

		catch (

		SQLException e) {
			printSQLException(e);
		}
	}

	public Employee selectEmployee(int id) {
		Employee employee = null;
		// Establishing a Connection
		try (Connection connection = getConnection();
				// Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EMPLOYEE_BY_ID)) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();

			// Process the ResultSet object.
			while (rs.next()) {
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				double salary = rs.getDouble("salary");
				boolean isCEO = rs.getBoolean("isCEO");
				boolean isManager = rs.getBoolean("isManager");
				Integer managerId = rs.getInt("managerId");

				employee = new Employee(id, firstName, lastName, salary, isCEO, isManager, managerId);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return employee;
	}

	public List<Employee> selectAllEmployees() {

		List<Employee> employees = new ArrayList<>();
		// Establishing a Connection
		try (Connection connection = getConnection();

				// Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EMPLOYEES);) {

			// Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Process the ResultSet object.
			while (rs.next()) {
				int id = rs.getInt("id");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				double salary = rs.getDouble("salary");
				boolean isCEO = rs.getBoolean("isCEO");
				boolean isManager = rs.getBoolean("isManager");
				Integer managerId = rs.getInt("managerId");
				employees.add(new Employee(id, firstName, lastName, salary, isCEO, isManager, managerId));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return employees;
	}

	// Delete employee from the DB
	public boolean deleteEmployee(int id) throws SQLException {
		boolean rowDeleted = false;
		Connection connection = getConnection();
		// check if the manager is managing other employees before deleting
		try (PreparedStatement statement = connection.prepareStatement(CHECK_IF_MANAGER_IS_MANAGING);) {
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();

			if (rs.next() == true) {
				System.out.print("This manager is managing one or more employees and cannot be removed.");

			} else
				try (PreparedStatement stmt = connection.prepareStatement(DELETE_EMPLOYEE);) {
					stmt.setInt(1, id);
					rowDeleted = stmt.executeUpdate() > 0;
				}
		}
		return rowDeleted;
	}

	// Update DB employee
	public boolean updateEmployee(Employee employee) throws SQLException {
		Connection connection = getConnection();
		boolean rowUpdated = false;
		try (PreparedStatement pS = connection.prepareStatement(UPDATE_EMPLOYEE)) {

			try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_FOR_CEO)) {
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next() == true && employee.isCEO()) {
					System.out.println("There already is a CEO");

				} else {

					if (!(employee.getManagerId() == null)) {
						if (employee.isCEO() == false && employee.isManager() == false) {
							try (PreparedStatement preparedStatement1 = connection
									.prepareStatement(CHECK_IF_MANAGER_IS_CEO)) {
								preparedStatement1.setInt(1, employee.getManagerId());
								ResultSet rs1 = preparedStatement1.executeQuery();

								if (rs1.next()) {
									System.out.println("The CEO cannot manage employees");

								} else {
									try (PreparedStatement preparedStatement2 = connection
											.prepareStatement(CHECK_IF_MANAGER_IS_EMPLOYEE)) {
										preparedStatement2.setInt(1, employee.getManagerId());
										ResultSet rs2 = preparedStatement2.executeQuery();

										if (rs2.next()) {
											System.out.println("A regular employee cannot manage another employee");

										} else {
											pS.setString(1, employee.getFirstName());
											pS.setString(2, employee.getLastName());
											pS.setDouble(3, employee.calculateSalary(employee.getSalary()));
											pS.setBoolean(4, employee.isCEO());
											pS.setBoolean(5, employee.isManager());
											pS.setInt(6, employee.getManagerId());
											pS.setInt(7, employee.getId());
											pS.executeUpdate();
										}
									}
								}
							}
						} else {
							pS.setString(1, employee.getFirstName());
							pS.setString(2, employee.getLastName());
							pS.setDouble(3, employee.calculateSalary(employee.getSalary()));
							pS.setBoolean(4, employee.isCEO());
							pS.setBoolean(5, employee.isManager());
							pS.setInt(6, employee.getManagerId());
							pS.setInt(7, employee.getId());
							pS.executeUpdate();
						}

					} else {
						if (employee.getManagerId() == null) {
							try (PreparedStatement pS1 = connection
									.prepareStatement(UPDATE_EMPLOYEE_WITH_NO_MANAGER_ID)) {
								pS1.setString(1, employee.getFirstName());
								pS1.setString(2, employee.getLastName());
								pS1.setDouble(3, employee.calculateSalary(employee.getSalary()));
								pS1.setBoolean(4, employee.isCEO());
								pS1.setBoolean(5, employee.isManager());
								pS1.setInt(6, employee.getId());
								pS1.executeUpdate();
							}

						}
					}
				}
			}
		}

		return rowUpdated;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}
