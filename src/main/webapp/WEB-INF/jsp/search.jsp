<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<title>Social Media Search</title>
<style type="text/css">
	body {
		background-image: url('http://crunchify.com/bg.png');
	}
	table, th, td {
   	 	border: 1px solid black;
   	 	align: center;
	}
</style>
</head>
<body>
	<div style="text-align: center">
		<a href="/merccann/operations/import">TO THE IMPORT PAGE</a>
		<br>
		<form>
			Search: <input type="text" name="searchTerm"></input>
			<input type="submit"name="submit"></input>
		</form>
		<br>
		<table id="table" border='1' align="center"></table>
	</div>
	<script>
		$(document).ready(function() {
			$("form").on("submit",function(event) {
				event.preventDefault();
				var queryparams = $( this ).serialize(); 
				$.get("/merccann/social-media/search?".concat(queryparams), function(retdata, status) {
					$("#table tbody").remove();
					$(function() {
						row = $("<tr></tr>");
						col1 = $("<td><b>Blog URL</b></td>");
						col2 = $("<td><b>Instagram</b></td>");
						col3 = $("<td><b>Instagram Followers</b></td>");
						col4 = $("<td><b>Twitter</b></td>");
						col5 = $("<td><b>Twitter Followers</b></td>");
						col6 = $("<td><b>Email</b></td>");
						row.append(col1,col2,col3,col4,col5,col6).prependTo("#table"); 
			            $.each(retdata, function(i, item) {
			                $('<tr>').append(
			                    $('<td>').text(item.blogUrl),
			                    $('<td>').text(item.instagramhandle),
			                    $('<td>').text(item.instagramFollowerCount),
			                    $('<td>').text(item.twitterHandle),
			                    $('<td>').text(item.twitterFollowerCount),
			                    $('<td>').text(item.emailAddress)
			                ).appendTo('#table');
			            });
			        });
				});
			});
		});
	</script>
</body>
</html>