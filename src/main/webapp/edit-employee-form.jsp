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
 <script>
    function disable() {
    ceoChoiceElement= document.getElementById("isCEO");
  	managerChoiceElement = document.getElementById("isManager");
    managerIdElement = document.getElementById("ManagerId");
    	    	 
    if(ceoChoiceElement.value==="true"){
    	managerChoiceElement.disabled = true;
    	managerIdElement.disabled = true;
    }else{
    	 managerChoiceElement.disabled = false;
    	 managerIdElement.disabled = false;
   }
   if(managerChoiceElement.value==="true"){
    	 ceoChoiceElement.disabled = true;
    }else{
    	  ceoChoiceElement.disabled = false;
    }
    	
    }
  </script>
<title>Edit Employee</title>
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

	<div class="d-flex justify-content-lg-center mt-3">
		<form action="update" method="post">
		
		<div>
			<input type="hidden" name="id" value="<c:out value='${employee.id}' />" />
			<label  class=form-label for="FirstName">First Name</label>
			<input class="form-control" type="text" value="<c:out value='${employee.firstName}' />" name="firstName" id="FirstName" required>
		</div>
		
		<div>
			<label  class="form-label" for="LastName">Last Name</label>
			<input class="form-control" type="text" value="<c:out value='${employee.lastName}' />" name="lastName" id="LastName" required>
		</div>
			
		<div>
			<label  class="form-label" for="Salary">Salary</label>
			<select class="form-select form-select-md mb-3" required name="salary" id="Salary">
			<c:forEach begin="0" end="9" step="1" varStatus="loop">
   		  	<option value="${loop.count}">${loop.count}</option>
			</c:forEach>
			</select>
		</div>
		
		<div>
		
			<label class="form-label" for="isCEO"> Is this the CEO?</Label>
			<select class="form-select form-select-md mb-3" name="isCEO" id="isCEO" onchange="disable()">
		 	<option value="true">Yes</option>
  			<option selected="selected" value="false">No</option>
  			</select>
		</div>
		
		<div>
			<label class="form-label" for="IsManager"> Is this a Manager?</Label>
			<select class="form-select form-select-md mb-3" name="isManager" id="isManager" onchange="disable()">
		 	<option value="true">Yes</option>
  			<option selected="selected" value="false">No</option>
  			</select>
		</div>
		
		<div>
			<label class="form-label" for="ManagerId"> Manager ID</label>
			<input  class="form-control mb-3" type="number"  min="0" step="1" value="<c:out value='${employee.managerId}' />" name="managerId" id="ManagerId">
		</div>
		
		<button type="submit" class="btn  btn-success">Save</button>
		</form>
							
	</div>
	
</body>
</html>