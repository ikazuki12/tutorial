<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ホーム</title>
</head>
<body>
<c:if test="${ not empty user }">
	<a href = "./logout/">ログアウト</a>
</c:if>
<c:if test="${ empty user }">
	<a href = "./login/">ログイン</a>
</c:if>
<c:if test="${ user.positionId == 1}">
	<a href = "./control/">ユーザー管理</a>
</c:if>
<a href = "./message/">新規投稿</a>
<c:out value="${user.name }" />
<c:forEach items="${messages}" var="message">
<form:form modelAttribute="commentForm" action="./comment/">
	<table>
		<tr>
			<td>
				<pre>カテゴリー[<c:out value="${message.category}" />]</pre>
			</td>
		</tr>
		<tr>
			<td>
				<c:forEach items="${users}" var="user">
					<c:if test="${ user.id == message.userId }">
						<c:out value="${user.name}"/>
					</c:if>
				</c:forEach>
			</td>
		</tr>
		<tr>
			<td>
				<fmt:formatDate value="${message.insertDate}" pattern="yyyy年MM月dd日 HH時mm分" />
			</td>
		</tr>
		<tr>
			<td>
				件名<pre><c:out value="${message.subject}"/></pre>
			</td>
		</tr>
		<tr>
			<td>
				本文<pre><c:out value="${message.text}" /></pre>
			</td>
		</tr>
		<tr>
			<td>
				<form:hidden path="messageId" value="${message.id}" />
				<form:textarea path="text" cols="20" rows="10"/>			
			</td>
		</tr>
		<tr>
			<td>
				<input type="submit" value="コメントする">
			</td>
		</tr>
	</table>
</form:form>	
</c:forEach>
</body>
</html>