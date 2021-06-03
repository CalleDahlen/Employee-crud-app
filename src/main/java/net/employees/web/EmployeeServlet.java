package net.employees.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.employees.dao.EmployeeDAO;
import net.employees.model.Employee;

@WebServlet("/")
public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EmployeeDAO employeeDAO;

	// calling DAO to get data from database
	public EmployeeServlet() {
		this.employeeDAO = new EmployeeDAO();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// get the request path to determine what the user wants to do.
		String path = req.getServletPath();

		switch (path) {
		case "/new":
			showNewForm(req, res);
			break;
		case "/add":
			try {
				addEmployee(req, res);
			} catch (SQLException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			break;
		case "/delete":
			try {
				deleteEmployee(req, res);
			} catch (SQLException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			break;
		case "/edit":
			try {
				showEditForm(req, res);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "/update":
			try {
				updateEmployee(req, res);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			try {
				showEmployees(req, res);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	// selects all instances in the database and shows them from a list
	private void showEmployees(HttpServletRequest req, HttpServletResponse res)
			throws SQLException, IOException, ServletException {
		List<Employee> listEmployees = employeeDAO.selectAllEmployees();
		req.setAttribute("listEmployees", listEmployees);
		RequestDispatcher dispatcher = req.getRequestDispatcher("show-employees.jsp");
		dispatcher.forward(req, res);
	}

	// form for creating a new employee
	private void showNewForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher dispatcher = req.getRequestDispatcher("employee-form.jsp");
		dispatcher.forward(req, res);
	}

	// A form for editing the Employee that has the current employee information.
	private void showEditForm(HttpServletRequest req, HttpServletResponse res)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		Employee currentEmployee = employeeDAO.selectEmployee(id);
		RequestDispatcher dispatcher = req.getRequestDispatcher("edit-employee-form.jsp");
		req.setAttribute("employee", currentEmployee);
		dispatcher.forward(req, res);

	}

	// a method to handle integer values that could be null
	public Integer parseIntOrNull(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// Add Employee from the retrieved information in the request
	private void addEmployee(HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException {

		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		double salary = Double.parseDouble(req.getParameter("salary"));
		boolean isCEO = Boolean.parseBoolean(req.getParameter("isCEO"));
		boolean isManager = Boolean.parseBoolean(req.getParameter("isManager"));
		Integer managerId = parseIntOrNull(req.getParameter("managerId"));

		Employee newEmployee = new Employee(firstName, lastName, salary, isCEO, isManager, managerId);
		employeeDAO.insertEmployee(newEmployee);

		res.sendRedirect("show");
	}

	// remove Employee
	private void deleteEmployee(HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException {
		// get ID of the Employee to be removed.
		int id = Integer.parseInt(req.getParameter("id"));
		employeeDAO.deleteEmployee(id);
		res.sendRedirect("show");

	}

	// update the employee
	private void updateEmployee(HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		double salary = Double.parseDouble(req.getParameter("salary"));
		boolean isCEO = Boolean.parseBoolean(req.getParameter("isCEO"));
		boolean isManager = Boolean.parseBoolean(req.getParameter("isManager"));
		Integer managerId = parseIntOrNull(req.getParameter("managerId"));

		Employee updatedEmployee = new Employee(id, firstName, lastName, salary, isCEO, isManager, managerId);
		employeeDAO.updateEmployee(updatedEmployee);
		res.sendRedirect("show");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		this.doGet(req, res);
	}

}
