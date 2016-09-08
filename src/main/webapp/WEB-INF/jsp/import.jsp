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
		<a href="/merccann/operations/search">TO THE SEARCH PAGE</a>
		<br>
		<form>
			Blog URL: <input type="text" name="blogUrl" size=30></input>
			<input type="submit"name="submit"></input>
		</form>
		<br>
		<strong>Recently Added</strong>
		<br>
		<table id="table" border='1' align="center"></table>
	</div>
	<script>
		$(document).ready(function() {
			row = $("<tr></tr>");
			col1 = $("<td><b>Blog URL</b></td>");
			col2 = $("<td><b>Instagram</b></td>");
			col3 = $("<td><b>Instagram Followers</b></td>");
			col4 = $("<td><b>Twitter</b></td>");
			col5 = $("<td><b>Twitter Followers</b></td>");
			col6 = $("<td><b>Email</b></td>");
			row.append(col1,col2,col3,col4,col5,col6).prependTo("#table"); 
			$("form").on("submit",function(event) {
				event.preventDefault();
				var data = {}
			    var Form = this;
				$.each(this.elements, function(i, v){
		            var input = $(v);
		        	data[input.attr("name")] = input.val();
		        	delete data["undefined"];
		    	});
				var jsonData = JSON.stringify(data)
				
				jQuery.ajax ({
   				 	url: "/merccann/social-media/import",
    				type: "POST",
    				data: jsonData,
    				dataType: "json",
    				contentType: "application/json; charset=utf-8",
    				success: function(retData){
    					$('<tr>').append(
			                    $('<td>').text(retData.blogUrl),
			                    $('<td>').text(retData.instagramhandle),
			                    $('<td>').text(retData.instagramFollowerCount),
			                    $('<td>').text(retData.twitterHandle),
			                    $('<td>').text(retData.twitterFollowerCount),
			                    $('<td>').text(retData.emailAddress)
			                ).appendTo('#table');
    				}
				});
			});
		});
	</script>
</body>
</html>