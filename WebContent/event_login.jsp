<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Event - Login</title>
<link href="bootstrap/css/bootstrap.css" rel="stylesheet"
	media="screen" />
<script src="http://code.jquery.com/jquery.js"></script>
<script src="bootstrap/js/bootstrap.js"></script>
</head>

<body>
<div>
	<form action="edit_event.do" method="post" class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="eventName">Event Name</label>
			<div class="controls">
				<input type="text" id="eventName" name="eventName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="eventKey">Event Key</label>
			<div class="controls">
				<input type="password" id="eventKey" name="eventKey" />
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
				<button type="submit" class="btn">View Event</button>
			</div>
		</div>
	</form>
</div>
</body>
</html>
