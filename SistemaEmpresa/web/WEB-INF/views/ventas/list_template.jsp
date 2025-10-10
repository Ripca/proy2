<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Venta" %>

<%
    List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Gestión de Ventas");
    request.setAttribute("pageIcon", "fas fa-shopping-cart");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/ventas/list_content.jsp" />
</jsp:include>
