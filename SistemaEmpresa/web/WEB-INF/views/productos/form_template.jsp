<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Producto" %>

<%
    Producto producto = (Producto) request.getAttribute("producto");
    boolean esEdicion = producto != null;
    String titulo = esEdicion ? "Editar Producto" : "Nuevo Producto";
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", titulo);
    request.setAttribute("pageIcon", "fas fa-box-open");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/productos/form_content.jsp" />
</jsp:include>
