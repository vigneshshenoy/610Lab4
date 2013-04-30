<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Error</title>
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen" />
<script src="http://code.jquery.com/jquery.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</head>

<body>
<div align="center">
	<c:choose>
		<c:when test="${ event.state == 0 }">
			<div class="alert">
				This event is not yet committed. 
			</div>
		</c:when>
		<c:when test="${ event.state == 2 }">
			<div class="alert">
				This event was aborted. 
			</div>
		</c:when>
		<c:otherwise>
			<ul class="thumbnails">
			<c:forEach var="image" items="${ event.images }">
				<li class="span3">
		    		<div class="thumbnail">
		    			<a href="image.do?id=${ image.photoId }">
		      				<img src="image.do?id=${ image.photoId }" alt="" />
		      			</a>
		    		</div>
  				</li>
			</c:forEach>
			</ul>
		</c:otherwise>
	</c:choose>
	
</div>
</body>
</html>
