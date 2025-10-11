<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("pageTitle", "Cambiar Contraseña");
    request.setAttribute("pageIcon", "fas fa-key");
%>
<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/usuarios/password_content.jsp" />
</jsp:include>
