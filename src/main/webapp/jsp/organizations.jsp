<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="header.jsp" %>
<table border="1">
    <tr>
        <td>Name</td>
        <td>Actions</td>
    </tr>
    <c:forEach items="${organizations}" var="i">
        <tr>
            <td><c:out value="${i.name}"/></td>
            <td><a href="organizations/edit/${i.id}">Edit</a>&nbsp;<a href="organizations/delete/${i.id}">Delete</a>
            </td>

        </tr>
    </c:forEach>
</table>
<br>

<form:form commandName="organization" method="POST">
    <form:hidden path="id"/>

    <table>
        <tr>
            <td>Name:</td>
            <td><form:input path="name"/></td>
            <td><form:errors path="name"/></td>

        </tr>

        <tr>
            <td></td>
            <td align="right"><input type="submit" value="submit"></td>
            <td></td>
        </tr>

    </table>


</form:form>