<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ユーザー編集</title>
</head>
<body>
<div class="menu">
	<a href="./control/">戻る</a>
</div>
<div class="user_name">
	<c:out value="${ loginUser.name }" />
</div>
<c:if test="${ not empty errorMessages }">
	<ul>
		<c:forEach items="${ errorMessages }" var="message">
			<li><span><c:out value="${message}" /></span></li>
		</c:forEach>
	</ul>
	<c:remove var="errorMessages" scope="session" />
</c:if>
<form:form modelAttribute="userForm">
	<table class="setting">
		<tr>
			<th>ログインID</th>
			<td>
				<form:input path="loginId" value="${user.loginId}"/>
			</td>
		</tr>
		<tr>
			<th>パスワード</th>
			<td>
				<form:password path="password"/>
			</td>
		</tr>
		<tr>
			<th>名前</th>
			<td>
				<form:input path="name" value="${user.name}"/>
			</td>
		</tr>
		<tr>
			<th>所属支店</th>
			<td>
				<form:select path="branchId">
					<c:forEach items="${ branches }" var="branch">
						<c:choose>
							<c:when test="${ branch.id == user.branchId }">
								<form:option value="${ branch.id }" selected="selected">${ branch.name }</form:option>
							</c:when>
							<c:otherwise>
								<form:option value="${ branch.id }">${ branch.name }</form:option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</form:select>
			</td>
		</tr>
		<tr>
			<th>部署・役職</th>
			<td>
				<form:select path="positionId">
					<c:forEach items="${ positions }" var="position">
						<c:choose>
							<c:when test="${ position.id == user.positionId }">
								<form:option value="${ position.id }" selected="selected">${ position.name }</form:option>
							</c:when>
							<c:otherwise>
								<form:option value="${ position.id }">${ position.name }</form:option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</form:select>
			</td>
		</tr>
	</table>
	<form:hidden path="id" value="${user.id}"/>
	<input type="submit" value="編集">
</form:form>
</body>
</html>