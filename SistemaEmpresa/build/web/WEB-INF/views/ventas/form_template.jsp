<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Venta" %>

<%
    Venta venta = (Venta) request.getAttribute("venta");
    boolean esEdicion = venta != null;
    String titulo = esEdicion ? "Editar Venta" : "Nueva Venta";
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", titulo);
    request.setAttribute("pageIcon", "fas fa-shopping-cart");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/ventas/form_content.jsp" />
</jsp:include>
