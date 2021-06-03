<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib
    prefix="c"
    uri="http://java.sun.com/jsp/jstl/core" 
%>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>All Employees</title>
</head>
<header>
	<nav class="navbar navbar-dark bg-dark">
		<ul class="navbar-nav me-auto m-2 mb-lg-0">
        	<li class="nav-item">
          		<a class="nav-link active" aria-current="page" href="<%=request.getContextPath()%>/show">All Employees</a>
        	</li>
	 	</ul>
	</nav>		
</header>
<body>
	<div class="container">
		<h1>All Employees</h1>
		<hr>
		<div >
			<a class="btn btn-success" href="<%=request.getContextPath()%>/new" role="button">Add New Employee</a>
		</div>
			<table class="table">
				<thead>
					<tr>
						<th scope="col">ID</th>
						<th scope="col">First Name</th>
						<th scope="col">Last Name</th>
						<th scope="col">Salary</th>
						<th scope="col">Role </th>
						<th scope="col">ManagerId</th>
						<th scope="col">Actions</th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="employee" items="${listEmployees}">
						<tr>
							<th scope="row"><c:out value="${employee.id}" /></th>
							<td><c:out value="${employee.firstName}" /></td>
							<td><c:out value="${employee.lastName}" /></td>
							<td><c:out value="${employee.salary}" /></td>
							<td> 
							<c:if test="${employee.isCEO() == true}">
       							 <c:out value="CEO" />
    						</c:if>
    						<c:if test="${employee.isManager() == true}">
       							 <c:out value="Manager" />
    						</c:if>
    						<c:if test="${employee.isCEO() == false && employee.isManager() == false}">
       							 <c:out value="Employee" />
    						</c:if>
							</td>
						 	
							<td>
							<c:if test="${employee.managerId == 0 }">
       							 <c:out value="None" />
    						</c:if>
    						<c:if test="${employee.managerId != 0 }">
       							<c:out value="${employee.managerId}" />
    						</c:if>
							
							</td>
							
							<td><a class="btn btn-success btn-sm" href="edit?id=<c:out value='${employee.id}' />">Edit</a></td>
							<td><a class="btn btn-danger btn-sm" href="delete?id=<c:out value='${employee.id}' />">Delete</a></td>
							
						</tr>
					</c:forEach>
				</tbody>

			</table>
		</div>
</body>
</html>