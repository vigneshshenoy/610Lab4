<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="vkshenoy.data.Event"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Event - ${ event.eventName }</title>
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen" />
<script src="http://code.jquery.com/jquery.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</head>
<%
	String duration = (String)request.getAttribute("vote_duration");
	int sec = -1;
	if(duration != null)	{
		sec = Integer.parseInt(duration);
	}
	
	Event event = (Event)session.getAttribute("event");
	String eventName = event.getEventName();
%>
<body onload="load()">
<script type="text/javascript">
	var sec = 0;
	var min = 0;
	var str = "";
	var evName = "<%=eventName %>";
	var interval = "";
	
	function count()
	{
		if(sec <= 0)	{
			min --;
			sec = 60;
		}
		
		sec --;
		
		if(min >= 0)	{
			var str = "You have "+min+" minutes, "+sec+" seconds to vote.";
			var msgDiv = document.getElementById("voteDiv");
			msgDiv.innerHTML = str;
		}	else	{
			var xmlhttp;
			if (window.XMLHttpRequest)
			{// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp=new XMLHttpRequest();
			}
			else
			{// code for IE6, IE5
				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			clearInterval(interval);
			setInterval(refreshPage, 5000);
			xmlhttp.open("POST", "refresh_event.do?eventName="+evName, true);
			xmlhttp.send();
			//document.location.reload(true);
		}
	}
	
	function refreshPage()
	{
		var xmlhttp;
		if (window.XMLHttpRequest)
		{// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp=new XMLHttpRequest();
		}
		else
		{// code for IE6, IE5
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.open("POST", "refresh_event.do?eventName="+evName, true);
		xmlhttp.send();
		document.location.reload(true);
	}
	
	function load()
	{	
		sec = "<%=sec %>";
		var valid = true;
		if(sec == -1)	{
			valid = false;
		}
		if(valid)	{
			min = Math.floor(sec / 60);
			sec = sec % 60;
			str = "You have "+min+" minutes, "+sec+" seconds to vote.";
		}	else	{
			str = "Voting has not begun.";
		}
		var msgDiv = document.getElementById("voteDiv");
		msgDiv.innerHTML = str;
		if(valid)	{
			interval = setInterval(count, 1000);
		}
	}
	
</script>
<div>
	<div align="center">
		<!-- Display Photos -->
		<!--  <div class="row-fluid"> -->
		<ul class="thumbnails">
		<c:forEach var="image" items="${ event.images }">
			<li class="span3">
	    		<div class="thumbnail">
	    			<a href="image.do?id=${ image.photoId }">
	      			<img src="image.do?id=${ image.photoId }" alt="" />
	      			</a>
	    		</div>
	    		<!-- <a href="delete_photo.do?id=${ image.photoId }" class="btn">Delete</a>  -->
  			</li>
		</c:forEach>
		</ul>
		<!-- </div> -->
	</div>
	<div align="center">
		<form action="refresh_event.do?eventName=${ event.eventName }" method="post" >
			<button type="submit" class="btn">Refresh</button>
		</form>
	</div>
	<div align="center">
		<!-- Upload Photos -->
		<c:if test="${ event.state == 0 }">
		<form action="upload_photo.do" method="post" enctype="multipart/form-data" class="form-horizontal" >
			<div class="control-group">
				<div class="controls">
					<input type="file" name="photo" accept="image/*;capture=camera" /><br />
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<input type="submit" value="Upload" />
				</div>
			</div>
		</form>
		</c:if>
	</div>
	<div align="center" style="background-color: aqua" >
		<!-- Voting -->
		<h3 align="center">VOTE</h3>
		<c:choose>
			<c:when test="${ (voter.voteStatus == 0) && (event.state == 0) }">
				<div id="voteDiv" class="alert">
				</div>
				<a href="event_vote.do?vote=accept" class="btn">ACCEPT</a>&nbsp;&nbsp;
				<a href="event_vote.do?vote=reject" class="btn">REJECT</a>
			</c:when>
			<c:when test="${ event.state == 1 }">
				<h4>This event is committed.</h4>
			</c:when>
			<c:when test="${ event.state == 2 }">
				<h4>This event is aborted.</h4>
			</c:when>
			<c:otherwise>
				<h4>Your vote has been registered.</h4>
			</c:otherwise>
		</c:choose>
	</div>
</div>
</body>
</html>
