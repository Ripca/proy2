<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Compra" %>

<%
    Compra compra = (Compra) request.getAttribute("compra");
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Detalle de Compra");
    request.setAttribute("pageIcon", "fas fa-eye");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/compras/view_content.jsp" />
</jsp:include>
