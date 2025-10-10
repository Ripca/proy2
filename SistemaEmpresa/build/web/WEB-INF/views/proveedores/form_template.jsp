<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Proveedor" %>

<%
    Proveedor proveedor = (Proveedor) request.getAttribute("proveedor");
    boolean esEdicion = proveedor != null;
    String titulo = esEdicion ? "Editar Proveedor" : "Nuevo Proveedor";
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", titulo);
    request.setAttribute("pageIcon", "fas fa-truck-loading");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/proveedores/form_content.jsp" />
</jsp:include>
