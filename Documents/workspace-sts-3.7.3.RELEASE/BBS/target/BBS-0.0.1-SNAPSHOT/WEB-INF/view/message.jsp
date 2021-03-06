<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新規投稿</title>
</head>
<body>
<a href="../">戻る</a>
<form:form modelAttribute="messageForm">
	<table>
		<tr>
			<th>件名</th>
			<td>
				<form:textarea path="subject" cols="20" row="5"/>
			</td>
		</tr>
		<tr>
			<th>本文</th>
			<td>
				<form:textarea path="text" cols="20" row="5"/>
			</td>
		</tr>
		<tr>
			<th>カテゴリー</th>
			<td>
				<form:input path="category"/>
			</td>
		</tr>
	</table>
	<input type="submit" value="投稿">
</form:form>
</body>
</html>