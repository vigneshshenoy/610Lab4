<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ClickPic</title>
<link href="bootstrap/css/bootstrap.css" rel="stylesheet"
	media="screen" />
<script src="http://code.jquery.com/jquery.js"></script>
<script src="bootstrap/js/bootstrap.js"></script>
</head>
<body>
<div>
	<form action="create_event.do" method="post" class="form-horizontal">
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
				<button type="submit" class="btn">Create Event</button>
			</div>
		</div>
	</form>
	<div class="alert">
		To add photos to an event <a href="event_login.jsp" class="btn">Click Here</a>.
	</div>
	<div>
		<form action="view_event.do" method="get" class="form-inline" >
			<label class="control-label" for="eventName">View a Montage :</label>
			<input type="text" id="eventName" name="eventName" />
			<button type="submit" class="btn">View Montage</button>
		</form>
	</div>
</div>
</body>
</html>