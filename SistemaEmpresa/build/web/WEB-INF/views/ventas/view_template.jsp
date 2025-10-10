<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Venta" %>

<%
    Venta venta = (Venta) request.getAttribute("venta");
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Detalle de Venta");
    request.setAttribute("pageIcon", "fas fa-eye");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/ventas/view_content.jsp" />
</jsp:include>
