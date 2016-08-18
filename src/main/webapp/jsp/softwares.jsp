<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="header.jsp" %>
<table border="1">
    <tr>
        <td>Name</td>
        <td>Organization</td>
        <td>Actions</td>
    </tr>
    <c:forEach items="${softwares}" var="i">
        <tr>
            <td><c:out value="${i.name}"/></td>
            <td><c:out value="${i.organization.name}"/></td>
            <td><a href="softwares/edit/${i.id}">Edit</a>&nbsp;<a href="softwares/delete/${i.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<br>

<form:form commandName="software" method="POST" action="addsoftware">
    <form:hidden path="id"/>

    <table>
        <tr>
            <td>Name:</td>
            <td><form:input path="name"/></td>
            <td><form:errors path="name"/></td>
        </tr>
        <tr>
            <td>Organization:</td>
            <td><form:select path="organization" items="${organizations}" itemLabel="name" itemValue="id"/></td>
            <td><form:errors path="organization"/></td>

        </tr>

        <tr>
            <td></td>
            <td align="right"><input type="submit" value="submit"></td>
            <td></td>
        </tr>

    </table>


</form:form>