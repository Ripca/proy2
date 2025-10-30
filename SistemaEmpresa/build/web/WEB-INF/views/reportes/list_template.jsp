<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Reportes");
    request.setAttribute("pageIcon", "fas fa-chart-bar");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/reportes/list_content.jsp" />
</jsp:include>

