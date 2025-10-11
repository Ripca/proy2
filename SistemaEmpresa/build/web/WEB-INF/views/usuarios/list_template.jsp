<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("pageTitle", "Usuarios del Sistema");
    request.setAttribute("pageIcon", "fas fa-users-cog");
%>
<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/usuarios/list_content.jsp" />
</jsp:include>
